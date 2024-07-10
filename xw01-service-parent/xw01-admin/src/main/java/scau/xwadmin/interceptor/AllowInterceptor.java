package scau.xwadmin.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import scau.xwcommon.annotation.Allow;
import scau.xwcommon.annotation.Login;
import scau.xwcommon.entity.Admins;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AllowInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Allow allow=null;
        if(handler instanceof HandlerMethod){
            HandlerMethod m = (HandlerMethod) handler;
            //获取方法上的注解
            allow=m.getMethodAnnotation(Allow.class);
            if(allow==null){
                return true;
            }
        }

        String[] roles=allow.roles();//获取操作权限
        String[] pmses=allow.pmses();

        Admins admin=(Admins) request.getSession().getAttribute("cur_admin");
        //判断当前用户的角色，是否在@Allow中约定的角色中
        for(String role:roles){
            if(admin.getLevel().contains(role)){
                return true;
            }
        }
        //判断当前用户的权限，是否在@Allow中约定的权限中
        for(String pms:pmses){
            if(admin.getPmses().contains(pms)){
                return true;
            }
        }

        response.setContentType("application/json;charset=utf-8");
        response.setStatus(401);
        response.getWriter().write("{\"code\":401,\"msg\":\"请先登录\"}");
        return false;
    }
}
