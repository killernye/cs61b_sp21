package gitlet;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) {
//        int[] arr = {1,2, 4};
//        File CWD = new File(System.getProperty("user.dir"));
//        try {
//            System.out.println(Arrays.toString(CWD.list()));
//        } catch (Exception e) {
//            System.out.println("err");
//        }

        Commit c = new Commit("nihao");
        c.saveCommit();



    }
}
