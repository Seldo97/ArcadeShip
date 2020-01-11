import java.lang.Math.*;


public class Dane {

    public static int wspolX[] = new int[100];
    public static int wspolY[] = new int[100];
    public static int tempX[] = new int[100];
    public static int tempY[] = new int[100];
    public static int bezierX[] = new int[1000];
    public static int bezierY[] = new int[1000];
    private static int punkty = 0;
    public static int pktBez = 0;
    PanelGraficzny panel;
    //private int n;

    public Dane(){}

    public void dodajPunkty(int x, int y){
        wspolX[punkty] = x;
        wspolY[punkty] = y;

        punkty++;
    }

    public void wyczysc(){
        if(punkty > 0){
            //for(int i = 0; i < punkty; i++)
            //    wspolY = wspolX = null;

            punkty = 0;
            pktBez = 0;
        }
    }

    public int getIlePunktow(){ return punkty;}


}
