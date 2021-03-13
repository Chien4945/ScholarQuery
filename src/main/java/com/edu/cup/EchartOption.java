package com.edu.cup;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class EchartOption {

    private JSONObject TitleOption(String title){
        JSONObject optTitle = new JSONObject();
        JSONObject textStyle = new JSONObject();
        final String TITLE = title;
        textStyle.put("fontSize",15);
        optTitle.put("text",TITLE);
        optTitle.put("textStyle",textStyle);

        return optTitle;
    }


    public JSONObject CreateProfile(JSONObject profileJson,List<String> domainInterst){
        JSONObject profileOpt = new JSONObject();
        profileOpt.put("name",profileJson.get("name"));
        profileOpt.put("affiliation",profileJson.get("affiliation"));
        profileOpt.put("email_domain",profileJson.get("email_domain"));
        profileOpt.put("citedby",profileJson.get("citedby"));
        profileOpt.put("citedby5y",profileJson.get("citedby5y"));
        profileOpt.put("hindex",profileJson.get("hindex"));
        profileOpt.put("hindex5y",profileJson.get("hindex5y"));
        profileOpt.put("i10index",profileJson.get("i10index"));
        profileOpt.put("i10index5y",profileJson.get("i10index5y"));
        profileOpt.put("portraiturl",profileJson.get("portraiturl"));
        profileOpt.put("domain",domainInterst);
        return profileOpt;
    }

    public JSONObject CreateLineChart(JSONObject citesperyear){
        JSONObject lineOpt = new JSONObject();
        JSONObject xAxis = new JSONObject();
        JSONObject yAxis = new JSONObject();
        JSONObject lineSeries = new JSONObject();


        JSONObject toolTip = new JSONObject();
        toolTip.put("show",true);
        toolTip.put("trigger","axis");


        lineOpt.put("title", TitleOption("CITEED/year"));
        lineOpt.put("backgroundColor","#607B8B");

        List<String> xdata = new ArrayList<>();
        List<String> ydata = new ArrayList<>();
        for (String key : citesperyear.keySet()){
            if (key != "2021"){
                xdata.add(key);
            }
        }
        Collections.sort(xdata);
        for(String key : xdata){
            ydata.add(citesperyear.getString(key));
        }

        xAxis.put("type","category");
        xAxis.put("data",xdata);

        yAxis.put("type","value");

        lineSeries.put("type","line");
        lineSeries.put("data",ydata);
        List<JSONObject> serList = new ArrayList<>();
        serList.add(lineSeries);

        lineOpt.put("xAxis",xAxis);
        lineOpt.put("yAxis",yAxis);
        lineOpt.put("series",serList);

        return lineOpt;
    }

    public JSONObject CreateSocialnet(List<Map> coAuthors,String message){
        JSONObject graphOpt = new JSONObject();

        graphOpt.put("title", TitleOption("SOCIAL NETWORK"));
        graphOpt.put("backgroundColor","#607B8B");

        JSONObject optTooltip=new JSONObject();
        graphOpt.put("tooltip",optTooltip);

        graphOpt.put("animationDurationUpdate",1500);
        graphOpt.put("animationEasingUpdate","quinticInOut");


        List<JSONObject> graphSeries = new ArrayList<>();
        JSONObject upopt = new JSONObject();

        upopt.put("type","graph");
        upopt.put("layout","force");
        upopt.put("symbolSize",50);
        upopt.put("roam",true);
        upopt.put("draggable",true);
        JSONObject forceOpt = new JSONObject();
        forceOpt.put("initLayout","circular");
        forceOpt.put("repulsion",400);
        int[] lengthRange = {0,185};
        forceOpt.put("edgeLength",lengthRange);
        upopt.put("force",forceOpt);

        JSONObject labelopt = new JSONObject();
        labelopt.put("show",true);
        upopt.put("label",labelopt);

        String[] edgeSymbol= {"circle","arrow"};
        int[] edgeSymbolSize = {4,10};
        JSONObject edgeLabel = new JSONObject();
        edgeLabel.put("fontSize",20);
        upopt.put("edgeSymbol",edgeSymbol);
        upopt.put("edgeSymbolSize",edgeSymbolSize);
        upopt.put("edgeLabel",edgeLabel);


        List<JSONObject> dataNode = new ArrayList<>();
        List<JSONObject> dataLink = new ArrayList<>();

        JSONObject rootNode = new JSONObject();
        JSONObject itemStyle = new JSONObject();
        itemStyle.put("color","#32C5E9");
        rootNode.put("name",message);
        rootNode.put("itemStyle",itemStyle);
        dataNode.add(rootNode);
        for(Map author : coAuthors){
            JSONObject nodeAuthor = new JSONObject();
            JSONObject linkAuthor = new JSONObject();

            String hIndex = (String) author.get("hindex");
            nodeAuthor.put("name",author.get("name"));
            nodeAuthor.put("value","h-index:"+hIndex);

            JSONObject nodeStyle = new JSONObject();
            nodeStyle.put("color",ToolBag.RgbReturn(hIndex));
            nodeAuthor.put("itemStyle",nodeStyle);

            linkAuthor.put("source",message);
            linkAuthor.put("target",author.get("name"));

            dataNode.add(nodeAuthor);
            dataLink.add(linkAuthor);
        }
        upopt.put("data",dataNode);
        upopt.put("links",dataLink);


        graphSeries.add(upopt);
        graphOpt.put("series",graphSeries);


        return graphOpt;
    }

    public JSONObject PublicationTable(List<Map> Publications){
        JSONObject pubTable = new JSONObject();
        List<JSONObject> publiCations = new ArrayList<>();

        for(Map pub : Publications){
            JSONObject pubTemp =new JSONObject();
            if (pub.containsKey("num_citations")){
                pubTemp.put("title",pub.get("title"));
                pubTemp.put("citations",Integer.parseInt((String) pub.get("num_citations")));
                pubTemp.put("years",pub.get("pub_year"));
                publiCations.add(pubTemp);
            }

        }

        //sort
        for(int i =1;i<publiCations.size();++i)
            for (int j = i - 1; (((int) (publiCations.get(j + 1).get("citations"))) < (int) (publiCations.get(j).get("citations"))) && (j >= 0); --j) {
                JSONObject tempSwap = publiCations.get(j + 1);
                publiCations.set(j + 1, publiCations.get(j));
                publiCations.set(j, tempSwap);
            }

        pubTable.put("publications",publiCations);
        return pubTable;
    }

    public JSONObject RiverChart(JSONObject jsonObj){
        JSONObject riverOpt = new JSONObject(); //5 Parts: tooltip, legend, singleAxis, series, color


        //tooltip
        JSONObject tooltip =new JSONObject();//2 Parts: trigger,axisPointer


        JSONObject axisPointerOftooltip = new JSONObject();//2 Parts:type, lineStyle

        JSONObject lineStyleOftooltip = new JSONObject();
        lineStyleOftooltip.put("color","rgba(0,0,0,0.2)");
        lineStyleOftooltip.put("width",1);
        lineStyleOftooltip.put("type","solid");

        axisPointerOftooltip.put("type","line");
        axisPointerOftooltip.put("lineStyle", lineStyleOftooltip);

        tooltip.put("trigger","axis");
        tooltip.put("axisPointer", axisPointerOftooltip);


        //color
        String[] color = {"#BBFFFF","#FFDAB9","#F0FFF0","#FFF0F5","#000080",
                "#00BFFF","#00CD00","#00FF7F","#FFFF00","#FF00FF",
                "#F0FFF0","#FF7F24","#9370DB","#FF6A6A","#FFFFE0",
                "#C1FFC1","#FAF0E6","#00F5FF","#EE2C2C","#D2691E",
        "#A52A2A","#2E8B57","#1C1C1C"};


        //legend
        JSONObject legend =new JSONObject(); //1 Part data
        legend.put("data",ToolBag.KeyofJsonObj(JSONObject.parseObject(jsonObj.get(ToolBag.KeyofJsonObj(jsonObj).get(0)).toString())));


        //singleAxis
        JSONObject singleAxis = new JSONObject();//7 Parts:5 + axisPointer, splitLine

            //axisPointer
        JSONObject axisPointerOfsingleAxis = new JSONObject();//2 Parts: animation, label

        JSONObject labelOfaxisPointerOfsingleAxis = new JSONObject();
        labelOfaxisPointerOfsingleAxis.put("show",true);

        axisPointerOfsingleAxis.put("animation",true);
        axisPointerOfsingleAxis.put("label",labelOfaxisPointerOfsingleAxis);

            //splitLine
        JSONObject  splitLine = new JSONObject();//2 Parts:show, linStyle

        JSONObject lineStyleOfsplitLine = new JSONObject();
        lineStyleOfsplitLine.put("type","dashed");
        lineStyleOfsplitLine.put("opacity",0.2);

        splitLine.put("show",true);
        splitLine.put("lineStyle",lineStyleOfsplitLine);

        singleAxis.put("top",50);
        singleAxis.put("bottom",50);
        singleAxis.put("axisTick","{}");
        singleAxis.put("axisLabe","{}");
        singleAxis.put("type","time");
        singleAxis.put("axisPointer",axisPointerOfsingleAxis);
        singleAxis.put("splitLine",splitLine);


        //series
        List<JSONObject> seriesOption = new ArrayList<>();
        JSONObject seriesOfriver = new JSONObject();//3 Parts: type, emphasis, data

            //itemStyle
        JSONObject itemStyle = new JSONObject();//1 Parts
        JSONObject emphasisOfseries = new JSONObject(); //2 Parts

        emphasisOfseries.put("shadowBlur",20);
        emphasisOfseries.put("shadowColor","rgba(0, 0, 0, 0.8)");

        itemStyle.put("emphasis",emphasisOfseries);


            //data
        List<List<Object>> dataOfseries = new ArrayList<>();

        for(String date:ToolBag.DateSort(ToolBag.KeyofJsonObj(jsonObj))){
            JSONObject dateTpc = JSONObject.parseObject(jsonObj.get(date).toString());

            for(String keyToc:dateTpc.keySet()){
                List<Object> dateEle = new ArrayList<>();
                dateEle.add(date);
                dateEle.add(Float.parseFloat(dateTpc.getString(keyToc)));
                dateEle.add(keyToc);
                dataOfseries.add(dateEle);
            }
        }

        seriesOfriver.put("type","themeRiver");
        seriesOfriver.put("itemStyle",itemStyle);
        seriesOfriver.put("data",dataOfseries);

        seriesOption.add(seriesOfriver);



        riverOpt.put("color",color);
        riverOpt.put("tooltip",tooltip);
        riverOpt.put("legend",legend);
        riverOpt.put("singleAxis",singleAxis);
        riverOpt.put("series",seriesOption);


        return riverOpt;
    }

    public JSONObject KlineTopic(JSONObject jsonObj){
        Float thresholdFrequency = new Float(37.0);

        JSONObject klineOpt = new JSONObject();//3 parts: title,xAxis,yAxis,series

        JSONObject xAxis = new JSONObject();
        JSONObject axisLabel =new JSONObject();
        axisLabel.put("interval",0);

        xAxis.put("data",ToolBag.Top5Wdfreq(jsonObj));
        xAxis.put("axisLabel",axisLabel);


        JSONObject yAxis = new JSONObject();
        JSONObject axisLine = new JSONObject();
        axisLine.put("show",false);
        yAxis.put("min",2005);
        yAxis.put("max",2025);
        yAxis.put("minInterval",1);
        yAxis.put("maxInterval",1);
        yAxis.put("axisLine",axisLine);
        yAxis.put("name","Top5's\n Distribution");


        List<JSONObject> seriesOfkline = new ArrayList<>();
        JSONObject seriesOfS = new JSONObject();

        seriesOfS.put("type","k");

        List<List<Float>> dataOpt = new ArrayList<>();
        for (String topWD:ToolBag.Top5Wdfreq(jsonObj)){
            dataOpt.add(ToolBag.SpanUpDown(topWD,jsonObj,thresholdFrequency));
        }

        seriesOfS.put("data",dataOpt);

        seriesOfkline.add(seriesOfS);

        klineOpt.put("xAxis",xAxis);
        klineOpt.put("yAxis",yAxis);
        klineOpt.put("series",seriesOfkline);
        return klineOpt;
    }
}
