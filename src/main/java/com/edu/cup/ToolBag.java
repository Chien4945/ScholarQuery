package com.edu.cup;

/**
 * @Project: KG and QA
 * @Author: zhangjian
 * @Employer： CUP-CS
 * @version: 1.0
 * @Date: 2021/1/16 14:14
 */
import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ToolBag {

//    public static String MaptoString(Map map) {
//        Set<String> keySet = map.keySet();
//        String mapString = "{";
//        for (String item : keySet) {
//            String valueMap = (String) map.get(item);
//            valueMap =valueMap .replace("，",",");
//            if (item.equals("cites_per_year")){
//                valueMap = valueMap .replace(" - ","\\\",");
//                valueMap = valueMap .replace(": ",":\\\"");
//                valueMap = valueMap .replace("\'","\\\"");
//                valueMap = valueMap .replace("}","\\\"}");
//                mapString = mapString + "\\\"" + item + "\\\"" + ":" + valueMap + ",";
//            }else{
//                mapString = mapString + "\\\"" + item + "\\\"" + ":" + "\\\"" + valueMap + "\\\"" + ",";
//            }
//        }
//        mapString = mapString.substring(0,mapString.length()-1);
//        mapString = mapString+"}";
//        return mapString;
//    }
    public static String MaptoString(Map map) {
        Set<String> keySet = map.keySet();
        String mapString = "{";
        for (String item : keySet) {
            String valueMap = (String) map.get(item);
            valueMap =valueMap .replace("，",",");
            if (item.equals("cites_per_year")){
                valueMap = valueMap .replace(" - ","\",");
                valueMap = valueMap .replace(": ",":\"");
                valueMap = valueMap .replace("\'","\"");
                valueMap = valueMap .replace("}","\"}");
                mapString = mapString + "\"" + item + "\"" + ":" + valueMap + ",";
            }else{
                mapString = mapString + "\"" + item + "\"" + ":" + "\"" + valueMap + "\"" + ",";
            }
        }
        mapString = mapString.substring(0,mapString.length()-1);
        mapString = mapString+"}";
        return mapString;
    }

    public static String MaptoString(List<Map> mapList){
        String maplistSting = "[";
        for (Map mapitem:mapList){
            maplistSting=maplistSting+MaptoString(mapitem)+",";
        }
        maplistSting = maplistSting.substring(0,maplistSting.length()-1);
        maplistSting = maplistSting + "]";
        return maplistSting;
    }
}