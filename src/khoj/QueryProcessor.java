/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jass
 */
public class QueryProcessor {

    private final String query;

    /**
     * What type of query it is: Type 1: Single Word Type 2: Phrase
     * query Type 3: Union Query Type 4: AND Query
     */
    final int queryType;
    final PositionalInvertedIndex engine;
    final IndexFile index;

    /**
     *
     * @param text Query to be processed
     * @param indexing Object of PositionalInvertedIndex
     */
    public QueryProcessor(final String text, final PositionalInvertedIndex indexing) {
        query = text;
        engine = indexing;
        index = engine.index;
        queryType = 3;

    }

    /**
     *
     * @return Returns result String list
     */
    public final List<String> singleWordQuery() {
        List<String> results;
        List<Integer> docIds = index.getDocIds(query);
        results = engine.getDocumentNames(docIds);
        return results;
    }

    /**
     *
     * @return Returns result list for multiword query.
     */
    public final List<String> generalQuery(String text) {
        List<String> results;
        List<List<Integer>> setDocIds = new ArrayList<>();
        String[] words = text.split(" ");
        for (String word : words) {
            setDocIds.add(index.getDocIds(word));
        }

        List<Integer> tempDocIdList = new ArrayList<>();
        for (List<Integer> docIdList : setDocIds) {
            if (tempDocIdList.isEmpty()) {
                tempDocIdList = docIdList;
            } else {
                tempDocIdList.retainAll(docIdList);
            }
        }
        results = engine.getDocumentNames(tempDocIdList);
        return results;
    }
    
    public final List<String> multiWordOrQuery()
    {
        String[] words = query.split("\\+");
        List<String> leftSideQueryResult = generalQuery(words[0]);
        List<String> rightSideQueryResult = generalQuery(words[1]);
        leftSideQueryResult.addAll(rightSideQueryResult);
        return leftSideQueryResult;
    }
}
