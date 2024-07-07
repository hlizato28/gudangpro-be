package co.id.bcafinance.hlbooking.util;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:55
@Last Modified 05/05/2024 21:55
Version 1.0
*/

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XSSValidationUtils {
    /** Ini adalah pola standar yang digunakan untuk validasi karakter */
    private static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.\\/?\\s]*$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidURL(String uri, List<String> listKataTerlarang) {
        AtomicBoolean flag = new AtomicBoolean(false);
        String[] urls = uri.split("\\/");

        Arrays.stream(urls).filter(e -> !StringUtils.isEmpty(e)).forEach(url -> {
            String val = String.valueOf(url);
            if (listKataTerlarang.stream().anyMatch(p -> val.toLowerCase().contains(p.toLowerCase()))) {
                flag.set(true);
            }
            Matcher matcher = pattern.matcher(val);
            if (!matcher.matches()) {
                flag.set(true);
            }
        });
        return !flag.get();
    }

    public static boolean isValidRequestParam(String param, List<String> listKataTerlarang) {
        AtomicBoolean flag = new AtomicBoolean(false);
        String[] paramList = param.split("&");

        Arrays.stream(paramList).filter(e -> !StringUtils.isEmpty(e)).forEach(url -> {
            String val = String.valueOf(url);
            if (listKataTerlarang.stream().anyMatch(val::equalsIgnoreCase)) {
                flag.set(true);
            }
            Matcher matcher = pattern.matcher(val);
            if (!matcher.matches()) {
                flag.set(true);
            }
        });
        return !flag.get();
    }

    public static boolean isValidJSONContent(String jsonString, List<String> listKataTerlarang) {
        AtomicBoolean flag = new AtomicBoolean(false);
        try {
            if (jsonString.trim().startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    validateJSONObject(jsonArray.getJSONObject(i), listKataTerlarang, flag);
                }
            } else {
                JSONObject jsonObject = new JSONObject(jsonString);
                validateJSONObject(jsonObject, listKataTerlarang, flag);
            }
        } catch (JSONException e) {
            // Log the error
            System.out.println("Error parsing JSON: " + e.getMessage());
            flag.set(true);
        }
        return !flag.get();
    }

    private static void validateJSONObject(JSONObject jsonObject, List<String> listKataTerlarang, AtomicBoolean flag) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                validateJSONObject((JSONObject) value, listKataTerlarang, flag);
            } else if (value instanceof JSONArray) {
                validateJSONArray((JSONArray) value, listKataTerlarang, flag);
            } else {
                validateString(String.valueOf(value), listKataTerlarang, flag);
            }
        }
    }

    private static void validateJSONArray(JSONArray jsonArray, List<String> listKataTerlarang, AtomicBoolean flag) {
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                validateJSONObject((JSONObject) value, listKataTerlarang, flag);
            } else if (value instanceof JSONArray) {
                validateJSONArray((JSONArray) value, listKataTerlarang, flag);
            } else {
                validateString(String.valueOf(value), listKataTerlarang, flag);
            }
        }
    }

    private static void validateString(String value, List<String> listKataTerlarang, AtomicBoolean flag) {
        if (listKataTerlarang.stream().anyMatch(value::equalsIgnoreCase)) {
            flag.set(true);
        }
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            flag.set(true);
        }
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json, retMap);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object, Map<String, Object> map) throws JSONException {
        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if (value instanceof JSONArray) {
                value = toList(key, (JSONArray) value, map);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value, map);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    public static List<Object> toList(String key, JSONArray array, Map<String, Object> map) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList(key, (JSONArray) value, map);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value, map);
            } else {
                String mapValue = String.valueOf(value);
                if (map.containsKey(key)) {
                    mapValue += "," + String.valueOf(map.get(key));
                }
                map.put(key, mapValue);
            }
            list.add(value);
        }
        return list;
    }

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }




