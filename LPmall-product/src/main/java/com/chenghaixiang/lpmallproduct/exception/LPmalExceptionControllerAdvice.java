package com.chenghaixiang.lpmallproduct.exception;

import com.chenghaixiang.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@Slf4j
@ResponseBody
@ControllerAdvice(basePackages = "com.chenghaixiang.lpmallproduct.controller")
public class LPmalExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e){
        log.error("数据异常{}，{}",e.getMessage(),e.getClass());

        // 如果精确匹配才有e.getBindingResult()获得一个异常的结果
        Map<String,String> error=new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().forEach(fieldError -> error.put(fieldError.getField(),fieldError.getDefaultMessage()));
        return R.error().put("data",error);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        return R.error();
    }
}
