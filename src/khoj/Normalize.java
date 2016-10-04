/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author jass
 */
public class Normalize {

    /**
     * Returns token after normalization
     *
     * @param term String to be normalized
     * @return normalized token list
     */
    public final List<String> normalizeToken(final String term) {
        String token = term;
        String[] tokenSplits = null;
        List<String> tokenList = new ArrayList<>();
        token = token.toLowerCase();
        /**
         * removes leading non-alphanumeric characters
         */
        token = token.replaceAll("^[^a-zA-Z0-9\\s]*", "");
        /**
         * removes trailing non-alphanumeric characters
         */
        token = token.replaceAll("[^a-zA-Z0-9\\s]*$", "");
        /**
         * removed apostrophes(') from string
         */
        token = token.replaceAll("[']", "");
        /**
         * split token if hyphen(-) encountered
         */
        if (token.contains("-")) {
            tokenSplits = token.split("-");
            tokenList.addAll(Arrays.asList(tokenSplits));
        }
        tokenList.add(token);
        return tokenList;
    }
}
