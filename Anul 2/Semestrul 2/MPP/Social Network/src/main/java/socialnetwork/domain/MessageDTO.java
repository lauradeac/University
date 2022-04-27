package socialnetwork.domain;

import java.time.LocalDateTime;

public class MessageDTO {
    Long id;
    String mesajTabel;

    public MessageDTO(Long id, String mesajTabel) {
        this.id = id;
        this.mesajTabel = mesajTabel;

    }

    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public String getMesajTabel() {
        return mesajTabel;
    }

    public void setMesajTabel(String mesajTabel) {
        this.mesajTabel = mesajTabel;
    }
}
