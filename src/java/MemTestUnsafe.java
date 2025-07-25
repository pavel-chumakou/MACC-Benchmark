import sun.misc.Unsafe;
import java.lang.reflect.Field;

public class MemTestUnsafe {
    private static Unsafe unsafe = unsafe();

    private static Unsafe unsafe() {
        try {
            return Unsafe.getUnsafe();
        } catch (SecurityException ignored) {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                return (Unsafe)f.get(null);
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize intrinsics.", e.getCause());
            }
        }
    }

    private static long seed = 50429807834L;
    private static final long multiplier = 25214903917L;
    private static final long addend = 11L;
    private static final long mask = (1L << 48) - 1;

    private static long rand(int bits) {
        long nextSeed;
        nextSeed = (seed * multiplier + addend) & mask;
        seed = nextSeed;
        return nextSeed >>> (48 - bits);
    }
    
    private static void resetSeed() {
        seed = 50429807834L;
    }

    private static void testMem(long memPtr, byte memBits) {
        resetSeed();
        
        long memSize = 1L << memBits;
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < memSize; i++) {
            unsafe.putByte(memPtr + i, (byte)(rand(8)));
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Sequential write: " + elapsedTime);

        long result = 0;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < memSize; i++) {
            result += unsafe.getByte(memPtr + rand(memBits)) & 0xff;
        }
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Random read: " + elapsedTime);

        startTime = System.currentTimeMillis();
        for (int i = 0; i < memSize; i++) {
            byte b = (byte)(rand(8));
            unsafe.putByte(memPtr + rand(memBits), b);
        }
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Random write: " + elapsedTime);

        startTime = System.currentTimeMillis();
        for (int i = 0; i < memSize; i++) {
            result += unsafe.getByte(memPtr + i) & 0xff;
        }
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Sequential read: " + elapsedTime);

        System.out.println("Result: " + result);
        System.out.println();
    }

    public static void main(String[] args) {
        byte memBits = 30;
        long memPtr = unsafe.allocateMemory(1L << memBits);

        System.out.println("Language: Java");
        System.out.println("Implementation: Unsafe");
        System.out.println("Compiler: jdk22");
        System.out.println("Options: ");
        System.out.println();

        for (int i = 0; i < 10; i++) {
            testMem(memPtr, memBits);
        }
        unsafe.freeMemory(memPtr);
    }
}