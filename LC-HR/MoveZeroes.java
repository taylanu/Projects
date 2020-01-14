class MoveZeroes {
    public void moveZeroes(int[] nums) {
        //dont copy array
        int j = 0;
        for(int i=0;i<nums.length;i++){
            if(nums[i] != 0){ //ensure that nums[i] is not 0
                int temp = nums[j];//set a temporary variable to 0,
                nums[j] = nums[i];//set 
                nums[i] = temp; //set 
                j++; //increment j to next value in array.
            }
        }
    }
}