inputList=[1412,2,42,41,7,2,6,12] # EXAMPLE, use user input.
for num in range(len(inputList)//2,len(inputList)-1): #loop through the second half of the list
    if(inputList[num+1]<inputList[num]):# if the next element in list is less than the current element, swap the two
        temp = inputList[num] # store current element in a temporary variable
        inputList[num]=inputList[num+1]
        inputList[num+1]=temp # simple swap of the two elements.            
print(inputList)