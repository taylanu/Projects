// C Tutorial #1 Derek Banas
/* Multiline Comment */
#include <stdio.h> //standard library
#include <string.h> //string library

#define MYNAME "Taylan Unal" //constants

int globalVar = 100; //global variable

main(){//main function in C
    char firstLetter = 'T'; //local variable
    int age = 20;

    long int superBigNum = -32767000; //larger than -32768 to 32768 in int

    float piVal = 3.14159; //32bit precision
    double reallyBigPi = 3.14159265358979; //64bit precision
    printf("\n");//newline reference
    printf("This will print to screen\n\n");

    printf("I am %d years old \n\n",20); //integer
    printf("I am %ld years old \n\n",20);//long integer
    printf("Pi = %.5f\n\n", piVal);//to print floats
    printf("BigPi = %.15f\n\n",reallyBigPi);//print doubles
    printf("First letter in my name is %c\n\n",firstLetter); //prints characters

    //STOPPED AT 10:30 https://www.youtube.com/watch?v=nXvy5900m3M&list=PLGLfVvz_LVvSaXCpKS395wbCcmsmgRea7&index=2


}