/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 *
 * @author dav
 */
public class Baum {
    private int posX;
    private int posY;
    private Image img;
    
    public Baum(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
        try {
            img = ImageIO.read(new File("baum.png"));
	} catch (IOException e) {
            System.out.println(e.toString());
            System.exit(0);
	}
    }
    
    public int getPosX(){
        return this.posX;
    } 
    public int getPosY(){
        return this.posY;
    }
    public void zeichne(Graphics g) {
		g.drawImage(img, this.posX, this.posY, null);
	}
}
