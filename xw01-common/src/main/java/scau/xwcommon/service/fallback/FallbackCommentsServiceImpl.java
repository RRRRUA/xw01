package scau.xwcommon.service.fallback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import scau.xwcommon.entity.Comments;
import scau.xwcommon.entity.Weibos;
import scau.xwcommon.service.CommentsService;
import scau.xwcommon.util.Result;

import java.util.List;
import java.util.Map;

@Service
public class FallbackCommentsServiceImpl implements CommentsService {
    @Override
    public Result<Comments> addComents(Integer wbid, String content, String loginUsername) {
        return Result.error("3服务器繁忙，请稍后再试");
    }

    @Override
    public Result<Page<Comments>> list(Integer pageNum, Integer pageSize, Integer state) {
        return Result.error("3服务器繁忙，请稍后再试");
    }

    @Override
    public Result<List<Map<String, Object>>> listCountTop4(List<Weibos> weibos) {
        return Result.error("3服务器繁忙，请稍后再试");
    }
}
