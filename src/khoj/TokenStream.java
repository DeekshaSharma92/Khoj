/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

/**
 *
 * @author jass
 */
public interface TokenStream {
   /**
    Returns the next token from the stream, or null if there is no token
    available.
     * @return next available token.
    */
   String nextToken();

   /**
    *
    * @return Returns true if the stream has tokens remaining.
    */
   boolean hasNextToken();
}