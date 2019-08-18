package zys.practice.seckillcommon;

import java.io.FileWriter;

public class CSV {

    public static void main(String[] args) {
        try {
            FileWriter fw = new FileWriter("D:\\user.csv");
            for (int i = 1; i <= 1000; i++) {
                fw.write(String.format("%d,%d\n", i, i%2+1));
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

}
