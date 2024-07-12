package scau.xwuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scau.xwcommon.entity.Comments;
import scau.xwcommon.entity.Users;
import scau.xwcommon.service.UsersService;
import scau.xwcommon.service.WeibosService;
import scau.xwcommon.util.Result;
import scau.xwuser.mapper.UsersMapper;

@RestController
public class UsersServiceImpl implements UsersService  {
    //继承UserService里面的网址，不用写@RequestMapping和@RequestParam
    //@RequestMapping("user/addScore")

    @Autowired
    private UsersMapper usersMapper;
    @Qualifier("scau.xwcommon.service.WeibosService")
    @Autowired
    private WeibosService weibosService;

    @Override
    @Transactional
    public Result<Users> reg(String nickname, String loginName, String loginPwd, String photo) {
        System.out.println("nickname:"+nickname+" loginName:"+loginName);
//        weibosService.addWeibo(nickname, "注册", "注册成功");
//        Users existUser = usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUserLoginname, loginName));
//        if(existUser!=null){
//            return Result.error("用户名已存在");
//        }

        Users users = new Users();
        users.setUserNickname(nickname);
        users.setUserLoginname(loginName);
        users.setUserLoginpwd(loginPwd);
        users.setUserPhoto(photo);
        users.setUserScore(100);
        users.setUserState(1);
        users.setUserAttioncount(0);
        //this.save(users);
        int result=usersMapper.insert(users);
        return result==1?Result.success(users):Result.error("注册失败");
    }

    @Override
    public Result<Users> login(String loginName, String loginPwd) {
        Users users=usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUserLoginname,loginName).eq(Users::getUserLoginpwd,loginPwd));
        return users==null?Result.error("用户名或密码错误"):Result.success(users);
    }

    @Override
    @Transactional
    public Result<Integer> addScore(String loginName, Integer score) {
        Users users=usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUserLoginname,loginName));
        if(users==null){
            return Result.error("用户不存在");
        }
        users.setUserScore(users.getUserScore()+score);
        int result=usersMapper.updateById(users);
        return result==1?Result.success(users.getUserScore()):Result.error("增加积分失败");
    }

    @Override
    public Result<Integer> updateStatus(String loginName, Integer status) {
        Users users=usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUserLoginname,loginName));
        if(users==null){
            return Result.error("用户不存在");
        }
        users.setUserState(status);
        int result=usersMapper.updateById(users);
        return result==1?Result.success(users.getUserState()):Result.error("修改状态失败");
    }

    @Override
    public Result<Users> findByLoginName(String userLoginName) {
        Users users=usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUserLoginname,userLoginName));
        return users==null?Result.error("用户不存在"):Result.success(users);
    }

    @Override
    public Result<Page<Users>> list(int pageNum, int pageSize, int status) {
        Page<Users> page=new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Users> qw=new LambdaQueryWrapper<>();
        qw.eq(Users::getUserState,status);
        Page<Users> usersPage=usersMapper.selectPage(page,qw);
        return Result.success(usersPage);
    }




}
