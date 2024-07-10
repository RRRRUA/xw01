package scau.xwadmin;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import scau.xwadmin.api.AdminApi;
import scau.xwadmin.service.impl.AdminsServiceImpl;
import scau.xwcommon.entity.Admins;
import scau.xwcommon.entity.Weibos;
import scau.xwcommon.service.AdminsService;
import scau.xwcommon.service.WeibosService;
import scau.xwcommon.util.Result;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@SpringBootTest
class Xw01AdminApplicationTests {


    @Autowired
    private AdminApi adminsService;

//    @Qualifier("scau.xwcommon.service.WeibosService")
    @Autowired
    private WeibosService weibosService;
    @Test
    void contextLoads() throws NoApiKeyException, InputRequiredException, InterruptedException {
        adminsService.aiCheck();
//        Result<List<Weibos>>result=weibosService.list2();
//        System.out.println(result);
    }

    @Test
    void password1() throws NoSuchAlgorithmException {
        //散列加密 只能加密不能解密MD5 Bcrypt,负担小，运行速度快 乱码比对
        MessageDigest md5=MessageDigest.getInstance("MD5");
        String key="gugu";//增加md5的破解难度 -->加盐
        String password="123456"+key;
        byte[] digest = md5.digest(password.getBytes());
        for(byte b:digest){//e10adc3949ba59abbe56e057f20f883e
            System.out.printf("%02x",b);
        }
    }
    @Test
    void password2(){
        String hashpwd=BCrypt.hashpw("123456",BCrypt.gensalt());
        //$2a$10$bHCV9osMsP6LXtlkDzhjguz2IbobwpqGI9tIOe6I3GivCPBoQUv/m
        //每次都会变化
        System.out.println(hashpwd);
        Boolean match=BCrypt.checkpw("123456",hashpwd);
        System.out.println(match);//match=ture
    }
    //散列算法运行速度快 MD5 SHA1 BCrypt
    //对称加密法，密钥相同，加密解密都用这个密钥 AED DES 能压缩大量数据，但无法判断数据是否被篡改
    //非对称加密法，公钥加密，私钥解密 RSA ECC 私钥加密，公钥解密 用于数字签名 运算速度慢

}
