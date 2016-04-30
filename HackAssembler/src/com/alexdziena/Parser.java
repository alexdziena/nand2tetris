package com.alexdziena;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexdziena on 4/21/16.
 */
public class Parser {
    private BufferedReader br;
    private Integer cmdnum = -1;
    String curCommand = null;

    private Pattern a_pattern = Pattern.compile("^@.*");
    private Pattern l_pattern = Pattern.compile("^\\(.*");
    private Pattern c_pattern = Pattern.compile("^[DAM0-9].*");

    private Pattern symbol_pattern = Pattern.compile("^[@(]([^\\)]*)\\)?(//)?.*");
    private Pattern dest_pattern = Pattern.compile("^([^=]*)=.*");
    private Pattern comp_pattern = Pattern.compile("^(?:.*=)?([^(//);]*);?.*");
    private Pattern jump_pattern = Pattern.compile("^(?:.*;){1}([^(//)]*)");

    public Parser(String filename) throws FileNotFoundException {

        // Open the file
        FileInputStream fstream = null;
        fstream = new FileInputStream(filename);
        this.br = new BufferedReader(new InputStreamReader(fstream));
    }

    public boolean hasMoreCommands() {
        boolean retval = false;
        try{
            this.br.mark(50);
            if (this.br.readLine() != null) {
                retval = true;
            }
            this.br.reset();
        }
        catch (IOException e) {
            e.printStackTrace();
            this.close();
        }
        return retval;
    }

    private String currentCommand() {
        return this.curCommand;
    }

    public void advance() {
        String line;
        try {
            boolean found = false;
            while (!found) {
                this.br.mark(5000);
                if ((line = this.br.readLine()) != null) {
                    line = line.split("//")[0];
                    line = line.trim();
                    if (line.length() > 0) {
                        Pattern p = Pattern.compile("^[(@DAM0-9].*");
                        Matcher m = p.matcher(line);
                        if (m.matches()){
                            found = true;
                            if (this.commandType() != CommandType.L_COMMAND) this.cmdnum++;
                            this.curCommand = line;
                        }
                    }
                }
                else {
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.close();
        }
    }

    public CommandType commandType() {
        String cmd = this.currentCommand();
        if (cmd != null) {
            Matcher m = this.a_pattern.matcher(cmd);
            if (m.matches()) return CommandType.A_COMMAND;
            m = this.l_pattern.matcher(cmd);
            if (m.matches()) return CommandType.L_COMMAND;
            m = this.c_pattern.matcher(cmd);
            if (m.matches()) return CommandType.C_COMMAND;
        }
        return null;
    }

    private String returnComponent(Pattern pattern, List<CommandType> validCommands) {
        if ( validCommands.indexOf(this.commandType()) == -1 ) {
            Logger.getGlobal().log(Level.WARNING, "Invalid component requested.");
            return null;
        }
        String cmd = this.currentCommand();
        if (cmd != null) {
            Matcher m = pattern.matcher(cmd);
            if (m.matches()) return m.group(1);
        }
        return null;

    }

    public String symbol() {
        List<CommandType> types = Arrays.asList(CommandType.A_COMMAND,CommandType.L_COMMAND);
        return this.returnComponent(this.symbol_pattern, types);
    }

    public String dest() {
        List<CommandType> types = Arrays.asList(CommandType.C_COMMAND);
        return this.returnComponent(this.dest_pattern, types);
    }

    public String comp() {
        List<CommandType> types = Arrays.asList(CommandType.C_COMMAND);
        return this.returnComponent(this.comp_pattern, types);
    }

    public String jump() {
        List<CommandType> types = Arrays.asList(CommandType.C_COMMAND);
        return this.returnComponent(this.jump_pattern, types);
    }

    public void close() {
        try {
            this.br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getCmdnum() {
        return cmdnum;
    }

    public void setCmdnum(Integer cmdnum) {
        this.cmdnum = cmdnum;
    }
}