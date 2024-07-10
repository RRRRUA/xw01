package scau.xwuser.service.impl;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.bind.annotation.RestController;
import scau.xwcommon.entity.Attentions;
import scau.xwuser.mapper.AttentionsMapper;
import org.springframework.stereotype.Service;

/**
* @author 86153
* @description 针对表【attentions】的数据库操作Service实现
* @createDate 2024-07-03 11:22:55
*/
@RestController
public class AttentionsServiceImpl extends ServiceImpl<AttentionsMapper, Attentions>
    implements IService<Attentions> {

}




