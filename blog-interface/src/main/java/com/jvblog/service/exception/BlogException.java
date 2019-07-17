package com.jvblog.service.exception;

import com.jvblog.service.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author javie
 * @date 2019/6/3 13:56
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BlogException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    @Override
    public String getMessage() {
        return exceptionEnum.getMsg();
    }
    public int getCode(){
        return exceptionEnum.getCode();
    }
}
