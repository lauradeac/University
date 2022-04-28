package model;

import java.io.Serializable;
import java.util.Objects;

public class Angajat implements Identifiable<Integer>, Serializable {

    private int ID;
    private String userName;
    private String password;

    public Angajat(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer id) {
        ID = id;
    }

    @Override
    public String toString() {
        return "Angajat{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Angajat angajat = (Angajat) o;
        return Objects.equals(ID, angajat.ID) && Objects.equals(userName, angajat.userName) && Objects.equals(password, angajat.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, userName, password);
    }
}
