package com.example.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class MessageDTO {
    private String from;
    private String content;
}
