package org.lyman.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lyman.utils.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * REST API 返回结果
 * </p>
 *
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class Messenger<T> implements Serializable {
	private static final long serialVersionUID = -7768268377330879232L;

	/**
     * 响应码
     */
    private int code;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    public Messenger() {
        timestamp = new Date();
    }

    public static Messenger<String> result(boolean flag){
        if (flag)
            return success();
        return fail();
    }

    public static Messenger<Boolean> result(Status status){
        return result(status,null);
    }

    public static <T> Messenger<T> result(Status status, T data){
        return result(status,null,data);
    }

    public static <T> Messenger<T> result(Status status, String message, T data){
        boolean success = false;
        if (status.getCode() == Status.SUCCESS.getCode())
            success = true;
        if (StringUtils.isEmpty(message))
            message = status.getMessage();
        return new Messenger<T>()
                .setCode(status.getCode())
                .setMessage(message)
                .setData(data)
                .setSuccess(success)
                .setTimestamp(new Date());
    }

    public static Messenger<String> success(){
        return success(null);
    }

    public static <T> Messenger<T> success(T data){
        return result(Status.SUCCESS, data);
    }

    public static <T> Messenger<T> success(T data, String message){
        return result(Status.SUCCESS, message, data);
    }

    public static Messenger<String> fail(Status status){
        return result(status,null);
    }

    public static Messenger<String> fail(String message){
        return result(Status.FAIL,message,null);
    }

    public static <T> Messenger<T> fail(Status status, T data){
        if (Status.SUCCESS == status)
            throw new RuntimeException("失败结果状态码不能为" + Status.SUCCESS.getCode());
        return result(status,data);
    }

    public static Messenger<String> fail(Integer errorCode, String message){
        return new Messenger<String>()
                .setSuccess(false)
                .setCode(errorCode)
                .setMessage(message);
    }

    public static Messenger<String> fail() {
        return fail(Status.FAIL);
    }
}