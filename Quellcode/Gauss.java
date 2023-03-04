
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

public class Gauss {
    static Path datei = Paths.get("./input2");
    static double[][] koeff = new double[3][4];
    static double[][] solMatrix = new double[3][4];

    public static void main(String[] args) throws IOException {
        einlesen();
        ausgabe(koeff, "Eingangsmatrix");
        gaussAlgo1();
        ausgabe(solMatrix, "Lösungsmatrix");
    }

    private static void einlesen() throws IOException {
        List<String> f = Files.readAllLines(datei, StandardCharsets.UTF_8);
        int zeilenIndex = 0;
        for (int i = 0; i < f.size(); i++) {
            String zeile = f.get(i);
            if (zeile.isEmpty() || zeile.charAt(0) == '#') {
                continue;
            }

            String[] zeilenElemente = zeile.replace(',', '.').split("\\s+");
            for (int spaltenIndex = 0; spaltenIndex < zeilenElemente.length; spaltenIndex++) {
                String element = zeilenElemente[spaltenIndex];
                double wert = Double.parseDouble(element);
                koeff[zeilenIndex][spaltenIndex] = wert;
            }

            zeilenIndex++;
         }
    }
    private static void ausgabe(double[][] mx, String matrixName){

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
        for(int zeilen = 1; zeilen < solMatrix.length; zeilen++) { // Iteriert wird nur über Zeile 2,3 Z.1 bleibt statisch.
             // TODO: Pivotisierung einführen

            double multiplikator = -solMatrix[zeilen][0] / solMatrix[0][0]; // verwenden x = -b/a um den multiplikator zu finden mit dem Zeilen 0 werden
            multiplyAndAdd(0, zeilen, multiplikator);
        }

        for(int zeilen = 2; zeilen < solMatrix.length; zeilen++) { // Iteriert wird nur über Zeile 2,3 Z.1 bleibt statisch.
             // TODO: Pivotisierung einführen
            double multiplikator = -solMatrix[zeilen][1] / solMatrix[1][1]; // verwenden x = -b/a um den multiplikator zu finden mit dem Zeilen 0 werden
            multiplyAndAdd(1, zeilen, multiplikator);
        }

        for(int zeilen = solMatrix.length-1; zeilen >= 0; zeilen--){
            double diagonale = solMatrix[zeilen][zeilen];

            for(int spalte = 0; spalte < solMatrix[zeilen].length; spalte++){
                solMatrix[zeilen][spalte] /= diagonale;

            }
            double result = solMatrix[zeilen][solMatrix[zeilen].length - 1];
            for(int rueckZeilen = zeilen - 1; rueckZeilen >= 0; rueckZeilen--){
                solMatrix[rueckZeilen][solMatrix[zeilen].length - 1] -= result * solMatrix[rueckZeilen][zeilen];
                solMatrix[rueckZeilen][zeilen] = 0.0;
            }
        }
    }


    private static void copyLine(int line){
        for(int i = 0; i < koeff[line].length; i++) {
            solMatrix[line][i] = koeff[line][i];
        }
    }

    private static void multiplyAndAdd(int lineOne, int lineTwo, double factor) {
        for(int spalten = 0; spalten < solMatrix[lineOne].length; spalten++){
            solMatrix[lineTwo][spalten] = (solMatrix[lineOne][spalten] * factor) + solMatrix[lineTwo][spalten];
        }

    }
}

