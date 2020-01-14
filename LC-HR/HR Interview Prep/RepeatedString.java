import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    // Complete the repeatedString function below.
    static long repeatedString(String s, long n) {
        //first check how many times we can find string
        long wholeStrings = n/s.length(); //total times s goes into the target # of characters.
        int remainder = (int)(n % s.length()); //the remainder portion used in the substring.
        long foundA = 0;
        //so first go over the initial string, find the number of 'a's.
        for(char i: s.toCharArray()){//loop through initial string, find the number of 'a's
            if(i=='a'){
                foundA++;
            }
        }
        long total = foundA * wholeStrings;//total times a occurs in all whole strings
        // now if we have a remainder
        if(remainder==0){
            return total;
        }
        else{ // in this case, we have to create a substring and count the number of 'a's in it.
            String partial = s.substring(0,remainder);
            for (char j: partial.toCharArray()){
                if(j=='a'){
                    total++;
                }
            }
        }
        return total;

    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String s = scanner.nextLine();

        long n = scanner.nextLong();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        long result = repeatedString(s, n);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
