package com.cyanogenmod.batterymonitor.utils;

public class  Utils {
	/** 
     * Returns a formated time HoursH MinutesM SecondsS
     * 
     * @param millis
     * @return
     */
    public static String formatTime(long millis) {
              String output = "";
              long seconds = millis / 1000;
              long minutes = seconds / 60;
              long hours = minutes / 60;
              long days = hours / 24;
              seconds = seconds % 60;
              minutes = minutes % 60;
              hours = hours % 24;

              String secondsD = String.valueOf(seconds);
              String minutesD = String.valueOf(minutes);
              String hoursD = String.valueOf(hours); 

              if (seconds < 10)
                secondsD = "0" + seconds;
              if (minutes < 10)
                minutesD = "0" + minutes;
              if (hours < 10){
                //hoursD = "0" + hours;
              }

              if( days > 0 ){
                      output = days +"d ";
              } 
                      output += hoursD + ":" + minutesD + ":" + secondsD;
             
              return output;
    }
}
