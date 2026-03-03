# Ring Buffer (Single Writer, Multiple Readers)

## Overview
This project implements a fixed-capacity ring buffer that supports:
 Single writer calling `write()`
 Multiple independent readers 
 Overwrite-on-full 

Reading by one reader does not remove items for other readers.

## Design (Responsibilities)
 **RingBuffer<E>** :
   Owns the fixed-capacity storage (`Slot[]`),
   Publishes items using a monotonically increasing sequence number,
   Creates independent readers via `createReader()`

 **RingBufferReader<E>** :
   Holds per-reader cursor (`nextSeq`),
   Reads independently without affecting other readers,
   Detects overwrite and returns `MISSED`

 **Slot<E>** :
   One cell holding `(value, seq)` for overwrite-safe validation

 **ReadResult<E>** :
   Encapsulates read outcome: `OK`, `EMPTY`, `MISSED`

## UML 
Diagrams are provided as PlantUML files under the `uml/` folder:
 `uml/class-diagram.puml`
 `uml/sequence-write.puml`
 `uml/sequence-read.puml`

## How to Run (No Maven)
### Compile
PowerShell (Windows) from project root:
```powershell
javac -d out (Get-ChildItem -Recurse -Filter *.java src\main\java | ForEach-Object FullName)