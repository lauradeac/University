package socialnetwork.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserFriendshipDTO {
    Long id;
    private String firstName;
    private String lastName;
    private String userName;
    LocalDate date;

    public UserFriendshipDTO(Long id, String userName, String firstName, String lastName, LocalDate date) {
        this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.userName=userName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName()
    {
        this.userName=userName;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getId(){return id;}

    @Override
    public String toString() {
        return id+"|" + userName+"|" +lastName+"|"+firstName+"|"+date;
    }
}
