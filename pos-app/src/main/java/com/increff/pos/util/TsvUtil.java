package com.increff.pos.util;

import com.increff.pos.exception.UploadException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TsvUtil {
    public static List<String> createRow(int lineNo, String value, String cause){
        List<String> temp = new ArrayList<>();
        temp.add(String.valueOf(lineNo));
        temp.add(value);
        temp.add(cause);
        return temp;
    }

    public static HashMap<String, Integer> parseHeader(String line, String[] names) throws UploadException {
        List<List<String>> error = new ArrayList<>();
        if(line.trim().isEmpty()){
            error.add(createRow(0, "empty", "header row cannot be empty"));
            throw new UploadException("No headers found", error);
        }
        HashMap<String, Integer> map = new HashMap<>();
        String[] headers = line.split("\t");
        if(headers.length != names.length) {
            error.add(createRow(0, String.valueOf(headers.length), "Headers must contain "+names.length+" columns"));
        }
        for(int i = 0 ; i < headers.length;i++){
            map.put(headers[i].trim().toLowerCase(), i);
        }
        for(String name: names){
            if(!map.containsKey(name)){
                error.add(createRow(0, name, "Header does not contain column with this name"));
            }
        }

        if(error.size() > 0){
            throw new UploadException("Invalid headers", error);
        }
        return map;
    }
}
