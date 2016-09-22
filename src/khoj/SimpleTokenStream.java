/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    private final Scanner mReader;
    /**
     * fileToOpen: file to be opened by scanner.
     */
    private final File fileToOpen;

    /**
     * Constructs a SimpleTokenStream to read from the specified file.
     *
     * @param file File to open
     * @throws java.io.FileNotFoundException Throws exception when file
     * not found
     */
    public SimpleTokenStream(final File file) throws FileNotFoundException {
        fileToOpen = file;
        mReader = new Scanner(new FileReader(fileToOpen));
    }

    /**
     * @return Returns true if the stream has tokens remaining.
     */
    @Override
    public final boolean hasNextToken() {
        return mReader.hasNext();
    }

    /**
     * @return Returns the next token from the stream, or null if there is no
     * token available.
     */
    @Override
    public final String nextToken() {
        if (!hasNextToken()) {
            return null;
        }

        String next = mReader.next().replaceAll("\\W", "").toLowerCase();

        return next.length() > 0 ? next
            : hasNextToken() ? nextToken() : null;
    }

    /**
     *
     * @param token Token whose position is to be found
     * @return return list of positions
     * @throws FileNotFoundException Throws file not found error
     */
    public final List<Integer> getTokenPosition(final String token)
        throws FileNotFoundException {
        Scanner reader = new Scanner(new FileReader(fileToOpen));
        Pattern p = Pattern.compile("\\b" + Pattern.quote(token) + "\\b");
        String line;
        List<Integer> pos = new ArrayList<>();
        int i = 0;
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
