// C Tutorial #1 Derek Banas
//Introduction to C
// https://www.youtube.com/watch?v=nXvy5900m3M&list=PLGLfVvz_LVvSaXCpKS395wbCcmsmgRea7&index=2
#include <stdio.h> //standard library
#include <string.h> //string library

#define MYNAME "Taylan Unal" //constants

int globalVar = 100; //global variable

main(){//main function in C
    // Basic Components of C
    char firstLetter = 'T'; //local variable
    int age = 20;

    long int superBigNum = -32767000; //larger than -32768 to 32768 in int

    float piVal = 3.14159; //32bit precision
    double reallyBigPi = 3.14159265358979; //64bit precision
    printf("\n");//newline reference
    printf("This will print to screen\n\n");

    //Using String Formatting
    printf("I am %d years old \n\n",20); //integer
    printf("I am %ld years old \n\n",20);//long integer
    printf("Pi = %.5f\n\n", piVal);//to print floats
    printf("BigPi = %.15f\n\n",reallyBigPi);//print doubles
    printf("First letter in my name is %c\n\n",firstLetter); //prints characters

    char myName[] = "Taylan Unal"; //need to instantiate with size. Need to include space for string terminator
    strcpy(myName,"Big Kahuna"); //copies string, changes value
    printf("My name is %s\n\n", myName);

    //Implementing Scanner
    char middleInitial;
    printf("What is your middle initial? ");

    scanf(" %c", &middleInitial);//takes user input using scanner

    char firstName[30], lastName[30];

    printf("What is your name? ");
    scanf(" %s %s", firstName, lastName);
    printf("Your name is %s %c %s\n\n", firstName,middleInitial,lastName);

    int month, day, year;
    printf("What is your birth date? ");
    scanf("\nBirth Date %d/%d/%d\n\n", month, day, year);
    //Operations +,-,*,/
    int num1 = 12, num2 =15, numAns;
    float decimal1 = 1.2, decimal2 = 1.5, decimalAns;

    printf("Integer Calc %d\n\n", num2/num1);
    printf("Float Calc %f\n\n", decimal2 / decimal1);
    printf("Modulus %d\n\n", num2 % num1);
    printf("Without Parentheses %d\n\n", 3+6 * 10);
    printf("With Parentheses %d\n\n", (3+6) * 10);

    int randomNum = 1;
    printf("1+= 2: %d\n\n", randomNum +=2);
    printf("%d += 2 : %d\n\n", randomNum, randomNum +=2);

    // -=, *=, /=, %=, var++, var-- all valid

    int exNum = 1;
    printf("++%d : %d\n\n", exNum, ++exNum); //add then print
    printf("%d++ : %d\n\n", exNum, ++exNum); //print then add

    //Casting
    int numberEx = 12, numberEx2 = 14;
    print("numberEx / numberEx2 : %f\n\n", (float) numberEx / numberEx2);
    
}
/* Part 1

*/