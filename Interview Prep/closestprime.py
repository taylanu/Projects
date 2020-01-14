# closest prime Python
def closestPrime(givenNumber):    # Initialize a list
    primes = []
    for possiblePrime in range(2, givenNumber + 1):     # Assume number is prime until shown it is not. 
        isPrime = True
        for num in range(2, possiblePrime):
            if possiblePrime % num == 0:
                isPrime = False        
            if isPrime:
                primes.append(possiblePrime)
    #return(primes)
    greatestIndex = len(primes) - 1
    print(primes[greatestIndex])

closestPrime(int(input("Enter a value n: ")))