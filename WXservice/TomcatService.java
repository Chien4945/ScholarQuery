import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.edu.cup.ScholarQuerySystem;

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

        String DBurls = "bolt://10.16.33.153:7687";

        String hint = request.getParameter("hint");


        if (hint.equals("recommand")){
            ScholarQuerySystem queryDB = new ScholarQuerySystem(DBurls,"neo4j", "neo123", "any");
            JSONObject rcmdResult = new JSONObject();
            rcmdResult.put("recommand",queryDB.RecommandRandom());

            Writer out = response.getWriter();
            out.write(JSON.toJSONString(rcmdResult));
            out.close();
        }else if(hint.equals("query")){
            String message = request.getParameter("message");
            ScholarQuerySystem queryDB = new ScholarQuerySystem(DBurls,"neo4j", "neo123", message);

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
        }else if (hint.equals("Topic")){
            String pathFreOpt = "/home/cup1504/apache-tomcat-9.0.43/webapps/TopicInfo/TopicFrequence.json";
            String pathSenOpt = "/home/cup1504/apache-tomcat-9.0.43/webapps/TopicInfo/TopicSen.json";
            JSONObject timeSpan = JSONObject.parseObject(request.getParameter("timespan"));
            int numTopic =Integer.parseInt(request.getParameter("numTopic").toString());
            String message = request.getParameter("message");
            String word = request.getParameter("word");

            if (message.equals("river")){
                JSONObject riverResult = ScholarQuerySystem.TopicEvolution(pathFreOpt,timeSpan,numTopic);
                Writer out = response.getWriter();
                out.write(JSON.toJSONString(riverResult));
                out.close();
            }else if (message.equals("words")){
                JSONObject wordsResult = new JSONObject();
                if (word.equals("all")){
                    wordsResult = ScholarQuerySystem.TopicWords(pathFreOpt,pathSenOpt,timeSpan,numTopic);
                }else{
                    String wordSingle = request.getParameter("word");
                    wordsResult = ScholarQuerySystem.TopicSingleWord(wordSingle,pathSenOpt);
                }
                Writer out = response.getWriter();
                out.write(JSON.toJSONString(wordsResult));
                out.close();
            }




        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
