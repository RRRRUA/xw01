package scau.xwattention.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scau.xwattention.service.impl.AttentionsServiceImpl;
import scau.xwcommon.entity.Attentions;
import scau.xwcommon.entity.Users;
import scau.xwcommon.util.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("api/attention")
public class AttentionApi {

    @Resource
    private AttentionsServiceImpl attentionsService;

    /**
     *  获取我的关注列表
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @GetMapping("/myattention")
    public ResponseEntity<Result<List<Attentions>>> myAttention(Integer pageNum, Integer pageSize, HttpSession session) {
        Users user = (Users) session.getAttribute("cur_user");
        if (user == null) {
            return ResponseEntity.status(401).body(Result.error("请先登录！"));
        }
        Result<List<Attentions>> result = attentionsService.findMyAttention(pageNum, pageSize, user.getUserLoginname());
        if(result.getCode() !=200)
        {
            return ResponseEntity.status(500).body(result);
        }
        return ResponseEntity.ok(result);
    }

    /**
     *  取消关注
     * @param marstLoginname
     * @param session
     * @return
     */
    @GetMapping("deleteAttention")
    public ResponseEntity<Result<Integer>> deleteAttention(String marstLoginname, HttpSession session) {
        Users user = (Users) session.getAttribute("cur_user");
        if (user == null) {
            return ResponseEntity.status(401).body(Result.error("请先登录！"));
        }
        Result<Integer> result = attentionsService.deleteAttention(marstLoginname, user.getUserLoginname());
        if(result.getCode() !=200)
        {
            return ResponseEntity.status(500).body(result);
        }
        return ResponseEntity.ok(result);
    }

    /**
     *  关注
     * @param marstLoginname
     * @param session
     * @return
     */
    @GetMapping("addAttention")
    public ResponseEntity<Result<Integer>> addAttention(String marstLoginname, HttpSession session) {
        Users user = (Users) session.getAttribute("cur_user");
        if (user == null) {
            return ResponseEntity.status(401).body(Result.error("请先登录！"));
        }
        Result<Integer> result = attentionsService.addAttention(marstLoginname, user.getUserLoginname());
        if(result.getCode() !=200)
        {
            return ResponseEntity.status(500).body(result);
        }
        return ResponseEntity.ok(result);
    }
}
