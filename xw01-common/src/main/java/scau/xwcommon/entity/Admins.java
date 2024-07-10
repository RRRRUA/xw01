package scau.xwcommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 
 * @TableName admins
 */
@TableName(value ="admins")
@Data
public class Admins implements Serializable {
    /**
     * 
     */
    @TableId(value = "admin_id", type = IdType.AUTO)
    private Integer adminId;

    /**
     * 
     */
    @TableField(value = "admin_name")
    private String adminName;

    /**
     * 
     */
    @TableField(value = "admin_loginname")
    private String adminLoginname;

    /**
     * 
     */
    @TableField(value = "admin_loginpwd")
    private String adminLoginpwd;

    /**
     *角色
     */
    @TableField(exist = false)
    private List<String> level;

    /**
     * 权限
     */
    @TableField(exist = false)
    private List<String> pmses;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}