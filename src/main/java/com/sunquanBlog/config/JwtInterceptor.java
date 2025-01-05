package com.sunquanBlog.config;

import org.json.JSONObject;
import com.sunquanBlog.common.util.JwtUtil;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 验证token的拦截器，在接收请求时先验证token
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is missing");
            return false;
        }

        try {
            Claims claims = JwtUtil.validateToken(token);
            request.setAttribute("claims", claims); // 将 claims 存入请求，以便后续使用
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token 已过期");
            return false;
        } catch (UnsupportedJwtException e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token 格式不支持");
            return false;
        } catch (MalformedJwtException e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token 无效");
            return false;
        } catch (SignatureException e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token 签名无效");
            return false;
        } catch (IllegalArgumentException e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token 参数无效");
            return false;
        }

        return true;
    }

    private void setErrorResponse(HttpServletResponse response, int status, String message) throws Exception {
        response.setStatus(status);
//        response.setStatus(HttpServletResponse.SC_OK); // 返回 200 状态码
        response.setContentType("application/json; charset=UTF-8");

        JSONObject responseJson = new JSONObject();
        responseJson.put("statusCode", status);
        responseJson.put("message", message);
        responseJson.put("data", JSONObject.NULL);

        response.getWriter().write(responseJson.toString());
        response.getWriter().flush();
    }
}