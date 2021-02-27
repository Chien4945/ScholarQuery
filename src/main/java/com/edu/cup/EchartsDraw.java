package com.edu.cup;

/**
 * @Project: KG and QA
 * @Author: zhangjian
 * @Employer： CUP-CS
 * @version: 1.0
 * @Date: 2021/1/16 16:14
 */


import javax.tools.Tool;
import javax.xml.crypto.dsig.spec.HMACParameterSpec;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/*Give up*/
public class EchartsDraw {
    private final String type;           // socialNet,publicationsNet,citedLine,INFO
    private final String drawscriptPath=
            "/Users/zhangjian/IdeaProjects/ScholarQuery/drawScript/drawScript.py";

    public EchartsDraw(String type){
        this.type=type;
    }

    private String ExecuteScript(String command){
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String fPath = in.readLine();
            in.close();
            proc.waitFor();
            System.out.println("EXECUTE!");
            return fPath;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String SaveHTML(List<Map> resultList,String scholar){
        if (this.type == "SocialNet" ){
            String msg4Python = ToolBag.MaptoString(resultList);
            String inputParameter = String.format("--type=\"%s\" --message=\"%s\" --scholar=\"%s\"",this.type,msg4Python,scholar);
            String command4Python = String.format("python %s %s",drawscriptPath,inputParameter);
            return ExecuteScript(command4Python);
        }
        else if(this.type == "publicationsNet"){
            String msg4Python = ToolBag.MaptoString(resultList);
            String inputParameter = String.format("%s %s",this.type,msg4Python);
            String command4Python = String.format("python %s %s",drawscriptPath,inputParameter);
            return ExecuteScript(command4Python);
        }
        return null;
    }

    public String SaveHTML(String citeData){
        if (this.type == "citedLine"){
            String msg4Python = citeData;
            String inputParameter = String.format("%s %s",this.type,msg4Python);
            String command4Python = String.format("python %s %s",drawscriptPath,inputParameter);
            return ExecuteScript(command4Python);
        }
        return null;
    }

    public static String showTopicEvolution(Map topics){
        return null;
    }
}