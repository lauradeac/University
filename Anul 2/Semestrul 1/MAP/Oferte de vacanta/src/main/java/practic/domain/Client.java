package practic.domain;

public class Client extends Entity<Long>{
    private Long clientId;
    private String name;
    private int fidelityGrade;
    private int age;
    private Enum<Hobby> hobby;

    public Client(Long clientId, String name, int fidelityGrade, int age, Enum<Hobby> hobby) {
        this.clientId = clientId;
        this.name = name;
        this.fidelityGrade = fidelityGrade;
        this.age = age;
        this.hobby = hobby;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFidelityGrade() {
        return fidelityGrade;
    }

    public void setFidelityGrade(int fidelityGrade) {
        this.fidelityGrade = fidelityGrade;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Enum<Hobby> getHobby() {
        return hobby;
    }

    public void setHobby(Enum<Hobby> hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", name='" + name + '\'' +
                ", fidelityGrade=" + fidelityGrade +
                ", age=" + age +
                ", hobby=" + hobby +
                '}';
    }
}
