package com.alexdziena;

import java.util.BitSet;
import java.util.HashMap;

/**
 * Created by alexdziena on 4/21/16.
 */
public final class Code {
    private static HashMap<String,Integer> dest_lookup;
    private static HashMap<String,Integer> comp_lookup;
    private static HashMap<String,Integer> jump_lookup;

    private static void init_dest_lookup() {
        dest_lookup = new HashMap<String,Integer>();
        dest_lookup.put(null,  0b000);
        dest_lookup.put("M",   0b001);
        dest_lookup.put("D",   0b010);
        dest_lookup.put("MD",  0b011);
        dest_lookup.put("A",   0b100);
        dest_lookup.put("AM",  0b101);
        dest_lookup.put("AD",  0b110);
        dest_lookup.put("AMD", 0b111);
    }

    private static void init_comp_lookup() {
        comp_lookup = new HashMap<String,Integer>();
        comp_lookup.put("0",   0b0101010);
        comp_lookup.put("1",   0b0111111);
        comp_lookup.put("-1",  0b0111010);
        comp_lookup.put("D",   0b0001100);
        comp_lookup.put("A",   0b0110000);
        comp_lookup.put("!D",  0b0001101);
        comp_lookup.put("!A",  0b0110001);
        comp_lookup.put("-D",  0b0001111);
        comp_lookup.put("-A",  0b0110011);
        comp_lookup.put("D+1", 0b0011111);
        comp_lookup.put("A+1", 0b0110111);
        comp_lookup.put("D-1", 0b0001110);
        comp_lookup.put("A-1", 0b0110010);
        comp_lookup.put("D+A", 0b0000010);
        comp_lookup.put("D-A", 0b0010011);
        comp_lookup.put("A-D", 0b0000111);
        comp_lookup.put("D&A", 0b0000000);
        comp_lookup.put("D|A", 0b0010101);
        comp_lookup.put("M",   0b1110000);
        comp_lookup.put("!M",  0b1110001);
        comp_lookup.put("-M",  0b1110011);
        comp_lookup.put("M+1", 0b1110111);
        comp_lookup.put("M-1", 0b1110010);
        comp_lookup.put("D+M", 0b1000010);
        comp_lookup.put("D-M", 0b1010011);
        comp_lookup.put("M-D", 0b1000111);
        comp_lookup.put("D&M", 0b1000000);
        comp_lookup.put("D|M", 0b1010101);
    }

    private static void init_jump_lookup() {
        jump_lookup = new HashMap<String,Integer>();
        jump_lookup.put(null,  0b000);
        jump_lookup.put("JGT", 0b001);
        jump_lookup.put("JEQ", 0b010);
        jump_lookup.put("JGE", 0b011);
        jump_lookup.put("JLT", 0b100);
        jump_lookup.put("JNE", 0b101);
        jump_lookup.put("JLE", 0b110);
        jump_lookup.put("JMP", 0b111);
    }

    static {
        init_dest_lookup();
        init_comp_lookup();
        init_jump_lookup();
    }

    public static Integer dest(String key){ return dest_lookup.get(key); }
    public static Integer comp(String key){ return comp_lookup.get(key); }
    public static Integer jump(String key){ return jump_lookup.get(key); }
}
