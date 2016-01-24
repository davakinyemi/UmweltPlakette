import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.awt.BorderLayout;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Beachte: AutoSpiel erbt von JFrame
public class UmweltplakettenSpiel extends JFrame {

    private static final long serialVersionUID = 1L;

    Zeichenflaeche zeichenflaeche;  // Zeichenfläche, auf der gezeichnet wird

    List<Auto> elemente = Collections.synchronizedList(new ArrayList<Auto>());

    private static final int FENSTER_BREITE = 1024;
    private static final int FENSTER_HOEHE = 730;
    private static final int FRAMERATE = 35;

    private int erstellungsFrequenz = 5000;
    private int leben = 10;
    private int richtig = 0;
    private int zaehler = 0;
    private int geschwindigkeitsFreq = 0;
    private int baeume = 4;

    private Timer timer = new Timer(1000 / FRAMERATE, null); // Timer zur regelmäßigen Aktualisierung der Zeichenfläche
    private Timer timer2; // Timer zur erstelleng von neue Autos;

    private JButton buttonStart;
    private JTextField textField;

    public UmweltplakettenSpiel() {
        // Zeichenfläche und Oberfläche initialisieren, bezieht sich auf die Funktionalität von JFrame
        super("Umweltplaketten-Spiel");
        setBounds(50, 50, FENSTER_BREITE, FENSTER_HOEHE);
        Container container = this.getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Zeichenfläche anlegen und dem Fenster hinzufügen
        zeichenflaeche = new Zeichenflaeche(FENSTER_BREITE, FENSTER_HOEHE);
        container.add(zeichenflaeche);

        zeichenflaeche.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                KlickBeiKoord(e.getX(), e.getY());
            }
        });

        erzeugeBaeume(baeume);
        buttonStart = new JButton("Start"); // button fürs spiel start
        getContentPane().add(buttonStart, BorderLayout.SOUTH);

        buttonStart.addActionListener((ActionEvent e) -> {
            neuesAuto();
            startSpiel();
        });

        textField = new JTextField();
        setTextField();
        textField.setEditable(false);
        getContentPane().add(textField, BorderLayout.NORTH);

        // darstellen
        setVisible(true);

        // Ein timer-gesteuerter regelmäßiger Thread zur Aktualisierung der Zeichenfläche                
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
        int geschwindigkeit = (int) (30 + Math.random() * geschwindigkeitsFreq); // 30..130 km/h
        int emission = (int) (0 + Math.random() * 3);
        int plkt = (int) (0 + Math.random() * 3);
        //System.out.println("Em: " + emission + ", Plak: " + plkt);
        Auto neuesAuto = new Auto(name, posY, geschwindigkeit, emission, plkt);

        elemente.add(neuesAuto);
        zeichenflaeche.add(neuesAuto);
    }

    private void erzeugeBaeume(int anzahl) {
        // Möglicher Startpunkt: Definieren Sie eine Klasse Baum
        // Orientieren Sie sich an der Klasse Auto (Image "baum.png")
        // Erzeugen Sie hier 0 bis 4 Baum-Objekte und fügen Sie sie der Zeichenfläche hinzu
        int posX, posY;
        for (int i = 0; i < anzahl; i++) {
            posX = (int) (Math.random() * (FENSTER_BREITE - 152));
            posY = (int) (Math.random() * (FENSTER_HOEHE - 250));
            Baum baum = new Baum(posX, posY);
            zeichenflaeche.add(baum);
        }
    }

    // lösche auto beim erreichung von rechtes Rand
    private void loescheAutosRechts() {
        synchronized (elemente) {
            Iterator<Auto> it = elemente.iterator();
            while (it.hasNext()) {
                Auto element = it.next();
                element.updatePosition(FRAMERATE);
                if (element.getPosX() > FENSTER_BREITE) {
                    testEmission(element);
                    if (elemente.isEmpty()) { // prüfen ob elemente leer ist, um "ConcurrentModificationException" zu vermeiden
                        return;
                    }
                    it.remove();
                    elemente.remove(element);
                    zeichenflaeche.remove(element);
                    System.out.println("Auto gelöscht " + element.getName());
                }
            }
        }
    }

    private void aktualisiereSzene() {
        synchronized (elemente) {
            elemente.stream().forEach((auto) -> {
                auto.updatePosition(FRAMERATE);
            });
        }
    }

    // vergleich zwischen emissions wert und plakette wert
    private void testEmission(Auto element) {
        if (element.testUmweltPlakette()) {
            richtig++;
        } else {
            leben--;
        }
        setTextField();
        if (leben == 0) {
            timer2.stop();
            zeigeErgebnisse();
            resetSpiel();
        }
    }

    // spiel start
    private void startSpiel() {
        setTextField();
        timer2 = new Timer(erstellungsFrequenz, null);
        timer2.addActionListener((ActionEvent e) -> {
            neuesAuto();
            erhoeheSchwierigkeit();
        });
        timer2.start();
        buttonStart.setEnabled(false);
    }

    private void erhoeheSchwierigkeit() {
        zaehler++;
        if (zaehler == 5) { // beim erstellung von 5 Autos, schwierigkeit erhöhern 
            erstellungsFrequenz -= 1000;
            geschwindigkeitsFreq += 25;
            zaehler = 0;
            if (erstellungsFrequenz <= 0) {
                erstellungsFrequenz = 1000;
            }
            if (geschwindigkeitsFreq > 100) {
                geschwindigkeitsFreq = 100;
            }
            timer2.setDelay(erstellungsFrequenz);
        }
    }

    // ergebnisse zeigen
    private void zeigeErgebnisse() {
        String result = richtig + " richtig.";
        System.out.println(result);
        JOptionPane.showMessageDialog(new JFrame(), result, "Gameover", 2);
    }

    // spiel zürucksetzen
    private void resetSpiel() {
        zaehler = 0;
        erstellungsFrequenz = 5000;
        geschwindigkeitsFreq = 0;
        richtig = 0;
        leben = 10;
        elemente.clear();
        zeichenflaeche.clear();
        buttonStart.setEnabled(true);
    }

    private void setTextField() {
        String text = "Richtig: " + richtig + " Leben: " + leben;
        textField.setText(text);
    }

    // Reaktion auf Mausklick
    private void KlickBeiKoord(int klickX, int klickY) {
        System.out.println("Mausklick bei: (" + klickX + "," + klickY + ")");
        synchronized (elemente) {
            elemente.stream().forEach((auto) -> {
                if ((klickX >= auto.getPosX() && klickX <= auto.getPosX() + auto.getImg().getWidth(null)) && (klickY >= auto.getPosY() && klickY <= auto.getPosY() + auto.getImg().getHeight(null))) {
                    auto.click();
                }
            });
        }
    }
}
