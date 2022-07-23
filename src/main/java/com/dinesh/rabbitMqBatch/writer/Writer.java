package com.dinesh.rabbitMqBatch.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
@Slf4j
public class Writer implements ItemWriter {
    @Override
    public void write(List items) throws Exception {
        for(Object item:items) {
            log.info("Request is: {}",item);
        }
    }
}
