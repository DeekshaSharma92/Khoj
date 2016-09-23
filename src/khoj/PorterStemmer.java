/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;
import java.util.regex.Pattern;

/**
 *
 * @author jass
 */
public class PorterStemmer {

    /**
     * fixed numbers.
     */
    public static final int THREE = 3;

    /**
     * a single consonant.
     */
    private static final String c = "[^aeiou]";

    /**
     * a single vowel.
     */
    private static final String v = "[aeiouy]";

    /**
     * a sequence of consonants. the second/third/etc consonant cannot
     * be 'y'
     */
    private static final String C = c + "[^aeiouy]*";

    /**
     * a sequence of vowels. the second/third/etc cannot be 'y'
     */
    private static final String V = v + "[aeiou]*";

    /**
     * this regex pattern tests if the token has measure > 0 [at least
     * one VC].
     */
    private static final Pattern mGr0 = Pattern.compile("^(" + C
        + ")?" + V + C);

    /**
     * add more Pattern variables for the following patterns: m equals
     * 1: token has measure == 1.
     */
    private static final Pattern mEq1 = Pattern.compile("^(" + C
        + ")?" + "(" + V + C + "){1}");

    /**
     * m greater than 1: token has measure > 1.
     */
    private static final Pattern mGr1 = Pattern.compile("^(" + C
        + ")?" + "(" + V + C + "){2,}");
    /**
     * vowel: token has a vowel after the first (optional) C.
     */
    private static final Pattern vAc = Pattern.compile("^(" + C
        + ")?" + V + "[" + V + C + "]*");
    /**
     * double consonant: token ends in two consonants that are the
     * same, unless they are L, S, or Z. (look up "backreferencing" to
     * help with this).
     */
    private static final Pattern dC = Pattern.compile("^(" + C
        + ")?" + "[" + V + C + "]*" + "([^lszaeiou])\\2$");
    /**
     * m equals 1, cvc: token is in Cvc form, where the last c is not
     * w, x, or y.
     */
    private static final Pattern cVc = Pattern.compile("^(" + C
        + ")?" + "[" + V + C + "]*" + "(" + c + v + "[^aeiouwxy])$");
    /**
     * vowel sandwiched by any other character.
     */
    private static final Pattern sVs = Pattern.compile("[" + V + C
        + "]*" + v + "[" + V + C + "]*");

