package com.edu.cup;

/**
 * @Project: KG and QA
 * @Author: zhangjian
 * @Employer： CUP-CS
 * @version: 1.0
 * @Date: 2021/1/16 14:14
 */
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.*;

public class ToolBag {

/*****一开始给python传消息使用的处理
    public static String MaptoString(Map map) {
        Set<String> keySet = map.keySet();
        String mapString = "{";
        for (String item : keySet) {
            String valueMap = (String) map.get(item);
            valueMap =valueMap .replace("，",",");
            if (item.equals("cites_per_year")){
                valueMap = valueMap .replace(" - ","\\\",");
                valueMap = valueMap .replace(": ",":\\\"");
                valueMap = valueMap .replace("\'","\\\"");
                valueMap = valueMap .replace("}","\\\"}");
                mapString = mapString + "\\\"" + item + "\\\"" + ":" + valueMap + ",";
            }else{
                mapString = mapString + "\\\"" + item + "\\\"" + ":" + "\\\"" + valueMap + "\\\"" + ",";
            }
        }
        mapString = mapString.substring(0,mapString.length()-1);
        mapString = mapString+"}";
        return mapString;
    }
 */



    private static float Date2Float(String date){
        List<String> dateStr = new ArrayList<>(Arrays.asList(date.split("/")));
        Float integerPart = Float.parseFloat(dateStr.get(0));
        Float decimalPart = Float.parseFloat(dateStr.get(1));
        decimalPart = decimalPart/12;
        return integerPart+decimalPart;
    }



    public static boolean DateMorethan(String dateA , String dateB){
        List<String> compareStrA = new ArrayList<>(Arrays.asList(dateA.split("/")));
        List<String> compareStrB = new ArrayList<>(Arrays.asList(dateB.split("/")));
        if (Integer.parseInt(compareStrA.get(0))>Integer.parseInt(compareStrB.get(0))){
            return true;
        }
        else if(Integer.parseInt(compareStrA.get(0))<Integer.parseInt(compareStrB.get(0))){
            return false;
        }
        else{
            if (Integer.parseInt(compareStrA.get(1))>Integer.parseInt(compareStrB.get(1))){
                return true;
            }
            else if(Integer.parseInt(compareStrA.get(1))<Integer.parseInt(compareStrB.get(1))){
                return false;
            }
        }
        return false; //相等
    }

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

    public static String RgbReturn(String HIndex){
        int Rcolor = 255;
        int Gcolor = 255;
        int Bcolor = 0;
        int hIndex = (Integer.parseInt(HIndex));

        if (hIndex > 254){
            return "#" + Integer.toHexString(255) + Integer.toHexString(0) + Integer.toHexString(0) + Integer.toHexString(0) + Integer.toHexString(0);
        }
        else {
            Gcolor = Gcolor -hIndex;
            if(Gcolor > 15){
                return "#"+Integer.toHexString(Rcolor) + Integer.toHexString(Gcolor) + Integer.toHexString(0) + Integer.toHexString(Bcolor);
            }
            else {
                return "#"+Integer.toHexString(Rcolor) + Integer.toHexString(0) + Integer.toHexString(Gcolor) + Integer.toHexString(0) + Integer.toHexString(0) + Integer.toHexString(Bcolor);
            }
        }
    }

    public static String JsDatePass(String dateJS){
        return dateJS.replace("-","/");
    }

    public static int MonthCalcu(String dateStart,String dateOver){
        //计算日期间的月份差.
        List<String> dateA = new ArrayList<>(Arrays.asList(dateStart.split("/")));
        int yearA = Integer.parseInt(dateA.get(0));
        int monthA =Integer.parseInt(dateA.get(1));

        List<String> dateB = new ArrayList<>(Arrays.asList(dateOver.split("/")));
        int yearB = Integer.parseInt(dateB.get(0));
        int monthB = Integer.parseInt(dateB.get(1));

        return (yearB-yearA-1)*12+(12-monthA+1)+(monthB);
    }

