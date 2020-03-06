# Simple program written on my own to find the product of values in a list.
def product(nums):
    #product = num[0]
    product = 1 # 1 because identity property
    for n in nums:
        product = product * n
    return product
    