package com.darkmidnight.slacklights;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import spark.Spark;
import static spark.Spark.*;

public class Main {

    private static SerialControl sc;
    private static Map<Integer, String> hexMap;
    
    static StringBuilder basicMapping = new StringBuilder("9842313212345686475132168453468165473843845768412346348613274387313468465354687351387364684645346354");

    public static void main(String[] args) {
        
        for (BasicColors bc : BasicColors.values()) {
            Color c = bc.getColor();
            System.out.println(bc.name()+"\t"+bc.ordinal()+"\t"+c.getRed()+"\t"+c.getGreen()+"\t"+c.getBlue());
        }
        
        hexMap = new TreeMap<>();
        for (int i = 0; i < 100; i++) {
            hexMap.put(i, "FFFFFF");

        }
        System.out.println("BASIC STRING "+basicMapping.length());
        
        sc = new SerialControl("/dev/ttyUSB0");
        sc.start();

        System.out.println("Running");
        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });
        port(4567);

        enableCORS("*", "GET,PUT,DELETE,POST,OPTIONS", "Access-Control-Allow-Origin,Content-Type, Access-Controler-Allow-Headers, Authorization, X-Requested-With");

        get("/basic/:basicColour", (rqst, rspns) -> {
            BasicColors bc;
            try {
                bc = BasicColors.valueOf(rqst.params("basicColour").toUpperCase());
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                return "ERROR BC";
            }
            
//            double timestamp = 0.0d;
//            String rawTs = rqst.queryParams("timestamp");
//            try {
//                if (rawTs.contains("x")) {
//                    rawTs = rawTs.replaceAll("x", "0");
//                }
//                timestamp = Double.parseDouble(rawTs);
//            } catch (NumberFormatException ex) {
//                System.out.println("NFE\n" + ex.getMessage());
//            }
            
            if (!rqst.queryString().equals(DataSingleton.getSingleton().getLastQS())) {
                List<Integer> idList = new ArrayList<>();
                String rawIds = rqst.queryParams("ids");
//                System.out.println(timestamp);
                try {
                    basicMapping.replace(Integer.parseInt(rawIds), Integer.parseInt(rawIds)+1, Integer.toString(bc.ordinal()));
                } catch (NumberFormatException ex) {
                    for (Integer idx : JacksonConverter.fromJSON_intArray(rawIds)) {
                        basicMapping.replace(idx, idx+1, Integer.toString(bc.ordinal()));
                    }
                }
                System.out.println("SENDING "+basicMapping.toString());
                sc.send(basicMapping.toString());
                sc.flush();
                System.out.println("Flushed");
//                DataSingleton.getSingleton().setLastTimestamp(timestamp);
                DataSingleton.getSingleton().setLastQS(rqst.queryString());
            }
            return ""; // Replace this with a proper OK message
        });

        get("/process/:hexValue", (rqst, rspns) -> {
            String hex = rqst.params("hexValue");
            try {
                Color c = Color.decode("#" + hex);
            } catch (NumberFormatException ex) {
                return "";
            }
            double timestamp = 0.0d;
            String rawTs = rqst.queryParams("timestamp");
            try {
                if (rawTs.contains("x")) {
                    rawTs = rawTs.replaceAll("x", "0");
                }
                timestamp = Double.parseDouble(rawTs);
            } catch (NumberFormatException ex) {
                System.out.println("NFE\n" + ex.getMessage());
            }

            if (timestamp > DataSingleton.getSingleton().getLastTimestamp() && !rqst.queryString().equals(DataSingleton.getSingleton().getLastQS())) {
                List<Integer> idList = new ArrayList<>();
                String rawIds = rqst.queryParams("ids");
                System.out.println(timestamp);
                try {
                    hexMap.put(Integer.parseInt(rawIds), hex);
                } catch (NumberFormatException ex) {
                    for (Integer idx : JacksonConverter.fromJSON_intArray(rawIds)) {
                        hexMap.put(idx, hex);
                    }
                }
                for (Integer idx : hexMap.keySet()) {
                    sc.send(hexMap.get(idx));
                }
                sc.flush();
                System.out.println("Flushed");
                DataSingleton.getSingleton().setLastTimestamp(timestamp);
                DataSingleton.getSingleton().setLastQS(rqst.queryString());
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
        }

    }

    interface BasicColorInterface {

        public Color getColor();
    }
}
