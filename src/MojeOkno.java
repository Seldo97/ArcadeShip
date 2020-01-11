import java.awt.*;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
//import sun.audio.*;

public class MojeOkno extends JFrame implements ActionListener, KeyListener  {


    private PanelGraficzny panel;
    private Dane dane = new Dane();
    public static Sound theme = new Sound("pliki/muzyka1.wav");
    private JPanel przyciski = new JPanel(new GridLayout(20, 1));
    private JButton test = new JButton("START");
    private JButton pause = new JButton("PAUZA");
    private JButton nowaGra = new JButton("NOWA GRA");
    public static JLabel score = new JLabel("0");
    public static JLabel highestScore = new JLabel("0");

    private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    public boolean pauza = false;
    public static boolean przegrana = false;

    public static boolean pressedRIGHT = false;
    public static boolean pressedLEFT= false;
    public static boolean pressedUP = false;
    public static boolean pressedDOWN = false;

    public MojeOkno() {

        //wywolanie konstruktora klasy nadrzednej (JFrame)
        super("ArcadeShip 0.8v by Marcin Olek");

        //ustawienie standardowej akcji po naciśnięciu przycisku zamkniecia
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new PanelGraficzny(850, 850);


        setFocusable(true);
        requestFocus();
        addKeyListener(this);


        test.setBackground(Color.black);
        test.setForeground(Color.WHITE);
        test.setFocusPainted(false);
        test.setFont(new Font("Tahoma", Font.BOLD, 12));

        pause.setBackground(Color.black);
        pause.setForeground(Color.WHITE);
        pause.setFocusPainted(false);
        pause.setFont(new Font("Tahoma", Font.BOLD, 12));

        nowaGra.setBackground(Color.black);
        nowaGra.setForeground(Color.WHITE);
        nowaGra.setFocusPainted(false);
        nowaGra.setFont(new Font("Tahoma", Font.BOLD, 12));


        przyciski.add(test);
        przyciski.add(pause);
        przyciski.add(nowaGra);
        przyciski.add(highestScore);
        przyciski.setBackground(Color.BLACK);

        score.setForeground(Color.green);
        highestScore.setForeground(Color.red);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        //add(scrollPane, BorderLayout.EAST);
        add(score, BorderLayout.SOUTH);
        //add(highestScore, BorderLayout.SOUTH);
        add(przyciski, BorderLayout.WEST);


        pack();
        //wysrodkowanie okna na ekranie
        setLocationRelativeTo(null);
        //wyswietlenie naszej ramki
        this.getContentPane().setBackground(Color.BLACK);
        setVisible(true);
        nasluchuj();

        panel.trasa();
        panel.pojazdHitBoxes();
        //panel.obiekty();

        try {
            panel.odczyt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        highestScore.setText(Integer.toString(panel.highestScore));

        repaint();
    }

    public void nasluchuj(){
       test.addActionListener(this);
       pause.addActionListener(this);
       nowaGra.addActionListener(this);

    }

    public void actionPerformed(ActionEvent e)
    {
        Object zrodlo = e.getSource();
        if(zrodlo == test){
            pauza = false;
            theme.loop();
            //panel.rysujProste();
            panel.pojazdHitBoxes();
            panel.timerTrasa.start();
            requestFocus();
        }
        if(zrodlo == pause){
            pauza = true;
            theme.stop();
            panel.timerTrasa.stop();
        }
        if(zrodlo == nowaGra){
            pressedRIGHT = false;
            pressedLEFT= false;
            pressedUP = false;
            pressedDOWN = false;
            pauza = false;
            theme.stop();
            theme.loop();
            panel.nowaGra();
            requestFocus();
        }
    }



    @Override
    public void keyPressed(KeyEvent evt) {
        //int key = evt.getKeyCode();

        if(evt.getKeyCode() == KeyEvent.VK_LEFT){
            pressedLEFT = true;
            //panel.setPojazdX(-7);
            //System.out.println("Lewy klawisz");
        }
        if(evt.getKeyCode() == KeyEvent.VK_RIGHT){
            pressedRIGHT = true;
            //if(pressedRIGHT)
              //  panel.setPojazdX(7);

            //System.out.println("Prawy klawisz");
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            pressedUP = true;
            //panel.setPojazdY(-7);
            //System.out.println("górny klawisz");
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            pressedDOWN = true;
            //panel.setPojazdY(7);
            //System.out.println("dolny klawisz");
        }
        if(evt.getKeyCode() == KeyEvent.VK_P){
            pauza = true;
            theme.stop();
            panel.timerTrasa.stop();
        }

    }

    @Override
    public void keyReleased(KeyEvent evt) {

        if(evt.getKeyCode() == KeyEvent.VK_LEFT){
            pressedLEFT = false;
            //panel.setPojazdX(-7);
            //System.out.println("Lewy klawisz");
        }
        if(evt.getKeyCode() == KeyEvent.VK_RIGHT){
            pressedRIGHT = false;
            //if(pressedRIGHT)
            //  panel.setPojazdX(7);

            //System.out.println("Prawy klawisz");
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            pressedUP = false;
            //panel.setPojazdY(-7);
            //System.out.println("górny klawisz");
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            pressedDOWN = false;
            //panel.setPojazdY(7);
            //System.out.println("dolny klawisz");
        }


    }

    @Override
    public void keyTyped(KeyEvent evt) {
    }




}
