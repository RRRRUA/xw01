package scau.xwadmin.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import scau.xwcommon.annotation.Allow;
import scau.xwcommon.annotation.Login;
import scau.xwcommon.entity.Admins;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
           HandlerMethod m = (HandlerMethod) handler;
              //获取方法上的注解
           if(m.getMethodAnnotation(Login.class)==null){
               return true;
           }
       }

        Admins admin=(Admins) request.getSession().getAttribute("cur_admin");


        System.out.println("session"+admin);
       if(admin==null){
           response.setContentType("application/json;charset=utf-8");
           response.setStatus(401);
           response.getWriter().write("{\"code\":401,\"msg\":\"请先登录\"}");
           return false;
       }
        return true;
    }

}
