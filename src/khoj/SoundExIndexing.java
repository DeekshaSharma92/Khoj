/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Character;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jass
 */
public class SoundExIndexing {

    /**
     * the list of file names that were processed.
     */
    private final List<String> fileNames = new ArrayList<>();

    /**
     * HashMap for soundEx index.
     */
    private final HashMap<String, List<Integer>> soundExIndex;

    /**
     * default constructor.
     *
     * @throws IOException Throws Input Output Exception
     */
    public SoundExIndexing() throws IOException {

        soundExIndex = new HashMap<>();

        final Path currentWorkingPath = Paths.get("./json/authors/")
            .toAbsolutePath();

        // This is our standard "walk through all author .json files" code.
        Files.walkFileTree(currentWorkingPath,
            new SimpleFileVisitor<Path>() {
            int mDocumentID = 0;

            public FileVisitResult preVisitDirectory(Path dir,
                final BasicFileAttributes attrs) {
                // make sure we only process the current working directory
                if (currentWorkingPath.equals(dir)) {
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.SKIP_SUBTREE;
            }

            @Override
            public FileVisitResult visitFile(final Path file,
                final BasicFileAttributes attrs)
                throws FileNotFoundException {
                // only process .json files
                if (file.toString().endsWith(".json")) {
                    // we have found a .txt file; add its name to
                    //the fileName list, then index the file and
                    //increase the document ID counter.
                   // System.out.println("Indexing file "
                   //     + file.getFileName());
                    fileNames.add(file.getFileName().toString());
                    indexFile(file.toFile(), mDocumentID);
                    mDocumentID++;
                }
                return FileVisitResult.CONTINUE;
            }

            // don't throw exceptions if files are locked/other errors occur
            @Override
            public FileVisitResult visitFileFailed(final Path file,
                final IOException e) {

                return FileVisitResult.CONTINUE;
            }

        });
        System.out.println("SoundEx Indexing Completed.\n"
        + "Total Documents Indexed: " + fileNames.size());
    }

    /**
     *
     * @param file to be indexed.
     * @param docID id of document to be indexed
     * @throws FileNotFoundException Throws file not found exception
     */
    private void indexFile(final File file, final int docID)
        throws FileNotFoundException {
        SimpleTokenStream streamFile = new SimpleTokenStream(file);
        Gson gson = new Gson();
        Authors author = gson.fromJson(streamFile.reader, Authors.class);
        String text = streamFile.readFile(author);
        SimpleTokenStream streamText = new SimpleTokenStream(text);
        int i = 0;
        while (streamText.hasNextToken()) {
            text = streamText.nextToken();
            if (text != null) {
                SoundEx ex = new SoundEx(text);
                text = ex.characterMapping();
                addTerm(text, docID);
            }
        }
    }

    /**
     *
     * @param text to be indexed.
     * @param docID docId of file.
     */
    private void addTerm(final String text, final int docID) {
        List<Integer> docIdList = new ArrayList<>();
        if (!soundExIndex.containsKey(text)) {
            docIdList.add(docID);
            soundExIndex.put(text, docIdList);
        } else {
            docIdList = soundExIndex.get(text);
            if (docIdList.get(docIdList.size() - 1) != docID) {
                docIdList.add(docID);
                soundExIndex.put(text, docIdList);
            }
        }
    }

    /**
     * implements indexing for soundEx
     */
    private final class SoundEx {

        /**
         * @token whose soundEx code is to be calculated
         */
        private String token;
        /**
         * constant for single space.
         */
        private final char singleSpace = ' ';
        /**
         * length of soundEx code.
         */
        private final int codeLength = 4;
        /**
         * characters need to be removed.
         */
        private final char[] charsToBeRemoved;
        /**
         * character mapping with integer.
         */
        private final Map<Character, Character> characterMap;

        /**
         * Parameterized Constructor for SoundEx class.
         *
         * @param word String to be converted
         */
        public SoundEx(final String word) {
            this.characterMap = new HashMap<Character, Character>() {
                {
                    put('b', '1');
                    put('f', '1');
                    put('p', '1');
                    put('v', '1');
                    put('c', '2');
                    put('g', '2');
                    put('j', '2');
                    put('k', '2');
                    put('q', '2');
                    put('s', '2');
                    put('x', '2');
                    put('z', '2');
                    put('d', '3');
                    put('t', '3');
                    put('l', '4');
                    put('m', '5');
                    put('n', '5');
                    put('r', '6');
                }
            };
            this.charsToBeRemoved = new char[]{'a', 'e', 'i', 'o', 'u',
                'h', 'w', 'y'};
            token = word.toLowerCase();
        }

        /**
         * @param text String from which duplicate characters need to
         * be removed.
         * @return String after removing duplicate characters.
         */
        private String removeDuplicateCharacters(final String text) {
            String result;
            StringBuilder temp = new StringBuilder(text);
            char previousCharacter;

            for (int i = 0; i < temp.length(); i++) {
                previousCharacter = temp.charAt(i);
                for (int j = i + 1; j < temp.length(); j++) {
                    if (previousCharacter == temp.charAt(j)) {
                        temp.setCharAt(j, singleSpace);
                    } else {
                        break;
                    }
                }

            }
            result = temp.toString().replaceAll("\\s+", "");

            return result;
        }

        /**
         *
         * @return returns character mappings
         */
        private String characterMapping() {
            StringBuilder temp
                = new StringBuilder(removeDuplicateCharacters(token));
            String tempResult = "";
            String result;
            String firstCharacter = "";
            for (int i = 0; i < temp.length(); i++) {
                if (i == 0) {
                    firstCharacter = Character.toString(temp.charAt(i));
                } else if (!(new String(charsToBeRemoved)
                    .contains(String.valueOf(temp.charAt(i))))) {

                    tempResult = tempResult + Character.toString(temp.charAt(i));
                }
            }
            temp = new StringBuilder(tempResult);
            for (int i = 0; i < temp.length(); i++) {
                temp.setCharAt(i, characterMap.get(temp.charAt(i)));
            }

            result = firstCharacter + temp;
            result = removeDuplicateCharacters(result);
            if (result.length() < codeLength) {
                result = result + "00000";
            }
            return result.substring(0, codeLength);

        }

    }

}
