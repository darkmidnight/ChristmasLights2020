package com.darkmidnight.slacklights;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JacksonConverter {

    public static String toJSON(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

    public static String toJSON_pretty(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    }

    public static <T> T fromJSON(String json, Class<?> cls) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return (T) mapper.readValue(json, cls);
    }

    public static Map<String, String> fromJSON_strMap(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<String, String>> typeRef
                = new TypeReference<HashMap<String, String>>() {
        };
        Map<String, String> map = mapper.readValue(s, typeRef);

        return map;
    }

    public static Map<String, Object> fromJSON_objMap(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>() {
        };
        Map<String, Object> map = mapper.readValue(s, typeRef);

        return map;
    }

    public static Map<Byte, Map<Byte, Integer>> jenova_byteMap(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<Byte, Map<Byte, Integer>>> typeRef
                = new TypeReference<HashMap<Byte, Map<Byte, Integer>>>() {
        };
        Map<Byte, Map<Byte, Integer>> map = mapper.readValue(s, typeRef);

        return map;
    }

    public static Map<Short, Map<Byte, Integer>> jenova_shortMap(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<Short, Map<Byte, Integer>>> typeRef
                = new TypeReference<HashMap<Short, Map<Byte, Integer>>>() {
        };
        Map<Short, Map<Byte, Integer>> map = mapper.readValue(s, typeRef);

        return map;
    }

    public static Map<Integer, Map<Byte, Integer>> jenova_intMap(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<Integer, Map<Byte, Integer>>> typeRef
                = new TypeReference<HashMap<Integer, Map<Byte, Integer>>>() {
        };
        Map<Integer, Map<Byte, Integer>> map = mapper.readValue(s, typeRef);

        return map;
    }

    public static Map<Long, Map<Byte, Integer>> jenova_longMap(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<Long, Map<Byte, Integer>>> typeRef
                = new TypeReference<HashMap<Long, Map<Byte, Integer>>>() {
        };
        Map<Long, Map<Byte, Integer>> map = mapper.readValue(s, typeRef);

        return map;
    }

    public static Map<Byte, Object> fromJSON_byteMap(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<Byte, Object>> typeRef
                = new TypeReference<HashMap<Byte, Object>>() {
        };
        Map<Byte, Object> map = mapper.readValue(s, typeRef);

        return map;
    }

    public static Map<Short, Object> fromJSON_shortMap(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<Short, Object>> typeRef
                = new TypeReference<HashMap<Short, Object>>() {
        };
        Map<Short, Object> map = mapper.readValue(s, typeRef);

        return map;
    }

    public static Map<Integer, Object> fromJSON_intMap(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<Integer, Object>> typeRef
                = new TypeReference<HashMap<Integer, Object>>() {
        };
        Map<Integer, Object> map = mapper.readValue(s, typeRef);

        return map;
    }

    public static Map<Integer, String> fromJSON_intStrMap(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<Integer, String>> typeRef
                = new TypeReference<HashMap<Integer, String>>() {
        };
        Map<Integer, String> map = mapper.readValue(s, typeRef);

        return map;
    }

    public static Map<Integer, Long> fromJSON_intLongMap(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<Integer, Long>> typeRef
                = new TypeReference<HashMap<Integer, Long>>() {
        };
        Map<Integer, Long> map = mapper.readValue(s, typeRef);

        return map;
    }

    public static Map<Long, Object> fromJSON_longMap(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<Long, Object>> typeRef
                = new TypeReference<HashMap<Long, Object>>() {
        };
        Map<Long, Object> map = mapper.readValue(s, typeRef);

        return map;
    }

    public static List<Integer> fromJSON_intArray(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Integer>> typeRef = new TypeReference<List<Integer>>() {
        };
        List<Integer> map = mapper.readValue(s, typeRef);
        return map;
    }

    public static List<String> fromJSON_strArray(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {
        };
        List<String> map = mapper.readValue(s, typeRef);
        return map;
    }

    public static List<Object> fromJSON_objArray(String s) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Object>> typeRef = new TypeReference<List<Object>>() {
        };
        List<Object> map = mapper.readValue(s, typeRef);
        return map;
    }

    public static <T> Collection<T> fromJSON_Collection(String json, Class<?> cls) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(Collection.class, cls);
        return mapper.readValue(json, type);
    }

}
