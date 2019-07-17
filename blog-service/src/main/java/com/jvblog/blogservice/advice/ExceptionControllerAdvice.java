package com.jvblog.blogservice.advice;

import com.jvblog.service.exception.BlogException;
import com.jvblog.service.exception.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice  // 拦截加了controller注解的类
public class ExceptionControllerAdvice {


    @ExceptionHandler(BlogException.class)    //拦截不同异常实现异常分类处理
    public ResponseEntity<ExceptionResult> handleException(BlogException e){
        log.debug(e.getMessage());
        return ResponseEntity.status(e.getCode()).body(new ExceptionResult(e.getExceptionEnum()));
    }


    //@ExceptionHandler(BlogException.class)
    public Object jsonErrorHandler(HttpServletRequest req, BlogException e){
        ModelAndView modelAndView = new ModelAndView();
        //modelAndView.addObject("msg", e.getMessage());
        //modelAndView.addObject("code", e.getCode());
        //modelAndView.addObject("url", req.getRequestURL());
        modelAndView.setViewName("error");

        return modelAndView;
    }
}
