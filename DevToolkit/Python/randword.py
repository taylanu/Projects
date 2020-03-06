# -*- coding: utf-8 -*-
import random
import os
import csv

#file_path = os.path.abspath("1kwords.csv")
file_path = "\\resources\\1kwords.csv"

with open(file_path) as file:
    random_line_num = random.choice(open(file_path).readlines()) #choice function calls random over the number of lines.
    for line in file:
        if line == random_line_num:
            print line 
    file.close()