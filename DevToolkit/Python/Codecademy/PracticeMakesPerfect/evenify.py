# This code was also purely mine, and it was originally named purify on codecademy.
# This naming was odd because the program actually cleans the list to include evens only.
# The program could easily be extensible to other values or number types.
def evenify(nums):
    even_list = []
    for n in nums:
        if n%2 ==0:
            even_list.append(n)
    return even_list