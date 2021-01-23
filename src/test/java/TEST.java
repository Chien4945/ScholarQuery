import com.edu.cup.CypherQuery;
import com.edu.cup.EchartsDraw;
import com.edu.cup.ScholarInfoModule;

import java.awt.event.TextEvent;
import java.util.List;

/**
 * @Project: KG and QA
 * @Author: zhangjian
 * @Employer： CUP-CS
 * @version: 1.0
 * @Date: 2021/1/16 17:47
 */

public class TEST {
    public String scholar ;

    public TEST(String scholar){
        this.scholar=scholar;
    }

    public void trySocialNet(){
        ScholarInfoModule T_T = new ScholarInfoModule("neo4j","neo123",this.scholar);
        System.out.println(T_T.scholarSocialNet());
        System.exit(0);
    }

    public void tryPublicationsNet(){
        ScholarInfoModule T_T = new ScholarInfoModule("neo4j","neo123",this.scholar);
        System.out.println(T_T.scholarPublicationsNet());
        System.exit(0);
    }

    public void tryCitesLine(){
        ScholarInfoModule T_T = new ScholarInfoModule("neo4j","neo123",this.scholar);
        System.out.println(T_T.scholarCitesLine());
        System.exit(0);
    }

    public static void main(String[] args) {
        TEST orz = new TEST("Christopher D Manning");
        //orz.tryCitesLine();
        //orz.tryPublicationsNet();
        orz.trySocialNet();
    }
}
