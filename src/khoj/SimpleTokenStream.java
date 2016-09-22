/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads tokens one at a time from an input stream. Returns tokens with minimal
 * processing: removing all non-alphanumeric characters, and converting to
 * lowercase.
 */
public class SimpleTokenStream implements TokenStream {

        private final Scanner mReader;
        private final File fileToOpen;

    /**
     * Constructs a SimpleTokenStream to read from the specified file.
     *
     * @param file
     * @throws java.io.FileNotFoundException
     */
    public SimpleTokenStream(File file) throws FileNotFoundException {
        fileToOpen = file;
        mReader = new Scanner(new FileReader(fileToOpen));
    }


    /**
     * Returns true if the stream has tokens remaining.
     */
    @Override
    public boolean hasNextToken() {
        return mReader.hasNext();
    }

    /**
     * Returns the next token from the stream, or null if there is no token
     * available.
     */
    @Override
    public String nextToken() {
        if (!hasNextToken()) {
            return null;
        }

        String next = mReader.next().replaceAll("\\W", "").toLowerCase();
        
        return next.length() > 0 ? next
            : hasNextToken() ? nextToken() : null;
    }

    public List<Integer> getTokenPosition(String token) throws FileNotFoundException {
        Scanner reader = new Scanner(new FileReader(fileToOpen));
        Pattern p = Pattern.compile("\\b" + Pattern.quote(token)  + "\\b");
        String line;
        List<Integer> pos = new ArrayList<>();
        int i =0 ;
        while (reader.hasNextLine()) {
            line = reader.nextLine();            
            Matcher m = p.matcher(line);
            
            // indicate all matches on the line
            
            while (m.find()) {
                
                pos.add(m.start());
            }
            
        }
        return pos;
    }
    
}
