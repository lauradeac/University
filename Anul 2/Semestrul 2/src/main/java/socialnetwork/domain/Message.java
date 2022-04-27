package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    private Utilizator from;
    private List<Utilizator> to;
    private String message;
    private LocalDateTime date;
    private Long originalMessage;

    public Message(Utilizator from, LocalDateTime date, List<Utilizator> to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.originalMessage = null;
    }

    public Utilizator getFrom() {
        return from;
    }

    public void setFrom(Utilizator from) {
        this.from = from;
    }

    public List<Utilizator> getTo() {
        return to;
    }

    public void setTo(List<Utilizator> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(Long originalMessage) {
        this.originalMessage = originalMessage;
    }

    @Override
    public String toString() {
        return "Mesaj ---> " + message + "\n"  + "from: " + from.getFirstName() + " " + from.getLastName()
                + "\nto: " + to;
    }
}
