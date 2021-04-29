package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class test {
    public static void main(String[] args) {
//        int[] arr = {1,2, 4};
//        File CWD = new File(System.getProperty("user.dir"));
//        try {
//            System.out.println(Arrays.toString(CWD.list()));
//        } catch (Exception e) {
//            System.out.println("err");
//        }

//        Commit c = new Commit("nihao");
//        c.saveCommit();

        Set<String> ss = new TreeSet<>();
        ss.add("one");
        ss.add("two");
        ss.add("q");
        ss.add("a");
        for (String s: ss) {
            System.out.println(s);
        }

    }
}
