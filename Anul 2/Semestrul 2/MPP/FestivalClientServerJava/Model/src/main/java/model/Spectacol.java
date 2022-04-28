package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


public class Spectacol implements Identifiable<Integer>, Serializable {

    private int ID;
    private String artistName;
    private LocalDate dateOfShow;
    private LocalTime time;
    private String location;
    private int remainingTickets;
    private int totalTickets;

    public Spectacol(int ID, String artistName, LocalDate dateOfShow, LocalTime time, String location, int remainingTickets, int totalTickets) {
        this.ID = ID;
        this.artistName = artistName;
        this.dateOfShow = dateOfShow;
        this.time = time;
        this.location = location;
        this.remainingTickets = remainingTickets;
        this.totalTickets = totalTickets;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public LocalDate getDateOfShow() {
        return dateOfShow;
    }

    public void setDateOfShow(LocalDate dateOfShow) {
        this.dateOfShow = dateOfShow;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRemainingTickets() {
        return remainingTickets;
    }

    public void setRemainingTickets(int remainingTickets) {
        this.remainingTickets = remainingTickets;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer id) {
        this.ID = id;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "ID=" + ID +
                ", artistName='" + artistName + '\'' +
                ", dateOfShow=" + dateOfShow +
                ", time=" + time +
                ", location='" + location + '\'' +
                ", remainingTickets=" + remainingTickets +
                ", totalTickets=" + totalTickets +
                '}';
    }

}
