
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

public class Gauss {
    static String datei; // der Parameter der Datei
//    static Path datei = Paths.get("./input1.txt"); // Einlesen der Datei
    static double[][] koeff = new double[3][4]; // Initialierung der erweiterten Koeffizienten Matrix
    static double[][] solMatrix = new double[3][4]; // Initialierung der Loesungsmatrix
    static String[] zeilenElemente;
    static int countSpalten;
    static int countZeilen;

    public static void main(String[] args) throws IOException { // Main Methode welche alle Methoden ausfuehrt
        /*
         * Hier wird der Dateiname als Parameter eingelesen
         */
        if (args.length < 1) {
            System.out.println("Bitte Dateiname als Parameter übergeben!");
            return;
        } else {
            datei = args[0];
        }

        einlesen();
        ausgabe(koeff, "Eingangsmatrix");
        gaussAlgo1();
        ausgabe(solMatrix, "Loesungsmatrix");
    }

    private static void einlesen() throws IOException { // Einlesen der Koeffizienten aus Datei


        List<String> f = Files.readAllLines(Paths.get(datei));
        int zeilenIndex = 0;
        for (int i = 0; i < f.size(); i++) {
            String zeile = f.get(i);
             // Kommentare und Whitespaces der input Datei werden ignoriert
            if (zeile.isEmpty() || zeile.charAt(0) == '#') {
                continue;
            }

            zeilenElemente = zeile.replace(',', '.').split("\\s+"); // Kommata werden zu Punkten
            for (int spaltenIndex = 0; spaltenIndex < zeilenElemente.length; spaltenIndex++) {
                String element = zeilenElemente[spaltenIndex];
                double wert = Double.parseDouble(element);
                // Der Koeffizientenmatrix werden die double Werte zugewiesen, welche in wert gespeichert wurden
                koeff[zeilenIndex][spaltenIndex] = wert;

            }

            zeilenIndex++;
         }
        // Speichert Zeilen und Spalten Anzahl
        countSpalten = zeilenElemente.length;
        countZeilen = koeff.length;
        System.out.println(countSpalten);
        System.out.println(countZeilen);
    }
    private static void ausgabe(double[][] mx, String matrixName){ // Ausgabe der Matrizen

        System.out.println("Die " + matrixName + ":");
        for (int j = 0; j < mx.length; j++) {
            for (int k = 0; k < mx[j].length; k++) {
                System.out.print(mx[j][k] + " ");
            }
            System.out.println();
        }
    }

    private static void gaussAlgo1() {
        copyLine(0);// Erste Zeile der Originalmatrix wird in Loesungs Matrix kopiert
        copyLine(1);
        copyLine(2);
        for (int zeile = 0; zeile < solMatrix.length - 1; zeile++) {
        int maxZeile = zeile;
        for (int i = zeile + 1; i < solMatrix.length; i++) { // Diese Schleife sucht in der aktuellen Spalte (durch die Variable zeile indiziert) nach dem Element mit dem groessten absoluten Wert und merkt sich die Zeilennummer dieses Elements in der Variable maxZeile.
            if (Math.abs(solMatrix[i][zeile]) > Math.abs(solMatrix[maxZeile][zeile])) {
                maxZeile = i;
            }
        }
        /*
         * Wenn das Element mit dem groessten absoluten Wert in einer anderen Zeile als in der aktuellen Zeile gefunden wurde,
         *werden die beiden Zeilen vertauscht. Dies ist die Pivotisierung.
         */
        if (maxZeile != zeile) {
            double[] temp = solMatrix[zeile];
            solMatrix[zeile] = solMatrix[maxZeile];
            solMatrix[maxZeile] = temp;
        }

        // Die Koeffizienten werden durch x = -b/a 0
        for (int i = zeile + 1; i < solMatrix.length; i++) {
            double factor = -solMatrix[i][zeile] / solMatrix[zeile][zeile];
            multiplyAndAdd(zeile, i, factor);
        }
        }

        for(int zeilen = solMatrix.length-1; zeilen >= 0; zeilen--){
            double diagonale = solMatrix[zeilen][zeilen];

            for(int spalte = 0; spalte < solMatrix[zeilen].length; spalte++){
                solMatrix[zeilen][spalte] /= diagonale;

            }
            double result = solMatrix[zeilen][solMatrix[zeilen].length - 1]; /*
                                                                              *  Durch ruecksubstitution werden die gefundenen Werte RUECKWAERTS in die uebere Zeile eingesetzt
                                                                              *  und so x und y errechnet
                                                                              */
            for(int rueckZeilen = zeilen - 1; rueckZeilen >= 0; rueckZeilen--){
                solMatrix[rueckZeilen][solMatrix[zeilen].length - 1] -= result * solMatrix[rueckZeilen][zeilen];
                solMatrix[rueckZeilen][zeilen] = 0.0;
            }
        }
    }


    private static void copyLine(int line){ // Diese Methode setzt die Endgleichung = erw. Koeffizientenmatrix
        for(int i = 0; i < koeff[line].length; i++) {
            solMatrix[line][i] = koeff[line][i];
        }
    }

    private static void multiplyAndAdd(int lineOne, int lineTwo, double factor) { // x = -b/a wird durchgefuehrt
        for(int spalten = 0; spalten < solMatrix[lineOne].length; spalten++){
            solMatrix[lineTwo][spalten] = (solMatrix[lineOne][spalten] * factor) + solMatrix[lineTwo][spalten];
        }

    }
}

