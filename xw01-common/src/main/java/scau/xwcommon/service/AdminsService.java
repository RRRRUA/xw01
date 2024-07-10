package scau.xwcommon.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import scau.xwcommon.entity.Admins;
import scau.xwcommon.service.fallback.FallbackAdminsServiceImpl;
import scau.xwcommon.service.fallback.FallbackUserServiceImpl;
import scau.xwcommon.util.Result;

@FeignClient(value = "xw01-admin",fallback = FallbackAdminsServiceImpl.class)
public interface AdminsService {
    /**
     * 注册
     * @param admins
     * @return
     */
    @PostMapping("admin/register")
    Result<Admins> register(@RequestBody Admins admins);

    /**
     * 登录
     * @param loginName
     * @param loginPwd
     * @return
     */
    @PostMapping("admin/login")
    Result<Admins> login(@RequestParam("loginName") String loginName, @RequestParam("loginPwd") String loginPwd);
}
