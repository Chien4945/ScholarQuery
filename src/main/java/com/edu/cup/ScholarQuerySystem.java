package com.edu.cup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.neo4j.driver.internal.shaded.io.netty.handler.codec.json.JsonObjectDecoder;

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

    public ScholarQuerySystem(String url,String neo4jUser, String neo4jPasswd, String message){
        this.message = message;
        this.Neo4jDB = new CypherQuery(url,neo4jUser,neo4jPasswd);
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

    public static JSONObject TopicEvolution(String path,JSONObject timeSpan){
        EchartOption echartOption = new EchartOption();
        JSONObject jsonFile = ToolBag.ReadJsonObj(path);
        JSONObject tpcevlOpt = new JSONObject();

        try{
            //River
            JSONObject riverOpt = echartOption.RiverChart(jsonFile,timeSpan);
            //bar
            JSONObject klineOpt = echartOption.KlineTopic(jsonFile);

            //成功标志
            tpcevlOpt.put("flag",true);

            tpcevlOpt.put("river",riverOpt);
            tpcevlOpt.put("kilne",klineOpt);

        }catch (Exception e){
            //失败标志
            tpcevlOpt.put("flag",false);
        }

        return tpcevlOpt;
    }

    public List<JSON> DomainCommon(){
        return null;
    }

}

