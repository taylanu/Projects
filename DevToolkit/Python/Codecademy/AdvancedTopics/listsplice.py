# List slicing in Advanced Topics

# List Slicing 1
l = [i ** 2 for i in range(1, 11)]
# Should be [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]
print l[2:9:2]

# List Slicing 2
my_list = range(1, 11) # List of numbers 1 - 10
print my_list
print my_list[::2]
my_list = range(1, 11)


#List Slicing 3
backwards = my_list[::-1]
print backwards

# List Slicing 4
to_one_hundred = range(101)
backwards_by_tens = to_one_hundred[::-10]
print backwards_by_tens

#List Slicing 5
to_21 = range(1,22)
odds = to_21[::2]
middle_third = to_21[7:14:1]