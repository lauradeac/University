package org.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = -1138446817700416884L;

    @JsonProperty
    private String operation;

    @JsonProperty
    private String message;

    @Override
    public String toString() {
        return "Message{" +
                "operation='" + operation + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
