# simple reverse script in Python.
# Especially proud because I wrote it on my own, the line under the for was in my opinion, pretty clever.

def reverse (text):
    revtext = ""
    for x in text:
        revtext = x + revtext
    return revtext