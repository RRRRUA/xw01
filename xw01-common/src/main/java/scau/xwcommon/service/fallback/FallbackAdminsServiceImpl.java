package scau.xwcommon.service.fallback;

import org.springframework.stereotype.Service;
import scau.xwcommon.entity.Admins;
import scau.xwcommon.service.AdminsService;
import scau.xwcommon.util.Result;

@Service
public class FallbackAdminsServiceImpl implements AdminsService{

    @Override
    public Result<Admins> register(Admins admins) {
        return Result.error("1服务器繁忙，请稍后再试");
    }

@Override
    public Result<Admins> login(String loginName, String loginPwd) {
        return Result.error("2服务器繁忙，请稍后再试");
    }
}
