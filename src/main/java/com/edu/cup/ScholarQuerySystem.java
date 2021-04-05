package com.edu.cup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.neo4j.driver.internal.shaded.io.netty.handler.codec.json.JsonObjectDecoder;

import java.util.*;
import java.util.stream.Collectors;

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

    public static JSONObject TopicEvolution(String path,JSONObject timeSpan,int tpcNum){
        EchartOption echartOption = new EchartOption();
        JSONObject jsonFile = ToolBag.ReadJsonObj(path);
        JSONObject tpcevlOpt = new JSONObject();
        try{
            //River
            JSONObject riverOpt = echartOption.RiverChart(jsonFile,timeSpan,tpcNum);
            //bar
            JSONObject klineOpt = echartOption.KlineTopic(jsonFile);
            System.out.println(klineOpt);
            //成功标志
            tpcevlOpt.put("flag",true);

            tpcevlOpt.put("river",riverOpt);
            tpcevlOpt.put("kline",klineOpt);

        }catch (Exception e){
            //失败标志
            tpcevlOpt.put("flag",false);
        }

        return tpcevlOpt;
    }

    public static JSONObject TopicWords(String pathFre,String pathSen,JSONObject timeSpan,int tpcNum){
        int cutEdge = 50;
        JSONObject fileFre = ToolBag.ReadJsonObj(pathFre);
        JSONObject fileSen = ToolBag.ReadJsonObj(pathSen);
        JSONObject tpdWds = new JSONObject();
        try {
            List<String> topFre = ToolBag.TopWdfreq(fileFre,tpcNum,timeSpan);
            List<JSONObject> msgJson = new ArrayList<>();

            for (String wd : topFre){
                JSONObject msgTmp = new JSONObject();
                msgTmp.put("word",wd);

                List<JSONObject> senOpt = new ArrayList<>();

                List<String> wdSen = (List<String>) fileSen.get(wd);
                Random random = new Random();
                int rIndex = random.nextInt(wdSen.size() - cutEdge);
                List<String> senCut = wdSen.subList(rIndex%wdSen.size(),(rIndex+cutEdge-1)% wdSen.size());

                for(String sen : senCut){
                    List<String> wordOFSen = Arrays.asList(sen.split(" "));
                    int splitIndex = wordOFSen.indexOf(wd);
                    if (splitIndex >0){
                        JSONObject senObj = new JSONObject();
                        senObj.put("head",wordOFSen.subList(0,splitIndex).stream().collect(Collectors.joining(" ")));
                        senObj.put("body",wd);
                        senObj.put("tail",wordOFSen.subList(splitIndex+1,wordOFSen.size()).stream().collect(Collectors.joining(" ")));
                        senOpt.add(senObj);
                    }
                }
                msgTmp.put("sentences",senOpt);
                msgJson.add(msgTmp);
            }
            tpdWds.put("flag",true);
            tpdWds.put("msg",msgJson);
        }catch (Exception e){
            tpdWds.put("flag",false);
        }
        return tpdWds;
    }

    public static JSONObject TopicSingleWord(String word,String pathSen){
        JSONObject fileSen = ToolBag.ReadJsonObj(pathSen);
        JSONObject tpdWds = new JSONObject();
        try{
            tpdWds.put("flag",true);
            List<String> sentenceSet = (List<String>) fileSen.get(word);
            Random random = new Random();
            int rIndex = random.nextInt(sentenceSet.size());
            String sentenceResult = sentenceSet.get(rIndex);

            List<String> wordOFSen = Arrays.asList(sentenceResult.split(" "));
            int splitIndex = wordOFSen.indexOf(word);
            if (splitIndex >0) {
                JSONObject senObj = new JSONObject();
                senObj.put("head", wordOFSen.subList(0, splitIndex).stream().collect(Collectors.joining(" ")));
                senObj.put("body", word);
                senObj.put("tail", wordOFSen.subList(splitIndex + 1, wordOFSen.size()).stream().collect(Collectors.joining(" ")));
                tpdWds.put("sentence",senObj);
            }else {
                tpdWds.put("flag",false);
            }
        }catch (Exception e){
            tpdWds.put("flag",false);
        }
        return tpdWds;
    }

    public List<JSON> DomainCommon(){
        return null;
    }

}

