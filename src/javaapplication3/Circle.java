/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;

import java.awt.Rectangle;

 public class Circle
{
    int x,y;
    boolean right=false;
    Circle(int a,int b)
    {
        x=a;
        y=b;
       
    }
    public void   move()
     {
        
         if(y==500){
             right= false;
         }
         else{
             right=true;
         }
         
         if(right==true){
             x-=01;
             y-=10;
         }
             
     }
    public Rectangle bounds()
    {
        return (new Rectangle(x,y,20,20) );
    }
}