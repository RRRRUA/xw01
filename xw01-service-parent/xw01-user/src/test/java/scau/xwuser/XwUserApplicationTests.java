package scau.xwuser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import scau.xwcommon.service.UsersService;

import javax.annotation.Resource;

@SpringBootTest
class XwUserApplicationTests {
    @Resource
    private UsersService xwUserService;
    @Test
    void contextLoads() {
        xwUserService.reg( "test", "123456", "123456", "123456");
    }

}
