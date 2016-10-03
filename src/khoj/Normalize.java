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

    public List<String> NormalizeToken(String token) {
        String[] tokenSplits = null;
        List<String> tokenList = new ArrayList<>();
        token = token.toLowerCase();
        token = token.replaceAll("^[^a-zA-Z0-9\\s]*", "");
        token = token.replaceAll("[^a-zA-Z0-9\\s]*$", "");
        token = token.replaceAll("[']", "");
        if (token.contains("-")) {
            tokenSplits = token.split("-");
            tokenList.addAll(Arrays.asList(tokenSplits));
        }
        tokenList.add(token);
        return tokenList;
    }
}
