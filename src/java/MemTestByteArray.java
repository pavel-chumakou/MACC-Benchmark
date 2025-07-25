
public class MemTestByteArray {
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

    private static void testMem(byte[] arr, byte memBits) {
        resetSeed();
        
        long memSize = 1L << memBits;
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < memSize; i++) {
            arr[i] = (byte)(rand(8));
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Sequential write: " + elapsedTime);

        long result = 0;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < memSize; i++) {
            result += arr[(int)rand(memBits)] & 0xff;
        }
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Random read: " + elapsedTime);

        startTime = System.currentTimeMillis();
        for (int i = 0; i < memSize; i++) {
            byte b = (byte)(rand(8));
            arr[(int)rand(memBits)] = b;
        }
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Random write: " + elapsedTime);

        startTime = System.currentTimeMillis();
        for (int i = 0; i < memSize; i++) {
            result += arr[i] & 0xff;
        }
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Sequential read: " + elapsedTime);

        System.out.println("Result: " + result);
        System.out.println();
    }

    public static void main(String[] args) {

        byte memBits = 30;
        byte[] arr = new byte[1 << memBits];

        System.out.println("Language: Java");
        System.out.println("Implementation: byte[] array");
        System.out.println("Compiler: jdk22");
        System.out.println("Options: ");
        System.out.println();

        for (int i = 0; i < 10; i++) {
            testMem(arr, memBits);
        }

    }

}