package scau.xwcommon.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import scau.xwcommon.entity.Comments;
import com.baomidou.mybatisplus.extension.service.IService;
import scau.xwcommon.entity.Weibos;
import scau.xwcommon.service.fallback.FallbackCommentsServiceImpl;
import scau.xwcommon.util.Result;

import java.util.List;
import java.util.Map;

/**
* @author 86153
* @description 针对表【comments】的数据库操作Service
* @createDate 2024-07-03 10:39:13
*/
@FeignClient(value = "xw01-weibo",contextId = "comments",fallback = FallbackCommentsServiceImpl.class)
public interface CommentsService  {

    /**
     * 添加评论
     * @param wbid
     * @param content
     * @param loginUsername
     * @return
     */
    @RequestMapping("comments/add")
    Result<Comments>addComents(@RequestParam("wbid") Integer wbid,
                               @RequestParam("content") String content,
                               @RequestParam("loginUsername") String loginUsername);

    @RequestMapping("comments/list")
    Result<Page<Comments>> list1(@RequestParam("pageNum") Integer pageNum,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam("state") Integer state);

    /**
     * 获取微博评论数
     * @param weibos
     * @return
     */
    @RequestMapping("comments/countTop4ByReadCount")
    Result<List<Map<String, Object>>> listCountTop4(@RequestBody List<Weibos> weibos);


    @RequestMapping("comments/findByuserName")
    Result<Page<Comments>> findByuserName(@RequestParam("pageNum") Integer pageNum,
                                          @RequestParam("pageSize") Integer pageSize,
                                          @RequestParam("userName") String userName);

    @PostMapping("comments/update")
    Result<Comments> update(@RequestParam("commentId")Integer commentId,
                            @RequestParam("state") Integer state);
}
