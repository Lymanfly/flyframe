/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
@Builder
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

    public static Messenger<Boolean> result(boolean flag){
        if (flag){
            return success();
        }
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
        return (Messenger<T>) Messenger.builder()
                .code(status.getCode())
                .message(message)
                .data(data)
                .success(success)
                .timestamp(new Date())
                .build();
    }

    public static Messenger<Boolean> success(){
        return success(null);
    }

    public static <T> Messenger<T> success(T data){
        return result(Status.SUCCESS, data);
    }

    public static <T> Messenger<T> success(T data, String message){
        return result(Status.SUCCESS, message, data);
    }

    public static Messenger<Boolean> fail(Status status){
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

    public static Messenger<Boolean> fail() {
        return fail(Status.FAIL);
    }
}