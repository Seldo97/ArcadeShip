import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.applet.Applet;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class PanelGraficzny extends JPanel{

    //obiekt do przechowywania grafiki
    BufferedImage plotno;
    BufferedImage tlo[] = new BufferedImage[5];
    Dane dane;
    Graphics2D g; // (Graphics2D) plotno.getGraphics();
    Timer timerTrasa;
    MojeOkno okno;

    //JLabel wynik = new JLabel("Twój Stary");


    private int pojazdX = 415;
    private int pojazdY = 770;
    private int shipX = pojazdX;
    private int shipY = pojazdY;
    private boolean czyGMD = false;
    private boolean flaga = true;
    private int looseX;
    private int looseY;

    public static int highestScore;

    private ImageIcon ikona;
    private int x, y;
    private Point point = new Point(x, y);
    private int shipWidth;
    private int shipHeight;
    private int shipPix[][];
    private RoundRectangle2D.Double obiekt [] = new RoundRectangle2D.Double[15];
    private Sound boom = new Sound("pliki/boom.wav");
    private ImageIcon bg;
    TexturePaint tp;

    public PanelGraficzny(int szer, int wys)
    {
        super();
        dane = new Dane();
        ustawRozmiar(new Dimension(szer,wys));
        ikona = new ImageIcon("pliki/rsz_ship2.png");

        shipHeight = ikona.getIconHeight();
        shipWidth = ikona.getIconWidth();
        shipPix = new int[shipHeight][shipWidth];


        for(int i = 0; i < 15; i++) {
            obiekt[i] = new RoundRectangle2D.Double();
        }
        losujObiekty();
        ustawTlo();
        wyczysc();
        //pack();
        //ustawTlo();

    }

    public void ustawRozmiar(Dimension r)
    {
        //przygotowanie płótna
        plotno = new BufferedImage((int)r.getWidth(), (int)r.getHeight(), BufferedImage.TYPE_INT_RGB);
        setPreferredSize(r);
    }

    public void wyczysc()
    {
        g = (Graphics2D) plotno.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, plotno.getWidth(), plotno.getHeight());
        setBorder(BorderFactory.createLineBorder(Color.black));
        //ustawTlo();
        //tlo.getGraphics();
        repaint();

    }

    public void ustawTlo(){
        File plik[] = new File[5];
        plik[0] = new File("pliki/text1.jpg");
        plik[1] = new File("pliki/text2.jpg");
        plik[2] = new File("pliki/text3.jpg");
        plik[3] = new File("pliki/text4.jpg");
        plik[4] = new File("pliki/text5.jpg");

        try{
            for(int i = 0; i < tlo.length; i++)
                tlo[i] = ImageIO.read(plik[i]);
            //repaint();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null,"Blad odczytu");
            e.printStackTrace();
        }
    }


    public void rysujTrase( int i ){
        Graphics2D g2d = (Graphics2D) plotno.getGraphics();
        g2d.setColor(Color.white);

        float[] dashingPattern2 = {3f, 9f};
        Stroke stroke2 = new BasicStroke(2.5f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dashingPattern2, 0.0f);

        g2d.setStroke(stroke2);
        //g2d.drawLine(1, 1, 200, 200);
        g2d.drawLine(600, -950+i, 600, 850+i);
        g2d.drawLine(250, -950+i, 250, 850+i);
        //g2d.fillOval(300+i,300+i,5,5);

    }

    public void GMD(){
        for (int q = 0; q < shipHeight; q++)
            for (int p = 1; p < shipWidth; p++) {
                for (int o = 0; o <= 15 - 1; o++) {
                    if (shipPix[q][p] > obiekt[o].getX() && shipPix[q][p] < obiekt[o].getX() + obiekt[o].width
                            && shipPix[q][0] > obiekt[o].getY() && shipPix[q][0] < obiekt[o].getY() + obiekt[o].height) {
                        czyGMD = true;
                        looseX = shipX-20;
                        looseY = shipY-20;
                        okno.theme.stop();
                        //timerTrasa.stop();
                        boom.play();
                        ikona = new ImageIcon("pliki/boom.png");
                        //g.drawString("GAME OVER", 375, 600);
                        //g.setColor(Color.green);
                        //repaint();
                        if(highestScore < punkty){
                            zapisz();
                            JOptionPane.showMessageDialog(null, "NEW RECORD! \n Score: "+okno.score.getText());
                            okno.highestScore.setText(Integer.toString(punkty));
                        }else{
                            JOptionPane.showMessageDialog(null, "GAME OVER! \n Score: "+okno.score.getText());
                        }
                        timerTrasa.stop();
                        return;

                    }
                }
            }
    }


    public void checkPressed(){
        if(okno.pressedRIGHT)
            setPojazdX(2);
        if(okno.pressedLEFT)
            setPojazdX(-2);
        if(okno.pressedUP)
            setPojazdY(-2);
        if(okno.pressedDOWN)
            setPojazdY(2);
    }

    public static int speed = 2;
    public static int punkty = 0;
    int i = 0;
    int ilosc = 0;
    int lvl = 500;
    public void trasa(){
        timerTrasa = new Timer(7, new ActionListener() {
            Random los = new Random();
            @Override
            public void actionPerformed(ActionEvent e) {
                wyczysc();
                rysujTrase(i);
                pojazdHitBoxes();

                checkPressed();


                if(!czyGMD) {
                    GMD();
                    okno.score.setText(Integer.toString(punkty));
                }

                if(ilosc >= 15) {
                    for (int k = 0; k <= 15 - 1; k++) {
                        obiekty(k, speed);
                    }
                }else{
                    for (int k = 0; k <= ilosc - 1; k++) {
                        obiekty(k, speed);
                    }
                }

                i+=2;

                repaint();
                //System.out.println("kupka: " + i);
                if(i == 900)
                    i = 0;
                if(850 < obiekt[14].getY()){
                    losujObiekty();
                    ilosc = 0;
                    speed+=1;
                }
                //System.out.println(obiekt[14].getY());
                punkty++;
                if(punkty > lvl){
                    ilosc+=los.nextInt(2)+1;
                    lvl+=los.nextInt(200)+100;
                }
                //System.out.println(ilosc);
            }
        });
    }

    public void pojazdHitBoxes(){

        //int shipPix[][] = new int[shipHeight][shipWidth];

        for(int i = 0; i < shipHeight; i++){
            for(int j = 0; j < shipWidth; j++){
                if(j == 0){
                    shipPix[i][j] = pojazdY+i;
                }else{
                    shipPix[i][j] = pojazdX+j;
                }
            }
        }

        Graphics2D g = (Graphics2D) plotno.getGraphics();
        g.setColor(Color.black);

        // for(int i = 0; i < shipHeight; i++)
        //     for(int j = 1; j < shipWidth; j++)
        //         p.drawRect(shipPix[i][j], shipPix[i][0], 1, 1);


        //repaint();
    }

    /*public void pojazd(){
        Graphics2D p = (Graphics2D) plotno.getGraphics();
        p.setColor(Color.black);

        p.drawRect(pojazdX, pojazdY, 37, 64);
        pojazdHitBoxes();

        repaint();
    }*/

    public void setPojazdX(int x){
        int tmp = pojazdX+x;
        if(tmp > 250 && tmp < 560) {
            pojazdX = pojazdX + x;
            shipX = shipX + x;
            pojazdHitBoxes();
        }
    }
    public void setPojazdY(int y){
        int tmp = pojazdY+y;
        if(tmp > 30 && tmp < 820){
            pojazdY = pojazdY + y;
            shipY = shipY + y;
            pojazdHitBoxes();
        }

    }

    int txt;
    public void obiekty(int i, int speed){

            //Graphics2D g = (Graphics2D) tlo.createGraphics();
            tp = new TexturePaint(tlo[txt], new Rectangle((int) obiekt[i].getX(), (int) obiekt[i].getY(), (int) obiekt[i].width, (int) obiekt[i].height));
            g.setPaint(tp);
            g.fillRoundRect((int) obiekt[i].getX(), (int) obiekt[i].getY(), (int) obiekt[i].width,
                    (int) obiekt[i].height, (int) obiekt[i].arcwidth, (int) obiekt[i].archeight);
            g.draw(obiekt[i]);


        obiekt[i].y+=speed;
        //repaint();
    }
    private void losujObiekty(){
        Random los = new Random();
        txt = los.nextInt(4);
        for(int i = 0; i < 15; i++){

            int w = los.nextInt(70)+40;
            int h = los.nextInt(40)+40;
            int x = los.nextInt(350-w)+250;
            int y = -100;
            int arcw =  los.nextInt(40)+15;
            int arch =  los.nextInt(40)+15;
            obiekt[i].setRoundRect(x,y,w,h,arcw,arch);

        }
    }


    public void odczyt() throws Exception, InterruptedException {
        Scanner input = new Scanner(new File("pliki/rekord.txt"));
        String answer = input.nextLine();
        highestScore = Integer.parseInt(answer);

        //System.out.println(answer);
    }

    public void zapiszRekord() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("pliki/rekord.txt", "UTF-8");
        writer.println(punkty);
        writer.close();
    }
    public void zapisz(){
        try {
            zapiszRekord();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void nowaGra(){
        timerTrasa.restart();
        timerTrasa.stop();
        wyczysc();
        pojazdX = 415;
        pojazdY = 770;
        shipX = pojazdX;
        shipY = pojazdY;
        ikona = new ImageIcon("pliki/rsz_ship2.png");

        shipHeight = ikona.getIconHeight();
        shipWidth = ikona.getIconWidth();
        shipPix = new int[shipHeight][shipWidth];
        czyGMD = false;
        trasa();
        pojazdHitBoxes();
        speed = 2;
        punkty = 0;
        i = 0;
        ilosc = 0;
        lvl = 500;
        losujObiekty();
        wyczysc();
        timerTrasa.start();
    }


    //przesłonięta metoda paintComponent z klasy JPanel do rysowania
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //bg.paintIcon (this, g, 0, 0);
        Graphics2D g2d = (Graphics2D) g;
        //wyrysowanie naszego płótna na panelu
        g2d.drawImage(plotno, 0, 0, this);
        if(czyGMD == false)
            ikona.paintIcon (this, g, shipX, shipY);
        else
            ikona.paintIcon (this, g, looseX, looseY);

        g2d.dispose();
        g.dispose();
    }

}
