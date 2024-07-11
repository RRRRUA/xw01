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
}




