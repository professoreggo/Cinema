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
public class Reservation_table implements Serializable{
    private int Reservation_id;
    private int Client_id;
    private int movie_id;
    private String status;

    public Reservation_table(int Reservation_id, int Client_id, int movie_id, String status) {
        this.Reservation_id = Reservation_id;
        this.Client_id = Client_id;
        this.movie_id = movie_id;
        this.status = status;
    }

    public int getReservation_id() {
        return Reservation_id;
    }

    public void setReservation_id(int Reservation_id) {
        this.Reservation_id = Reservation_id;
    }

    public int getClient_id() {
        return Client_id;
    }

    public void setClient_id(int Client_id) {
        this.Client_id = Client_id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
}
