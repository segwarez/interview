# Java Memory Management Examples

This project demonstrates different memory management concepts in Java with four examples:

1. **memory-leak**  
   Demonstrates a memory leak by continuously creating tasks and adding them to a collection without cleaning up, leading to memory exhaustion.

2. **memory-polluter**  
   Simulates excessive memory consumption by repeatedly allocating large arrays, stressing the JVM's memory management.

3. **phantom-reference**  
   Shows the use of `PhantomReference` to track and finalize resources once objects are garbage collected.

4. **threads**  
   Demonstrates basic thread management, uncaught exceptions handling, and thread sleep behavior.
