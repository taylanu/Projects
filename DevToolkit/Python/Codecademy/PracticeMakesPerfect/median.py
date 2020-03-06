# This code is problematic for me right now. SOLVED by changing division for return statement to 2.0 instead of 2, because the division must be in float form
# The code was entirely written by me, but the changing of the 2 to 2.0 was inspired by a codecademy user post.
def median(nums):
    med = 0
    listlen = len(nums)
    nums.sort()
    if listlen%2 == 0:#checks for even list length
        listlen = int(listlen/2)
        return (nums[listlen] + nums[listlen-1])/2.0
    else:#if list is not even, it is odd.
        listlen= int(listlen/2)
        return nums[listlen]