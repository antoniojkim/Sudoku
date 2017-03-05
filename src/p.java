
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**

@author Antonio
*/
public class p {
    
    //Delay
    public static void delay (double time){
        try{
            Thread.sleep((long)Math.floor(time), (int)((time-Math.floor(time))*1000000));
            //System.out.println("Delayed "+(long)Math.floor(time)+"Milliseconds and "+(int)((time-Math.floor(time))*100000)+" Nanoseconds");
        }catch(InterruptedException e){}
    }
    
    //String functions
    public static int stringWidth(String string, Font f){
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        Font font = f;
        int width = (int)(font.getStringBounds(string, frc).getWidth());
        return width;
    }
    public static int stringHeight(String string, Font f){
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        Font font = f;
        int height = (int)(font.getStringBounds(string, frc).getHeight());
        return height;
    }
    
    //ToolKit
    public static double getScreenWidth(){
        return Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    }
    public static double getScreenHeight(){
        return Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    }
    public static int getFontSize(double old){
        return (int)(Main.w.getWindowDiagonal()/(1389.2443989449805/old));
    }
    public static int convertX(double old){
        return (int)(Main.w.width/(1200.0/old));
    }
    public static int convertY(double old){
        return (int)(Main.w.height/(700.0/old));
    }
    public static int convertScreenX(double old){
        return (int)(getScreenWidth()/(1200.0/old));
    }
    public static int convertScreenY(double old){
        return (int)(getScreenHeight()/(700.0/old));
    }
    
}
