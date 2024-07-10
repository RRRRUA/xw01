package scau.xwcommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName attentions
 */
@TableName(value ="attentions")
@Data

public class Attentions implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer attId;

    /**
     * 
     */
    private String attUserLoginname;

    /**
     * 
     */
    private String attMarstLoginname;

    @TableField(exist = false)
    private Map<String,Object> map=new HashMap<>();

}