import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.awt.BorderLayout;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionEvent;

// Beachte: AutoSpiel erbt von JFrame
public class UmweltplakettenSpiel extends JFrame {
	private static final long serialVersionUID = 1L;

	Zeichenflaeche zeichenflaeche;	// Zeichenfläche, auf der gezeichnet wird
	
	List<Auto> elemente = Collections.synchronizedList(new ArrayList<Auto>());

	private static final int FENSTER_BREITE = 1024 + 62; // 62 == buttonStart.getWidth()
	private static final int FENSTER_HOEHE = 730;
	private static final int FRAMERATE = 35;
        private int erstellungsFrequenz = 5000;
        private int count = 0;
	Timer timer = new Timer(1000 / FRAMERATE, null);		// Timer zur regelmäßigen Aktualisierung der Zeichenfläche
        Timer timer2;

	public UmweltplakettenSpiel()
	{
		// Zeichenfläche und Oberfläche initialisieren, bezieht sich auf die Funktionalität von JFrame
		super("Umweltplaketten-Spiel");
		setBounds(50, 50, FENSTER_BREITE, FENSTER_HOEHE);
		Container container = this.getContentPane();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Zeichenfläche anlegen und dem Fenster hinzufügen
		zeichenflaeche = new Zeichenflaeche(FENSTER_BREITE, FENSTER_HOEHE);
		container.add(zeichenflaeche);
                erzeugeBaeume(4);
		// Ein erstes Beispiel. Für das spätere Spiel nicht benötigt: 
		// Einen Neues-Auto-Button anlegen und dem Fenster anfügen
		//JButton buttonNeuesAuto = new JButton("Neues Auto");
                JButton buttonStart = new JButton("Start");
                getContentPane().add(buttonStart, BorderLayout.EAST);
		//getContentPane().add(buttonNeuesAuto, BorderLayout.SOUTH);
		/*buttonNeuesAuto.addActionListener((ActionEvent e) -> {
                    neuesAuto();
                });*/
                
                buttonStart.addActionListener((ActionEvent e) -> {
                    startSpiel();
                });
		
		// darstellen
		setVisible(true);

		// Ein timer-gesteuerter regelmäßiger Thread zur Aktualisierung der Zeichenfläche
		/*timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				loescheAutosRechts();
                                aktualisiereSzene();
                                zeichenflaeche.repaint();
			}
		}, 1000 / FRAMERATE, 1000 / FRAMERATE);*/
                
                timer.addActionListener((ActionEvent e) -> {
                    loescheAutosRechts();
                    aktualisiereSzene();
                    zeichenflaeche.repaint();
                });
                timer.start();
                
                this.setResizable(false);
	}
	
	// Hauptdialog anlegen
	public static void main(String arg[]) {
            new UmweltplakettenSpiel();
	}

	// Methode zum Erzeugen eines neuen Autos
	private void neuesAuto() {
		String name = "IL-SSE " + String.format("%03d", elemente.size() + 1);
		int posY = (int) (Math.random() * (FENSTER_HOEHE - 200));
		int geschwindigkeit = (int) (30 + Math.random() * 100); // 30..130 km/h
                int emission = (int) (1 + Math.random() * 3);
                int plkt = (int) (1 + Math.random() * 3);  
		Auto neuesAuto = new Auto(name, posY, geschwindigkeit, emission, plkt);
		elemente.add(neuesAuto);
		zeichenflaeche.add(neuesAuto);
                
	}
	
	private void erzeugeBaeume(int anzahl) {
	// Möglicher Startpunkt: Definieren Sie eine Klasse Baum
	// Orientieren Sie sich an der Klasse Auto (Image "baum.png")
	// Erzeugen Sie hier 0 bis 4 Baum-Objekte und fügen Sie sie der Zeichenfläche hinzu
            int posX, posY;
            for(int i = 0; i < anzahl; i++){
                posX = (int) (Math.random() * (FENSTER_BREITE - 152));
                posY = (int) (Math.random() * (FENSTER_HOEHE - 250));
                Baum baum = new Baum(posX, posY);
                zeichenflaeche.add(baum);
            }
	}
        
        // lösche auto beim erreichung von rechtes Rand
        private void loescheAutosRechts(){
            synchronized (elemente) {
					Iterator<Auto> it = elemente.iterator();
					while (it.hasNext()) {
						Auto element = it.next();
						element.updatePosition(FRAMERATE);
						if (element.getPosX() > FENSTER_BREITE) {
                                                        testEmission(element);
							it.remove();
							elemente.remove(element);
							zeichenflaeche.remove(element);
							System.out.println("Auto gelöscht " + element.getName());
						}
					}
				}
        }
        
        private void aktualisiereSzene(){
            synchronized(elemente){
                elemente.stream().forEach((auto) -> {
                    auto.updatePosition(FRAMERATE);
                });
            }
        }
        
        // vergleich zwischen emissions wert und plakette wert
        private boolean testEmission(Auto element){
            return element.testUmweltPlakette();
        }
        
        private void startSpiel(){
            /*timer2.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
                            neuesAuto();
                            count++;
                            if(count == 5){
                                erstellungsFrequenz -= 5000;
                                timer2.cancel();
                            }
			}
		}, 0, erstellungsFrequenz);*/
            timer2 = new Timer(erstellungsFrequenz, null);
            timer2.addActionListener((ActionEvent e) -> {
                    neuesAuto();
                    count++;
                    if(count == 5){
                        erstellungsFrequenz -= 1000;
                        count = 0;
                        if(erstellungsFrequenz <= 0){
                            erstellungsFrequenz = 1000;
                        }
                        timer2.setDelay(erstellungsFrequenz);
                    }
                });
                timer2.start();
        }
}