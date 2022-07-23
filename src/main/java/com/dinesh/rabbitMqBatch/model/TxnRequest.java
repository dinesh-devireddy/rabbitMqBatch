package com.dinesh.rabbitMqBatch.model;

import lombok.Data;

@Data
public class TxnRequest {
    private String name;
    private String id;
}
