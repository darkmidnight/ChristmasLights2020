package com.darkmidnight.slacklights;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MorseThread extends Thread {

    private String morseString;
    private boolean running = true;

    @Override
    public void run() {
        while (running) {
            try {
                String morseStr = getMorseString();
                for (int i = 0; i < morseStr.length(); i++) {
                    Files.write(new File("/sys/class/gpio/gpio234/value").toPath(), morseStr.substring(i, i + 1).getBytes(), StandardOpenOption.WRITE);
                    Thread.sleep(500);
                }
                Thread.sleep(5000);
            } catch (IOException | InterruptedException ex) {
                System.out.println("ERR " + ex.getMessage());
            }
        }

    }

    public synchronized String getMorseString() {
        return morseString;
    }

    public synchronized void setMorseString(String morseString) {
        this.morseString = morseString;
    }

}
