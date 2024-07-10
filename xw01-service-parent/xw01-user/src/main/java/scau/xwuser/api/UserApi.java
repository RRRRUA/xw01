package scau.xwuser.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import scau.xwcommon.entity.Users;
import scau.xwcommon.entity.Weibos;
import scau.xwcommon.service.UsersService;
import scau.xwcommon.service.WeibosService;
import scau.xwcommon.util.Result;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
@RefreshScope
//这种普通的controller或者普通方法，只能手工@SemtinelResource写限流、熔断等，不能使用openfeign
public class UserApi {

    @Autowired
    //确保引用的是本地的UserService
    @Qualifier("usersServiceImpl")
    private UsersService usersService;


    @Autowired
    @Qualifier("scau.xwcommon.service.WeibosService")
    private WeibosService weiboService;

    //获取配置文件中的上传路径
    @Value("${my.upload_dir}")
    private String uploadDir;



    /**
     * 用户注册
     * @param nickname
     * @param loginName
     * @param loginPwd
     * @param photoFile
     * @return
     * @throws IOException
     */
    @RequestMapping("reg")
    //添加事务注解，实现分布式事务
    @GlobalTransactional
    public ResponseEntity<Result> reg(String nickname, String loginName, String loginPwd, MultipartFile photoFile) throws IOException, TransactionException {
        String photoName = null;
        if(!photoFile.isEmpty()) {
            //获取文件原始文件名
            photoName=photoFile.getOriginalFilename();
            //处理文件名，保证文件名唯一，通过首先生成一个随机的UUID，然后将其与原始文件名的扩展名拼接在一起
            photoName= UUID.randomUUID()+photoName.substring(photoName.lastIndexOf("."));

           File uploadDirFile = new File(uploadDir);
           //如果文件夹不存在，则创建文件夹
            if(!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            photoFile.transferTo(new File(uploadDir+"/"+photoName));
        }

        Result<Users> result = usersService.reg(nickname, loginName, loginPwd, photoName);
        if(result.getCode()!=200)
            return ResponseEntity.status(500).body(result);

        Result<Weibos> weibosResult=weiboService.addWeibo(loginName, "欢迎注册小微微博", "欢迎" + nickname + "加入", "");
        if(weibosResult.getCode()!=200){
            //转人工抛出异常
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            return ResponseEntity.status(500).body(weibosResult);
        }
        result.getData().setUserLoginpwd(null);
        return ResponseEntity.ok(result);
    }


    /**
     * 用户登录
     * @param loginName
     * @param loginPwd
     * @return
     */
    //每个请求都写一个资源名称，用于限流、熔断等过于麻烦，openfeign中已经实现了自动配置
//    @SentinelResource(value = "login",fallback = "loginBlockHandler")
    @RequestMapping("login")
    public ResponseEntity<Result<Users>> login(String loginName, String loginPwd, HttpSession session) {
        Result<Users> result = usersService.login(loginName, loginPwd);
        if(result.getCode()==200){
            Users users = result.getData();
            session.setAttribute("cur_user", users);
            users.setUserLoginpwd(null);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(401).body(result);
    }

    /**
     * 降级方法，当发生熔断或限流时，会调用此方法
     * 用户登录的熔断处理
     */
//    public ResponseEntity<Result<Users>> loginBlockHandler(String loginName, String loginPwd, HttpSession session,Throwable r){
//        //登记日志，发送短信，邮件等
//        return ResponseEntity.status(500).body(Result.error("访问过于频繁"));
//    }

    @GetMapping("/test")
    public ResponseEntity<Result<Users>> test(HttpSession session) {
        Users users = (Users) session.getAttribute("cur_user");
        return ResponseEntity.ok(Result.success(users));
    }

}
