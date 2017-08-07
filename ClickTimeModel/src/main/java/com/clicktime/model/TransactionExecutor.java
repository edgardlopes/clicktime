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
public class TransactionExecutor {
    public <T> T execute(Transaction<T> w) throws Exception {
        Connection conn = ConnectionManager.getInstance().getConnection();
        T run = null;
        try {
            run = w.run(conn);
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
        return run;
    }
}
