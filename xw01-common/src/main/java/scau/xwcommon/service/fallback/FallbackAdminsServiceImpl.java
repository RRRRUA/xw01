package scau.xwcommon.service.fallback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import scau.xwcommon.entity.Admins;
import scau.xwcommon.service.AdminsService;
import scau.xwcommon.util.Result;

@Service
public class FallbackAdminsServiceImpl implements AdminsService{


    @Override
    public Result<Admins> register(String adminName, String adminLoginname, String adminLoginpwd, Integer level) {
        return Result.error("1服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Admins> login(String loginName, String loginPwd) {
        return Result.error("2服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Page<Admins>> list(int currentPage, int pageSize) {
        return Result.error("3服务器繁忙，请稍后再试");
    }
}
