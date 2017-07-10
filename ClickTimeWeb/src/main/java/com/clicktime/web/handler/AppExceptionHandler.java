package com.clicktime.web.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author edgard
 */
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllException(Exception ex) {
        ModelAndView mv = new ModelAndView("/error");
        ex.printStackTrace();
        //  ex.printStackTrace(); imprime a stacktrace de erro
        return mv;
    }
}
