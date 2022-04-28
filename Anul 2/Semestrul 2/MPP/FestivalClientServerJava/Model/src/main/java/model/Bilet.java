package model;

import java.io.Serializable;

public class Bilet implements Identifiable<Integer>, Serializable {

    private int IDBilet;
    private Spectacol show;
    private int noTickets;
    private String BuyerName;


    public Bilet(int IDBilet, Spectacol show, int noTickets, String buyerName) {
        this.IDBilet = IDBilet;
        this.show = show;
        this.noTickets = noTickets;
        BuyerName = buyerName;
    }

    @Override
    public Integer getID() {
        return IDBilet;
    }

    @Override
    public void setID(Integer idBilet) {
        IDBilet = idBilet;
    }

    public int getIDBilet() {
        return IDBilet;
    }

    public void setIDBilet(int IDBilet) {
        this.IDBilet = IDBilet;
    }

    public Spectacol getShow() {
        return show;
    }

    public void setShow(Spectacol show) {
        this.show = show;
    }

    public int getNoTickets() {
        return noTickets;
    }

    public void setNoTickets(int noTickets) {
        this.noTickets = noTickets;
    }

    public String getBuyerName() {
        return BuyerName;
    }

    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    @Override
    public String toString() {
        return "Bilet{" +
                "IDBilet="+IDBilet +
                "Spectacol=" + show +
                ", noTickets=" + noTickets +
                ", BuyerName='" + BuyerName + '\'' +
                '}';
    }

}
