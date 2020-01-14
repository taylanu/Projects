import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    // Complete the jumpingOnClouds function below.
    static int jumpingOnClouds(int[] c) {
        //integer array c stating if 0 is safe, 1 is avoid. There will never be two ones in a row.
        int numJumps = 0;
        int i=0;
        //Use a while loop when incrementing by amounts
        while(i<c.length-1){
            if(i+2 == c.length || c[i+2]==1){//if there is a 1 two spaces away, only jump by one, or if two spaces away, we are at the end of the list.
                i++;
                numJumps++;
            }
            else{//every other case we jump two spaces
                i+=2;
                numJumps++;
            }
        }
        return numJumps;

    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int[] c = new int[n];

        String[] cItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < n; i++) {
            int cItem = Integer.parseInt(cItems[i]);
            c[i] = cItem;
        }

        int result = jumpingOnClouds(c);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
