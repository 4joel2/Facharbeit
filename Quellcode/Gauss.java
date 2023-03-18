/**
 * @author Joel Mantik
 */
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;


public class Gauss {
    static String datei; // der Parameter der Datei
    static double[][] koeff; // Initialierung der erweiterten Koeffizienten Matrix
    static double[][] solMatrix; // Initialierung der Loesungsmatrix
    static String[] zeilenElemente;
    static int countSpalten = 0;
    static int countZeilen;
    static final boolean SONDERFALL = true;
    static final boolean NORMALFALL = false;

    /**
     * main Methode in welcher alle Methoden ausgefuehrt werden
     * @param args liest die Datei mit der Matrix ein
     */
    public static void main(String[] args) throws IOException {
        /*
         * Hier wird der Dateiname als Parameter eingelesen
         */
        if (args.length < 1) {
            System.out.println("Bitte Dateiname als Parameter uebergeben!");
            return;
        } else {
            datei = args[0];
        }

        einlesen();
        ausgabe(koeff, "Eingangsmatrix");
        gaussAlgo1();
        ausgabe(solMatrix, "Loesungsmatrix");
    }

    /**
     * Diese Methode liest die Koeffizienten aus der Datei ein
     */
    private static void einlesen() throws IOException {

        List<String> f = Files.readAllLines(Paths.get(datei));
        int zeilenIndex = 0;

        for (int i = 0; i < f.size(); i++) {
            String zeile = f.get(i);
             // Kommentare und Whitespaces der input Datei werden ignoriert
            if (zeile.isEmpty() || zeile.charAt(0) == '#') {
                continue;
            }

            zeilenElemente = zeile.replace(',', '.').split("\\s+"); // Kommata werden zu Punkten
            // Speichert Spaltenanzahl
            if (countSpalten == 0){
                countSpalten = zeilenElemente.length;
                koeff     = new double[countSpalten - 1][countSpalten];
                solMatrix = new double[countSpalten - 1][countSpalten];
            }
            for (int spaltenIndex = 0; spaltenIndex < countSpalten; spaltenIndex++) {
                String element = zeilenElemente[spaltenIndex];
                double wert = Double.parseDouble(element);
                // Der Koeffizientenmatrix werden die double Werte zugewiesen, welche in wert gespeichert wurden
                koeff[zeilenIndex][spaltenIndex] = wert;

            }

            zeilenIndex++;
         }
        /*double[][] hilfMat = koeff;
        if(hilfMat.length != countSpalten){
            System.out.println("Falsche Matrix!");
            return;
        }*/

        // Speichert Zeilenanzahl
        countZeilen = koeff.length;
        System.out.println(countSpalten);
        System.out.println(countZeilen);
    }

    /**
     * Methode ausgabe gibt die Eingangsmatrix und die Loesungsmatrix aus
     * @param mx nimmt die Eingangsmatrix oder die Loesungsmatrix an
     * @param matrixName nimmt den Namen der Matrix an
     */
    private static void ausgabe(double[][] mx, String matrixName){ // Ausgabe der Matrizen

        System.out.println("Die " + matrixName + ":");
        for (int j = 0; j < mx.length; j++) {
            for (int k = 0; k < mx[j].length; k++) {
                System.out.print(Math.round(mx[j][k] * 10000d) / 10000d + " "); // TODO: GILT NUR FUER NETTE ZAHLEN
            }
            System.out.println();
        }
        System.out.println();
    }