//    /** ini adalah pola standar yang digunakan hacker untuk menyerang dengan xss*/
//    private static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.\\/?\\s]*$", Pattern.CASE_INSENSITIVE);
//
//    public static boolean isValidURL(String uri, List<String> listKataTerlarang) {
//        AtomicBoolean flag= new AtomicBoolean(false);
//        String[]  urls=uri.split("\\/");
//
//        Arrays.stream(urls).filter(e->!StringUtils.isEmpty(e)).forEach(url->{
//            String val = String.valueOf(url);
//            // if(listKataTerlarang.stream().anyMatch(val::equalsIgnoreCase)){
//            if(listKataTerlarang.stream().anyMatch(p->val.toLowerCase().contains(p.toLowerCase()))){
////                System.out.println("kata yang dilarang ditemukan !!!!!");
//                flag.set(true);
//            }
//            Matcher matcher = pattern.matcher(val);
//            if (!matcher.matches()) {
////                System.out.println("pola yang dilarang ditemukan !!!!!");
//                flag.set(true);
//            }
//            /** buka pada saat development saja , tapi tutup manual saat pentest*/
////            else{
////                System.out.println("valid char found: "+val);
////            }
//        });
//        return !flag.get();
//    }
//
//    public static boolean isValidRequestParam(String param, List<String> listKataTerlarang) {
//        AtomicBoolean flag= new AtomicBoolean(false);
//        String[]  paramList=param.split("&");
//
//        Arrays.stream(paramList).filter(e->!StringUtils.isEmpty(e)).forEach(url->{
//            String val=String.valueOf(url);
////            System.out.println("value:"+val);
//            if(listKataTerlarang.stream().anyMatch(val::equalsIgnoreCase)){
////                System.out.println("kata yang dilarang ditemukan !!!!!");
//                flag.set(true);
//            }
//            Matcher matcher = pattern.matcher(val);
//            if (!matcher.matches()) {
////                System.out.println("pola yang dilarang ditemukan!!!!!");
//                flag.set(true);
//            }
//            /** buka pada saat development saja , tapi tutup manual saat pentest*/
////            else{
////                System.out.println("Aman  : "+val);
////            }
//        });
//        return !flag.get();
//    }
//
//
//    public static boolean isValidURLPattern(String uri, List<String> listKataTerlarang) {
//        AtomicBoolean flag= new AtomicBoolean(false);
//        String[]  urls=uri.split("\\/");
//
//        try {
//            Arrays.stream(urls).filter(e -> !StringUtils.isEmpty(e)).forEach(url -> {
//                String val = String.valueOf(url);
//                Map<String, Object> mapping = jsonToMap(new JSONObject(val));
////                System.out.println("Map; " + mapping);
//                mapping.forEach((key, value) -> {
//                    if (listKataTerlarang.stream().anyMatch(String.valueOf(value)::equalsIgnoreCase)) {
////                        System.out.println("kata yang dilarang ditemukan !!!!!");
//                        flag.set(true);
//                    }
//                    Matcher matcher = pattern.matcher(String.valueOf(value));
//                    if (!matcher.matches()) {
////                        System.out.println(key + "  : Pola yang dilarang ditemukan!!!!!");
//                        flag.set(true);
//                    }
//                    /** buka pada saat development saja , tapi tutup manual saat pentest*/
////                    else {
////                        System.out.println("Aman : " + String.valueOf(value));
////                    }
//                });
//
//            });
//        }catch(Exception ex){
//            flag.set(true);
//        }
//        return !flag.get();
//    }
//
//    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
//        Map<String, Object> retMap = new HashMap<String, Object>();
//
//        if(json != JSONObject.NULL) {
//            retMap = toMap(json,retMap);
//        }
//        return retMap;
//    }
//
//    public static Map<String, Object> toMap(JSONObject object, Map<String, Object> map) throws JSONException {
//
//
//        Iterator<String> keysItr = object.keys();
//        while(keysItr.hasNext()) {
//            String key = keysItr.next();
//            Object value = object.get(key);
//            //   System.out.println("key  "+key+"  value:"+ value);
//            if(value instanceof JSONArray) {
//                value = toList(key,(JSONArray) value,map);
//            }else if(value instanceof JSONObject) {
//                value = toMap((JSONObject) value,map);
//            }else {
//                map.put(key, value);
//            }
//        }
//        return map;
//    }
//
//    public static List<Object> toList(String key,JSONArray array,Map<String, Object> map ) throws JSONException {
//        List<Object> list = new ArrayList<Object>();
//        for(int i = 0; i < array.length(); i++) {
//            Object value = array.get(i);
//            if(value instanceof JSONArray) {
//                value = toList(key,(JSONArray) value,map);
//            }else if(value instanceof JSONObject) {
//                value = toMap((JSONObject) value,map);
//            }else{
//                String mapValue=String.valueOf(value);
//                if(map.containsKey(key)){
//                    mapValue+=","+String.valueOf(map.get(key));
//                }
//                map.put(key, mapValue);
//            }
//            list.add(value);
//        }
//        return list;
//    }
//
//
//    public static String convertObjectToJson(Object object) throws JsonProcessingException {
//        if (object == null) {
//            return null;
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.writeValueAsString(object);
//    }
}

