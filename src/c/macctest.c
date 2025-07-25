#include <stdio.h>
#include <stdlib.h>
#include <time.h>

unsigned long long seed = 50429807834;
static const long long multiplier = 25214903917;
static const long addend = 11L;
static const long long mask = ( (long long)1 << 48) - 1;

unsigned long long macc_rand(const int bits) {
    const unsigned long long nextSeed = (seed * multiplier + addend) & mask;
    seed = nextSeed;
    return nextSeed >> (48 - bits);
}

void reset_seed()
{
    seed = 50429807834;
}

void testMem(unsigned char* memPtr, const unsigned char memBits)
{
    reset_seed();

    const int memSize = 1 << memBits;

    // sequential write
    long startTime = clock();
    for (int i = 0; i < memSize; i++)
    {
        memPtr[i] = (unsigned char) macc_rand(8);
    }
    long elapsedTime = clock() - startTime;
    printf("Sequential write: %ld\n", elapsedTime);

    // random read
    long long result = 0;
    startTime = clock();
    for (int i = 0; i < memSize; i++)
    {
        result += memPtr[macc_rand(memBits)];
    }
    elapsedTime = clock() - startTime;
    printf("Random read: %ld\n", elapsedTime);

    // random write
    startTime = clock();
    for (int i = 0; i < memSize; i++)
    {
        unsigned char b = (unsigned char) macc_rand(8);
        memPtr[macc_rand(memBits)] = b;
    }
    elapsedTime = clock() - startTime;
    printf("Random write: %ld\n", elapsedTime);

    //sequential read
    startTime = clock();
    for (int i = 0; i < memSize; i++)
    {
        result += memPtr[i];
    }
    elapsedTime = clock() - startTime;
    printf("Sequential read: %ld\n", elapsedTime);

    printf("Result: %lld\n", result);
    printf("\n");
}

int main(void)
{
    printf("Language: C\n");
    printf("Implementation: \n");
    printf("Compiler: \n");
    printf("Options: \n");
    printf("\n");

    const unsigned char memBits = 3;
    unsigned char* memPtr = malloc(1LL << memBits);

    for (int i = 0; i < 10; i++)
    {
        testMem(memPtr, memBits);
    }

    free(memPtr);
    return 0;
}
