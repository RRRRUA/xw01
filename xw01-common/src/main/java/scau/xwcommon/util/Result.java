package scau.xwcommon.util;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 统一返回对象Result
 *
 * @author gugu
 * @since 2024-07-03 10:51:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -41540875952145013L;
    private Integer code; //状态码, 200成功, 500失败
    private T Data;
    private String message;

    // 成功的静态方法
    public static <T> Result<T> success(T data) {
        return new Result<>(200, data, null);
    }

    public static <T> Result<T> success() {
        return new Result<>(200, null, null);
    }

    // 失败的静态方法
    public static <T> Result<T> error(String message) {
        return new Result<>(500, null, message);
    }
}

