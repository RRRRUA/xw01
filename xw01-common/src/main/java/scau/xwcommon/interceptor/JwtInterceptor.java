package scau.xwcommon.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import scau.xwcommon.entity.Users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JwtInterceptor implements HandlerInterceptor {


    private String jwtKey="gugu";
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

     /*   Integer userId = Integer.parseInt(claims.get("userId").toString());
        String userNickname = claims.get("userNickname").toString();
        String userLoginname = claims.get("userLoginname").toString();
        Integer userScore = Integer.parseInt(claims.get("userScore").toString());
        Integer userAttionCount = Integer.parseInt(claims.get("userAttionCount").toString());

        Users user = new Users();
        user.setUserId(userId);
        user.setUserNickname(userNickname);
        user.setUserLoginname(userLoginname);
        user.setUserScore(userScore);
        user.setUserAttioncount(userAttionCount);


        request.getSession().setAttribute("cur_user",user);*/

        return true;





    }
}
