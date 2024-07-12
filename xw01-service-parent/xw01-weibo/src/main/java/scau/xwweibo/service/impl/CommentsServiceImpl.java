package scau.xwweibo.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import scau.xwcommon.entity.Comments;
import scau.xwcommon.entity.Weibos;
import scau.xwcommon.service.CommentsService;
import scau.xwcommon.util.Result;
import scau.xwweibo.mapper.CommentsMapper;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 86153
* @description 针对表【comments】的数据库操作Service实现
* @createDate 2024-07-03 11:22:55
*/
@RestController
public class CommentsServiceImpl implements CommentsService {


    @Autowired
    private CommentsMapper commentsMapper;

    @Override
    @Transactional
    public Result<Comments> addComents(Integer wbid, String content, String loginUsername) {
        Comments comments = new Comments();
        comments.setCmWeiboid(wbid);
        comments.setCmContent(content);
        comments.setCmUserLoginname(loginUsername);
        comments.setCmCreatetime(new Date());
        comments.setCmState(1);
        int result=commentsMapper.insert(comments);
        return result==1?Result.success(comments):Result.error("评论失败");
    }

    @Override
    public Result<Page<Comments>> list1(Integer pageNum, Integer pageSize, Integer state) {
        System.out.println(pageNum+" 1"+pageSize+" "+state);
        Page<Comments> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cm_state",state);
        Page<Comments> commentsPage = commentsMapper.selectPage(page, queryWrapper);
        return Result.success(commentsPage);
    }


    @Override
    public Result<List<Map<String, Object>>> listCountTop4(List<Weibos> weibos) {

        //根据微博id查询评论数前4的微博
        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("cm_weiboid as weiboId,COUNT(*) as count");
        queryWrapper.lambda().in(Comments::getCmWeiboid, weibos.stream().map(Weibos::getWbId).toArray(Integer[]::new))
                .eq(Comments::getCmState, 1)
                .groupBy(Comments::getCmWeiboid)
                .last("ORDER BY COUNT(*) DESC, max(cm_createTime) DESC LIMIT 4");
        List<Map<String, Object>> result = commentsMapper.selectMaps(queryWrapper);
        return Result.success(result);
    }


    @Override
    public Result<Page<Comments>> findByuserName(Integer pageNum, Integer pageSize, String userName) {
        Page<Comments> page=new Page<>(pageNum,pageSize);
        QueryWrapper<Comments> qw = new QueryWrapper<>();
        qw.lambda().eq(Comments::getCmUserLoginname,userName);
        Page<Comments> commentsPage = commentsMapper.selectPage(page, qw);
        return Result.success(commentsPage);
    }

    @Override
    public Result<Comments> update(Integer commentId, Integer state) {
        Comments comments = commentsMapper.selectById(commentId);
        comments.setCmState(state);
        int result = commentsMapper.updateById(comments);
        return result==1?Result.success(comments):Result.error("更新失败");
    }


}





