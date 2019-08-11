package zys.learning.redismiaoshademo.util;

import java.io.FileWriter;

public class CSVWriter {

    public static void main(String[] args) {
        try {
            FileWriter fw = new FileWriter("D:\\user.csv");
            for (int i = 1; i <= 1000; i++) {
                fw.write(String.format("%d,1\n", i));
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

}
