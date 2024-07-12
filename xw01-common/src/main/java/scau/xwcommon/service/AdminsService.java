package scau.xwcommon.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import scau.xwcommon.entity.Admins;
import scau.xwcommon.service.fallback.FallbackAdminsServiceImpl;
import scau.xwcommon.service.fallback.FallbackUserServiceImpl;
import scau.xwcommon.util.Result;

import java.util.List;

@FeignClient(value = "xw01-admin",fallback = FallbackAdminsServiceImpl.class)
public interface AdminsService {

    /**
     * 注册
     * @param adminName
     * @param adminLoginname
     * @param adminLoginpwd
     * @param level
     * @return
     */
    @PostMapping("admin/register")
    Result<Admins> register(@RequestParam("adminName") String adminName, @RequestParam("adminLoginname") String adminLoginname, @RequestParam("adminLoginpwd") String adminLoginpwd, @RequestParam("level") Integer level);


    /**
     * 登录
     * @param loginName
     * @param loginPwd
     * @return
     */
    @PostMapping("admin/login")
    Result<Admins> login(@RequestParam("loginName") String loginName, @RequestParam("loginPwd") String loginPwd);

    /**
     * 列出所有管理员
     * @return
     */
    @PostMapping("admin/list")
    Result<Page<Admins>> list(@RequestParam("pageNum") int currentPage, @RequestParam("pageSize") int pageSize);


}
