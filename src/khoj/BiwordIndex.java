/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author deeksha
 */
public class BiwordIndex {

    /**
     * token for indexing.
     */
    String query;
    /**
     * map holding index.
     */
    Map<String, List<Integer>> biwordMap = new HashMap<>();
    /**
     * to normalize token.
     */
    Normalize normalizeToken = new Normalize();

    /**
     *
     * @param file File to be opened.
     * @param documentID docId of file
     * @throws IOException for exception handeling
     */
    public final void tokenize(final File file, final int documentID)
        throws IOException {
        int i = 0;
        String bwphrase;
        List<String> biwordList = new ArrayList<String>();
        SimpleTokenStream streamFile = new SimpleTokenStream(file);
        Gson gson = new Gson();
        Article article = gson.fromJson(streamFile.reader, Article.class);
        String query = streamFile.readFile(article);
        SimpleTokenStream streamText = new SimpleTokenStream(query);

        while (streamText.hasNextToken()) {

            String text = streamText.nextToken();
            text = normalizeToken.normalizeToken(text).get(0);

            text = PorterStemmer.processToken(text);
            if (biwordList.size() == 2) {
                bwphrase = biwordList.get(0) + " " + biwordList.get(1);
                compose(bwphrase.trim(), documentID);
                biwordList.remove(0);
                biwordList.add(text);
            } else {
                biwordList.add(text);
            }
        }
    }

    /**
     *
     * @param term For indexing.
     * @param documentID docId of document
     */
    public final void compose(final String term, final int documentID) {
        String biwordPhrase = term;
        List<Integer> phrasedocID = new ArrayList<>();
        if (!biwordMap.containsKey(biwordPhrase)) {

            phrasedocID.add(documentID);
            biwordMap.put(biwordPhrase, phrasedocID);
        } else {
            phrasedocID = biwordMap.get(biwordPhrase);
            if (phrasedocID.get(phrasedocID.size() - 1) != documentID) {
                phrasedocID.add(documentID);
                biwordMap.put(biwordPhrase, phrasedocID);
            }

        }

    }

    /**
     *
     * @return number of terms in index
     */
    public final int getTermCount() {
        // TO-DO: return the number of terms in the index.
        int count = biwordMap.size();
        return count;
    }

    /**
     *
     * @param phrase
     * @return
     */
    public List<Integer> biwordQuery(String phrase) {
        List<Integer> docIdList = biwordMap.get(phrase.trim());
        return docIdList;
    }
}
