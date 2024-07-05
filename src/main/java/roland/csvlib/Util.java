package roland.csvlib;

import java.lang.reflect.InvocationTargetException;

class Util {
    private Util() {}

public static String[] SplitByNewline(String CSV){

        String[] lines = CSV.split("\n"); // split by unix newline

        if (lines.length == 1) // might be old mac newlines
            lines = CSV.split("\r"); // split by pre-OS X newline (cant conform if this is nessesary)

        if (lines[0].toCharArray()[lines[0].length()-1] == '\r') // if OS with /r/n
        {
            // remove /r
            for (int i = 0;i < lines.length;i++)
                lines[i] = lines[i].substring(0,lines[i].length()-1);  // remove th last character in every line
        }


        return lines;
    }

}
