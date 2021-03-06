import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {
    static int countingValleys(int n, String s) {

        //The key is that sea level is established and a valley only counts once you've reached an equivalent # of downs and ups to go back to sea level. This lends itself to a stack.
        int altitude = 0;
        int numValleys = 0;

        for (int i=0;i<n;i++){
            if(s.charAt(i)=='U'){
                if(altitude==-1){
                    numValleys++;//we get back up to altitude
                }
                altitude++;
            }
            if(s.charAt(i)=='D'){
                altitude--;
            }
        }
        return numValleys;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        String s = scanner.nextLine();

        int result = countingValleys(n, s);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
