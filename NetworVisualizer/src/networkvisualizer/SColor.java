/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.Color;

/**
 *
 * @author matthiaskeim
 */
public class SColor {
    
    public static Color getColorFromHex(String hex) {
        int[] rgb = getRGBOfHex(hex);
        return new Color(rgb[0], rgb[1], rgb[2]);
    }
    
    public static String hexFromColor(Color c) {
        return getHexColorOfRGB(c.getRed(), c.getGreen(), c.getBlue());
    }
    
    
    public static int[] getRGBOfHex(String hex) {
        int[] rgb = new int[3];
        rgb[0] = hexToDec(hex.substring(0, 2));
        rgb[1] = hexToDec(hex.substring(2, 4));
        rgb[2] = hexToDec(hex.substring(4, 6));
        
        return rgb;
        
    }
    
    public static String getHexColorOfRGB(int r, int g, int b) {
        String hexR = decToHex(r);
        String hexG = decToHex(g);
        String hexB = decToHex(b);
        System.out.println(hexR);
        
        
        return hexR+hexG+hexB;
    }
    
    private static String decToHex(int dec) {
        
        return String.format("%02X", dec);
    }
    
    private static int hexToDec(String hex) {
        return Integer.valueOf(hex, 16);
    }
}
