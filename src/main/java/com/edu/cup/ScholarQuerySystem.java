package com.edu.cup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPatch;

import java.util.*;

/**
 * @Project: KG and QA
 * @Author: zhangjian
 * @Employer： CUP-CS
 * @version: 1.0
 * @Date: 2021/1/17 10:52
 */
public class ScholarQuerySystem {
    private final String message;
    private final CypherQuery Neo4jDB;

    public ScholarQuerySystem(String neo4jUser, String neo4jPasswd, String message){
        this.message = message;
        this.Neo4jDB = new CypherQuery(neo4jUser,neo4jPasswd);
    }

    public void ExceptClose(){
        Neo4jDB.close();
    }

    public List<JSONObject> ScholarInfo(){
        List<JSONObject> infoReturn = new ArrayList<>();
        JSONObject jsonResult = JSONObject.parseObject(ToolBag.MaptoString(Neo4jDB.GetAuthorinfo(message)));


        //学者信息
        JSONObject inforOpt = new JSONObject();

        inforOpt.put("name",jsonResult.get("name"));
        inforOpt.put("email_domain",jsonResult.get("email_domain"));
        inforOpt.put("citedby",jsonResult.get("citedby"));
        inforOpt.put("citedby5y",jsonResult.get("citedby5y"));
        inforOpt.put("hindex",jsonResult.get("hindex"));
        inforOpt.put("hindex5y",jsonResult.get("hindex5y"));
        inforOpt.put("i10index",jsonResult.get("i10index"));
        inforOpt.put("i10index5y",jsonResult.get("i10index5y"));
        inforOpt.put("portraiturl",jsonResult.get("portraiturl"));
        List<String> domainInterst = Neo4jDB.GetDomain(this.message);
        inforOpt.put("domain",domainInterst);


        //折线图配置
        JSONObject lineOpt = new JSONObject();

        JSONObject tempCiteperyear = JSONObject.parseObject(jsonResult.getString("cites_per_year"));
        JSONObject xAxis = new JSONObject();
        JSONObject yAxis = new JSONObject();
        JSONObject linSeries = new JSONObject();

        List<String> xdata = new ArrayList<>();
        List<String> ydata = new ArrayList<>();
        for (String key : tempCiteperyear.keySet()){
            if (key != "2021"){
                xdata.add(key);
            }
        }
        Collections.sort(xdata);
        for(String key : xdata){
            ydata.add(tempCiteperyear.getString(key));
        }

        xAxis.put("type","category");
        xAxis.put("data",xdata);

        yAxis.put("type","value");

        linSeries.put("type","line");
        linSeries.put("data",ydata);
        List<JSONObject> serList = new ArrayList<>();
        serList.add(linSeries);

        lineOpt.put("xAxis",xAxis);
        lineOpt.put("yAxis",yAxis);
        lineOpt.put("series",serList);


        //关系图配置
        JSONObject graphOpt = new JSONObject();

        JSONObject optTitle = new JSONObject();
        final String TITLE = String.format("关系图%s",message);
        optTitle.put("text",TITLE);
        graphOpt.put("title",optTitle);

        JSONObject optTooltip=new JSONObject();
        graphOpt.put("tooltip",optTooltip);

        graphOpt.put("animationDurationUpdate",1500);
        graphOpt.put("animationEasingUpdate","quinticInOut");

            //绘图配置
        List<JSONObject> graphSeries = new ArrayList<>();

        JSONObject upopt = new JSONObject();
        upopt.put("type","graph");
        upopt.put("layout","force");
        upopt.put("symbolSize",50);
        upopt.put("roam",true);
        upopt.put("draggable",true);
        JSONObject forceOpt = new JSONObject();
        forceOpt.put("repulsion",400);
        forceOpt.put("gravity",0.5);
        forceOpt.put("edgeLength",256);
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

                //节点与边配置
                //data:[] links:[] lineStyle:{}
        //获取共同作者信息 List<Map>
        List<Map> coAuthors = Neo4jDB.GetCoauthors(this.message);
        List<JSONObject> dataNode = new ArrayList<>();
        List<JSONObject> dataLink = new ArrayList<>();

        //root
        JSONObject rootNode = new JSONObject();
        JSONObject itemStyle = new JSONObject();
        itemStyle.put("color","#32C5E9");
        rootNode.put("name",this.message);
        rootNode.put("itemStyle",itemStyle);
        dataNode.add(rootNode);


        for(Map author : coAuthors){
            JSONObject nodeAuthor = new JSONObject();
            JSONObject linkAuthor = new JSONObject();

            String hIndex = (String) author.get("hindex");
            nodeAuthor.put("name",author.get("name"));

            JSONObject nodeStyle = new JSONObject();
            nodeStyle.put("color",ToolBag.RgbReturn(hIndex));
            nodeAuthor.put("itemStyle",nodeStyle);

            linkAuthor.put("source",this.message);
            linkAuthor.put("target",author.get("name"));

            dataNode.add(nodeAuthor);
            dataLink.add(linkAuthor);
        }
        upopt.put("data",dataNode);
        upopt.put("links",dataLink);


        //获取出版物信息
        List<Map> pubLications = Neo4jDB.GetPublications(this.message);
        JSONObject pubOpt = new JSONObject();
        List<JSONObject> infoPublic = new ArrayList<>();
        for (Map pub : pubLications){
            JSONObject tmpPub = new JSONObject();
            tmpPub.put("title",pub.get("title"));
            tmpPub.put("pub_year",pub.get("pub_year"));
            tmpPub.put("num_citations",pub.get("num_citations"));
            infoPublic.add(tmpPub);
            //List<String> authors = Neo4jDB.GetAuthors((String) pub.get("title"));
        }
        pubOpt.put("publication",infoPublic);



        graphSeries.add(upopt);
        graphOpt.put("series",graphSeries);


        infoReturn.add(inforOpt);
        infoReturn.add(lineOpt);
        infoReturn.add(graphOpt);
        infoReturn.add(pubOpt);

        Neo4jDB.close();
        return infoReturn;
    }

    public List<String> RecommandRandom(){
        List<String> resultsRecommand = Neo4jDB.GetRecommmand();
        Neo4jDB.close();
        return resultsRecommand;
    }

    public List<String> FuzzyReturn(String msg){
        List<String> fuzzyResult = Neo4jDB.ProbsReturn(msg);
        Neo4jDB.close();
        return fuzzyResult;
    }

    public List<JSON> DomainCommon(){
        return null;
    }

}
