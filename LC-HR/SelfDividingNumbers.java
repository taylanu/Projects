class SelfDividingNumbers {
    public List<Integer> selfDividingNumbers(int left, int right) {
        List<Integer> sd = new ArrayList<Integer>(); //store all good values for return
        
        for(int i=left;i<=right;i++){//lower bound is left, upper bound is right. got this part right
            int temp = left;
            while(temp > 0){
                int currDigit = temp % 10;//look at last digit on number
                if(currDigit==0) break; //means that not divisible, not possible to div by 0
                if(left % currDigit != 0)//checks and sees if the current digit in the number cleanly divides the current number
                    break;
                temp /= 10;
            }
            if(temp == 0) sd.add(left); //still within the for loop, outside of the while loop, if the remaining temp value  now equals 0, add the number to the arraylist
        }
        return sd;
    }

    public static void main(String[] args){
        System.out.println(SelfDividingNumbers(1,10));//should return all values except 10.
    }
}