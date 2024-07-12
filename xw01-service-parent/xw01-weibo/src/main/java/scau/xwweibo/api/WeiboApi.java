package scau.xwweibo.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import scau.xwcommon.entity.Comments;
import scau.xwcommon.entity.Users;
import scau.xwcommon.entity.Weibos;
import scau.xwcommon.service.CommentsService;
import scau.xwcommon.service.UsersService;
import scau.xwcommon.service.WeibosService;
import scau.xwcommon.util.Result;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("api/weibo")
public class WeiboApi {
    @Qualifier("weiboServiceImpl")
    @Autowired
    private WeibosService weiboService;

    @Qualifier("commentsServiceImpl")
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private UsersService userService;
    @Value("${my.upload_dir}")
    private String uploadDir;

    //发表微博(可以增加用户积分,分布式事务,先登录)

    /**
     * 发表微博
     * @param title
     * @param content
     * @param photofile
     * @param session
     * @return
     * @throws IOException
     */
    @RequestMapping("add")
    @GlobalTransactional
    public ResponseEntity<Result<Weibos>> addWeibo(String title, String content, MultipartFile photofile, HttpSession session) throws IOException, TransactionException {

        Users cur_user = (Users) session.getAttribute("cur_user");
//        if(cur_user==null){
//            return ResponseEntity.status(400).body(Result.error("请先登录"));
//        }

        String username = cur_user.getUserLoginname();
        String photoName = null;
        if (!photofile.isEmpty()) {
            photoName = photofile.getOriginalFilename();
            photoName = UUID.randomUUID() + photoName.substring(photoName.lastIndexOf("."));
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            photofile.transferTo(new File(uploadDir + photoName));
        }
        Result<Weibos> result = weiboService.addWeibo(username, title, content, photoName);
        if (result.getCode() != 200) {
            return ResponseEntity.status(400).body(result);
        }
        Result<Integer> userResult = userService.addScore(username, 10);
        if (userResult.getCode() != 200) {
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            return ResponseEntity.status(400).body(Result.error("增加积分失败"));
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 根据id查询单条微博对象
     * @param wbId
     * @return
     */
    @RequestMapping("findById")
    public ResponseEntity<Result<Weibos>> findById(Integer wbId) {
        Result<Weibos> result = weiboService.findById(wbId);
        if (result.getCode() == 200) {
            Weibos wb = result.getData();
            Result<Users> author = userService.findByLoginName(wb.getWbUserLoginname());
            if (author.getCode() == 200)
                wb.getMap().put("author", author.getData());
            else {
                wb.getMap().put("author_error", author.getMessage());
            }
        }
        return ResponseEntity.ok(result);
    }


    /**
     * 分页查询所有微博
     * @param pageNum
     * @param pageSize
     * @param findtxt
     * @param state
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<Result<Page<Weibos>>> list(Integer pageNum, Integer pageSize, String findtxt, Integer state) {
        Result<Page<Weibos>> result = weiboService.list(pageNum, pageSize, findtxt, state);
        if (result.getCode() == 200) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(400).body(result);
    }


    /**
     * 分页查询我的微博
     * @param pageNum
     * @param pageSize
     * @param username
     * @param session
     * @return
     */
    @GetMapping("mylist")
    public ResponseEntity<Result<Page<Weibos>>> mylist(Integer pageNum, Integer pageSize,String username, HttpSession session) {
        Result<Page<Weibos>> result=null;
        if(username==null|| username.isEmpty()){
            Users cur_user = (Users) session.getAttribute("cur_user");
            if (cur_user == null) {
                return ResponseEntity.status(400).body(Result.error("请先登录"));
            }
            String curUser = cur_user.getUserLoginname();
            result = weiboService.findByUsername(curUser, pageNum, pageSize,0);
        }else{
            result = weiboService.findByUsername(username, pageNum, pageSize,1);
        }


        if (result.getCode() == 200) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(400).body(result);
    }

    //根据微博id返回微博对象和作者对象和评论列表和该作者所有微博列表

    /**
     * 根据微博id返回微博对象和作者对象和评论列表
     * @param wbId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("detail")
    public ResponseEntity<Result<Weibos>> detail(Integer wbId,int pageNum,int pageSize){
        Result<Weibos> result=weiboService.findById(wbId);
//        System.out.println("result1"+result);
        if(result.getCode()==200){
            Weibos wb=result.getData();
            wb.setMap(new HashMap<>());
//            System.out.println("wb"+wb);
            Result<Users> author=userService.findByLoginName(wb.getWbUserLoginname());
//            System.out.println("author"+author);
            if(author.getCode()==200)
                wb.getMap().put("author",author.getData());
            else{
                wb.getMap().put("author_error",author.getMessage());
            }
            Result<Page<Comments>> comments = commentsService.list1(pageNum, pageSize, 1);
            if (comments.getCode() == 200)
                wb.getMap().put("comments", comments.getData());
            else {
                wb.getMap().put("comments_error", comments.getMessage());
            }
        }
        return ResponseEntity.ok(result);
    }

    /**
     *
     * @return
     */
    @GetMapping("findWeibosByCommentCountTop4")
    public ResponseEntity<Result<List<Map<String, Object>>>> findWeibosByCommentCountTop4() {
        Result<List<Weibos>> result = weiboService.findTop4ByCommentCount();
        if (result.getCode() != 200) {
            return ResponseEntity.status(500).body(new Result<>(result.getCode(), null, result.getMessage()));
        }
        Result<List<Map<String, Object>>> weibos = commentsService.listCountTop4(result.getData());
        if (result.getCode() != 200) {
            return ResponseEntity.status(500).body(new Result<>(weibos.getCode(), null, weibos.getMessage()));
        }
        weibos.getData().forEach(wb -> {
            wb.put("weiboTitle", weiboService.findById((Integer) wb.get("weiboId")).getData().getWbTitle());
        });
        return ResponseEntity.ok(weibos);
    }

    /**
     *
     * @return
     */
    @GetMapping("findWeibosByReadCountTop4")
    public ResponseEntity<Result<List<Weibos>>> findWeibosByReadCountTop4() {
        Result<List<Weibos>> result = weiboService.findTop4ByReadCount();
        if (result.getCode() != 200) {
            return ResponseEntity.status(500).body(new Result<>(result.getCode(), null, result.getMessage()));
        }

        return ResponseEntity.ok(result);
    }
}
