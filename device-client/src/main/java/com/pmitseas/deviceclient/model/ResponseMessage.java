package com.pmitseas.deviceclient.model;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ResponseMessage implements Serializable {
    private UUID id;
    private String status;
    private String time;
    private byte[] data;
}
