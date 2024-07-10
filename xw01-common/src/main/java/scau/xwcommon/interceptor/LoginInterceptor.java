package scau.xwcommon.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getSession().getAttribute("cur_user")!=null){
            return true;
        }
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(400);
        response.getWriter().write("{\"code\":400,\"msg\":\"请先登录\"}");
        return false;
    }
}
