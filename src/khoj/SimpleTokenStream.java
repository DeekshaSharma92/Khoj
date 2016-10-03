/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

/**
 * Reads tokens one at a time from an input stream. Returns tokens
 * with minimal processing: removing all non-alphanumeric characters,
 * and converting to lowercase.
 */
public class SimpleTokenStream implements TokenStream {

    /**
     * mReader: to read file.
     */
    private Scanner mReader;
    public Reader reader;
    private String body = null;
    int position = 0;

    /**
     * Constructs a SimpleTokenStream to read from the specified file.
     *
     * @param file File to open
     * @throws java.io.FileNotFoundException Throws exception when
     * file not found
     */
    public SimpleTokenStream(final File file) throws FileNotFoundException {
        //fileToOpen = file;
        reader = new FileReader(file);
    }

    /**
     *
     * @param text Text to be indexed
     */
    public SimpleTokenStream(final String text) {
        body = text;
        mReader = new Scanner(body);
    }

    /**
     *
     * @return returns body of the json doc
     */
    public final String readFile(Article article) {
        
        String articleBody = article.body.toString();
        return articleBody;
    }
    
    /**
     *
     * @return returns author's name of the json doc
     */
    public final String readFile(Authors author) {
        
        String authorName = "";
        try{
        authorName = author.author.toString();
        }
        catch (Exception ex){
            authorName = "invalid name";
            System.out.println("Invalid Json files");
            System.exit(0);
        }
        return authorName;
    }


    /**
     * @return Returns true if the stream has tokens remaining.
     */
    @Override
    public final boolean hasNextToken() {
        return mReader.hasNext();
    }

    /**
     * @return Returns the next token from the stream, or null if
     * there is no token available.
     */
    @Override
    public final String nextToken() {
        if (!hasNextToken()) {
            return null;
        }

        String next = mReader.next();

        return next.length() > 0 ? next
            : hasNextToken() ? nextToken() : null;
    }

    /**
     *
     * @return return list of positions
     */
    public int getTokenPosition() {
        position++;
        return position;
    }

}