    /**
     * Die Methode gaussAlgo fuehrt den eigentlichen Algorithmus aus
     */
    private static void gaussAlgo1() {
        for (int i = 0; i < countZeilen; i++) {
            for (int j = 0; j < countSpalten; j++) {
                solMatrix[i][j] = koeff[i][j];
            }
        }
       // copyLine(0);// Erste Zeile der Originalmatrix wird in Loesungs Matrix kopiert
        //copyLine(1);
        //copyLine(2);
        /**
         * Diese for-Schleife triangularisiert die Matrix
         */
        for (int zeile = 0; zeile < countZeilen - 1; zeile++) {
            int maxZeile = zeile;

            /*
            *  Diese Schleife sucht in der aktuellen Spalte (durch die Variable zeile indiziert)
            *  nach dem Element mit dem groessten absoluten Wert und merkt sich die Zeilennummer dieses Elements in der Variable maxZeile.
            */
            for (int i = zeile + 1; i < countZeilen; i++) {
                // Der spaltenIndex der solMatrix rueckt nacht jeder Vertauschung einen weiter deswegen kann als spaltenIndex zeile benutzt werden
                if (Math.abs(solMatrix[i][zeile]) > Math.abs(solMatrix[maxZeile][zeile])) {
                    maxZeile = i;
                }
            }
            /*
            * Wenn das Element mit dem groessten absoluten Wert in einer anderen Zeile als in der aktuellen Zeile (maxZeile) gefunden wurde
            * werden die maxZeile und die oberste Zeile vertauscht vertauscht. Dies ist die Pivotisierung.
            */
            if (maxZeile != zeile) {
                double[] temp = solMatrix[zeile];
                solMatrix[zeile] = solMatrix[maxZeile];
                solMatrix[maxZeile] = temp;
            }

            // Die Koeffizienten werden durch x = -b/a 0
            for (int i = zeile + 1; i < countZeilen; i++) {
                // TODO: Vielleicht wird durch 0 geteilt
                double factor = -solMatrix[i][zeile] / solMatrix[zeile][zeile];
                multiplyAndAdd(zeile, i, factor);
            }
        }

        ausgabe(solMatrix, "Triangularisierte Matrix");

        /*
         * Untersuchung der Triangularisierten Matrix: 1.: wvl 0 Zeilen gibt es am Ende
         */
        if(checkWievielNullZeilen() == SONDERFALL){
            System.exit(-1);
        }

        /*
         *  Durch ruecksubstitution werden die gefundenen Werte RUECKWAERTS in die uebere Zeile eingesetzt
         *  und so x und y errechnet
         */
        for(int zeilen = countZeilen-1; zeilen >= 0; zeilen--){
            double diagonale = solMatrix[zeilen][zeilen];
            // TODO: Teilen durch Diagonale
            for(int spalte = 0; spalte < countSpalten; spalte++){
                solMatrix[zeilen][spalte] /= diagonale;
            }
            double result = solMatrix[zeilen][solMatrix[zeilen].length - 1];
            for(int rueckZeilen = zeilen - 1; rueckZeilen >= 0; rueckZeilen--){
                solMatrix[rueckZeilen][countSpalten - 1] -= result * solMatrix[rueckZeilen][zeilen];
                solMatrix[rueckZeilen][zeilen] = 0.0;
            }
        }
    }



    /**
     * multiplyAndAdd bringt die Koeffizienten auf 0
     * @param lineOne  nimmt die 1. Zeile welche benutzt werden soll an
     * @param lineTwo  nimmt die 2. Zeile welche benutzt werden soll an
     * @param factor   der Wert mit dem multiplizert wird, so dass der Koeffzient 0 wird
     */
    private static void multiplyAndAdd(int lineOne, int lineTwo, double factor) { // x = -b/a wird durchgefuehrt
        for(int spalten = 0; spalten < solMatrix[lineOne].length; spalten++){
            solMatrix[lineTwo][spalten] = (solMatrix[lineOne][spalten] * factor) + solMatrix[lineTwo][spalten];
        }

    }

    /**
     * Die Methode prüft wieviele Nullzeilen die Matrix hat.
     * @return SONDERFALL wird zurückgegeben wenn die Matrix ein Sonderfall ist.
     * @return NORMALFALL wird sonst zurückgegeben
     *
     *
     */

    private static boolean checkWievielNullZeilen() {
        int nullCounter = 0;
        for(int i = countZeilen - 1; i >= 0; i--){
            if(checkObNull(i) == true){
                nullCounter++;
            } else {
                if(nullCounter != 0){
                    System.out.println("Sonderfall gefunden: Es gibt " + nullCounter + " Nullzeilen " );
                    return SONDERFALL;
                }else{
                    return NORMALFALL;
                }
            }
        }
        System.out.println("Sonderfall gefunden, Matrix besteht aus Nullen!");
        return SONDERFALL;
    }

    /**
     * @param zeile Die Zeile welchhe geprüft wird
     * Die Methode prüft für jedes Element ob es den Wert Null hat
     */
    private static boolean checkObNull(int zeile){
        for(int i= 0; i < countSpalten; i++){
            if(solMatrix[zeile][i] != 0){
                return false;
            }
        }
        return true;
    }

}

