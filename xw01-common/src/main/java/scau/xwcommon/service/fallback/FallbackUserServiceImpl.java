package scau.xwcommon.service.fallback;

import org.springframework.stereotype.Service;
import scau.xwcommon.entity.Users;
import scau.xwcommon.service.UsersService;
import scau.xwcommon.util.Result;
//统一处理降级 熔断操作 openfeign远程调用代理的类，才可以用这种方式统一降级
//什么时候起作用，当远程调用失败，或者超时，或者服务不可用，就会触发降级操作
@Service
public class FallbackUserServiceImpl implements UsersService {
    @Override
    public Result<Users> reg(String nickname, String loginName, String loginPwd, String photo) {
        return Result.error("4服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Users> login(String loginName, String loginPwd) {
        return Result.error("5服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Integer> addScore(String loginName, Integer score) {
        return Result.error("6服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Integer> updateStatus(String loginName, Integer status) {
        return Result.error("7服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Users> findByLoginName(String userLoginName) {
        return Result.error("8服务器繁忙，请稍后再试");
    }
}
