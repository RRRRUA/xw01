package scau.xwadmin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import scau.xwcommon.entity.Admins;

import java.util.List;

/**
* @author 86153
* @description 针对表【admins】的数据库操作Mapper
* @createDate 2024-07-08 16:00:33
* @Entity scau.xwadmin.Admins
*/
public interface AdminsMapper extends BaseMapper<Admins> {
    @Select("select r.rolename from sec_role r\n" +
            "join sec_user_role ur on ur.roleid=r.roleid\n" +
            "join admins a on a.admin_id=ur.userid\n" +
            "where a.admin_id=#{adminid}")
    List<String> selectRoles(int adminid);

    @Select("select distinct(p.pmsname) from sec_role r\n" +
            "join sec_user_role ur on ur.roleid=r.roleid\n" +
            "join admins a on a.admin_id=ur.userid\n" +
            "join sec_role_permission rp on rp.roleid=r.roleid\n" +
            "join sec_permission p on rp.pmsid=p.pmsid\n" +
            "where a.admin_id=#{adminid}\n" +
            "UNION\n" +
            "select pmsname from sec_user_add_permission uap \n" +
            "join sec_permission p on uap.pmsid=p.pmsid\n" +
            "where userid=#{adminid}")
    List<String> selectPmses(int adminid);
}




