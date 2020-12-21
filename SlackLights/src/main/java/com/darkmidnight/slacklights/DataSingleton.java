/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkmidnight.slacklights;

/**
 *
 * @author anthony
 */
public class DataSingleton {

    private static DataSingleton singleton;
    private double lastTimestamp = 0;
    private String lastQS = "";
    private boolean isBlocked = false;

    private DataSingleton() {

    }

    public static DataSingleton getSingleton() {
        if (singleton == null) {
            singleton = new DataSingleton();
        }
        return singleton;
    }

    public double getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(double lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public String getLastQS() {
        return lastQS;
    }

    public void setLastQS(String lastQS) {
        this.lastQS = lastQS;
    }

    public boolean isIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }
    
    
}
