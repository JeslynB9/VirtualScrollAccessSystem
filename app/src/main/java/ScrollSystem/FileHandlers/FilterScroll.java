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

    public FilterScroll(String path) throws IOException {
        this.path = path;
        this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(path))); // to read binary file 
        this.currentLine = -1; 
        this.totalLines = countLines(); 
    }

    /**
     * Gets the current line of text in the binary file 
     * @params:     
     *      lineNumber: int 
     * @ret: 
     *      string on that line 
     */
    public String getLine(int lineNumber) throws IOException {
        resetReader();
        String line;
        int currentLine = 0;

        while ((line = reader.readLine()) != null) {
            if (currentLine == lineNumber) {
                this.currentLine = currentLine; 
                return line; 
            }
            currentLine++;
        }
        return null; 
    }

    /**
     * Gets the next line in the binary file 
     * @ret:
     *      string of next line 
     */
    public String nextLine() throws IOException {
        if (currentLine + 1 >= totalLines) {
            return "END OF FILE"; 
        }
        return getLine(currentLine + 1); 
    }

    /**
     * Gets the previous line in the binary file 
     * @ret:
     *      string of previous line 
     */
    public String previousLine() throws IOException {
        return getLine(currentLine - 1); 
    }

    /**
     * Resets the current line 
     */
    private void resetReader() throws IOException {
        if (reader != null) {
            reader.close();
        }
        this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        this.currentLine = -1; 
    }

    /** 
     * closes the reader 
     */
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    /**
     * Gets the number of lines in the file 
     * @ret: 
     *      number of lines in the file 
     */
    private int countLines() throws IOException {
        int lines = 0;
        try (BufferedReader tempReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            while (tempReader.readLine() != null) {
                lines++;
            }
        }
        return lines;
    }
}