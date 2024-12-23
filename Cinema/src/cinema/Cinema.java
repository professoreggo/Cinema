/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cinema;

import java.util.List;

/**
 *
 * @author mosta
 */
public class Cinema {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
    DBConnection db = new DBConnection();

    // Get all transaction logs
    List<log_table> logEntries = db.viewTransactionLog();

    // Print out the log entries
    for (log_table entry : logEntries) {
        System.out.println(entry);
    }
    
    
    }
}
