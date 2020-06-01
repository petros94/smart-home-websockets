package com.pmitseas.devicemgmt.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@ToString
public class ResponseMessage implements Serializable {
    private UUID id;
    private String status;
    private String time;
    private byte[] data;
}