    public static JSONObject ReadJsonObj(String jsonPath){
        JSONObject jsonObj =new JSONObject();
        try {
            File jsonFile = new File(jsonPath);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonObj = JSONObject.parseObject(sb.toString());
            return jsonObj;
        } catch (IOException e) {
            e.printStackTrace();
            return jsonObj;
        }
    }

    public static List<String> KeyofJsonObj(JSONObject jsonObj){
        return  new ArrayList<>(jsonObj.keySet());
    }

    public static List<String> DateSort(List<String> DateList){
        //Input : ["2020/12","2021/1","2021/2","2021/3"
        List<String> sortDate = new ArrayList<>();
        int lenDatelist=DateList.size();

        for (int count=0;count<lenDatelist;++count){
            int minIndex = 0;
            int goIndex = 0;
            for(;goIndex<DateList.size();++goIndex){
                if(DateMorethan(DateList.get(minIndex),DateList.get(goIndex))==true){
                    minIndex=goIndex;
                }
            }
            sortDate.add(DateList.get(minIndex));
            DateList.remove(minIndex);
        }
        return sortDate;
    }

    public static List<String> TopWdfreq(JSONObject jsonObj,int num,JSONObject timeSpan){
        //统计在一定时间跨度timespan内，频度前num个词
         List<String> wordsAll = new ArrayList<>(JSONObject.parseObject(jsonObj.get(ToolBag.KeyofJsonObj(jsonObj).get(0)).toString()).keySet());
         List<String> Top5wd= new ArrayList<>();
         JSONObject FreStac = new JSONObject();

         for (String word: wordsAll){
             FreStac.put(word,0.0);
         }

        String dateStart = ToolBag.JsDatePass(timeSpan.getString("startDate"));//输入的起始日期
        String dateOver =ToolBag.JsDatePass(timeSpan.getString("overDate"));//输入的结束日期
        List<String> dateList = ToolBag.DateSort(ToolBag.KeyofJsonObj(jsonObj)); //保存数据的跨度
        List<String> spanList =new ArrayList<>();
        for (String dateTmp:dateList){
            if ((ToolBag.DateMorethan(dateTmp,dateStart))&&(ToolBag.DateMorethan(dateOver,dateTmp))){
                spanList.add(dateTmp);
            }
        }

         for(String keyOfjsonObj:spanList){
             JSONObject dateEle = JSONObject.parseObject(jsonObj.get(keyOfjsonObj).toString());
             for(String wdOfdate : dateEle.keySet()){
                 float valFreqsum = FreStac.getFloat(wdOfdate) + dateEle.getFloat(wdOfdate);
                 FreStac.put(wdOfdate,valFreqsum);
             }
         }
         for (int count = 0;count<num;++count){
             float maxFreq = 0;
             String topFreq = null ;
             for(String keyOfFrest:FreStac.keySet()){
                 if (FreStac.getFloat(keyOfFrest)>maxFreq){
                     maxFreq = FreStac.getFloat(keyOfFrest);
                     topFreq = keyOfFrest;
                 }
             }
             Top5wd.add(topFreq);
             FreStac.remove(topFreq);
         }
         return Top5wd;
    }

    public static List<Float> SpanUpDown(String word,JSONObject jsonObj,Float threshold){
        Float yearStart =new Float(-0.1);
        Float yearEnd =new Float(-0.1);

        for(String date:DateSort(KeyofJsonObj(jsonObj))){
            if (JSONObject.parseObject(jsonObj.get(date).toString()).getFloat(word) >threshold ){
                if (yearStart < 0){
                    yearStart =  Date2Float(date);
                }
                yearEnd =Date2Float(date);
            }
        }
        List<Float> dataOpt =new ArrayList<>();
        dataOpt.add(yearStart);
        dataOpt.add(yearEnd);
        dataOpt.add(yearStart);
        dataOpt.add(yearEnd);
        return dataOpt;
    }
}
