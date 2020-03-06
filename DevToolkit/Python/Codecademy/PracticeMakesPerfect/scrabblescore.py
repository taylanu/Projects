#simple scrabble score calculator.
score = {"a": 1, "c": 3, "b": 3, "e": 1, "d": 2, "g": 2, 
         "f": 4, "i": 1, "h": 4, "k": 5, "j": 8, "m": 3, 
         "l": 1, "o": 1, "n": 1, "q": 10, "p": 3, "s": 1, 
         "r": 1, "u": 1, "t": 1, "w": 4, "v": 4, "y": 4, 
         "x": 8, "z": 10}
         
def scrabble_score(word):
	word = str(word) # always do type checking for functions 
    word = word.lower()#each of the values of the string have to be lowercase to be matched with the dictionary
    word_score = 0 #keeps running score of the word
    for letter in word: # loops through each of the letters in the word
        word_score = word_score + score[letter]
    return word_score