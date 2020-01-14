import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    Complete the twoStrings function below.
    static String twoStrings(String s1, String s2) { //IDEA 1
        //initial thought is to use brute force to loop through with a 2D for loop.
        for(int i=0;i<s1.length();i++){
            for(int j=0;j<s2.length();j++){
                if(s1.charAt(i)==s2.charAt(j)){
                    return "YES";
                }
            }
        }
        return "NO";
    }

    static String twoStrings(String s1, String s2) { //IDEA 2
        if(s1.length() < s2.length()){
            for(int i=0;i<s1.length();i++){//loop through all characters in smaller string.
                if(s2.indexOf(s1.charat(i)) != -1){//if indexOf retur
                    return "YES";
                }
            }
        }
        return "NO";

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int q = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int qItr = 0; qItr < q; qItr++) {
            String s1 = scanner.nextLine();

            String s2 = scanner.nextLine();

            String result = twoStrings(s1, s2);

            bufferedWriter.write(result);
            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        scanner.close();
    }
}
