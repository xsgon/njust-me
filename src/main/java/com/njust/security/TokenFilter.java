package com.njust.security;

import com.njust.model.response.ErrorCode;
import com.njust.model.response.OperationResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import io.jsonwebtoken.JwtException;
import com.njust.security.*;

/*
This filter checks if there is a token in the Request service header and the token is not expired
it is applied to all the routes which are protected
*/
@Slf4j
public class TokenFilter extends GenericFilterBean {

    private final TokenUtil tokenUtil;

    public TokenFilter(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        log.info("do token filter");

        try {
            SecurityContextHolder.getContext().setAuthentication(null);

            Optional<Authentication> authentication = tokenUtil.verifyToken(request);
            if (authentication.isPresent()) {
                SecurityContextHolder.getContext().setAuthentication(authentication.get());
            } else {
                if (!request.getRequestURI().startsWith("/session")) {
                    throw new JwtException("not a session request");
                }
            }

            filterChain.doFilter(req, res);
        } catch (JwtException e) {
            OperationResponse resp = new OperationResponse();
            resp.setCode(ErrorCode.CODE_AUTH_ERROR);
            resp.setMessage(ErrorCode.MSG_AUTH_ERROR);
            JSONObject jsonObject = new JSONObject(resp);

            response.setCharacterEncoding("utf8");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(jsonObject.toString());
            response.getWriter().flush();
            response.getWriter().close();
        } finally {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }

}
