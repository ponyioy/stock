import java.io.*;

/**
 * Created by ucs_yangshijie on 2017/7/11.
 */
public class test {
    public static  void readFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("E:/编辑1.txt")));
        String tempString = null;
        int line = 1;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = reader.readLine()) != null) {
            if(tempString.length() >= 6)
            if(tempString.substring(0,6).matches("^\\d{6}$"))
                System.out.println(tempString.substring(0,6));
        }
    }
    public static void main(String[] args) throws IOException {
//        test.readFile();
    	
    }
}
