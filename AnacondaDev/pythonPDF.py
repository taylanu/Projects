# -*- coding: utf-8 -*-
# https://medium.com/@rqaiserr/how-to-convert-pdfs-into-searchable-key-words-with-python-85aab86c544f 
import PyPDF2
#import textract
import nltk
#nltk.download('punkt')
#nltk.download('stopwords')

from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords

pdfFileObj = open(input("Enter Filename: ")+'.pdf','rb') #open and read file
#outputFile = 
# The pdfReader object is a readable object that can be parsed
pdfReader = PyPDF2.PdfFileReader(pdfFileObj)

num_pages = pdfReader.numPages
count = 0
text = ""

#read through each page
while count < 5: #replace 1 with num_pages
        pageObj = pdfReader.getPage(count)
        count += 1
        text += pageObj.extractText() #pull text from each page

if text != "": #check to ensure that the reader returned any words
        text = text
# so if above is false, then have to run OCR 'textract' command to convert scans/images to text
#else:
#    text = textract.process(filename, method='tesseract', language='eng')
#words = [word for word in text
#             if ]

word_count = len(text.split())
#print("Extracted Text: ",text," \n Word Count: ",word_count)

# Convert text into keywords

tokens = word_tokenize(text)
#create a new list with punctuation that we want to clean
punctuation = ['(',')',';',':','[',']',',']

# create a list of stopwords like 'The', 'I', 'and' etc.
stop_words = stopwords.words('english')

# create a list of words not in stop_words or punctuation to isolate only important words
keywords = [word for word in tokens 
                if not word in stop_words and not word in punctuation]
keywordPrint = ' '.join(keywords)
keyword_count = len(keywords)
#print("Keywords: ", keywords, " \n Keyword Count: ", keyword_count)
print("Keywords:", keywordPrint, "\n Keyword Count:", keyword_count, "words", "\nDown from:", word_count, " words") #"\n Reduced by", keyword_count/word_count, " percent")