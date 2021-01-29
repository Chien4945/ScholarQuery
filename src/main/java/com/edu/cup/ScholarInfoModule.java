package com.edu.cup;

/**
 * @Project: KG and QA
 * @Author: zhangjian
 * @Employer： CUP-CS
 * @version: 1.0
 * @Date: 2021/1/17 10:52
 */
public class ScholarInfoModule {
    private final String scholar;
    private final CypherQuery Neo4jDB;

    public ScholarInfoModule(String neo4jUser,String neo4jpasswd,String scholar){
        this.scholar = scholar;
        this.Neo4jDB = new CypherQuery(neo4jUser,neo4jpasswd);
    }

    public String ScholarInfoVisualize(){
        EchartsDraw echart  = new EchartsDraw("InfoVis");
        String filepath = echart.SaveHTML(this.Neo4jDB.//(this.scholar),this.scholar);
    }

    public String ScholarSocialnet(){
        EchartsDraw echart = new EchartsDraw("SocialNet");
        String filepath = echart.SaveHTML(this.Neo4jDB.GetCoauthors(this.scholar),this.scholar);
        System.out.println(filepath);
        return filepath;
    }

//    public String scholarPublicationsNet(){
//        EchartsDraw echart = new EchartsDraw("publicationsNet");
//        String filepath = echart.saveHTML(this.Neo4jDB.GetPublications(this.scholar));
//        return filepath;
//    }

    public String scholarCitesLine(){
        EchartsDraw echart = new EchartsDraw("citedLine");
        String filepath = echart.SaveHTML(this.Neo4jDB.GetCitesperyears(this.scholar));
        return filepath;
    }

    public String researchdominNet(){
        return null;
    }

    public String affiliationNet(){
        return null;
    }
}
