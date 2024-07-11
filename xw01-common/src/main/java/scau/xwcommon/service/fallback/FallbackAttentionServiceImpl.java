package scau.xwcommon.service.fallback;

import org.springframework.stereotype.Service;
import scau.xwcommon.entity.Attentions;
import scau.xwcommon.service.AttentionsService;
import scau.xwcommon.util.Result;

import java.util.List;

@Service
public class FallbackAttentionServiceImpl implements AttentionsService {

    @Override
    public Result<List<Attentions>> findMyAttention(Integer pageNum, Integer pageSize, String userLoginname) {
        return Result.error("6服务调用失败，请稍后重试！");
    }
}
