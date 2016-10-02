/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author jass
 */
public class IndexFile {

    /**
     *
     */
    private final HashMap<String, HashMap<Integer, List<Integer>>> mIndex;

    /**
     *
     */
    public IndexFile() {
        mIndex = new HashMap<>();
    }

    /**
     *
     * @param stream
     * @param term
     * @param documentID
     * @throws FileNotFoundException
     */
    public final void addTerm(final SimpleTokenStream stream, String term,
        final int documentID) throws FileNotFoundException {
        // TO-DO: add the term to the index hashtable. If the table
        // does not have  an entry for the term, initialize a
        //new ArrayList<Integer>, add the docID to the list, and put
        //it into the map. Otherwise add the docID to the list that
        //already exists in the map, but ONLY IF the list does
        // not already contain the docID.

        HashMap<Integer, List<Integer>> doc = new HashMap<>();
        List<Integer> positionList = new ArrayList<>();
        int position = stream.getTokenPosition();
        if (!mIndex.containsKey(term)) {
            HashMap<Integer, List<Integer>> tempList = new HashMap<>();
            positionList.add(position);
            tempList.put(documentID, positionList);
            mIndex.put(term, tempList);

        } else {
            doc = mIndex.get(term);
            if (doc.containsKey(documentID)) {
                positionList = doc.get(documentID);
                if ((positionList.get(positionList.size() - 1) != position)) {
                    positionList.add(position);
                    doc.put(documentID, positionList);
                    mIndex.put(term, doc);
                }
            } else {
                positionList.add(position);
                doc.put(documentID, positionList);
                mIndex.put(term, doc);
            }
            //System.out.println(mIndex);
        }

    }

    /**
     *
     * @param term
     * @return
     */
    public HashMap<Integer, List<Integer>> getPostings(String term) {
        // TO-DO: return the postings list for the given term from
        //the index map.
        if (mIndex.containsKey(term)) {
            return mIndex.get(term);
        } else {
            return null;
        }
    }

    /**
     *
     * @return
     */
    public int getTermCount() {
        // TO-DO: return the number of terms in the index.
        int count = mIndex.size();
        return count;
    }

    /**
     *
     * @param term
     * @return
     */
    public List<Integer> getDocIds(String term) {
        // TO-DO: fill an array of Strings with all the keys from the hashtable.
        // Sort the array and return it.
        HashMap<Integer, List<Integer>> docIds;
        docIds = mIndex.get(term);
        List<Integer> listDocIds = new ArrayList<>();
        try{
        listDocIds = new ArrayList<>(docIds.keySet());
        }
        catch (Exception ex)
        {
            
        }
        return listDocIds;
    }
}
