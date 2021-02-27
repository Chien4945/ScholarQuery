package com.edu.cup;

/**
 * @Project: KG and QA
 * @Author: zhangjian
 * @Employer： CUP-CS
 * @version: 1.0
 * @Date: 2021/1/16 14:14
 */

import static org.neo4j.driver.Values.parameters;

import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class CypherQuery implements AutoCloseable{
    private final Driver driver;


    public CypherQuery(String url,String user, String password ) {
        driver = GraphDatabase.driver( url, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() {
        this.driver.close();
    }

    //多结果返回查询
    private List<String> MutiResults(final String Query,final String message){
        try (Session session = driver.session()) {
            return session.readTransaction(tx -> {
                List<String> results = new ArrayList<>();
                Result result = tx.run(Query, parameters("elem", message));
                while (result.hasNext()) {
                    String rs = result.next().get(0).asString();
                    results.add(rs);
                }return results;
            });
        }
    }

    //多结果返回查询，返回多个结果的json信息。
    private List<Map> MutiProperties(final String Query, final String message ) {
        try (Session session = driver.session()) {
            return session.readTransaction(tx -> {
                List<Map> mutiResult = new ArrayList<>();
                Result result = tx.run(Query,parameters("elem", message));
                while (result.hasNext()) {
                    Map mutiReturn = result.next().asMap();
                    mutiResult.add((Map) mutiReturn.get("properties(n)"));
                } return mutiResult;
            });
        }
    }

    //单结果返回查询
    private String SingleResult(final String Query, final String message ) {
//        Query:输入的查询语句
//        message：查询的对象
        try (Session session = driver.session()) {
            return session.readTransaction(tx -> {
                Result result = tx.run(Query,parameters("elem", message));
                return result.single().get(0).asString();
            });
        }
    }

    //单结果属性查询，返回一个结果的json信息
    private Map SinglrProperties(final String Query,final String message){
        try (Session session = driver.session()) {
            return session.readTransaction(tx -> {
                Result result = tx.run(Query,parameters("elem", message));
                Map mapReturn = result.single().asMap();
                return (Map) mapReturn.get("properties(n)"); //变量名与查询语句命名有关
            });
        }
    }

    //查询兴趣领域
    public List<String> GetDomain(final String message){
        return MutiResults("MATCH (a:SCHOLAR{name:$elem})-[:interest]->(n) RETURN n.domain",message);
    }

    //查询合作学者
    public List<Map> GetCoauthors(final String message ) {
        List<Map> results= MutiProperties(
                "MATCH (a:SCHOLAR{name:$elem})-[:coauthors]->(n) RETURN properties(n)",
                message);
        return results;
    }

    //查询出版物
    public List<Map> GetPublications(final String message ) {
        List<Map> results= MutiProperties(
                "MATCH (a:SCHOLAR{name:$elem})-[:publish]->(n) RETURN properties(n)",
                message);
        return results;
    }

    //查询被引增减情况
    public String GetCitesperyears(final String message){
        String Query = String.format("MATCH (n:SCHOLAR{name:$elem}) return n.%s","cites_per_year");
        String CitesList = SingleResult(Query,message);
        return CitesList;
    }

    //查询单个学者信息
    public Map GetAuthorinfo(final String message){
        Map temp = SinglrProperties("MATCH (n:SCHOLAR{name:$elem}) RETURN properties(n)",message);
        return temp;
    }

    //返回推荐搜索的学者
    public List<String> GetRecommmand(){
        String[] ENDS={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        String[] STARTS={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        Random randomIndex = new Random();
        List<String> start = new ArrayList<>();
        List<String> end = new ArrayList<>();
        List<String> returnRecommand = new ArrayList<>();

        String startQuery = String.format("match (n:SCHOLAR)" +
                "where n.name ends with(\"%s\")" +
                "return n.name  limit 3",STARTS[randomIndex.nextInt(52)]);
        String endQuery = String.format("match (n:SCHOLAR)" +
                "where n.name starts with(\"%s\")" +
                "return n.name  limit 3",STARTS[randomIndex.nextInt(26)]);
        start = MutiResults(startQuery,"name");
        end = MutiResults(endQuery,"name");
        returnRecommand.addAll(start);
        returnRecommand.addAll(end);
        return returnRecommand;
    }

    //返回模糊查询的可能结果
    public List<String> ProbsReturn(String msg){
        String probQuery = String.format("match (n:SCHOLAR)" +
                "where n.name contains \"%s\"" +
                "return n.name",msg) ;
        return MutiResults(probQuery,"None");
    }

    //返回出版物的作者（目前速度还是太慢！暂时没法用）
    public List<String> GetAuthors(String message){
        //"MATCH (a:PUBLICATION{title:$elem})-[:author]->(n) return n.name"
        String authorsQuery ="MATCH (n)-[:publish]->(a:PUBLICATION{title:$elem}) return n.name";
        List<String> authors = MutiResults(authorsQuery,message);
        return authors;
    }

    //查询某一领域的学者
    public String GetResearchdomain(final String message){
        return null;
    }

    //查询某一单位学者
    public String GetAffiliation(final String message){
        return null;
    }
}
