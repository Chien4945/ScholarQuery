package com.edu.cup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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


    public List<JSONObject> ScholarInfo(){
        List<JSONObject> profileOption = new ArrayList<>();
        EchartOption echartOption = new EchartOption();

        JSONObject scholarJson = JSONObject.parseObject(ToolBag.MaptoString(Neo4jDB.GetAuthorinfo(this.message)));
        profileOption.add(echartOption.CreateProfile(scholarJson,Neo4jDB.GetDomain(this.message)));
        profileOption.add(echartOption.CreateLineChart(JSONObject.parseObject(scholarJson.getString("cites_per_year"))));
        profileOption.add(echartOption.CreateSocialnet(Neo4jDB.GetCoauthors(this.message),this.message));
        profileOption.add(echartOption.PublicationTable(Neo4jDB.GetPublications(this.message)));

        Neo4jDB.close();
        return profileOption;
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

/**
0225晚任务：
    1.学者配置信息重新整理，按类型分开；
    2.修改配置：title，调整斥力...；
**/