#Solution provided by a member of the Codecademy forums, and it was quite ingenious.
#My contribution was the type checking to make sure that the input passed in would in fact be a string type.
def anti_vowel(text):
    temptext = str(text)#ensures the input is a string
    #vowels = ["A", "a", "E", "e", "I", "i", "O", "o", "U", "u"]
    for i in "aeiouAEIOU":
        temptext = temptext.replace(i,"")
    return temptext
print anti_vowel("Red Robin")