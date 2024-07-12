package scau.xwweibo.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scau.xwcommon.entity.Comments;
import scau.xwcommon.entity.Users;
import scau.xwcommon.service.CommentsService;
import scau.xwcommon.service.UsersService;
import scau.xwcommon.util.Result;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("api/comment")
public class CommentApi {

    @Autowired
    private CommentsService commentsService;
    @Autowired
    private UsersService usersService;
    //发表评论，增加积分1分，要求先登录

    /**
     * 发表评论
     * @param wbid
     * @param content
     * @param session
     * @return
     * @throws TransactionException
     */
    @RequestMapping("add")
    @GlobalTransactional
    public ResponseEntity<Result<Comments>> addComment(int wbid,String content,HttpSession session) throws TransactionException {
        Users cur_user = (Users)session.getAttribute("cur_user");

//        if(cur_user == null)
//        {
//            return ResponseEntity.ok(Result.error("请先登录"));
//        }
        String loginUsername = cur_user.getUserLoginname();
        Result<Comments> result = commentsService.addComents(wbid, content, loginUsername);
        if(result.getCode() != 200)
        {
            return ResponseEntity.status(400).body(result);
        }
        //增加积分
        Result<Integer> integerResult = usersService.addScore(loginUsername, 1);
        if(integerResult.getCode() != 200)
        {
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            return ResponseEntity.status(400).body(Result.error("增加积分失败"));
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 我的评论
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping("mycomment")
    public ResponseEntity<Result> myComment(int pageNum,int pageSize,HttpSession session) {
        Users cur_user = (Users)session.getAttribute("cur_user");

        String loginUsername = cur_user.getUserLoginname();
        Result<Page<Comments>> result = commentsService.findByuserName(pageNum,pageSize,loginUsername);
        return ResponseEntity.ok(result);
    }
}
