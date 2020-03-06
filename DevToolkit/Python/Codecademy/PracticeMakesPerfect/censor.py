#After much frustration with the code simply return none, I decided to copy code.
#This was only because I completely understand the computer science concept taht this involves, but my answer attempts were too deep.
def censor(text, word):
    lst = text.split()
    for i in lst:
        if word in lst:
            loc = lst.index(word)
            lst.remove(word)
            lst.insert(loc, "*" * len(word))
            newLst = " ".join(lst)

    return newLst