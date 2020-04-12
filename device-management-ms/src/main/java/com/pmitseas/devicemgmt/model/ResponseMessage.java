package com.pmitseas.devicemgmt.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ResponseMessage implements Serializable {
    private String status;
    private LocalDateTime time;
    private byte[] data;
}
