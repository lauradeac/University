package socialnetwork.domain;

import java.time.*;


public class Prietenie extends Entity<Tuple<Long,Long>> {

    /**
     * date: data la care a fost creata prietenia
     */
    LocalDate date;

    public Prietenie(Long id1,Long id2,int an,int luna,int zi) {
        Tuple<Long,Long> id=new Tuple<Long,Long>(id1,id2);
        setId(id);
        date=LocalDate.of(an,luna,zi);
    }

    public Prietenie(Long id1,Long id2, LocalDate date) {
        Tuple<Long,Long> id=new Tuple<Long,Long>(id1,id2);
        setId(id);
        this.date = date;
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date){
        this.date=date;
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
