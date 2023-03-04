
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

public class Gauss {
    static Path datei = Paths.get("./input1.txt"); // Einlesen der Datei
    static double[][] koeff = new double[3][4]; // Initialierung der erweiterten Koeffizienten Matrix
    static double[][] solMatrix = new double[3][4]; // Initialierung der Lösungsmatrix

    public static void main(String[] args) throws IOException { // Main Methode welche alle Methoden ausführt
        einlesen();
        ausgabe(koeff, "Eingangsmatrix");
        gaussAlgo1();
        ausgabe(solMatrix, "Lösungsmatrix");
    }

    private static void einlesen() throws IOException { // Einlesen der Koeffizienten aus Datei
        List<String> f = Files.readAllLines(datei, StandardCharsets.UTF_8);
        int zeilenIndex = 0;
        for (int i = 0; i < f.size(); i++) {
            String zeile = f.get(i);
            if (zeile.isEmpty() || zeile.charAt(0) == '#') { // Kommentare und Whitespaces der input Datei werden ignoriert
                continue;
            }

            String[] zeilenElemente = zeile.replace(',', '.').split("\\s+"); // Kommata werden zu Punkten
            for (int spaltenIndex = 0; spaltenIndex < zeilenElemente.length; spaltenIndex++) {
                String element = zeilenElemente[spaltenIndex];
                double wert = Double.parseDouble(element);
                koeff[zeilenIndex][spaltenIndex] = wert; // Der Koeffizientenmatrix werden die double Werte zugewiesen, welche in wert gespeichert wurden
            }

            zeilenIndex++;
         }
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
        copyLine(0);// Erste Zeile der Originalmatrix wird in Lösungs Matrix kopiert
        copyLine(1);
        copyLine(2);
        for (int zeile = 0; zeile < solMatrix.length - 1; zeile++) {
        int maxZeile = zeile;
        for (int i = zeile + 1; i < solMatrix.length; i++) { // Diese Schleife sucht in der aktuellen Spalte (durch die Variable zeile indiziert) nach dem Element mit dem größten absoluten Wert und merkt sich die Zeilennummer dieses Elements in der Variable maxZeile.
            if (Math.abs(solMatrix[i][zeile]) > Math.abs(solMatrix[maxZeile][zeile])) {
                maxZeile = i;
            }
        }
        if (maxZeile != zeile) { // Wenn das Element mit dem größten absoluten Wert in einer anderen Zeile als der aktuellen Zeile gefunden wurde, werden die beiden Zeilen vertauscht. Dies ist die Pivotisierung.
            double[] temp = solMatrix[zeile];
            solMatrix[zeile] = solMatrix[maxZeile];
            solMatrix[maxZeile] = temp;
        }

        for (int i = zeile + 1; i < solMatrix.length; i++) { // Die Koeffizienten werden durch x = -b/a 0
            double factor = -solMatrix[i][zeile] / solMatrix[zeile][zeile];
            multiplyAndAdd(zeile, i, factor);
        }
        }

        for(int zeilen = solMatrix.length-1; zeilen >= 0; zeilen--){
            double diagonale = solMatrix[zeilen][zeilen];

            for(int spalte = 0; spalte < solMatrix[zeilen].length; spalte++){
                solMatrix[zeilen][spalte] /= diagonale;

            }
            double result = solMatrix[zeilen][solMatrix[zeilen].length - 1]; // Durch rücksubstitution werden die gefundenen Werte RÜCKWÄRTS in die übere Zeile eingesetzt und so x und y errechnet
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

    private static void multiplyAndAdd(int lineOne, int lineTwo, double factor) { // x = -b/a wird durchgeführt
        for(int spalten = 0; spalten < solMatrix[lineOne].length; spalten++){
            solMatrix[lineTwo][spalten] = (solMatrix[lineOne][spalten] * factor) + solMatrix[lineTwo][spalten];
        }

    }
}

