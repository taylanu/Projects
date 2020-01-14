class CountBits{
    public static int countBits(int n){
        //goal should be to take the integer value n, find the number of binary places needed to represent it.
        return (int)(Math.log(n)/Math.log(2)+1);
        //Math.log(n)/Math.log(2) means that we're doing logbase 2. It returns the exponent to which we should raise 2 to get the value, we add 1 afterwards.
    }
    public static void main(String[] args){
        System.out.println(countBits(6));

    }
}