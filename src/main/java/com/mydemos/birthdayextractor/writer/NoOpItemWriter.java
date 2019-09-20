package com.mydemos.birthdayextractor.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class NoOpItemWriter<T> implements ItemWriter<T> {

    @Override
    public void write(List<? extends T> items) throws Exception {
        for (T item : items) {
            //System.out.println(item.toString());
        }
    }
}