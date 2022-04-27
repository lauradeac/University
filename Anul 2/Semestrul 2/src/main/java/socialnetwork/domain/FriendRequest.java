package socialnetwork.domain;

import java.time.LocalDate;

public class FriendRequest extends Entity<Tuple<Long, Long>> {
    private Utilizator user1;
    private Utilizator user2;
    private Enum<Status> status;
    LocalDate date;

    public FriendRequest(Utilizator user1, Utilizator user2,int an,int luna, int zi) {
        this.user1 = user1;
        this.user2 = user2;
        Tuple<Long,Long> id=new Tuple<Long,Long>(user1.getId(), user2.getId());
        setId(id);
        this.status = Status.PENDING;
        date=LocalDate.of(an,luna,zi);
    }



    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date){
        this.date=date;
    }

    public Utilizator getUser1() {
        return user1;
    }

    public void setUser1(Utilizator user1) {
        this.user1 = user1;
    }

    public Utilizator getUser2() {
        return user2;
    }

    public void setUser2(Utilizator user2) {
        this.user2 = user2;
    }

    public Enum<Status> getStatus() {
        return status;
    }

    public void setStatus(Enum<Status> status) {
        this.status = status;
    }

    public int getYear(){
        return date.getYear();
    }

    public int getMonth(){
        return date.getMonthValue();
    }

    public int getDay(){
        return date.getDayOfMonth();
    }
}