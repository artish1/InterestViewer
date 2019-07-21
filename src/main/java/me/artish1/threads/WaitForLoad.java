package me.artish1.threads;

import me.artish1.auction.Auction;

public class WaitForLoad implements Runnable {

    //Used to stall the application until all the Auctions are fully loaded into memory.
    @Override
    public void run() {

        if (Auction.getLoading() > 0) {
            try {
                Thread.sleep(250);
                run();
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Done!");


    }
}
