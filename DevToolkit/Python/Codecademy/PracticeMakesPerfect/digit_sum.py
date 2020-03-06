#Sums each of the individual digits in a number
def digit_sum (n):
    total = 0
    nstring = str(n)
    for n in nstring:
       total = total + int(n)
    return total