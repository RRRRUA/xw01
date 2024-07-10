package scau.xwadmin.api;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.baidubce.qianfan.Qianfan;
import com.baidubce.qianfan.core.auth.Auth;
import com.baidubce.qianfan.model.chat.ChatResponse;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scau.xwcommon.annotation.Allow;
import scau.xwcommon.annotation.Login;
import scau.xwcommon.entity.Admins;
import scau.xwcommon.entity.Weibos;
import scau.xwcommon.service.AdminsService;
import scau.xwcommon.service.WeibosService;
import scau.xwcommon.util.Result;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.lang.Thread.sleep;

@RestController
@RequestMapping("api/admin")
@RefreshScope
public class AdminApi {


    @Qualifier("adminsServiceImpl")
    @Autowired
    private AdminsService adminsService;

    @Autowired
    private WeibosService weibosService;

    @Value("${my.password}")
    private String jwtKey;

    /**
     * 登录
     *
     * @param loginName
     * @param loginPwd
     * @param response
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<Result<Admins>> login(String loginName, String loginPwd, HttpServletResponse response) {
        System.out.println(loginName + ",,,,,,,,,,,,," + loginPwd);
        Result<Admins> result = adminsService.login(loginName, loginPwd);
        if (result.getCode() == 200) {
            Admins admin = result.getData();
            //生成jwt
            JwtBuilder builder = Jwts.builder();
            //设置唯一编号
            builder.setId(UUID.randomUUID().toString());
            //凭证创建的时间
            builder.setIssuedAt(new Date());
            //设置过期时间
            builder.setExpiration(new Date(System.currentTimeMillis() + 1000*60*6));
            //设置凭证验签的加密方式和密码
            builder.signWith(SignatureAlgorithm.HS256, jwtKey);
            //携带的参数
            builder.claim("adminId", admin.getAdminId());
            builder.claim("loginName", admin.getAdminLoginname());
            builder.claim("adminName", admin.getAdminName());
            builder.claim("level", admin.getLevel());
            builder.claim("pmses", admin.getPmses());
            builder.setSubject(admin.getAdminId() + "");
            String jwt = builder.compact();
            System.out.println(jwt);
            //将jwt放到响应头中
            response.setHeader("Authorization", jwt);


            return ResponseEntity.ok(result);

        }
        return ResponseEntity.status(400).body(result);
    }

    /**
     * 注册
     *
     * @param admins
     * @return
     */
    @Login
    @Allow(roles = {"超级管理员"}, pmses = {"新增管理员"})
    @PostMapping("register")
    public ResponseEntity<Result<Admins>> register(@RequestBody Admins admins) {
        System.out.println("api:" + admins.toString());
        Result<Admins> result = adminsService.register(admins);
        result.getData().setAdminLoginpwd(null);
        return ResponseEntity.ok(result);
    }

    public static final String QIANFAN_ACCESS_KEY = System.getenv("QIANFAN_ACCESS_KEY");
    public static final String QIANFAN_SECRET_KEY = System.getenv("QIANFAN_SECRET_KEY");

    /**
     * ai审核
     */
    @Scheduled(cron = "0 0 */2 * * ?")
    public void aiCheck() throws NoApiKeyException, InputRequiredException, InterruptedException {
        List<Weibos> weibos = weibosService.list2().getData();
        System.out.println("ai审核");
        Qianfan qianfan = new Qianfan(Auth.TYPE_OAUTH, QIANFAN_ACCESS_KEY, QIANFAN_SECRET_KEY);
        String order = "你是文章内容的审核员，你的任务是根据用户录入的文章的标题和内容进行审核，请结合上下文判断该文章及标题是否包含敏感和非法的词句，如果确定非法，则只返回-1;如果判定为可能非法，则只返回3;如果无不良用语则只返回1。请注意返回值不要包含其他文字。" +
                "举例：“他妈的傻逼”，这就属于违禁词，返回-1；“他妈做的饭真好吃”，这就不属于违禁词，返回1，当然，如果你不确定是否属于违禁词可以返回3。";
        for (Weibos weibo : weibos) {
            sleep(1000);
            StringBuilder sb = new StringBuilder();
            sb.append("标题:");
            sb.append(weibo.getWbTitle()).append("\n");
            sb.append("内容:");
            sb.append(weibo.getWbContent()).append("\n");
            System.out.print("审核内容：" + sb);

//            ChatResponse response = qianfan.chatCompletion()
//                    .model("Qianfan-Chinese-Llama-2-13B") // 使用model指定预置模型
//                    // .endpoint("completions_pro") // 也可以使用endpoint指定任意模型 (二选一)
//                    .addMessage("user", order + sb) // 添加用户消息 (此方法可以调用多次，以实现多轮对话的消息传递)
//                    .temperature(0.7) // 自定义超参数
//                    .execute(); // 发起请求
//            Integer answer = Integer.valueOf(response.getResult());
            Generation gen = new Generation();
            QwenParam params = QwenParam.builder().model("qwen-max")
                    .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                    .prompt(order+"需要审核的文章的标题和内容为："+sb)
                    .seed(1234)
                    .topP(0.8)
                    .resultFormat("message")
                    .enableSearch(false)
                    .maxTokens(50)
                    .temperature((float)0.85)
                    .repetitionPenalty((float)1.0)
                    .build();

            GenerationResult result = gen.call(params);
            Integer answer= Integer.valueOf(result.getOutput().getChoices().get(0).getMessage().getContent());
            System.out.println(weibo.getWbId()+"审核结果：" + answer);
            System.out.println();
            if (answer == -1) {
                weibosService.updateState(weibo.getWbId(), -1);
            } else if (answer == 3) {
                weibosService.updateState(weibo.getWbId(), 3);
            } else if (answer == 1) {
                weibosService.updateState(weibo.getWbId(), 1);
            }else{
                System.out.println(weibo.getWbId()+"审核结果错误:"+result.getOutput().getChoices().get(0).getMessage().getContent());
            }

//        System.out.println(response.getResult());

        }
    }
}
