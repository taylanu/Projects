import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    // Complete the twoStrings function below.
    static String twoStrings(String s1, String s2) {
        //initial thought is to use brute force to loop through with a 2D for loop.
        //key concepts to note are that there are only 26 letters.
        //Use Hashsets of characters.
        HashSet<Character> string1Chars = new HashSet();
        HashSet<Character> string2Chars = new HashSet();

        for(int i=0; i<s1.length(); i++){
            string1Chars.add(s1.charAt(i));
        }
        for(int i=0; i<s2.length(); i++){
            string2Chars.add(s2.charAt(i));
        }

        string1Chars.retainAll(string2Chars);//built in HashSet method that deletes everything in String1Chars that doesnt exist in String2Chars.
        //basically an AND or intersection method that keeps only characters in both.
        if(!string1Chars.isEmpty()){
            return "YES";
        }
        else{
            return "NO";
        }

    }

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
