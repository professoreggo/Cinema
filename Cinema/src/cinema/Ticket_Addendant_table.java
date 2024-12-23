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
public class Ticket_Addendant_table {
    private int ticket_attendant_id;
    private String name;
    private String email;
    private String Password;

    public Ticket_Addendant_table(int ticket_attendant_id, String name, String email, String Password) {
        this.ticket_attendant_id = ticket_attendant_id;
        this.name = name;
        this.email = email;
        this.Password = Password;
    }

    public int getTicket_attendant_id() {
        return ticket_attendant_id;
    }

    public void setTicket_attendant_id(int ticket_attendant_id) {
        this.ticket_attendant_id = ticket_attendant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
    
    
    
}
