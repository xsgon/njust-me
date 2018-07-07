package com.njust.security;

import com.njust.model.response.ErrorCode;
import com.njust.model.response.OperationResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;

import org.json.*;
import lombok.extern.slf4j.Slf4j;

import com.njust.security.*;
import com.njust.model.session.*;

import static com.njust.model.response.OperationResponse.*;

import com.fasterxml.jackson.databind.*;


/* This filter maps to /session and tries to validate the username and password */
@Slf4j
public class SessionFilter extends AbstractAuthenticationProcessingFilter {

    private TokenUtil tokenUtil;

    protected SessionFilter(String urlMapping, AuthenticationManager authenticationManager, TokenUtil tokenUtil) {
        super(new AntPathRequestMatcher(urlMapping));
        setAuthenticationManager(authenticationManager);
        this.tokenUtil = tokenUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException, JSONException {
//        try {
//            log.info("attemptAuthentication, received session request from web");
//            String jsonString = IOUtils.toString(request.getInputStream(), "UTF-8");
//            /* using org.json */
//            JSONObject userJSON = new JSONObject(jsonString);
//            String username = userJSON.getString("username");
//            String password = userJSON.getString("password");
//            boolean logout = userJSON.has("logout");
//            String browser = request.getHeader("UserMapper-Agent") != null ? request.getHeader("UserMapper-Agent") : "";
//            String ip = request.getRemoteAddr();
//            log.info("\nip:{} \nbrowser:{} \n----", ip, browser);
//
//            Authentication auth = null;
//            if (logout) {
//                tokenUtil.revokeToken(request);
//                SecurityContextHolder.getContext().setAuthentication(null);
//            } else {
//                final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
//                auth = getAuthenticationManager().authenticate(authToken); // This will take to successfulAuthentication or faliureAuthentication function
//            }
//            return auth;
//
//        } catch (JSONException | AuthenticationException e) {
//            throw new AuthenticationServiceException(e.getMessage());
//        }
        log.info("attemptAuthentication, received session request from web");
        String jsonString = IOUtils.toString(request.getInputStream(), "UTF-8");
        /* using org.json */
        JSONObject userJSON = new JSONObject(jsonString);
        String username = userJSON.getString("username");
        String password = userJSON.getString("password");
        boolean logout = userJSON.has("logout");
        String browser = request.getHeader("UserMapper-Agent") != null ? request.getHeader("UserMapper-Agent") : "";
        String ip = request.getRemoteAddr();
        log.info("\nip:{} \nbrowser:{} \n----", ip, browser);

        Authentication auth = null;
        if (logout) {
            tokenUtil.revokeToken(request);
            SecurityContextHolder.getContext().setAuthentication(null);
        } else {
            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            auth = getAuthenticationManager().authenticate(authToken); // This will take to successfulAuthentication or faliureAuthentication function
        }
        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authToken) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authToken);

        log.info("Login Success");

        TokenUser tokenUser = (TokenUser) authToken.getPrincipal();
        tokenUser.getUser().setPassword("shadowed");
        tokenUser.getUser().set_id("shadowed");

        OperationResponse resp = new OperationResponse();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String tokenString = this.tokenUtil.createTokenForUser(tokenUser);
        SessionItem respItem = new SessionItem();
        respItem.setToken(tokenString);
        respItem.setUser(tokenUser.getUser());

        resp.setCode(ErrorCode.CODE_SUCCESS);
        resp.setMessage("Login Success");
        resp.setBody(respItem);
        String jsonRespString = ow.writeValueAsString(resp);

        res.setCharacterEncoding("utf8");
        res.setContentType("application/json");
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(jsonRespString);
        //res.getWriter().write(jsonResp.toString());
        res.getWriter().flush();
        res.getWriter().close();

        // DONT call supper as it contine the filter chain super.successfulAuthentication(req, res, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        super.unsuccessfulAuthentication(request, response, failed);
        OperationResponse rsp = new OperationResponse();
        if (failed instanceof UsernameNotFoundException) {
            rsp.setCode(ErrorCode.CODE_USER_NOT_EXISTS);
            rsp.setMessage(ErrorCode.MSG_USER_NOT_EXISTS);
        } else if (failed instanceof DisabledException) {
            rsp.setCode(ErrorCode.CODE_USER_DISABLED);
            rsp.setMessage(ErrorCode.MSG_USER_DISABLED);
        } else {
            rsp.setCode(ErrorCode.CODE_AUTH_ERROR);
            rsp.setMessage(ErrorCode.MSG_AUTH_ERROR + ":" + failed.getMessage());
        }

        JSONObject jsonObject = new JSONObject(rsp);
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonObject.toString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
