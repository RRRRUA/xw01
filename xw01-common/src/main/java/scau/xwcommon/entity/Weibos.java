package scau.xwcommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.Data;

/**
 * 
 * @TableName weibos
 */
@TableName(value ="weibos")
@Data
public class Weibos implements Serializable {
    /**
     * 
     */
    @TableId(value = "wb_id", type = IdType.AUTO)
    private Integer wbId;

    /**
     * 
     */
    @TableField(value = "wb_user_loginname")
    private String wbUserLoginname;

    /**
     * 
     */
    @TableField(value = "wb_title")
    private String wbTitle;

    /**
     * 
     */
    @TableField(value = "wb_content")
    private String wbContent;

    /**
     * 
     */
    @TableField(value = "wb_createtime")
    private Date wbCreatetime;

    /**
     * 
     */
    @TableField(value = "wb_readcount")
    private Integer wbReadcount;

    /**
     * 
     */
    @TableField(value = "wb_img")
    private String wbImg;

    /**
     * 
     */
    @TableField(value = "wb_state")
    private Integer wbState;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Map<String, Object> map;

}