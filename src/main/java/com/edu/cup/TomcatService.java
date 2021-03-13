package com.edu.cup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project: KG and QA
 * @Author: zhangjian
 * @Employer： CUP-CS
 * @version: 1.0
 * @Date: 2021/2/21 11:41
 */
public class TomcatService extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");

        String hint = request.getParameter("hint");


        if (hint.equals("recommand")){
            ScholarQuerySystem queryDB = new ScholarQuerySystem("bolt://10.16.30.69:7687","neo4j", "neo123", "any");
            JSONObject rcmdResult = new JSONObject();
            rcmdResult.put("recommand",queryDB.RecommandRandom());

            Writer out = response.getWriter();
            out.write(JSON.toJSONString(rcmdResult));
            out.close();
        }else if(hint.equals("query")){
            String message = request.getParameter("message");
            ScholarQuerySystem queryDB = new ScholarQuerySystem("bolt://10.16.30.69:7687","neo4j", "neo123", message);

            List<JSONObject> qryResult = new ArrayList<>();
            try{
                qryResult = queryDB.ScholarInfo();
            }catch (Exception e){
                JSONObject fuzzRtn = new JSONObject();
                fuzzRtn.put("fuzzy",queryDB.FuzzyReturn(message));
                qryResult.add(fuzzRtn);
            }
            Writer out = response.getWriter();
            out.write(JSON.toJSONString(qryResult));
            out.close();
        }else if (hint.equals("evolution")){
            String pathOpt = "/Users/zhangjian/Desktop/TopicFrequence.json";
            List<JSONObject> evlResult = ScholarQuerySystem.TopicEvolution(pathOpt);

            Writer out = response.getWriter();
            out.write(JSON.toJSONString(evlResult));
            out.close();
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}