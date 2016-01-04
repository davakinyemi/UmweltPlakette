import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;
import javax.swing.JPanel;

public class Zeichenflaeche extends JPanel {
	private static final long serialVersionUID = 1L;

	private Vector<Auto> elemente = new Vector<Auto>();
        private Vector<Baum> baeume   = new Vector<Baum>();

	private Graphics bufferImageGraphics;
	private Image bufferImage;

	private int breite, hoehe;

	public Zeichenflaeche(int breite, int hoehe) {
		this.breite = breite;
		this.hoehe  = hoehe;
	}

	public void add(Auto element) {
		this.elemente.add(element);
                this.add(element.getImgPanel()); // plakette von Auto in zeichenflache einfügen
	}

        public void add(Baum element){
                this.baeume.add(element);
        }
        
	public void remove(Auto element) {
		this.elemente.remove(element);
                this.remove(element.getImgPanel()); // plakette von Auto in zeichenflache löschen
	}

	@Override
	public void paintComponent(Graphics g) {
		if (bufferImage == null) {
			bufferImage         = this.createImage(this.breite, this.hoehe);
			bufferImageGraphics = bufferImage.getGraphics();
		}
                
		bufferImageGraphics.clearRect(0, 0, breite, hoehe);
                
                elemente.stream().forEach((element) -> {
                        element.zeichne(bufferImageGraphics);
                });
                
                baeume.stream().forEach((element) -> {
                        element.zeichne(bufferImageGraphics);
                });
                
		g.drawImage(bufferImage, 0, 0, this);
	}

	public void zeichne(Graphics g) {
		paint(g);
	}
        
        // beim spiel zürucksetzen, Autos von zeichenfläsche löschen
        public void clear(){
                elemente.stream().forEach((element) -> {
                        this.remove(element.getImgPanel());
                });

                elemente.clear();
        }
}