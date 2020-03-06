# One through twelve in python. 
# Did all but eleven and twelve on my own.
one = 0b1
two = 0b10
three = 0b11
four = 0b100
five = 0b101
six = 0b110
seven = 0b111
eight = 0b1000
nine = 0b1001
ten = 0b1010
eleven = 0b1011
twelve = 0b1100

#bin() function finds the binary representation of the number.
print bin(1)
print bin(2)
print bin(3)
print bin(4)
print bin(5)

print int("1",2)
print int("10",2)
print int("111",2)
print int("0b100",2)
print int(bin(5),2)
# Print out the decimal equivalent of the binary 11001001.
print int("11001001",2)# using two as a second parameter, the string is recognized as base two notation