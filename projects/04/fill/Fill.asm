// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

// Put your code here.
  @white
  M=0
  @black
  M=-1
  @write
  M=0
  @8192 // 32*256 for a 512x256 screen (each word written is 16 bits)
  D=A
  @scrsize
  M=D


(LOOP)
  @SCREEN
  D=A
  @writeaddr
  M=D
  @scrsize
  D=M
  @n
  M=D
  @KBD
  D=M
  @WHITE
  D;JEQ
  @BLACK
  0;JMP

(BLACK)
  @black
  D=M
  @write
  M=D
  @WRITE
  0;JMP

(WHITE)
  @white
  D=M
  @write
  M=D
  @WRITE
  0;JMP

(WRITE)
  @write
  D=M
  @writeaddr
  A=M
  M=D
  @writeaddr
  M=M+1
  @n
  M=M-1
  D=M
  @WRITE
  D;JNE

  @LOOP
  0;JMP