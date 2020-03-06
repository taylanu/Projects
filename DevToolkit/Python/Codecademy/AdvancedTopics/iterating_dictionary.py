# Dictionary in Python
my_dict = {
    "Name":"Jesse",
    "Age":"24",
    "Gender": "Male"
}
print my_dict.items()
print my_dict.keys()
print my_dict.values()

for key in my_dict:
    #print key + " " + my_dict[key]
    print key, my_dict[key]


# Interesting way to build arrays using inline statements like the ones below.	
doubles_by_3 = [x*2 for x in range(1,6) if (x*2) % 3 == 0]
print doubles_by_3
# Complete the following line. Use the line above for help.
even_squares = [x**2 for x in range(1,11) if (x**2) % 2 == 0]
print even_squares
cubes_by_four = [(x**3) for x in range(1,11) if (x**3)%4 ==0]
print cubes_by_four