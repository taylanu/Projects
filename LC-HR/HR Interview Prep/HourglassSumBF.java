//BRUTE FORCE SOLUTION. WORKS
import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    // Complete the hourglassSum function below.
    static int hourglassSum(int[][] arr) {
        //Standard 2D array problem, get rows and columns of 2D array
        //this is a brute force solution. can be improved.
        int rows = arr.length;
        int columns = arr[0].length;
        int maxHourglassSum = -63;//initially set it to lowest possible hourGlass sum.
        //constraints for elements are that each element can only be from -9 to 9.
        for(int i =0;i<rows-2;i++){
            for(int j=0;j<columns-2;j++){//loops through entire 2D array, one element at a time
                int currentHourglassSum = arr[i][j]+arr[i][j+1]+arr[i][j+2]+arr[i+1][j+1]+arr[i+2][j]+arr[i+2][j+1]+arr[i+2][j+2];
                maxHourglassSum = Math.max(maxHourglassSum,currentHourglassSum);
            }
        }
        return maxHourglassSum;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int[][] arr = new int[6][6];

        for (int i = 0; i < 6; i++) {
            String[] arrRowItems = scanner.nextLine().split(" ");
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            for (int j = 0; j < 6; j++) {
                int arrItem = Integer.parseInt(arrRowItems[j]);
                arr[i][j] = arrItem;
            }
        }

        int result = hourglassSum(arr);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
