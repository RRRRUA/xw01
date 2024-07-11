package scau.xwcommon.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import scau.xwcommon.entity.Comments;
import com.baomidou.mybatisplus.extension.service.IService;
import scau.xwcommon.service.fallback.FallbackCommentsServiceImpl;
import scau.xwcommon.util.Result;

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

    Result<Page<Comments>> list(@RequestParam("pageNum") Integer pageNum,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam("state") Integer state);
}
