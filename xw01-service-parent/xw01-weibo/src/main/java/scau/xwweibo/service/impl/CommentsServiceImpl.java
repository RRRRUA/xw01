package scau.xwweibo.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scau.xwcommon.entity.Comments;
import scau.xwcommon.service.CommentsService;
import scau.xwcommon.util.Result;
import scau.xwweibo.mapper.CommentsMapper;

import java.util.Date;

/**
* @author 86153
* @description 针对表【comments】的数据库操作Service实现
* @createDate 2024-07-03 11:22:55
*/
@Service
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
    public Result<Page<Comments>> list(Integer pageNum, Integer pageSize, Integer state) {
        Page<Comments> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cm_state",state);
        Page<Comments> commentsPage = commentsMapper.selectPage(page, queryWrapper);
        return Result.success(commentsPage);
    }


}




