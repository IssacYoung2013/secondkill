package com.issac.seckill.exception;

import com.issac.seckill.result.CodeMsg;
import com.issac.seckill.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobleExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletRequest request,Exception e) {
        if(e instanceof GlobleException) {
            GlobleException ex = (GlobleException)e;
            return Result.error(ex.getCm());
        }
        else if(e instanceof BindException) {
            BindException ex = (BindException)e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);

            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else  {
            log.info(e.getMessage());
            return Result.error(CodeMsg.ERROR);
        }
    }
}
