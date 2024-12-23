/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cinema;

/**
 *
 * @author mosta
 */
public class Client_table {
    
    private int Client_id;
    private String username;
    private String email;
    private String password;

    public Client_table(int Client_id, String username, String email, String password) {
        this.Client_id = Client_id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public int getClient_id() {
        return Client_id;
    }

    public void setClient_id(int Client_id) {
        this.Client_id = Client_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
}
