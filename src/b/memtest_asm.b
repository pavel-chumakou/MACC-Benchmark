seed 50429807834;
multiplier 25214903917;
addend 11;
mask 281474976710655; //((1 << 48) - 1)

rand(bits) {
    auto nextseed;
    nextseed = (seed * multiplier + addend) & mask;
    seed = nextseed;
    return (nextseed >> (48 - bits));
}

reset_seed() {
    seed = 50429807834;
}
    
putbyte __asm__ (
    "movb %dl, (%rcx)",
    "ret"
);

getbyte __asm__ (
    "movq $0, %rax",
    "movb (%rcx), %al",
    "ret"
);

memtest(memptr, membits) {
    extrn printf, clock;    
    
    reset_seed();
    
    auto memsize; memsize = 1 << membits;
    
    // sequential write
    auto timestamp; timestamp = clock();
    auto i; 
    i = 0; while (i < memsize) {
        putbyte(memptr + i, rand(8));
        i++;
    }
    auto elapsed; elapsed = clock() - timestamp;
    printf("Sequential write: %ld\n", elapsed);
    
    // random read
    auto result; result = 0;
    timestamp = clock();
    i = 0; while (i < memsize) {
        result += getbyte(memptr + rand(membits));
        i++;
    }
    elapsed = clock() - timestamp;
    printf("Random read: %ld\n", elapsed);
    
    // random write
    timestamp = clock();
    i = 0; while (i < memsize) {
        auto r; r = rand(8);
        putbyte(memptr + rand(membits), r); 
        i++;
    }
    elapsed = clock() - timestamp;
    printf("Random write: %ld\n", elapsed);

    // sequential read
    timestamp = clock();
    i = 0; while (i < memsize) {
        result += getbyte(memptr + i);
        i++;
    }
    elapsed = clock() - timestamp;
    printf("Sequential read: %ld\n", elapsed);
    printf("Result: %lld\n", result);
    printf("\n");
}


main() {
    extrn printf, malloc;
   
    auto membits; membits = 30;
    auto memptr; memptr = malloc(1 << membits);
    
    printf("Language: %s\n", "B");
    printf("Implementation: %s\n", "__asm__");
    printf("Compiler: \n");
    printf("Options: \n");
    printf("\n");
    
    auto i; i = 5; while (i--) memtest(memptr, membits); 
}



