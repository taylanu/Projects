import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    // Complete the countingValleys function below.
    static int countingValleys(int n, String s) {
        // given that n is the number of steps in the hike. String s defines what direction Gary's moving.
        

        //The key is that sea level is established and a valley only counts once you've reached an equivalent # of downs and ups to go back to sea level. This lends itself to a stack.
        
        int valleyCount = 0;//valleys traveled
        int elevation = 0; //current elevation, 0 is sea level
        for(char i: s.toCharArray()){//or can just do a regular for loop
            if(i=='U'){
                elevation++;
            }
            if(i=='D'){
                elevation--;
            }
            if(elevation==0){
                valleyCount++;
            }
        }
        return valleyCount;

        // //whenever there is a D->U pattern, count that as a valley. The next time you see a D->U pattern then its another valley. Don't double count.
        // for(int i=0;i<s.length();i++){
        //     if(s.charAt(i) == 'D' && s.charAt(i+1) == 'U'){
        //         valleyCount++;
        //     }
        // }
        // return valleyCount; THIS CODE WORKS FOR MY INTERPRETATION. REREAD

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
