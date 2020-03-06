# REVIEW for Advanced topics

#1
movies = {
	"Monty Python and the Holy Grail": "Great",
	"Monty Python's Life of Brian": "Good",
	"Monty Python's Meaning of Life": "Okay"
}
for x in movies: 
    #print movies[x] This function only returns the values of each key.
    print movies.items() # this function instead returns the entire listing.

#2
# this problem was especially difficult due to the extremely condensed nature of the call. I missed the fact that the call is x for x instead of just for x
threes_and_fives = [x for x in range(1,16) if (x%3==0) or (x%5==0)] 
print threes_and_fives

#3
# My trouble with this one was using filter statements that I had used before in lambda for this problem. Instead I had to use slicing statements.
garbled = "!XeXgXaXsXsXeXmX XtXeXrXcXeXsX XeXhXtX XmXaX XI"
print garbled
rev_garbled = garbled[::-1]
print rev_garbled
message = rev_garbled[::2]
print message

#3
garbled = "IXXX aXXmX aXXXnXoXXXXXtXhXeXXXXrX sXXXXeXcXXXrXeXt mXXeXsXXXsXaXXXXXXgXeX!XX"

message = filter(lambda x: x!="X",garbled)
print message