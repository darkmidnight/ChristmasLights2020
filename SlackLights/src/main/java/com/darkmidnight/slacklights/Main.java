package com.darkmidnight.slacklights;

import com.darkmidnight.geck.datastructures.binary.BinaryUtils;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Spark;
import static spark.Spark.*;

public class Main {

    static boolean ready = true;
    private static SerialControl sc;

    static byte[] prevBytes = new byte[300];
    private static Map<String, String> morseMap;
    private static MorseThread morseThread;

    public static Map<String, String> createMorseMap() {
        Map<String, String> aMap = new HashMap<>();
        aMap.put("A", ".-");
        aMap.put("B", "-...");
        aMap.put("C", "-.-.");
        aMap.put("D", "-..");
        aMap.put("E", ".");
        aMap.put("F", "..-.");
        aMap.put("G", "--.");
        aMap.put("H", "....");
        aMap.put("I", "..");
        aMap.put("J", ".---");
        aMap.put("K", "-.-");
        aMap.put("L", ".-..");
        aMap.put("M", "--");
        aMap.put("N", "-.");
        aMap.put("O", "---");
        aMap.put("P", ".--.");
        aMap.put("Q", "--.-");
        aMap.put("R", ".-.");
        aMap.put("S", "...");
        aMap.put("T", "-");
        aMap.put("U", "..-");
        aMap.put("V", "...-");
        aMap.put("W", ".--");
        aMap.put("X", "-..-");
        aMap.put("Y", "-.--");
        aMap.put("Z", "--..");
        aMap.put(".", ".-.-.-");
        aMap.put(",", "--..--");
        aMap.put("?", "..--..");
        aMap.put("/", "-..-.");
        aMap.put("@", "...-.-");
        aMap.put("1", ".----");
        aMap.put("2", "..---");
        aMap.put("3", "...--");
        aMap.put("4", "....-");
        aMap.put("5", ".....");
        aMap.put("6", "-....");
        aMap.put("7", "--...");
        aMap.put("8", "---..");
        aMap.put("9", "----.");
        aMap.put("0", "-----");
        return aMap;
    }

