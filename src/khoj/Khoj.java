/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoj;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jass
 */
public class Khoj {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException throws Input Output Exception
     */
    public static void main(final String[] args) throws IOException {
        // TODO code application logic here
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new SearchEngineUI().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Khoj.class.getName())
                        .log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
