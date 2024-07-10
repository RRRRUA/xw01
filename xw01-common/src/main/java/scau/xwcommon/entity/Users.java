package scau.xwcommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName users
 */
@TableName(value ="users")
@Data
public class Users implements Serializable {
    /**
     * 
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 
     */
    @TableField(value = "user_nickname")
    private String userNickname;

    /**
     * 
     */
    @TableField(value = "user_loginname")
    private String userLoginname;

    /**
     * 
     */
    @TableField(value = "user_loginpwd")
    private String userLoginpwd;

    /**
     * 
     */
    @TableField(value = "user_photo")
    private String userPhoto;

    /**
     * 
     */
    @TableField(value = "user_score")
    private Integer userScore;

    /**
     * 
     */
    @TableField(value = "user_attionCount")
    private Integer userAttioncount;

    /**
     * 
     */
    @TableField(value = "user_state")
    private Integer userState;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}