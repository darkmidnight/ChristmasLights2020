package com.darkmidnight.slacklights;

import com.fazecast.jSerialComm.SerialPort;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SerialControl extends Thread {

    private final SerialPort sp;
    private DataOutputStream dos;
    private PrintWriter pw;
    private  BufferedReader br;

    public SerialControl(String portDesc) {
        for (SerialPort s : SerialPort.getCommPorts()) {
            System.out.println(s.getSystemPortName());
        }
        this.sp = SerialPort.getCommPort(portDesc);

        sp.openPort();
        sp.setBaudRate(9600);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        dos = new DataOutputStream(sp.getOutputStream());
        br = new BufferedReader(new InputStreamReader(sp.getInputStream()));
        
    }

    @Override
    public void run() {
        try {
            String aLine = "";
            while ((aLine = br.readLine()) != null) {
                System.out.println("RESPONSE: "+aLine);
            }
        } catch (IOException ex) {
            System.out.println("ERR "+ex.getMessage());
        }
    }
    

    public void send(int idx, int r, int g, int b) throws IOException {
        if (!DataSingleton.getSingleton().isIsBlocked()) {
            DataSingleton.getSingleton().setIsBlocked(true);
            String s = idx + "," + g + "," + r + "," + b + "!";
            System.out.println(s);
            dos.write(s.getBytes());
            dos.flush();
        }
        DataSingleton.getSingleton().setIsBlocked(false);
    }

    /**
     * Static convenience method for seeing what ports are available.
     *
     * @return
     */
    public static List<String> getPortList() {
        List<String> aList = new ArrayList<>();
        for (SerialPort sp : SerialPort.getCommPorts()) {
            System.out.println(sp.getSystemPortName());
            aList.add(sp.getSystemPortName());
        }
        return aList;
    }

    void send(String params) throws IOException {
        dos.write((params+"\n").getBytes());
    }
    void flush() throws IOException {
        dos.flush();
    }

}
