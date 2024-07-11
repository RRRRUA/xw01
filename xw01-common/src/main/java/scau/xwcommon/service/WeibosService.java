package scau.xwcommon.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import scau.xwcommon.entity.Weibos;
import scau.xwcommon.service.fallback.FallbackWeibosServiceImpl;
import scau.xwcommon.util.Result;

import java.util.List;

/**
 * @author 86153
 * @description 针对表【weibos】的数据库操作Service
 * @createDate 2024-07-03 10:39:13
 */

@FeignClient(value = "xw01-weibo",contextId = "weibos",fallback = FallbackWeibosServiceImpl.class)
public interface WeibosService {
    /**
     * 发表微博
     * @param userLoginName
     * @param title
     * @param content
     * @param img
     * @return
     */
    @PostMapping("weibo/addWeiBo")
    Result<Weibos> addWeibo(@RequestParam("userLoginName") String userLoginName,
                            @RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("img") String img);

    /**
     * 根据id查询单条微博对象
     * @param wbId
     */
    @GetMapping("weibo/findById")
    Result<Weibos> findById(@RequestParam("wbId") Integer wbId);

    /**
     * 根据用户名查询微博对象
     * @param userName
     * @param pageNum
     * @param pageSize
     */
    @GetMapping("weibo/findByUsername")
    Result<Page<Weibos>> findByUsername(@RequestParam("userName") String userName,
                                        @RequestParam("pageNum") Integer pageNum,
                                        @RequestParam("pageSize") Integer pageSize);

    /**
     * 查询所有微博
     * @param pageNum
     * @param pageSize
     * @param findtxt
     * @param state
     */
    @GetMapping("weibo/list")
    Result<Page<Weibos>> list(@RequestParam("pageNum") Integer pageNum,
                              @RequestParam("pageSize") Integer pageSize,
                              @RequestParam("findtxt") String findtxt,
                              @RequestParam("state") Integer state);

    /**
     * 修改阅读量
     * @param wbId
     */
    @PostMapping("weibo/updateReadCount")
    Result<Integer> updateReadCount(@RequestParam("wbId") Integer wbId);

    /**
     * 管理员修改状态
     * @param wbId
     * @param state
     */
    @PostMapping("weibo/updateState")
    Result<Integer> updateState(@RequestParam("wbId") Integer wbId,
                                @RequestParam("state") Integer state);

    /**
     * 根据用户名查询微博总数
     * @param userLoginName
     * @param state
     */
    @GetMapping("weibo/findcountByUser")
    Result<Integer> findcountByUser(@RequestParam("userLoginName") String userLoginName,
                                    @RequestParam("state") Integer state);

    /**
     * 删除微博
     * @param wbId
     */
    @PostMapping("weibo/delete")
    Result<Integer> delete(@RequestParam("wbId") Integer wbId);

    /**
     * 查询状态为2的微博
     */
    @GetMapping("weibo/list2")
    Result<List<Weibos>> list2();

    @GetMapping("weibo/findTop4ByCommentCount")
    Result<List<Weibos>> findTop4ByCommentCount();

    @GetMapping("weibo/findTop4ByReadCount")
    Result<List<Weibos>> findTop4ByReadCount();
}
