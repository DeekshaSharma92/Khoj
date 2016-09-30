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
     * What type of query it is:
     *   Type 1: Single Word
     *   Type 2: Phrase query
     *   Type 3: Union Query
     *   Type 4: AND Query
     */
    final int queryType;
    final SimpleEngine engine;

    /**
     *
     * @param text Query to be processed
     */
    public QueryProcessor(final String text, final SimpleEngine indexing) {
        query = text;
        engine = indexing;
        queryType = 1;
        
    }
    
    public List<String> SingleWordQuery(){
        List<String> results = new ArrayList<>();
        NaiveInvertedIndex index = engine.index;
        Integer[] docIds = index.getDocIds(query);
        results = engine.getDocumentNames(docIds);
        return results;
    }
}
