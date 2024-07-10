package scau.xwadmin.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.servlet.HandlerInterceptor;
import scau.xwcommon.entity.Admins;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RefreshScope
public class JwtInterceptor implements HandlerInterceptor {

    @Value("${my.password}")
    private String jwtKey;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String jwtKey="gugu";

        response.setCharacterEncoding("utf-8");
        //获取请求头中的令牌
        String jwt=request.getHeader("Authorization");
        System.out.println("jwtKey:"+jwtKey);
        System.out.println("jwt:"+jwt);
        //只处理分解，不管登录控制
        if(jwt==null||jwt.equals("")){
            return true;
        }
        Claims claims=null;
        try {
            //解析令牌
            claims= Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(jwt).getBody();
        }catch (ExpiredJwtException e){
            //令牌过期
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"msg\":\"令牌无效\"}");
            return false;
        }catch (SignatureException e){
            //令牌被修改
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"msg\":\"登录凭证以被修改\"}");
            return false;
        }
        //分解数据
        int id=Integer.parseInt(claims.get("adminId").toString());
        String name=claims.get("adminName").toString();
        String loginName=claims.get("loginName").toString();
        List<String> roles=(List<String>) claims.get("roles");
        List<String> pmses=(List<String>) claims.get("pmses");

        //将数据放入请求中
        Admins admin=new Admins();
        admin.setAdminId(id);
        admin.setAdminName(name);
        admin.setAdminLoginname(loginName);
        admin.setLevel(roles);
        admin.setPmses(pmses);
        System.out.println("admin:"+admin.toString());
        request.getSession().setAttribute("cur_admin",admin);

        return true;





    }
}
