package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一捕获异常类
 */
@ControllerAdvice//控制器增强
public class ExceptionCatch {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    //定义map，配置异常类型所对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    //定义map的builder对象，去构建ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();


    // 捕获CustomException
    @ExceptionHandler(CustomException.class)
    public
    @ResponseBody //错误信息以json格式返回给前端
    ResponseResult customException(CustomException customException) {
        LOGGER.error("catch exception:{}", customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

    //捕获不可预知Exception
    @ExceptionHandler(Exception.class)
    public
    @ResponseBody //错误信息以json格式返回给前端
    ResponseResult exception(Exception exception) {
        LOGGER.error("catch exception:{}", exception.getMessage());
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();//EXCEPTION构建成功
        }
        //从EXCEPTION中赵异常类型所对应的错误代码，如果找到了错误代码就响应给用户，如果找不到给用户相应99999异常
        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
        if (resultCode != null) {
            return new ResponseResult(resultCode); //找到了可能存在的异常
        } else {
            //返回99999异常
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }

    static {
        //定义了异常的错误代码
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }
}
