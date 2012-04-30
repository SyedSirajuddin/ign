/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ignq3;

/**
 *
 * @author Syed
 */
public class LicensePattern{
    
    final private static int DIGITS = 10;
    final private static int ALPHABET = 26;
    
    private int numNumbers = 0;
    private int numLetters = 0;
    
    public LicensePattern() {
        
    }
    
    public LicensePattern(LicensePattern p) {
        numNumbers = p.getNumbers();
        numLetters = p.getLetters();
    }
    public void setNumbers(int numbers) {
        this.numNumbers = numbers;
    }
    
    public void setLetters(int letters) {
        this.numLetters = letters;
    }
    
    public int getNumbers() {
        return this.numNumbers;
    }
    
    public int getLetters() {
        return this.numLetters;
    }
    
    public void addNumber() {
        numNumbers++;
    }
    public void addLetter() {
        numLetters++;
    }
    public double findNumCombinations() {
        double x =  Math.pow(DIGITS, numNumbers)*Math.pow(ALPHABET, numLetters);
        //System.out.println("NumComb = " + x);
        return x;
 
    }
    
    public String toString() {
        return "\nNumber of digits = " + numNumbers + "\nNumber of letters = " + numLetters +"\n";
    }
}
