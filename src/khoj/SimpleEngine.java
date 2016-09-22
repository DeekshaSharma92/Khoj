/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.System;

/**
 * A very simple search engine. Uses an inverted index over a folder
 * of TXT files.
 */
public class SimpleEngine {

    /**
     *
     * @throws IOException Throws Input Output Exception
     */
    public final void startIndexing() throws IOException {

        final Path currentWorkingPath = Paths.get("").toAbsolutePath();
        // the inverted index
        final NaiveInvertedIndex index = new NaiveInvertedIndex();

        // the list of file names that were processed
        final List<String> fileNames = new ArrayList<>();
        // This is our standard "walk through all .txt files" code.
        Files.walkFileTree(currentWorkingPath, new SimpleFileVisitor<Path>() {
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
                final BasicFileAttributes attrs) throws FileNotFoundException {
                // only process .json files
                if (file.toString().endsWith(".json")) {
                    // we have found a .txt file; add its name to
                    //the fileName list, then index the file and
                    //increase the document ID counter.
                    System.out.println("Indexing file " + file.getFileName());
                    fileNames.add(file.getFileName().toString());
                    indexFile(file.toFile(), index, mDocumentID);
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

        printResults(index, fileNames);
        // Implement the same program as in Homework 1: ask the user
        //for a term, retrieve the postings list for that term,
        //and print the names of the documents which contain the term.
    }

    /**
     * Indexes a file by reading a series of tokens from the file,
     * treating each token as a term, and then adding the given
     * document's ID to the inverted index for the term.
     *
     * @param file a File object for the document to index.
     * @param index the current state of the index for the files that
     * have already been processed.
     * @param docID the integer ID of the current document, needed
     * when indexing each term from the document.
     * @throws FileNotFoundException Throws file not found exception
     */
    private static void indexFile(final File file,
        final NaiveInvertedIndex index, final int docID)
        throws FileNotFoundException {
        // TO-DO: finish this method for indexing a particular file.
        // Construct a SimpleTokenStream for the given File.
        // Read each token from the stream and add it to the index.
        SimpleTokenStream streamFile = new SimpleTokenStream(file);
        String text = streamFile.readFile();
        SimpleTokenStream streamText = new SimpleTokenStream(text);
        int i = 0;
        while (streamText.hasNextToken()) {
            text = streamText.nextToken();
            index.addTerm(streamText, text, docID);
        }
    }

    /**
     *
     * @param index
     * @param fileNames
     */
    private static void printResults(final NaiveInvertedIndex index,
        final List<String> fileNames) {

        // TO-DO: print the inverted index.
        // Retrieve the dictionary from the index. (It will already be sorted.)
        // For each term in the dictionary, retrieve the postings list for the
        // term. Use the postings list to print the list of document names that
        // contain the term. (The document ID in a postings list corresponds to
        // an index in the fileNames list.)
        // Print the postings list so they are all left-aligned starting at the
        // same column, one space after the longest of the term lengths.
        //Example:
        // as:      document0 document3 document4 document5
        // engines: document1
        // search:  document2 document4
        String[] dict = index.getDictionary();
        int longestWord = 0;
        for (String terms : dict) {
            longestWord = Math.max(longestWord, terms.length());
        }
        for (int i = 0; i < index.getTermCount(); i++) {
            HashMap<Integer, List<Integer>> postings =
                index.getPostings(dict[i]);
            System.out.print(dict[i] + ":");
            printSpaces(longestWord - dict[i].length() + 1);
            for (int j = 0; j < postings.size(); j++) {
                // System.out.print(" " + fileNames.get(postings.get()));
            }
            System.out.print("\n");
        }

    }

    /**
     *
     * @param spaces Adds empty spaces
     */
    private static void printSpaces(final int spaces) {
        for (int i = 0; i < spaces; i++) {
            System.out.print(" ");
        }
    }
}
