import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

enum Emission {

    HOCH, MITTEL, NIEDRIG
}

public class Auto {

    private String name;
    private Color farbe;
    private int posX, posY, geschwindigkeit, emWidth;
    private Emission emission;
    private Image img = null;
    private Image em; // emission
    private Image plakette;
    private int plakWert;

    public Auto(String name, int posY, int geschw, int emission, int plak) {
        this.emission = Emission.values()[emission];
        this.name = name;
        this.posX = -200;
        this.posY = posY;
        this.geschwindigkeit = geschw;
        this.farbe = new Color((int) (Math.random() * 255.0), (int) (Math.random() * 255.0), (int) (Math.random() * 255.0));
        this.plakWert = plak;

        try {
            img = ImageIO.read(new File("auto.png"));
        } catch (IOException e) {
            System.out.println(e.toString());
            System.exit(0);
        }
        //System.out.println("Auto erzeugt " + this.name);
        setEmmision(this.emission);
        setPlakette(plakWert);
    }

    public String getName() {
        return this.name;
    }

    public Image getImg() {
        return img;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public int getGeschwindigkeit() {
        return this.geschwindigkeit;
    }

    ;
        
        private void setPlakette(int plkt) {
        try {
            switch (plkt) {
                case 0:
                    plakette = ImageIO.read(new File("plakette2.png"));
                    break;
                case 1:
                    plakette = ImageIO.read(new File("plakette3.png"));
                    break;
                case 2:
                    plakette = ImageIO.read(new File("plakette4.png"));
                    break;
            }
        } catch (IOException e) {
            System.out.println(e.toString());
            System.exit(0);
        }
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
        g.drawImage(plakette, this.posX + 77, this.posY + 62, 40, 40, null);
    }

    private void setEmmision(Emission emi) {
        try {
            switch (emi) {
                case NIEDRIG:
                    em = ImageIO.read(new File("abgas2.png"));
                    break;
                case MITTEL:
                    em = ImageIO.read(new File("abgas3.png"));
                    break;
                case HOCH:
                    em = ImageIO.read(new File("abgas4.png"));
                    break;
            }
        } catch (IOException e) {
            System.out.println(e.toString());
            System.exit(0);
        }
        emWidth = em.getWidth(null);
        //System.out.println("DIM: " + em.getWidth(null) + ", " + em.getHeight(null));
    }

    public void click() {
        plakWert++;
        if (plakWert > 2) {
            plakWert = 0;
        }
        setPlakette(plakWert);
    }

    public boolean testUmweltPlakette() {
        System.out.println(emission + ", " + plakWert);
        if (emission == Emission.HOCH && plakWert == 0) {
            return true;
        } else if (emission == Emission.MITTEL && plakWert == 1) {
            return true;
        } else if (emission == Emission.NIEDRIG && plakWert == 2) {
            return true;
        }
        return false;
    }
}
