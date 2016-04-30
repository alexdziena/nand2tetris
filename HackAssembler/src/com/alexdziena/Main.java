package com.alexdziena;

import org.apache.commons.cli.*;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static String infile = "infile.asm";
    private static String outfile = "outfile.hack";
    private static Logger logger;
    private static SymbolTable symbolTable = null;
    private static Integer nextMemAddr = 16;

    private static PrintWriter writeHandler = null;

    public static void main(String[] args) throws IOException {
        Main.logger = Logger.getLogger("main");
        Main.logger.setLevel(Level.WARNING);
        parseCLI(args);
        Main.logger.log(Level.INFO, "Infile: {0}", Main.infile);
        Main.logger.log(Level.INFO, "Outfile: {0}", Main.outfile);
        symbolTable = new SymbolTable();

        firstPass();
        Parser parser = new Parser(Main.infile);
        while(parser.hasMoreCommands()) {
            parser.advance();
            CommandType type = parser.commandType();
            Main.logger.log(Level.INFO, "Type: {0}", type);

            StringBuilder lout = new StringBuilder();
            if (type == CommandType.C_COMMAND) {
                lout.append("111");
                lout.append(
                        String.format("%7s", Integer.toBinaryString(Code.comp(parser.comp()))).replace(" ", "0")
                );
                lout.append(
                        String.format("%3s", Integer.toBinaryString(Code.dest(parser.dest()))).replace(" ", "0")
                );
                lout.append(
                        String.format("%3s", Integer.toBinaryString(Code.jump(parser.jump()))).replace(" ", "0")
                );
            } else if (type == CommandType.A_COMMAND) {
                lout.append("0");
                Integer addr = null;
                if (symbolTable.contains(parser.symbol())) {
                    addr = symbolTable.GetAddress(parser.symbol());
                } else {
                    if (parser.symbol().matches("^\\d+$")){
                        addr = Integer.parseInt(parser.symbol());
                    } else {
                        symbolTable.addEntry(parser.symbol(), nextMemAddr);
                        addr = nextMemAddr;
                        nextMemAddr++;
                    }
                }
                lout.append(
                        String.format("%15s", Integer.toBinaryString(addr)).replace(" ", "0")
                );
            }
            if (type != CommandType.L_COMMAND) {
                Main.logger.log(Level.FINE, "Writing '{0}' to {1}", new Object[]{lout.toString(), Main.outfile});
                try {
                    writeLine(lout.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Main.writeHandler.close();
                    throw e;
                }
            }
        }
        Main.writeHandler.close();
        parser.close();
    }


//    private static String bitSetToString(BitSet bs) {
//        long[] arr = bs.toLongArray();
//        if (arr.length > 0) {
//            return Long.toString(arr[0], 2);
//        } else {
//            long[] ret = {0};
//            return Long.toString(ret[0], 2);
//        }
//    }
    private static void firstPass() throws FileNotFoundException {
        Parser parser = new Parser(Main.infile);
        while(parser.hasMoreCommands()) {
            parser.advance();
            CommandType type = parser.commandType();
            if (type == CommandType.L_COMMAND) {
                symbolTable.addEntry(parser.symbol(), parser.getCmdnum());
            }
        }
        parser.close();
    }

    private static void writeLine(String line) throws IOException {
        if (Main.writeHandler == null) {
            Main.writeHandler = new PrintWriter(new FileWriter(Main.outfile));
        }
        Main.writeHandler.println(line);
    }

    private static void parseCLI(String[] args) {
        Options options = new Options();
        final Option infile = Option.builder("i")
                .longOpt("infile")
                .argName("infile")
                .hasArg()
                .desc("parse this asm file")
                .build();
        final Option outfile = Option.builder("o")
                .longOpt("outfile")
                .argName("outfile")
                .hasArg()
                .desc("put the machine code here")
                .build();

        options.addOption(infile);
        options.addOption(outfile);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if(cmd.hasOption("i")) {
                Main.infile = cmd.getOptionValue("i");
            }
            if(cmd.hasOption("o")) {
                Main.outfile = cmd.getOptionValue("o");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
