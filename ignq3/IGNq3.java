/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ignq3;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Syed
 */
public class IGNq3 {

    /**
     * @param args the command line arguments
     */
    
    private static double population;
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            System.out.println("Please enter population size: ");
       
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            
            String ps = in.readLine();
            
            population = Double.parseDouble(ps);
            
            population = Math.ceil(population);
            
            LicensePattern p = getPattern();
            
            System.out.println("Number of letters = " + p.getLetters());
            System.out.println("Number of numbers = " + p.getNumbers());
            System.out.println("Excess License Plates = " + (p.findNumCombinations() - population));
            
        } catch (IOException ex) {
            Logger.getLogger(IGNq3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.err.println("Incorrect double input");
            System.exit(-1);
        }
        
        
    }
    
    private static LicensePattern getPattern() {
        // call recursive method to add numbers or letters for pattern
        // Find the minimum excess pattern.
        
        LicensePattern min = new LicensePattern();
        
        return addValue(min);
    }
    
    private static LicensePattern addValue(LicensePattern p) {
        
        if(p.findNumCombinations() >= population) {
            return p;
        }
        
        LicensePattern a = new LicensePattern(p);
        LicensePattern b = new LicensePattern(p);
        
        a.addNumber();
        b.addLetter();
        return minExcess(addValue(a),addValue(b));
    }
    
    private static LicensePattern minExcess(LicensePattern a, LicensePattern b) {
        if((a.findNumCombinations() - population) >= (b.findNumCombinations() - population))
            return b;
        else return a;
    }
}
