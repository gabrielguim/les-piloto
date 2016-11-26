package com.example.semtempo.model;

/**
 * Created by Lucas on 26/11/2016.
 */
public class ValidateInfo {

    private final static String nullObject = null;
    private final static String emptyString = "";
    private final static int MAX_HOUR = 24;
    private final static int MIN_HOUR = 1;

    public static boolean isNameValid(String name){
        boolean answer = true;
        if(name == nullObject || name.equals(emptyString) || name.matches("^[0-9]*$")){
            answer = false;
        }
        return answer;
    }

    public static boolean isHourValid(int hour){
        boolean answer = true;
        if(hour < MIN_HOUR || hour > MAX_HOUR ){
            answer = false;
        }
        return answer;
    }

}
