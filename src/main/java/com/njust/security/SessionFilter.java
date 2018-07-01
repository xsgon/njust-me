package com.njust.security;

import com.njust.model.response.ErrorCode;
import com.njust.model.response.OperationResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
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
        try {
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
                //final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken("demo", "demo");
                final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
//            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, "$2a$10$Mf0kwi6u.AAMnBjYK.BrKuJ4yU82H4xO48Bk3Iqq4.uYd2Ph82VQu");
                auth = getAuthenticationManager().authenticate(authToken); // This will take to successfulAuthentication or faliureAuthentication function
            }
            return auth;

        } catch (JSONException | AuthenticationException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authToken) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authToken);

        log.info("Login Success");

        TokenUser tokenUser = (TokenUser) authToken.getPrincipal();
        OperationResponse resp = new OperationResponse();
        SessionItem respItem = new SessionItem();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String tokenString = this.tokenUtil.createTokenForUser(tokenUser);

        respItem.setEmail(tokenUser.getUser().getEmail());
        respItem.setToken(tokenString);
        respItem.setRole(tokenUser.getRole());

        resp.setCode(ErrorCode.CODE_SUCCESS);
        resp.setMessage("Login Success");
        resp.setBody(respItem);
        String jsonRespString = ow.writeValueAsString(resp);

        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(jsonRespString);
        //res.getWriter().write(jsonResp.toString());
        res.getWriter().flush();
        res.getWriter().close();

        // DONT call supper as it contine the filter chain super.successfulAuthentication(req, res, chain, authResult);
    }
}
