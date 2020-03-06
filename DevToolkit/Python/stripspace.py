# -*- coding: utf-8 -*-
# @Author taylanu
# Can be modified and reused as needed
#
print("Welcome to the file stripper")
user_file_input = input("What file would you like to strip")

with open(user_file_input) as file:
    for line in file: # initially to strip line
        line = line.rstrip()
        file.write("%s\n" % line)
    file.close()
