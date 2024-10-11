package ScrollSystem.FileHandlers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FilterScroll {
    private String path; 
    private BufferedReader reader;
    private int currentLine;
    private int totalLines;

    public FilterScroll(String path) {
        this.path = path;
        try {
            this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(path))); // to read binary file 
            this.currentLine = -1; 
            this.totalLines = countLines(); 
        } catch (IOException e) {
            System.err.println("Error initialising FilterScroll: " + e.getMessage());
            e.printStackTrace(); 
            this.reader = null; 
            this.totalLines = 0; 
        }
    }

    /**
     * Gets the current line of text in the binary file 
     * @params:     
     *      lineNumber: int 
     * @ret: 
     *      string on that line 
     */
    public String getLine(int lineNumber) {
        resetReader();
        String line;
        int currentLine = 1;
    
        try {
            while ((line = reader.readLine()) != null) {
                if (currentLine == lineNumber) {
                    this.currentLine = currentLine; 
                    return line; 
                }
                currentLine++;
            }
        } catch (IOException e) {
            System.err.println("Error reading line: " + lineNumber + " | " + e.getMessage());
            return null; 
        }
        return null; 
    }

    /**
     * Gets the next line in the binary file 
     * @ret:
     *      string of next line 
     */
    public String nextLine() {
        try{
            if (currentLine + 1 >= totalLines) {
                return "END OF FILE"; 
            }
            return getLine(currentLine + 1); 
        }
        catch (Exception e) {
            System.err.println("Error getting next line: " + e.getMessage());
            return "ERROR";
        }
    }

    /**
     * Gets the previous line in the binary file 
     * @ret:
     *      string of previous line 
     */
    public String previousLine() {
        try {
            if (currentLine <= 0) {
                System.out.println("Error: No previous line");
                return "ERROR";
            }
            return getLine(currentLine - 1);
        } catch (Exception e) {
            System.err.println("Error getting previous line: " + e.getMessage());
            return "ERROR";
        }
    }

    /**
     * Gets all the lines in the file 
     * @ret:
     *      String of liens in the file 
     */
    public String getAllLines() {
        int totalLines = countLines();
        StringBuilder result = new StringBuilder();
    
        for (int i = 0; i < totalLines; i++) {
            String line = getLine(i);
            if (line != null) {
                result.append(line).append("\n");
            } 
            else {
                System.out.println("Error: Unable to retrieve line " + i);
                return "ERROR";
            }
        }
    
        return result.toString();
    }

    /**
     * Gets the lines between two given line numbers (inclusive)
     * @params: 
     *      start, end : int 
     * @ret
     *      lines between the line numbers 
     */
    public String getLinesBetween(int start, int end) {
        if (start < 0 || end < 0 || start >= totalLines || end >= totalLines) {
            System.out.println("Error: invlid line numbers");
            return "ERROR";
        }
        if (start > end) {
            System.out.println("Error: end line is larger than start line");
            return "ERROR";
        }
    
        StringBuilder result = new StringBuilder();
    
        for (int i = start - 1; i < end; i++) {
            String line = getLine(i);
            if (line != null) {
                result.append(line).append("\n");
            } else {
                System.out.println("ERROR: Unable to retrieve line " + i);
                return "ERROR";
            }
        }
    
        return result.toString();
    }

    /**
     * Resets the current line 
     */
    private void resetReader() {
        try {
            if (reader != null) {
                reader.close();
            }
            this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            this.currentLine = -1; 
        } catch (IOException e) {
            System.err.println("Error resetting reader: " + e.getMessage());
        }
    }

    /** 
     * closes the reader 
     */
    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing reader: " + e.getMessage());
        }
    }

    /**
     * Gets the number of lines in the file 
     * @ret: 
     *      number of lines in the file 
     */
    public int countLines() {
        int lines = 0;
        try (BufferedReader tempReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            while (tempReader.readLine() != null) {
                lines++;
            }

            return lines;
        } catch (IOException e) {
            System.err.println("Error counting lines: " + e.getMessage());
            return 0;
        }
    }
}