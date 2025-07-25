module macctest;

import std.stdio;
import core.stdc.stdlib;
import core.stdc.time;

ulong seed = 50429807834;
static const(ulong) multiplier = 25214903917;
static const(int) addend = 11;
static const(ulong) mask = (cast(ulong)1 << 48) - 1;

ulong rand(const(int) bits) {
    ulong nextSeed;
    nextSeed = (seed * multiplier + addend) & mask;
    seed = nextSeed;
    return  nextSeed >> (48 - bits);
}

void reset_seed() 
{
    seed = 50429807834;
}

void testMem(ubyte* memPtr, const(ubyte) memBits)
{
    reset_seed();

    const(int) memSize = 1 << memBits;

    // sequential write
    auto startTime = clock();
    for (int  i = 0; i < memSize; i++)
    {
        memPtr[i] = cast(ubyte)rand(8);
    }
    auto elapsedTime = clock() - startTime;
    writeln("Sequential write: ", elapsedTime );

    // random read
    long result = 0;
    startTime = clock();
    for (int  i = 0; i < memSize; i++)
    {
        result += memPtr[rand(memBits)];
    }
    elapsedTime = clock() - startTime;
    writeln("Random read: ", elapsedTime);

    // random write
    startTime = clock();
    for (int  i = 0; i < memSize; i++)
    {
        ubyte b = cast(ubyte)rand(8);
        memPtr[rand(memBits)] = b;
    }
    elapsedTime = clock() - startTime;
    writeln("Random write: ", elapsedTime);

    // sequential read
    startTime = clock();
    for (int  i = 0; i < memSize; i++)
    {
        result += memPtr[i];
    }
    elapsedTime = clock() - startTime;
    writeln("Sequential read: ", elapsedTime);

    writeln("Result: ", result);
    writeln();
}

int main()
{
    writeln("Language: D");
    writeln("Implementation: ");
    writeln("Compiler: ");
    writeln("Options: ");
    writeln();

    const(ubyte) memBits = 30;
    ubyte* memPtr = cast(ubyte*)malloc(1L << memBits);

    for (int i = 0; i < 10; i++)
    {
        testMem(memPtr, memBits);
    }

    free(memPtr);
    return 0;
}

