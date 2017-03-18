/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.clicktime.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author Edgard Lopes <edgard-rodrigo@hotmail.com>
 */
@Controller
public class PrincipalController {
    @RequestMapping(value = "/erro", method = RequestMethod.GET)
    public String erro(){
        return "/error_usuario";
    }
}