    public static void main(String[] args) {

        morseThread = new MorseThread();
        morseThread.setMorseString("010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101");
        morseThread.start();
        morseMap = createMorseMap();
        sc = new SerialControl(args[0]);
        sc.start();

        System.out.println("Running");
        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });
        port(4567);

        enableCORS("*", "GET,PUT,DELETE,POST,OPTIONS", "Access-Control-Allow-Origin,Content-Type, Access-Controler-Allow-Headers, Authorization, X-Requested-With");

        get("/morse", (rqst, rspns) -> {
            String input = rqst.queryParams("morseStr");
            input = input.toUpperCase();
            if (input.length()>25) { return ""; }
            StringBuilder decode = new StringBuilder();
            for (int i = 0; i < input.length(); i++) {
                String s = new String(new char[]{input.charAt(i)});
//                System.out.println(s);
                String morse = morseMap.get(s);
                if (morse != null) {
                    decode.append(morse);
                }
            }
            String morseStr = decode.toString();
//            System.out.println(morseStr);
            morseStr = morseStr.replace(".", "10");
            morseStr = morseStr.replace("-", "1110");
            morseThread.setMorseString(morseStr);
            
            return "";
        });
        get("/random", (rqst, rspns) -> {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int i = 1; i < 101; i++) {
                int r = (int) (Math.random() * 255);
                int g = (int) (Math.random() * 255);
                int b = (int) (Math.random() * 255);
                byte[] allBytes = new byte[4];
                allBytes[0] = (byte) (i + 1);
                allBytes[1] = BinaryUtils.signByte(g);
                allBytes[2] = BinaryUtils.signByte(r);
                allBytes[3] = BinaryUtils.signByte(b);
                sc.send(allBytes);
            }

            return "";
        });
        get("/rgb/:r/:g/:b", (rqst, rspns) -> {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int r = Integer.parseInt(rqst.params("r"));
            int g = Integer.parseInt(rqst.params("g"));
            int b = Integer.parseInt(rqst.params("b"));
            byte[] allBytes = new byte[4];
            allBytes[0] = 0;
            allBytes[1] = BinaryUtils.signByte(g);
            allBytes[2] = BinaryUtils.signByte(r);
            allBytes[3] = BinaryUtils.signByte(b);
            sc.send(allBytes);
            return "";
        });

        get("/led/:idx/:color1", (rqst, rspns) -> {
            try {
                int idx = Integer.parseInt(rqst.params("idx"));
                if (idx > 0 && idx < 100) {
                    byte[] allBytes = new byte[4];
                    byte[] rgb = findColor(rqst.params("color1"));
                    allBytes[0] = BinaryUtils.signByte(idx);
                    allBytes[1] = rgb[1];
                    allBytes[2] = rgb[0];
                    allBytes[3] = rgb[2];
                    sc.send(allBytes);
                }
            } catch (IOException | NumberFormatException ex) {
                System.out.println(ex.getMessage());
                return "ERROR BC";
            }

            return ""; // Replace this with a proper OK message
        });
        get("/all/:color1", (rqst, rspns) -> {
            try {

                byte[] allBytes = new byte[4];
                byte[] rgb = findColor(rqst.params("color1"));
                allBytes[0] = 0;
                allBytes[1] = rgb[1];
                allBytes[2] = rgb[0];
                allBytes[3] = rgb[2];
                sc.send(allBytes);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                return "ERROR BC";
            }

            return ""; // Replace this with a proper OK message
        });

        get("/top/:color1", (rqst, rspns) -> {
            try {
                byte[] allBytes = new byte[4];
                byte[] rgb = findColor(rqst.params("color1"));
                allBytes[0] = 101;
                allBytes[1] = rgb[1];
                allBytes[2] = rgb[0];
                allBytes[3] = rgb[2];
                sc.send(allBytes);

            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                return "ERROR BC";
            }

            return ""; // Replace this with a proper OK message
        });
        get("/bottom/:color1", (rqst, rspns) -> {
            try {
                byte[] allBytes = new byte[4];
                byte[] rgb = findColor(rqst.params("color1"));
                allBytes[0] = 102;
                allBytes[1] = rgb[1];
                allBytes[2] = rgb[0];
                allBytes[3] = rgb[2];
                sc.send(allBytes);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                return "ERROR BC";
            }
            return ""; // Replace this with a proper OK message
        });
        get("/odd/:color1", (rqst, rspns) -> {
            try {
                byte[] allBytes = new byte[4];
                byte[] rgb = findColor(rqst.params("color1"));
                allBytes[0] = 103;
                allBytes[1] = rgb[1];
                allBytes[2] = rgb[0];
                allBytes[3] = rgb[2];
                sc.send(allBytes);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                return "ERROR BC";
            }
            return ""; // Replace this with a proper OK message
        });
        get("/even/:color1", (rqst, rspns) -> {
            try {
                byte[] allBytes = new byte[4];
                byte[] rgb = findColor(rqst.params("color1"));
                allBytes[0] = 104;
                allBytes[1] = rgb[1];
                allBytes[2] = rgb[0];
                allBytes[3] = rgb[2];
                sc.send(allBytes);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                return "ERROR BC";
            }
            return ""; // Replace this with a proper OK message
        });
        get("/half/:color1/:color2", (rqst, rspns) -> {

            try {
                byte[] allBytes = new byte[4];
                byte[] rgb = findColor(rqst.params("color1"));
                allBytes[0] = 101;
                allBytes[1] = rgb[1];
                allBytes[2] = rgb[0];
                allBytes[3] = rgb[2];
                sc.send(allBytes);

                allBytes = new byte[4];
                rgb = findColor(rqst.params("color2"));
                allBytes[0] = 102;
                allBytes[1] = rgb[1];
                allBytes[2] = rgb[0];
                allBytes[3] = rgb[2];
                sc.send(allBytes);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                return "ERROR BC";
            }
            return ""; // Replace this with a proper OK message
        });
        get("/alternate/:color1/:color2", (rqst, rspns) -> {

            try {
                byte[] allBytes = new byte[4];
                byte[] rgb = findColor(rqst.params("color1"));
                allBytes[0] = 103;
                allBytes[1] = rgb[1];
                allBytes[2] = rgb[0];
                allBytes[3] = rgb[2];
                sc.send(allBytes);

                allBytes = new byte[4];
                rgb = findColor(rqst.params("color2"));
                allBytes[0] = 104;
                allBytes[1] = rgb[1];
                allBytes[2] = rgb[0];
                allBytes[3] = rgb[2];
                sc.send(allBytes);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                return "ERROR BC";
            }
            return ""; // Replace this with a proper OK message
        });
    }

    /**
     * This is a boilerplate example of enabling CORS in Spark. We might want to
     * make modifications as we go.
     *
     * @param origin
     * @param methods
     * @param headers
     */
    public static void enableCORS(final String origin, final String methods, final String headers) {
        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            response.type("application/json");
        });
    }

    static synchronized void setReady(boolean isReady) {
        ready = isReady;
    }

    static synchronized boolean isReady() {
        return ready;
    }

    public static byte[] findColor(String s) {
        Color c;
        try {
            c = BasicColors.valueOf(s.toUpperCase()).getColor();
        } catch (IllegalArgumentException ex) {
            try {
                c = Color.decode("#" + s);
            } catch (NumberFormatException ex2) {
                c = Color.black;
            }
        }
        return new byte[]{BinaryUtils.signByte(c.getRed()), BinaryUtils.signByte(c.getGreen()), BinaryUtils.signByte(c.getBlue())};

    }

    enum BasicColors implements BasicColorInterface {
        RED {
            @Override
            public Color getColor() {
                return Color.RED;
            }
        },
        GREEN {
            @Override
            public Color getColor() {
                return Color.GREEN;
            }
        },
        BLUE {
            @Override
            public Color getColor() {
                return Color.BLUE;
            }
        },
        WHITE {
            @Override
            public Color getColor() {
                return Color.WHITE;
            }
        },
        YELLOW {
            @Override
            public Color getColor() {
                return Color.YELLOW;
            }
        },
        MAGENTA {
            @Override
            public Color getColor() {
                return Color.MAGENTA;
            }
        },
        ORANGE {
            @Override
            public Color getColor() {
                return Color.ORANGE;
            }
        },
        PINK {
            @Override
            public Color getColor() {
                return Color.PINK;
            }
        },
        CYAN {
            @Override
            public Color getColor() {
                return Color.CYAN;
            }
        },
        BLACK {
            @Override
            public Color getColor() {
                return Color.BLACK;
            }
        };

        @Override
        public byte[] getRGB() {
            Color c = getColor();
            return new byte[]{BinaryUtils.signByte(c.getRed()), BinaryUtils.signByte(c.getGreen()), BinaryUtils.signByte(c.getBlue())};
        }

    }

    interface BasicColorInterface {

        public byte[] getRGB();

        public Color getColor();
    }
}
