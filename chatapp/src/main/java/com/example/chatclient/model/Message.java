package com.example.chatclient.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Message implements Serializable {
    private String from;
    private String content;
}
