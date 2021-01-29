import com.edu.cup.CypherQuery;
import com.edu.cup.ScholarInfoModule;
import com.edu.cup.ToolBag;
import java.util.List;
import java.util.Map;

/**
 * @Project: KG and QA
 * @Author: zhangjian
 * @Employer： CUP-CS
 * @version: 1.0
 * @Date: 2021/1/16 17:47
 */

/***
 * 01.28注：
对于非法查找，给出错误信息，注意与echart的结果。
echarts在微信小程序上的兼容。
*/

public class TEST {
    public String scholar ;

    public TEST(String scholar){
        this.scholar=scholar;
    }

    public void trySocialNet(){
        ScholarInfoModule T_T = new ScholarInfoModule("neo4j","neo123",this.scholar);
        System.out.println(T_T.ScholarSocialnet());
    }

//    public void tryPublicationsNet(){
//        ScholarInfoModule T_T = new ScholarInfoModule("neo4j","neo123",this.scholar);
//        System.out.println(T_T.scholarPublicationsNet());
//        System.exit(0);
//    }

    public void tryCitesLine(){
        ScholarInfoModule T_T = new ScholarInfoModule("neo4j","neo123",this.scholar);
        System.out.println(T_T.scholarCitesLine());
        System.exit(0);
    }

    public void TestMap(){
        CypherQuery test = new CypherQuery("neo4j","neo123");
        List<Map> m=test.GetCoauthors(this.scholar);
        System.out.println(ToolBag.MaptoString(m));
 //       System.exit(0);
    }

    public static void main(String[] args) {
        TEST orz = new TEST("Zhiyuan Liu");
        //orz.tryCitesLine();
        //orz.tryPublicationsNet();
        orz.trySocialNet();
        //orz.TestMap();
    }
}
