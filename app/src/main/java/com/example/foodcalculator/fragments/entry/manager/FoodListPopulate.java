package com.example.foodcalculator.fragments.entry.manager;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FoodListPopulate implements Runnable {

    private ConcurrentLinkedQueue<Food> queue;
    private Context context;

    public FoodListPopulate(ConcurrentLinkedQueue<Food> queue, Context context) {
        this.queue = queue;
        this.context = context;
    }

    @Override
    public void run() {

        long t = System.currentTimeMillis();
        long end = t + 30 * 1000;

        while (System.currentTimeMillis() < end) {
            if (!queue.isEmpty()) {
                try {
                    new FoodListManager(context).write(queue.poll());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
