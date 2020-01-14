//https://www.hackerrank.com/challenges/drawing-book/problem?h_r=next-challenge&h_v=zen
import java.io.*;
import java.math.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

public class Solution {

    /*
     * Complete the pageCount function below.
     */
    static int pageCount(int n, int p) {
        // Given two integers, n is the number of pages in the book, and p is the page you'd like to turn to. Note that we're looking for the minimum number of flips, either starting from front or back.
        int totalFlipsFromFront = n/2; // maximum flips (each page flip has 2 page #s, so this would signify the target being on the last page)
        int targetFlipsFromFront = p/2; // minimum flips (optimally it only takes linear time where we flip p/2 times to get to the desired page)
        
        int targetFlipsFromBack = totalFlipsFromFront - targetFlipsFromFront; //from back, it would mean that you start from back and try to get to the desired page from back

        int minimumFlips = Math.min(targetFlipsFromFront, targetFlipsFromBack);

        return minimumFlips;

    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])*");

        int p = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])*");

        int result = pageCount(n, p);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
