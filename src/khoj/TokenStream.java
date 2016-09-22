/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

public interface TokenStream {
   /**
    Returns the next token from the stream, or null if there is no token
    available.
     * @return 
    */
   String nextToken();

   /**
    Returns true if the stream has tokens remaining.
     * @return 
    */
   boolean hasNextToken();
}