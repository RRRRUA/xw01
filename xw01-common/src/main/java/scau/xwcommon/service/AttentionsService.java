package scau.xwcommon.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import scau.xwcommon.entity.Attentions;
import scau.xwcommon.service.fallback.FallbackAttentionServiceImpl;
import scau.xwcommon.util.Result;

import java.util.List;


/**
* @author 86153
* @description 针对表【attentions】的数据库操作Service
* @createDate 2024-07-03 10:39:13
*/
@FeignClient(value = "xw01-attention",fallback = FallbackAttentionServiceImpl.class)
public interface AttentionsService  {

    @GetMapping("/findMyAttention")
    Result<List<Attentions>> findMyAttention(Integer pageNum, Integer pageSize, String userLoginname);

    @GetMapping("/deleteAttention")
    Result<Integer> deleteAttention(String marstLoginname, String userLoginname);

    @GetMapping("/addAttention")
    Result<Integer> addAttention(String marstLoginname, String userLoginname);
}
