package scau.xwcommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import scau.xwcommon.entity.Comments;
import scau.xwcommon.entity.Users;
import scau.xwcommon.service.fallback.FallbackUserServiceImpl;
import scau.xwcommon.util.Result;

/**
 * @author 86153
 * @description 针对表【users】的数据库操作Service
 * @createDate 2024-07-03 10:39:13
 */

// 通过@FeignClient注解来指定这个接口所要调用的服务名称
//@FeignClient("xw01-user")
//openfeign的fallback属性指定了降级处理类
@FeignClient(value = "xw01-user",fallback = FallbackUserServiceImpl.class)
//制造接口的目的是生成动态代理
public interface UsersService{

    /**
     * 注册
     * @param nickname
     * @param loginName
     * @param loginPwd
     * @param photo
     * @return
     */
    @RequestMapping("user/reg")
    Result<Users> reg(@RequestParam("nickname") String nickname,
                      @RequestParam("loginName") String loginName,
                      @RequestParam("loginPwd") String loginPwd,
                      @RequestParam("photo") String photo);

    /**
     * 登录
     * @param loginName
     * @param loginPwd
     */
    @RequestMapping("user/login")
    Result<Users> login(@RequestParam("loginName") String loginName,
                 @RequestParam("loginPwd") String loginPwd);

    /**
     * 添加积分
     * @param loginName
     * @param score
     * @return
     */
    @RequestMapping("user/addScore")
    // 通过@RequestParam注解来指定参数名称（一定要）
    Result<Integer> addScore(@RequestParam("loginName") String loginName,
                    @RequestParam("score") Integer score);

    /**
     * 修改状态
     * @param loginName
     * @param status
     */
    @RequestMapping("user/updateStatus")
    Result<Integer> updateStatus(@RequestParam("loginName") String loginName,
                        @RequestParam("status") Integer status);


    /**
     * 根据登录名查询用户
     * @param userLoginName
     * @return
     */
    @RequestMapping("user/findByLoginName")
    Result<Users> findByLoginName(@RequestParam("userLoginName") String userLoginName);


}
