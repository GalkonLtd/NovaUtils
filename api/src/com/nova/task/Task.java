package com.nova.task;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 7/23/2015
 */
public abstract class Task implements Runnable {

    public abstract boolean execute();

    @Override
    public void run() {
        this.execute();
    }

}
