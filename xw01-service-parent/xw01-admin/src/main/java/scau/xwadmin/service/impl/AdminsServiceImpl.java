package scau.xwadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import scau.xwadmin.mapper.AdminsMapper;
import scau.xwcommon.entity.Admins;
import scau.xwcommon.service.AdminsService;
import scau.xwcommon.util.Result;

import java.util.List;

/**
* @author 86153
* @description 针对表【admins】的数据库操作Service实现
* @createDate 2024-07-08 16:00:33
*/
@RestController
public class AdminsServiceImpl implements AdminsService {

    @Autowired
    private AdminsMapper adminsMapper;
    @Override
    @Transactional
    public Result<Admins> register(Admins admins) {
        //加密密码
        System.out.println("service:"+admins.toString());
        String hashpwd= BCrypt.hashpw(admins.getAdminLoginpwd(),BCrypt.gensalt());
        System.out.println(hashpwd);
        admins.setAdminLoginpwd(hashpwd);
        adminsMapper.insert(admins);
        return Result.success(admins);
    }

    @Override
    public Result<Admins> login(String loginName, String loginPwd) {
        System.out.println(loginName+",,,,,,,,,,,,0,"+loginPwd);
        QueryWrapper<Admins> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(Admins::getAdminLoginname,loginName);
        Admins admin=adminsMapper.selectOne(queryWrapper);
        if(admin==null){
            return Result.error("用户不存在");
        }
        System.out.println(admin.toString());
        //验证密码
        if(BCrypt.checkpw(loginPwd,admin.getAdminLoginpwd())){
            List<String> roles=adminsMapper.selectRoles(admin.getAdminId());
            List<String> pmses=adminsMapper.selectPmses(admin.getAdminId());
            admin.setLevel(roles);
            admin.setPmses(pmses);
            admin.setAdminLoginpwd(null);
            return Result.success(admin);
        }
        else{
            return Result.error("密码错误");
        }
    }
}




