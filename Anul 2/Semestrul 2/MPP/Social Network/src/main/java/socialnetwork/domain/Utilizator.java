package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Long>{
    private String userName;
    private String firstName;
    private String lastName;
    private List<Utilizator> friends=new ArrayList<Utilizator>();

    /**
     * Adauga un utilizator in lista de prieteni daca acesta nu este prezent
     * @param f utiliatorul adaugat
     */
    public void addFriend(Utilizator f){
        if(!friends.contains(f)) friends.add(f);
    }

    /**
     * Sterge un utilizator in lista de prieteni daca acesta este prezent
     * @param f utlizatorul sters
     */
    public void removeFriend(Utilizator f){
        if(friends.contains(f)) friends.remove(f);
    }

    public Utilizator(String userName, String firstName, String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * @return prenumele unui utilizator
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * seteaza prenumele unui utilizator
     * @param firstName prenumele utilizatorului
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return numele utilizatorului
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * seteaza numele  utilizatorului
     * @param lastName numele  utilizatorului
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return lista de prieteni a  utilizatorului
     */
    public List<Utilizator> getFriends() {
        return friends;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Utilizator -> " +
                " userName = " + userName +
                ", nume = " + firstName +
                ", prenume = " + lastName +
                ", friends = " + friends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        if ( getUserName()!=null && getFirstName()!=null && getLastName()!=null && getFriends() !=null)
            return getUserName().equals(that.getUserName()) &&
                getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getFirstName(), getLastName(), getFriends());
    }
}