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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.lang.System;

/**
 * A very simple search engine. Uses an inverted index over a folder
 * of TXT files.
 */
public class PositionalInvertedIndex {

    /**
     * the inverted index.
     *
     */
    final IndexFile index = new IndexFile();
    /**
     * the list of file names that were processed.
     */
    private final List<String> fileNames = new ArrayList<>();

    /**
     *
     * @throws IOException Throws Input Output Exception
     */
    public PositionalInvertedIndex() throws IOException {

        final Path currentWorkingPath = Paths.get("./json/articles/")
            .toAbsolutePath();

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
                    // System.out.println("Indexing file " + file.getFileName());
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
        System.out.println("Position Inverted Indexing Completed.\n"
            + "Total Documents Indexed: " + fileNames.size());
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
        final IndexFile index, final int docID)
        throws FileNotFoundException {
        // TO-DO: finish this method for indexing a particular file.
        // Construct a SimpleTokenStream for the given File.
        // Read each token from the stream and add it to the index.
        SimpleTokenStream streamFile = new SimpleTokenStream(file);
        Gson gson = new Gson();
        Article article = gson.fromJson(streamFile.reader, Article.class);
        String text = streamFile.readFile(article);
        SimpleTokenStream streamText = new SimpleTokenStream(text);
        int i = 0;
        while (streamText.hasNextToken()) {
            text = streamText.nextToken();
            if (text != null) {
                text = PorterStemmer.processToken(text);
                index.addTerm(streamText, text, docID);
            }
        }
    }

    /**
     *
     * @param docIds list of document Id
     * @return Returns Doc Names as docIds
     */
    public final List<String> getDocumentNames(final List<Integer> docIds) {
        List<String> docNames = new ArrayList<>();
        for (int docId = 0; docId < docIds.size(); docId++) {
            docNames.add(fileNames.get(docIds.get(docId)));
        }
        return docNames;
    }
}