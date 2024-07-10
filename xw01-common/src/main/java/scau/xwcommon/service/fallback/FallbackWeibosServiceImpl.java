package scau.xwcommon.service.fallback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.stereotype.Service;
import scau.xwcommon.entity.Weibos;
import scau.xwcommon.service.WeibosService;
import scau.xwcommon.util.Result;

import java.util.List;

@Service
public class FallbackWeibosServiceImpl implements WeibosService {
    @Override
    public Result<Weibos> addWeibo(String userLoginName, String title, String content, String img) {
        return Result.error("9服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Weibos> findById(Integer wbId) {
        return Result.error("10服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Page<Weibos>> findByUsername(String userName, Integer pageNum, Integer pageSize, Integer state) {
        return Result.error("11服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Page<Weibos>> list(Integer pageNum, Integer pageSize, String findtxt, Integer state) {
        return Result.error("12服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Integer> updateReadCount(Integer wbId) {
        return Result.error("13服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Integer> updateState(Integer wbId, Integer state) {
        return Result.error("14服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Integer> findcountByUser(String userLoginName, Integer state) {
        return Result.error("15服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Integer> delete(Integer wbId) {
        return Result.error("16服务器繁忙，请稍后再试");
    }

    @Override
    public Result<List<Weibos>> list2() {
        return Result.error("17服务器繁忙，请稍后再试");
    }


}
