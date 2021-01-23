package com.edu.cup;

/**
 * @Project: KG and QA
 * @Author: zhangjian
 * @Employer： CUP-CS
 * @version: 1.0
 * @Date: 2021/1/16 14:14
 */

import static org.neo4j.driver.Values.parameters;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CypherQuery implements AutoCloseable{
    private final Driver driver;
    private final String url = "bolt://10.16.30.69:7687";


    public CypherQuery(String user, String password )
    {
        driver = GraphDatabase.driver( this.url, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception {
        this.driver.close();
    }

    //多结果返回查询
    private List<String> mutiResults( final String Query,final String message ) {
        try (Session session = driver.session()) {
            return session.readTransaction(tx -> {
                List<String> coauthorsNames = new ArrayList<>();
                Result result = tx.run(Query,parameters("elem", message));
                while (result.hasNext()) {
                    coauthorsNames.add(result.next().get(0).asString());
                }
                return coauthorsNames;
            });
        }
    }

    //单结果返回查询
    private String singleResults( final String Query,final String message ) {
        try (Session session = driver.session()) {
            return session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run(Query ,parameters("elem",message));
                    return result.single().get( 0 ).asString();
                }
            }
            );
        }
    }

    //查询合作学者
    public List<String> getCoauthors( final String message ) {
        List<String> results=mutiResults(
                "MATCH (a:SCHOLAR{name:$elem})-[:COAUTHOR]->(n) RETURN n.name",
                message);
        results.add(message);
        return results;
    }

    //查询出版物
    public List<String> getPublications( final String message ) {
        return mutiResults(
                "MATCH (a:SCHOLAR{name:$elem})-[:AUTHOR]->(n) RETURN n.title",
                message);
    }

    //查询被引增减情况
    public String getCitesperyears(final String message){
        String Query = String.format("MATCH (n:SCHOLAR{name:$elem}) return n.%s","cites_per_year");
        String CitesList = singleResults(Query,message);
        return CitesList;
    }

    //查询某一领域的学者
    public String getResearchdomain(final String message){
        return null;
    }

    //查询某一单位学者
    public String getAffiliation(final String message){
        return null;
    }
}
