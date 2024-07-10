package scau.xwweibo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import scau.xwcommon.entity.Weibos;
import scau.xwcommon.service.WeibosService;
import scau.xwcommon.util.Result;
import scau.xwweibo.mapper.WeibosMapper;

import java.util.Date;
import java.util.List;


//在微服务中，业务层本身已经对外提供服务（为其他兄弟微服务提供远程调用）
@RestController
public class WeiboServiceImpl implements WeibosService {
    @Autowired
    private WeibosMapper weibosMapper;

    @Override
    @Transactional
    public Result<Weibos> addWeibo(String userLoginName, String title, String content, String img) {

        Weibos weibos = new Weibos();
        weibos.setWbTitle(title);
        weibos.setWbUserLoginname(userLoginName);
        weibos.setWbContent(content);
        weibos.setWbImg(img);
        weibos.setWbReadcount(0);
        weibos.setWbState(1);
        weibos.setWbCreatetime(new Date());
        int result=weibosMapper.insert(weibos);
        if(result>0){
            return Result.success(weibos);
        }
        return Result.error("发表微博失败");
    }

    @Override
    public Result<Weibos> findById(Integer wbId) {
        Weibos weibos = weibosMapper.selectById(wbId);
        if(weibos!=null){
            return Result.success(weibos);
        }
            return Result.error("微博不存在");
    }

    @Override
    public Result<Page<Weibos>> findByUsername(String userName, Integer pageNum, Integer pageSize, Integer state) {
        Page<Weibos> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Weibos> queryWrapper = new LambdaQueryWrapper<Weibos>()
                .eq(Weibos::getWbUserLoginname,userName)
                .eq(Weibos::getWbState,state)
                .orderByDesc(Weibos::getWbCreatetime);

//        weibosMapper.selectPage(page,new QueryWrapper<Weibos>().lambda()
//                .eq(Weibos::getWbUserLoginname,userName)
//                .eq(Weibos::getWbState,state));

        page= weibosMapper.selectPage(page,queryWrapper);
        System.out.println("查询到的微博："+page.getRecords());
        return Result.success(page);
    }

    @Override
    public Result<Page<Weibos>> list(Integer pageNum, Integer pageSize, String findtxt, Integer state) {
        Page<Weibos> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Weibos> queryWrapper = new LambdaQueryWrapper<Weibos>()
                .like(Weibos::getWbTitle,findtxt).or().like(Weibos::getWbContent,findtxt)
                .eq(Weibos::getWbState,state);

        QueryWrapper<Weibos> qw= new QueryWrapper<>();
        qw.lambda().eq(Weibos::getWbState,state)
                .and(q->q.like(Weibos::getWbTitle,findtxt).or().like(Weibos::getWbContent,findtxt));
        page=weibosMapper.selectPage(page,queryWrapper);
        return Result.success(page);
    }

    @Override
    public Result<Integer> updateReadCount(Integer wbId) {
        Weibos weibos = weibosMapper.selectById(wbId);
        weibos.setWbReadcount(weibos.getWbReadcount()+1);
        weibosMapper.updateById(weibos);
        return Result.success(weibos.getWbReadcount());
    }

    @Override
    public Result<Integer> updateState(Integer wbId, Integer state) {
        LambdaQueryWrapper<Weibos> queryWrapper = new LambdaQueryWrapper<Weibos>()
                .eq(Weibos::getWbId,wbId);
        Weibos weibos = weibosMapper.selectOne(queryWrapper);
        weibos.setWbState(state);
        int result=weibosMapper.updateById(weibos);
        if(result>0){
            return Result.success(state);
        }
        return Result.error("修改状态失败");
    }

    @Override
    public Result<Integer> findcountByUser(String userLoginName, Integer state) {
        return null;
    }

    @Override
    public Result<Integer> delete(Integer wbId) {
        return null;
    }

    @Override
    public Result<List<Weibos>> list2() {
        QueryWrapper<Weibos> qw= new QueryWrapper<>();
        qw.lambda().eq(Weibos::getWbState,2);
        List<Weibos> list=weibosMapper.selectList(qw);
        System.out.println("查询到的微博："+list);
        return Result.success(list);
    }

    //业务层不应该调用别的微服务  eg 发表微博加分，自动发的不加分
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private UserService userService;

//    @Override
//    public Result addWeibo(String username, String title, String content) {
//        System.out.println(username + "发表了" + title + ",内容为:" + content+"插入数据库");
//        //1、远程调用user微服务，为用户增加积分
////        String r=restTemplate.getForObject(
////                "http://localhost:8080/user/addScore?username="+username+"&score="+10,String.class);
////        System.out.println("不是哥们"+restTemplate.getClass().getName());
////        String r=restTemplate.getForObject(
////                "http://xw01-user/user/addScore?username="+username+"&score="+10,String.class);
//
///*        等价于2、本地调用，但实际上是远程调用
//        @Autowired
//        private UserService userService;
//        String r=userService.addScore(username,10);*/
//
////        System.out.println("都是哥们"+userService.getClass().getName());
////        String r=userService.addScore(username,10);
////        return username+"添加微博成功"+title+"并且"+r;
//        return Result.success("添加微博成功");
//    }


}
