# MACC-Benchmark

MACC-Benchmark is a benchmarking tool designed to evaluate memory access performance across various programming languages. The benchmark executes a series of standardized memory access actions: sequentially write, random read, random write, sequentially read.

### Memory Access Actions

Every benchmark executes exactly the same memory access actions: 
- **Sequential Write**: Write 2^30 pseudo-random bytes sequentially. 
- **Random Read**: Read 1 byte at a pseudo-random position, repeated 2^30 times.
- **Random Write**: Write a pseudo-random byte to a pseudo-random position, repeated 2^30 times.
- **Sequential Read**: Read 2^30 bytes sequentially.
  
### Results

| Language | Setup | Sequential write | Sequential read | Random write | Random read |
| :--- | :---: | :---: | :---: | :---: | :---: |
| B | <small>asm, tsoding/b</small> |7974 | 3430 | 87344 | 66361 | 
| B | <small>tsoding/b</small> |11262 | 5217 | 110364 | 74002 | 
| C | <small>gcc 15.1.0, -O2</small> |**1215** | **139** | 11128 | 11557 | 
| C2 | <small>c2c 58209d6d</small> |1434 | 165 | **10787** | **10907** | 
| C3 | <small>c3c 0.7.2, -O5</small> |4747 | 224 | 12415 | 13653 | 
| D | <small>dmd 2.111, -O</small> |1412 | 575 | 14397 | 20825 | 
| Java | <small>byte[] array, jdk22</small> |1245 | 289 | 11199 | 16018 | 
| Java | <small>Direct ByteBuffer, jdk22</small> |1405 | 715 | 11463 | 17102 | 
| Java | <small>Unsafe, jdk22</small> |1378 | 1129 | 10815 | 15984 | 
| Java | <small>Memory API, jdk22</small> |1318 | 980 | 11659 | 17414 | 
| Java | <small>ByteBuffer, jdk22</small> |1498 | 756 | 10884 | 18482 | 
| PureBasic | <small>PureBasic 6.21</small> |3384 | 1466 | 58491 | 37273 | 
| Rust (unsafe) | <small>rustc 1.88.0, -O</small> |1633 | 164 | 11461 | 11554 | 

### Notes
1. Each test is performed 5-10 times. Results table shows the average value in milliseconds.
2. Results table was automitically generated using [TestResultsComposer.java](https://github.com/pavel-chumakou/MACC-Benchmark/blob/main/results/TestResultsComposer.java) tool:
```bash
java ./results/TestResultsComposer.java ./results/win11_amd_8840hs/
```
3. [C2](https://github.com/c2lang/c2compiler) language has C backend. It means that results for C and C2 should be mostly the same.
4. Surprisingly standard Java byte[] array outperforms Java Unsafe/Memory API for sequential memory acсess (random access is also good).
5. [B language](https://github.com/tsoding/b) is not fully implemented yet, I added it mostly just for fun. However, B has some potential in low-level programming.
   
### ToDo
1. Add C#, Rust, Ada, Go, Odin, Zig, Hare, Jai, Python, Nim, Pascal
2. Re-test it on Linux platform (currently I have only my work Win11 laptop with non-disableable antivirus, not good for performance testing)
3. Repeat the tests for smaller memory buffers (2^24 - 2^29). It will be intersting to investigate relative changes in performance with variable memory buffer and fixed CPU cache/ memory bandwidth. Most likely C or Rust implementations will be enough for such benchmarks.

