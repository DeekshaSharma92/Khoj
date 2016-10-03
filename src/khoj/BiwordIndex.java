/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import com.google.gson.Gson;
import java.util.*;
import java.io.*;

/**
 *
 * @author deeksha
 */
public class BiwordIndex  {    
    String query;
    
    public void tokenize(final File file, int documentID) throws IOException{
        
        
        int i=0;
        String bwphrase;
        List<String> biwordList = new ArrayList<String>();
        SimpleTokenStream streamFile = new SimpleTokenStream(file);
        Gson gson = new Gson();
        Article article = gson.fromJson(streamFile.reader, Article.class);
        String query = streamFile.readFile(article);
        SimpleTokenStream streamText = new SimpleTokenStream(query);
    
    while(streamText.hasNextToken()){
        
        String text = streamText.nextToken();
        if(biwordList.size()==2)
        {
            bwphrase= biwordList.get(0) + " " + biwordList.get(1);
            compose(bwphrase,documentID);  
            biwordList.remove(0);
            biwordList.add(text);
        }
        else{
            biwordList.add(text);
        }
        
    }
    }
    Map<String, List<Integer>> biwordMap = new HashMap<>();
    
    public void compose(String term, int documentID){
        String biwordPhrase = term;
        List<Integer> phrasedocID= new ArrayList<>();
        if(!biwordMap.containsKey(biwordPhrase))
        {
            
            phrasedocID.add(documentID);
            biwordMap.put(biwordPhrase, phrasedocID);
        }
        else
        {
            phrasedocID= biwordMap.get(biwordPhrase);
            if(phrasedocID.get(phrasedocID.size()-1) != documentID)
            {
                phrasedocID.add(documentID);
                biwordMap.put(biwordPhrase, phrasedocID);
            }
            
        }  
       
    }
    }
    
    
    
 
   