    /**
     *
     * @param token
     * @return return processed token
     */
    public static String processToken(String token) {
        if (token.length() < THREE) {
            return token; // token must be at least 3 chars
        }
        // step 1a
        // step 1a.1
        if (token.endsWith("sses")) {
            token = token.substring(0, token.length() - 2);

        } else if (token.endsWith("ies")) {
            //step 1a.2
            token = token.substring(0, token.length() - 2);

        } else if (token.endsWith("ss")) {
            //do nothing
        } else if (!token.endsWith("ss") && token.endsWith("s")) {
            // note that Step 1a.3 implies that there is only a
            //single 's' as the suffix; ss does not count. you may
            //need a regex pattern here for "not s followed by s".
            //step 1a.4
            token = token.substring(0, token.length() - 1);

        }

        // step 1b
        boolean doStep1bb = false;
        // step 1b
        if (token.endsWith("eed")) { // 1b.1
            // token.substring(0, token.length() - 3) is the stem
            //prior to "eed".
            // if that has m>0, then remove the "d".
            String stem = token.substring(0, token.length() - THREE);
            if (mGr0.matcher(stem).find()) { // if the pattern matches the stem
                token = stem + "ee";
            }
        } else if (token.endsWith("ed")) {
            // program the rest of 1b. set the boolean doStep1bb
        //to true if Step 1b* should be performed.
            String stem = token.substring(0, token.length() - 2);

            if (sVs.matcher(stem).find()) {
                doStep1bb = true;
                token = stem;
            }
        } else if (token.endsWith("ing")) {
            String stem = token.substring(0, token.length() - THREE);

            if (sVs.matcher(stem).find()) {
                doStep1bb = true;
                token = stem;
            }
        }

        // step 1b*, only if the 1b.2 or 1b.3 were performed.
        if (doStep1bb) {
            if (token.endsWith("at") || token.endsWith("bl")
                || token.endsWith("iz")) {
                token = token + "e";

            } else if (dC.matcher(token).find()) {
                token = token.substring(0, token.length() - 1);

            } else if (mEq1.matcher(token).find()
                && cVc.matcher(token).find()) {
                token = token + "e";

            }

            // use the regex patterns you wrote for 1b*.4 and 1b*.5
        }

        // step 1c
        // program this step. test the suffix of 'y' first,
        //then test the condition *v* on the stem.
        if (token.endsWith("y")) {
            String stem = token.substring(0, token.length() - 1);
            if (sVs.matcher(stem).find()) {
                token = stem + "i";

            }

        }
        // step 2
        // program this step. for each suffix, see if the
        //token ends in the suffix.
        //    * if it does, extract the stem, and do NOT test any other suffix.
        //    * take the stem and make sure it has m > 0.
        //        * if it does, complete the step and do not test any others.
        //          if it does not, attempt the next suffix.
        // you may want to write a helper method for this. a matrix of
        // "suffix"/"replacement" pairs might be helpful. It could look like
        // string[][] step2pairs = {  new string[] {"ational", "ate"},

        String[][] step2pairs = {new String[]{"ational", "ate"},
        new String[]{"tional", "tion"}, new String[]{"enci", "ence"},
        new String[]{"izer", "ize"}, new String[]{"abli", "able"},
        new String[]{"alli", "al"}, new String[]{"entli", "ent"},
        new String[]{"eli", "e"}, new String[]{"ousli", "ous"},
        new String[]{"ization", "ize"}, new String[]{"ation", "ate"},
        new String[]{"ator", "ate"}, new String[]{"alism", "al"},
        new String[]{"iveness", "ive"}, new String[]{"fulness", "ful"},
        new String[]{"ousness", "ous"}, new String[]{"aliti", "al"},
        new String[]{"iviti", "ive"}, new String[]{"biliti", "ble"},
        new String[]{"anci", "ance"}
        };
        token = suffixReplacementHelper(token, mGr0, step2pairs);

        // step 3
        // program this step. the rules are identical to step 2 and you can use
        // the same helper method. you may also want a matrix here.
        String[][] step3pairs = {new String[]{"icate", "ic"},
        new String[]{"ative", ""}, new String[]{"alize", "al"},
        new String[]{"iciti", "ic"}, new String[]{"ical", "ic"},
        new String[]{"ful", ""}, new String[]{"ness", ""}
        };

        token = suffixReplacementHelper(token, mGr0, step3pairs);
        // step 4
        // program this step similar to step 2/3, except now the stem must have
        // measure > 1.
        String[][] step4pairs = {new String[]{"al", ""},
            new String[]{"ance", ""}, new String[]{"ence", ""},
            new String[]{"er", ""}, new String[]{"ic", ""},
            new String[]{"able", ""}, new String[]{"ible", ""},
            new String[]{"ant", ""}, new String[]{"ement", ""},
            new String[]{"ment", ""}, new String[]{"ent", ""},
            new String[]{"sion", ""}, new String[]{"tion", ""},
            new String[]{"ou", ""}, new String[]{"ism", ""},
            new String[]{"ate", ""}, new String[]{"iti", ""},
            new String[]{"ous", ""}, new String[]{"ive", ""},
            new String[]{"ize", ""}
        };
        token = suffixReplacementHelper(token, mGr1, step4pairs);

        // note that ION should only be removed if the suffix is SION or TION,
        // which would leave the S or T.
        // as before, if one suffix matches, do not try any others even if the
        // stem does not have measure > 1.
        // step 5
        // program this step. you have a regex for m=1 and for "Cvc", which
        // you can use to see if m=1 and NOT Cvc.
        if (mGr1.matcher(token).find() && token.endsWith("e")) {
            token = token.substring(0, token.length() - 1);
        } else if (mEq1.matcher(token).find()
            && !cVc.matcher(token).find() && token.endsWith("e")) {
            token = token.substring(0, token.length() - 1);
        }
        // all your code should change the variable token, which represents
        // the stemmed term for the token.
        if (mGr1.matcher(token).find() && dC.matcher(token).find()
            && token.endsWith("ll")) {
            token = token.substring(0, token.length() - 1);
        }
        return token;
    }
    /**
     *
     * @param token
     * @param m
     * @param pairs
     * @return
     */
    private static String suffixReplacementHelper(String token,
        final Pattern m, String[][] pairs) {

        for (String[] pair : pairs) {
            if (token.endsWith(pair[0])) {

                String stem = token.substring(0, token.length()
                    - pair[0].length());
                if (m.matcher(stem).find()) {
                    token = stem + pair[1];
                    break;

                }
            }
        }
        return token;
    }

}
