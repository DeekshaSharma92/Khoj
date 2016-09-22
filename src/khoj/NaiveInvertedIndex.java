/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import java.io.FileNotFoundException;
import java.util.*;

public class NaiveInvertedIndex {

    private HashMap<String, HashMap<Integer, List<Integer>>> mIndex;

    public NaiveInvertedIndex() {
        mIndex = new HashMap<>();
    }

    public void addTerm(SimpleTokenStream stream, String term, int documentID) throws FileNotFoundException {
        // TO-DO: add the term to the index hashtable. If the table does not have
        // an entry for the term, initialize a new ArrayList<Integer>, add the 
        // docID to the list, and put it into the map. Otherwise add the docID
        // to the list that already exists in the map, but ONLY IF the list does
        // not already contain the docID.

        HashMap<Integer, List<Integer>> doc = new HashMap<>();
        List<Integer> pos = stream.getTokenPosition(term);
        if (!mIndex.containsKey(term)) {
            HashMap<Integer, List<Integer>> temp_list = new HashMap<>();

            temp_list.put(documentID, pos);
            mIndex.put(term, temp_list);

        } else {
            doc = mIndex.get(term);
            if (!doc.containsKey(documentID)) {
                
                doc.put(documentID, pos);
                mIndex.remove(term);
                mIndex.put(term, doc);
            }
            System.out.print(mIndex);

        }

    }

    public HashMap<Integer, List<Integer>> getPostings(String term) {
        // TO-DO: return the postings list for the given term from the index map.
        if (mIndex.containsKey(term)) {
            return mIndex.get(term);
        } else {
            return null;
        }
    }

    public int getTermCount() {
        // TO-DO: return the number of terms in the index.
        int count = mIndex.size();
        return count;
    }

    public String[] getDictionary() {
        // TO-DO: fill an array of Strings with all the keys from the hashtable.
        // Sort the array and return it.
        String[] list_terms;
        list_terms = mIndex.keySet().toArray(new String[mIndex.size()]);
        Arrays.sort(list_terms);
        return list_terms;
    }
}
