package com.darkmidnight.slacklights;

import com.fazecast.jSerialComm.SerialPort;
import java.io.BufferedReader;
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
    private BufferedReader br;
    private boolean running = true;

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
        while (running) {
            try {
                String aLine = "";
                while ((aLine = br.readLine()) != null) {
                    System.out.println("RESPONSE: " + aLine);
                    if (aLine.equals("READY")) {
                        Main.setReady(true);
                    } else if (aLine.equals("WORKING")) {
                        Main.setReady(false);
                    }
                }
            } catch (IOException ex) {
                System.out.println("ERR " + ex.getMessage());
            }
        }

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

    void send(byte[] bytes) throws IOException {
        if (!Main.isReady()) {
            System.out.println("BLocked - not ready");
            return;
        }
        Main.setReady(false);
        dos.write(bytes);
        dos.flush();
        while (!Main.isReady()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                throw new IOException(ex);
            }
        }
    }

}
