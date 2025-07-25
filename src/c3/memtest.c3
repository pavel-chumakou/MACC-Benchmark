module memtest;
import libc;

ulong seed = 50429807834;
ulong multiplier = 25214903917;
ulong addend = 11L;
ulong mask = ((ulong)1 << 48) - 1;

fn ulong rand(uint bits) {
    ulong nextSeed = (seed * multiplier + addend) & mask;
    seed = nextSeed;
    return nextSeed >> (48 - bits);
}

fn void reset_seed()
{
    seed = 50429807834;
}

fn void testMem(char* memPtr, char memBits)
{
    reset_seed();

    int memSize = 1 << memBits;

    // sequential write
    ulong startTime = libc::clock();
    for (int i = 0; i < memSize; i++)
    {
        memPtr[i] = (char)rand(8);
    }
    ulong elapsedTime = libc::clock() - startTime;
    libc::printf("Sequential write: %ld\n", elapsedTime);

    // random read
    ulong result = 0;
    startTime = libc::clock();
    for (int i = 0; i < memSize; i++)
    {
        result += memPtr[rand(memBits)];
    }
    elapsedTime = libc::clock() - startTime;
    libc::printf("Random read: %ld\n", elapsedTime);

    // random write
    startTime = libc::clock();
    for (int i = 0; i < memSize; i++)
    {
        char b = (char) rand(8);
        memPtr[rand(memBits)] = b;
    }
    elapsedTime = libc::clock() - startTime;
    libc::printf("Random write: %ld\n", elapsedTime);

    // sequential read
    startTime = libc::clock();
    for (int i = 0; i < memSize; i++)
    {
        result += memPtr[i];
    }
    elapsedTime = libc::clock() - startTime;
    libc::printf("Sequential read: %ld\n", elapsedTime);

    libc::printf("Result: %lld\n", result);
    libc::printf("\n");

}


fn int main(String[] args)
{
    libc::printf("Language: C3\n");
    libc::printf("Implementation: default\n");
    libc::printf("Compiler: \n");
    libc::printf("Options: \n");
    libc::printf("\n");

    char memBits = 30;
    char* memPtr = libc::malloc(1LL << memBits);

    for (int i = 0; i < 10; i++)
    {
        testMem(memPtr, memBits);
    }

    libc::free(memPtr);
	return 0;
}
