Global seed.q = 50429807834
Global memSize.i

Procedure RandomLCG(bits)
  #multiplier = 25214903917
  #addend = 11
  #mask = (1 << 48) - 1
  nextseed.q = (seed * #multiplier + #addend) & #mask
  seed = nextseed
  ProcedureReturn (seed >> (48 - bits))
EndProcedure

Procedure MemTest(*memPtr, bits)
  
  seed = 50429807834
  
  ;sequential write
  startTime.q = ElapsedMilliseconds()
  For i = 0 To memSize - 1 
    PokeB(*memPtr + i, RandomLCG(8))
  Next 
  PrintN("Sequential write: " +  Str(ElapsedMilliseconds() - startTime))
  
  result.q = 0
  ;random read
  startTime = ElapsedMilliseconds()
  For i = 0 To memSize - 1 
    result = result + PeekB(*memPtr + RandomLCG(bits)) & 255
  Next  
  PrintN("Random read: " +  Str(ElapsedMilliseconds() - startTime))
  
  ;random write
  startTime = ElapsedMilliseconds()
  For i = 0 To memSize - 1
    r = RandomLCG(8)
    PokeB(*memPtr + RandomLCG(bits), r)
  Next 
  PrintN("Random write: " +  Str(ElapsedMilliseconds() - startTime))
  
  ;sequential read
  startTime = ElapsedMilliseconds()
  For i = 0 To memSize - 1 
    result = result + PeekB(*memPtr + i) & 255
  Next  
  PrintN("Sequential read: " +  Str(ElapsedMilliseconds() - startTime))
  
  PrintN("Result: " +  result)
  PrintN("")
EndProcedure


bits = 30
memSize = 1 << bits
*memPtr = AllocateMemory(memSize, #PB_Memory_NoClear)

OpenConsole()
PrintN("Language: PureBasic")
PrintN("Implementation: ")
PrintN("Compiler: PureBasic 6.21")
PrintN("Options: ")
PrintN("")

For i = 0 To 9  
  MemTest(*memPtr, bits)
Next 

CloseConsole()

FreeMemory(*memPtr)

End








; IDE Options = PureBasic 6.21 (Windows - x64)
; ExecutableFormat = Console
; CursorPosition = 61
; FirstLine = 30
; Folding = -
; Optimizer
; EnableXP
; DPIAware
; Executable = memtest-pb.exe
; DisableDebugger