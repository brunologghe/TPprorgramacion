package com.example.pedidosYA.Utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {
    
    public static Map<String, Object> success(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        return response;
    }
    
    public static Map<String, Object> success(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        return response;
    }
    
    public static Map<String, Object> error(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return response;
    }
    
    public static Map<String, Object> successWithProperty(String message, String propertyKey, Object propertyValue) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put(propertyKey, propertyValue);
        return response;
    }
}
