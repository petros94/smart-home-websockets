package com.pmitseas.devicemgmt.event;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ActionResultEvent implements Serializable {
  private UUID transactionId;
  private String status;
  private String time;
  private byte[] data;
}
