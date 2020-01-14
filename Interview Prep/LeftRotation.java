import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    // Complete the rotLeft function below.
    static int[] rotLeft(int[] a, int d) {
        //given an array of integers and a value 
        int size = a.length;
        int[] rotatedArray = new int[size];

        int i =0;//used to index new Array.
        int rotateIndex = d; //original input. Where we want our new array to start. Then reset back to 0 up to d to append the first elements to the end of the array.
        while(rotateIndex < size){ //while our rotateIndex is less than the original size of the array.
            rotatedArray[i] = a[rotateIndex];//then increement both counters.
            i++;
            rotateIndex++;
        }// THIS COVERS from d up to end of the array

        rotateIndex = 0; //reset index
        while (rotateIndex < d){
            rotatedArray[i] = a[rotateIndex];//then increement both counters.
            i++;
            rotateIndex++;
        }// THIS covers from start up to d

        return rotatedArray;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] nd = scanner.nextLine().split(" ");

        int n = Integer.parseInt(nd[0]);

        int d = Integer.parseInt(nd[1]);

        int[] a = new int[n];

        String[] aItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < n; i++) {
            int aItem = Integer.parseInt(aItems[i]);
            a[i] = aItem;
        }

        int[] result = rotLeft(a, d);

        for (int i = 0; i < result.length; i++) {
            bufferedWriter.write(String.valueOf(result[i]));

            if (i != result.length - 1) {
                bufferedWriter.write(" ");
            }
        }

        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
