package com.dinesh.rabbitMqBatch.model;

import lombok.Data;


@Data
public class GenericRequest<T> {

    private Header header;
    private T txnRequest;
}
