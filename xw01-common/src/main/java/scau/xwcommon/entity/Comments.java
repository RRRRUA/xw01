package scau.xwcommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName comments
 */
@TableName(value ="comments")
@Data
public class Comments implements Serializable {
    /**
     * 
     */
    @TableId(value = "cm_id", type = IdType.AUTO)
    private Integer cmId;

    /**
     * 
     */
    @TableField(value = "cm_weiboid")
    private Integer cmWeiboid;

    /**
     * 
     */
    @TableField(value = "cm_user_loginname")
    private String cmUserLoginname;

    /**
     * 
     */
    @TableField(value = "cm_content")
    private String cmContent;

    /**
     * 
     */
    @TableField(value = "cm_createtime")
    private Date cmCreatetime;

    /**
     * 
     */
    @TableField(value = "cm_state")
    private Integer cmState;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}