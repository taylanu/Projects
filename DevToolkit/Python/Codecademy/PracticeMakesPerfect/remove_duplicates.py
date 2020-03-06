# All of my code was my creation, but the commented code after the if was in fact faulty 
# @datfatcat on Codecademy Forum: 
# "Remove the cleannums = in cleannums = cleannums.append(n). You are assigning the variable to the action done on the variable...which makes Python confused hence that error pops up."

def remove_duplicates(nums):
    cleannums=[]
    for n in nums:
        if n not in cleannums:
            # cleannums = cleannums.append(n)
            cleannums.append(n)
    return cleannums