import tweepy
from textblob import TextBlob
# Using TayIntelligentApps API on my personal Dev Account
# https://www.analyticsvidhya.com/blog/2018/02/natural-language-processing-for-beginners-using-textblob/
""" Polarity is float which lies in the range of [-1,1] where 1 means positive statement and -1 means a negative statement. 
Subjective sentences generally refer to personal opinion, emotion or judgment whereas objective refers to factual information. 
Subjectivity is also a float which lies in the range of [0,1]. """

# Step 1 - Authenticate
consumer_key= 'I1jN1KuahA42GHwAsPh5OzXhA'
consumer_secret= 'NlpRuHy7WDbCfalLrHgdZLpVMxNSOH96qZBJjiFS2yhw9T9D20'

access_token='760282510710497280-VivrqpSEKuZ0TYTSS8OoFFBlIzuAxzM'
access_token_secret='QkwlSWmMYl9BxSZ1ucL95yQEKcrLvO9QexG2ipOzwPPOH'

auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)

api = tweepy.API(auth)

#Step 3 - Retrieve Tweets
public_tweets = api.search('Trump')

#CHALLENGE - Instead of printing out each tweet, save each Tweet to a CSV file
#and label each one as either 'positive' or 'negative', depending on the sentiment 
#You can decide the sentiment polarity threshold yourself

for tweet in public_tweets:
    print(tweet.text)
    
    #Step 4 Perform Sentiment Analysis on Tweets
    analysis = TextBlob(tweet.text)
    print(analysis.sentiment)
    print("")
