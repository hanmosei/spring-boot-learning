package com.hms.learn.auth.handler;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        JSONObject json = new JSONObject();
        json.put("code", HttpStatus.UNAUTHORIZED.value());
        if (exception instanceof LockedException) {
            json.put("message", "账户被锁定，登录失败！");
        } else if (exception instanceof BadCredentialsException) {
            json.put("message", "账户名或密码错误，登录失败！");
        } else if (exception instanceof DisabledException) {
            json.put("message", "账户被禁用，登录失败！");
        } else if (exception instanceof AccountExpiredException) {
            json.put("message", "账户已过期，登录失败！");
        } else if (exception instanceof CredentialsExpiredException) {
            json.put("message", "密码已过期，登录失败！");
        } else {
            json.put("message", "登录失败！");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        out.write(objectMapper.writeValueAsString(json));
        out.flush();
        out.close();
    }
}
