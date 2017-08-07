/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clicktime.model;

import java.sql.Connection;

/**
 *
 * @author edgard
 */
public interface Transaction<T> {

    T run(Connection conn) throws Exception;

}
