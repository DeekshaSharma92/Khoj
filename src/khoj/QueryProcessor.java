/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author jass
 */
public class QueryProcessor {

    private String query;

    /**
     * What type of query it is: Type 1: Single Word Type 2: Phrase
     * query Type 3: Union Query Type 4: AND Query
     */
    final int queryType;
    final PositionalInvertedIndex engine;
    IndexFile index;
    final SoundExIndexing soundIndexing;
    final Normalize normalizeToken = new Normalize();
    static int specialQueryType = 0;

    /**
     *
     * @param text Query to be processed
     * @param indexing Object of PositionalInvertedIndex
     */
    public QueryProcessor(final String text,
        SoundExIndexing soundIndex,
        PositionalInvertedIndex inverted) throws IOException {
        query = text;
        engine = inverted;
        index = engine.index;
        soundIndexing = soundIndex;
        if (query.startsWith(":")) {
            queryType = 2;
        } else {
            queryType = 1;
        }

    }

    public final List<String> specialQuery() throws IOException {
        List<String> result = new ArrayList<>();
        String[] queryBreak = query.split(" ");
        queryBreak[0] = queryBreak[0].replaceAll("\\W", "").toLowerCase();
        String token;
        String path;
        if (null != queryBreak[0]) {
            switch (queryBreak[0]) {
                case "stem":
                    specialQueryType = 1;
                    token = queryBreak[1].replaceAll("\\W", "").toLowerCase();
                    String stemmed = PorterStemmer.processToken(token);
                    result.add(stemmed);
                    break;
                case "vocab":
                    specialQueryType = 2;
                    result = index.getVocab();
                    break;
                case "q":
                    System.exit(0);
                    break;
                case "author":
                    specialQueryType = 3;
                    token = queryBreak[1].replaceAll("\\W", "").toLowerCase();
                    result = soundIndexing.soundExQuery(token);
                    break;
                case "index":
                    specialQueryType = 4;
                    path = queryBreak[1];
                    index = engine.startIndexing(path);
                    break;
                case "soundindex":
                    specialQueryType = 5;
                    path = queryBreak[1];
                    soundIndexing.startIndexing(path);
                    break;
                default:
                    break;

            }
        }
        return result;
    }

    /**
     *
     * @return Returns result String list
     */
    public final List<String> performQuery() {
        List<String> results = new ArrayList<>();
        List<String> subQueries = new ArrayList<>();
        subQueries = Arrays.asList(query.split("\\+"));
        return results;
    }

    /**
     *
     * @return Returns result list for multiword query after
     * performing and operation on result of each.
     */
    public final List<Integer> andQuery(String text) {
        List<String> results;
        List<List<Integer>> setDocIds = new ArrayList<>();
        String[] words = text.split(" ");
        String phrase = "";
        boolean isPhrase = false;
        for (String word : words) {
            
            if (word.startsWith("\"") && word.endsWith("\"")) {
                word = normalizeToken.NormalizeToken(word).get(0);
                if (!word.isEmpty()) {
                    setDocIds.add(index.getDocIds(word));
                }
            }
            if (word.startsWith("\"")) {
                word = normalizeToken.NormalizeToken(word).get(0);
                word = PorterStemmer.processToken(word);
                phrase = phrase + word + " ";
                isPhrase = true;
            } else if (word.endsWith("\"")) {
                word = normalizeToken.NormalizeToken(word).get(0);
                word = PorterStemmer.processToken(word);
                phrase = phrase + word + " ";
                setDocIds.add(phraseQuery(phrase));
                phrase = "";
                isPhrase = false;
            } else if (isPhrase) {
                word = word.replaceAll("\\W", "").toLowerCase();
                phrase = phrase + word + " ";
            } else {
                List<String> normalizedTokens = normalizeToken
                    .NormalizeToken(word);
                for (String token : normalizedTokens) {
                    token = PorterStemmer.processToken(token);
                    if (!token.isEmpty()) {
                        setDocIds.add(index.getDocIds(token));
                    }
                }
            }
        }

        List<Integer> tempDocIdList = new ArrayList<>();
        boolean firstIteration = true;
        for (List<Integer> docIdList : setDocIds) {
            if (firstIteration) {
                tempDocIdList = docIdList;
                firstIteration = false;
            } else {
                tempDocIdList.retainAll(docIdList);
            }
        }

        return tempDocIdList;
    }

    public final List<String> multiWordOrQuery() {
        List<String> results;
        String[] words = query.split("\\+");
        List<List<Integer>> setDocIds = new ArrayList<>();
        for (String word : words) {
            setDocIds.add(andQuery(word.trim()));
        }
        List<Integer> tempDocIdList = new ArrayList<>();
        for (List<Integer> docIdList : setDocIds) {
            if (tempDocIdList.isEmpty()) {
                tempDocIdList = docIdList;
            } else {
                tempDocIdList.addAll(docIdList);
            }
        }
        results = engine.getDocumentNames(tempDocIdList);
        return results;
    }

    public final List<Integer> phraseQuery(String phrase) {
        List<Integer> resultDocIds = new ArrayList<>();
        String[] words = phrase.split(" ");
        if (words.length == 2) {
            BiwordIndex biword = engine.bwindex;
            
            resultDocIds = biword.biwordQuery(phrase);
        } else {
            List<HashMap<Integer, List<Integer>>> postings = new ArrayList<>();
            for (String word : words) {
                word = PorterStemmer.processToken(word.toLowerCase());
                postings.add(index.getPostings(word));
            }
            HashMap<Integer, List<Integer>> docPositionMap = postings.get(0);
            if (docPositionMap != null) {
                for (int i = 1; i < postings.size(); i++) {
                    (docPositionMap.keySet()).retainAll((postings.get(i).keySet()));
                }
                List<Integer> docIdsList = new ArrayList<>(docPositionMap.keySet());

                for (int i = 0; i < docPositionMap.size(); i++) {
                    List<List<Integer>> positionList = new ArrayList<>();
                    Integer docId = docIdsList.get(i);
                    for (String word : words) {
                        word = PorterStemmer.processToken(word.toLowerCase());
                        HashMap<Integer, List<Integer>> posting
                            = index.getPostings(word);
                        List<Integer> positions = posting.get(docId);
                        positionList.add(positions);
                    }
                    List<Integer> tempList = positionList.get(0);
                    for (int j = 1; j < positionList.size(); j++) {
                        List<Integer> nextList = positionList.get(j);
                        for (int pos = 0; pos < nextList.size(); pos++) {
                            nextList.set(pos, nextList.get(pos) - j);
                        }
                        tempList.retainAll(nextList);
                    }
                    if (!tempList.isEmpty()) {
                        resultDocIds.add(docId);
                    }
                }
            }
        }

        return resultDocIds;
    }
}
