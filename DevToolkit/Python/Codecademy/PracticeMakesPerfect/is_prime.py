#Simple prime checking program in Python
def is_prime(x): #function is_prime with argument x
    if x < 2: #A number less than 2 is not a prime number
        return False
    for n in range(2, x-1): #The loop, as the exercise asked for it, from 2 to x-1
        if x % n == 0: #if x is evenly divisible by n
            return False
    else: #See explanation for this one
        return True