/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;

/**
 *
 * @author Arif
 */
public class scoreclass {
     static int score=0;
   
   public static void someoneScored()
    {
      score++;
      
    }
  public static String getscore()
   {
      String numberAsString = Integer.toString(score);
      String str="SCORE: " + numberAsString;
      return str;  
   }
    
}
