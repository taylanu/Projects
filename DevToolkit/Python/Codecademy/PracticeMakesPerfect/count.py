#This code was uniquely mine, but the answer was quite straightforward
#I know that I know how to solve the problems, and can write them out but putting them into code seems difficult in a new language.
def count(sequence,item):
    counter = 0
    for i in sequence:
        if i==item:
            counter+=1
    return counter
print count([6,4,2,4,1],6)