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


public class CypherQuery implements AutoCloseable{
    private final Driver driver;
    private final String url = "bolt://10.16.30.69:7687";


    public CypherQuery(String user, String password ) {
        driver = GraphDatabase.driver( this.url, AuthTokens.basic( user, password ) );
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

    //查询出版物作者(查找很慢)
    public List<String> GetAuthors(final String message){
        return MutiResults("match (a:SCHOLAR)-[:publish]->(n:PUBLICATION{title:$elem}) return a.name",
                message);
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


    //查询某一领域的学者
    public String GetResearchdomain(final String message){
        return null;
    }

    //查询某一单位学者
    public String GetAffiliation(final String message){
        return null;
    }
}
