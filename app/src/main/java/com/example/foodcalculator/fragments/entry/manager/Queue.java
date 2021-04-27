package com.example.foodcalculator.fragments.entry.manager;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Queue implements Runnable {

    private ConcurrentLinkedQueue<Food> queue;
    private Food data;

    public Queue(ConcurrentLinkedQueue<Food> queue, Food data) {
        this.queue = queue;
        this.data = data;
    }

    @Override
    public void run() {
//        System.out.println(data.getFoodName());
        queue.add(data);
    }
}
