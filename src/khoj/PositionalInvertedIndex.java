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
    static IndexFile index = new IndexFile();
    /**
     * path of files to be indexed.
     */
    static String filePath;
    /**
     * BiwordIndex object for doing biword indexing.
     */
    BiwordIndex bwindex = new BiwordIndex();
    /**
     * the list of file names that were processed.
     */
    public final List<String> fileNames = new ArrayList<>();

    /**
     *
     * @param path path of files to be indexed.
     * @return object to refer files indexed.
     * @throws IOException Throws Input Output Exception
     */
    public final IndexFile startIndexing(final String path) throws IOException {

        final Path currentWorkingPath = Paths.get(path)
            .toAbsolutePath();
        filePath = currentWorkingPath.toString();

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
                final BasicFileAttributes attrs)
                throws FileNotFoundException, IOException {
                // only process .json files
                if (file.toString().endsWith(".json")) {
                    // we have found a .json file; add its name to
                    //the fileName list, then index the file and
                    //increase the document ID counter.
                    fileNames.add(file.getFileName().toString());
                    indexFile(file.toFile(), index, mDocumentID);
                    bwindex.tokenize(file.toFile(), mDocumentID);
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
        System.out.println("Inverted Index Size:" + fileNames.size());
        return index;
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
     * @throws IOException for exception handling
     */
    private static void indexFile(final File file,
        final IndexFile index, final int docID)
        throws FileNotFoundException, IOException {
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
                index.addTerm(streamText, text, docID);
            }
        }
        streamFile.reader.close();
    }

    /**
     *
     * @param docIds list of document Id
     * @return Returns Doc Names as docIds
     */
    public final List<String> getDocumentNames(final List<Integer> docIds) {
        List<String> docNames = new ArrayList<>();
        if (docIds != null) {
            for (int docId = 0; docId < docIds.size(); docId++) {
                String name = fileNames.get(docIds.get(docId));
                if (!docNames.contains(name)) {
                    docNames.add(fileNames.get(docIds.get(docId)));
                }
            }
        }
        return docNames;
    }

    /**
     *
     * @return filePath.
     */
    public final String returnFilePath() {
        return filePath;
    }
}
