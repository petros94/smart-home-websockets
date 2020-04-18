package com.pmitseas.deviceclient.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ResponseMessage implements Serializable {
    private String status;
    private String time;
    private byte[] data;
}
