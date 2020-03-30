package com.example.chatclient.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Message implements Serializable {
    private String from;
    private String content;
}
