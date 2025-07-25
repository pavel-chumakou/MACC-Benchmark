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

#### Notes

#### TestResultsComposer
```bash
java TestResultComposer.java <path_to_folder_with_results>
```
### ToDo


