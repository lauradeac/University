package socialnetwork.domain;

import java.time.LocalDate;
import java.util.Objects;

public class FriendRequestsDTO {
    Tuple<Long, Long> id;
    private String userName;
    private String firstName;
    private String lastName;
    private String status;

    LocalDate date;

    public FriendRequestsDTO( Tuple<Long,Long> id, String username, String firstName, String lastName,String status, LocalDate date) {
        this.id=id;
        this.firstName = firstName;
        this.lastName= lastName;
        this.date = date;
        this.status=status;
        this.userName=username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String nume) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String nume) {
        this.userName = userName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String prenume) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tuple<Long, Long> getId() {
        return id;
    }

    public void setId(Tuple<Long, Long> id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FriendRequestsDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequestsDTO that = (FriendRequestsDTO) o;
        return id.equals(that.id) && userName.equals(that.userName) && firstName.equals(that.firstName) && lastName.equals(that.lastName) && status.equals(that.status) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, firstName, lastName, status, date);
    }
}
