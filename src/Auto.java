import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

// plakette klasse
class ImgPanel extends JPanel{
        private BufferedImage img;
        int i; // bild wert
        boolean first;
        boolean mouseClicked = false;
        
        public ImgPanel(int plkt){
                Dimension size = new Dimension(40, 40);
                setPreferredSize(size);
                setMinimumSize(size);
                setMaximumSize(size);
                setSize(size);
                setLayout(null);
                imageEinstellen(plkt);
                this.setOpaque(false);
                i              = plkt;
                first          = true;
                addMouseListener(new MouseAdapter() {               
                        @Override
                        public void mousePressed(MouseEvent e) {
                                if(first == true){
                                        i++;
                                }
                                if(i > 3){
                                        i = 1;
                                }
                                
                                imageEinstellen(i++);
                                repaintPanel();  
                                mouseClicked = true;
                        }
                });
        }

        private void imageEinstellen(int plkt){               
                try{
                        switch(plkt){
                                case 1:
                                        img = ImageIO.read(new File("plakette2.png"));
                                        break;
                                case 2:
                                        img = ImageIO.read(new File("plakette3.png"));
                                        break;
                                case 3:
                                        img = ImageIO.read(new File("plakette4.png"));
                                        break;
                        }
                }catch(IOException e){
                        System.out.println(e.toString());
                        System.exit(0);
                }
        }

        @Override
        protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(img, 0, 0, 39, 39, null);
        }
        
        private void repaintPanel(){
                this.repaint();
                first = false;
        }

        public int getImageWert(){
                if(mouseClicked){
                        i--;
                }
                return i;        
        }
}

public class Auto {
	private String name;
	private Color farbe;
	private int posX, posY, geschwindigkeit, emWidth, emWert;
	private Image img = null;
        private Image em; // emission
        private ImgPanel imgPanel;

	public Auto(String name, int posY, int geschw, int emission, int plak) {
		this.name            = name;
		this.posX            = -200;
		this.posY            = posY;
		this.geschwindigkeit = geschw;
		this.farbe           = new Color((int) (Math.random() * 255.0), (int) (Math.random() * 255.0), (int) (Math.random() * 255.0));
                emWert               = emission;
                
		try {
			img = ImageIO.read(new File("auto.png"));
		} catch (IOException e) {
			System.out.println(e.toString());
			System.exit(0);
		}
		//System.out.println("Auto erzeugt " + this.name);
                setEmmision(emission);
                setPlakette(plak);
	}
	
	public String getName() {
		return this.name;
	}
	
        public ImgPanel getImgPanel(){
                return imgPanel;
        }
        
	public int getPosX() {
		return this.posX;
	}

	public int getPosY() {
		return this.posY;
	}

	public int getGeschwindigkeit() {
		return this.geschwindigkeit;
	};
        
        private void setPlakette(int plkt){
            imgPanel = new ImgPanel(plkt);
            
        }
        
	public void updatePosition(int framerate) {
		int posXAenderung = (int) (1.0 / framerate * (400.0 * this.geschwindigkeit / 130.0)); // 130km/h --> 400px/s
		this.posX = this.posX + posXAenderung;
		//System.out.println("Auto " + this.name + " bei (" + this.posX + "," + this.posY + ")");
	}

	public void zeichne(Graphics g) {
		g.drawImage(img, this.posX, this.posY, null);
		g.setColor(this.farbe);
		g.fillOval(this.posX + 77, this.posY + 62, 40, 40);
		g.setFont(new Font("Dialog", Font.BOLD, 18));
		g.setColor(Color.black);
		g.drawString(this.name, this.posX + 50, this.posY + 145);
                g.drawImage(em, this.posX - emWidth, this.posY + 62, null);                  
                imgPanel.setLocation(this.posX + 77, this.posY + 62);
                //g.drawImage(plakette, this.posX + 77, this.posY + 62, 40, 40, null);
	}
        
        private void setEmmision(int emission){
            try{
                switch(emission){
                    case 1:
                        em = ImageIO.read(new File("abgas2.png"));
                        emWidth = 55;
                        break;
                    case 2:
                        em = ImageIO.read(new File("abgas3.png"));
                        emWidth = 113;
                        break;
                    case 3:
                        em = ImageIO.read(new File("abgas4.png"));
                        emWidth = 189;
                        break;
                }
            }catch(IOException e){
                System.out.println(e.toString());
		System.exit(0);
            }
        }
        
        public boolean testUmweltPlakette(){
                int emissionVal = emWert, plaketteVal = imgPanel.getImageWert();
                System.out.println("Emission: " + emissionVal + "\n" + "Plakatte: " + plaketteVal);
                System.out.println(emissionVal == plaketteVal);
                System.out.println();
                return emWert == imgPanel.getImageWert();
        }
}