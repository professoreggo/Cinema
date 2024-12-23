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
public class Movie_table implements Serializable{
    private int id;
    private String name;
    private String time;
    private int reservationNumber;

    public Movie_table(int id, String name, String time, int reservationNumber) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.reservationNumber = reservationNumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(int reservationNumber) {
        this.reservationNumber = reservationNumber;
    }
    
    
   
    
    
    
    
    
    
}
