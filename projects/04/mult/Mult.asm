// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.
  @R0
  D=M

  @mult
  M=D

  @sum
  M=0

  @R1
  D=M
  @i
  M=D

(LOOP)
  @i
  D=M
  @STORE
  D;JEQ

  @mult
  D=M
  @sum
  M=D+M

  @i
  M=M-1
  @LOOP
  0;JMP

(STORE)
  @sum
  D=M
  @R2
  M=D
  @END
  0;JMP

(END)
  @END
  0;JMP