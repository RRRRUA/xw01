package scau.xwattention.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.bind.annotation.RestController;
import scau.xwattention.mapper.AttentionsMapper;
import org.springframework.stereotype.Service;
import scau.xwcommon.entity.Attentions;
import scau.xwcommon.service.AttentionsService;
import scau.xwcommon.util.Result;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 64573
* @description 针对表【attentions】的数据库操作Service实现
* @createDate 2024-07-11 16:04:56
*/
@RestController
public class AttentionsServiceImpl implements AttentionsService {
@Resource
private AttentionsMapper attentionsMapper;
    @Override
    public Result<List<Attentions>> findMyAttention(Integer pageNum, Integer pageSize,String userLoginname) {
        Page<Attentions> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Attentions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attentions::getAttUserLoginname, userLoginname);
        List<Attentions> attentionsList = attentionsMapper.selectPage(page, wrapper).getRecords();
        return Result.success(attentionsList);
    }

    @Override
    public Result<Integer> deleteAttention(String marstLoginname, String userLoginname) {
        LambdaQueryWrapper<Attentions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attentions::getAttUserLoginname, userLoginname)
                .eq(Attentions::getAttMarstLoginname, marstLoginname);
        int count = attentionsMapper.delete(wrapper);
        if(count!=1)
            return Result.error("删除失败");
        return Result.success(count);
    }

    @Override
    public Result<Integer> addAttention(String marstLoginname, String userLoginname) {

        Attentions attentions = new Attentions();
        attentions.setAttMarstLoginname(marstLoginname);
        attentions.setAttUserLoginname(userLoginname);
        int count = attentionsMapper.insert(attentions);
        if(count!=1)
            return Result.error("添加失败");
        return Result.success(count);
    }

    @Override
    public Result<Boolean> attentionStatus(String marstLoginname, String userLoginname) {
        LambdaQueryWrapper<Attentions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attentions::getAttUserLoginname, userLoginname)
                .eq(Attentions::getAttMarstLoginname, marstLoginname);
        List<Attentions> attentionsList = attentionsMapper.selectList(wrapper);
        if(attentionsList.size() == 0)
            return Result.success(false);
        return Result.success(true);
    }
}




