//https://www.hackerrank.com/challenges/sock-merchant/problem
import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    // Complete the sockMerchant function below.
    static int sockMerchant(int n, int[] ar) {
        //given an int n (# of socks in array) and array of numbers, find how many pairs can be created
        int pairs = 0;
        Set<Integer> set = new HashSet();
        if(ar.length == 0) return pairs; //basic precheck
        for(int i=0; i<ar.length; i++){//linear scan
            if(!set.contains(ar[i])){//if set doesn't have the array values, then add it to the set.
                set.add(ar[i]);
            }
            else{//if already in the set, we found a pair
                pairs++;
                set.remove(ar[i]); //remove once we've found identical value.
            }
        }
        return pairs;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int[] ar = new int[n];

        String[] arItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < n; i++) {
            int arItem = Integer.parseInt(arItems[i]);
            ar[i] = arItem;
        }

        int result = sockMerchant(n, ar);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}