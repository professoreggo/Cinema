/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cinema;

import java.io.Serializable;

/**
 *
 * @author mosta
 */
public class log_table implements Serializable{
    private int transaction_id;
    private String info;

    public log_table(int transaction_id, String info) {
        this.transaction_id = transaction_id;
        this.info = info;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
    
    
}
