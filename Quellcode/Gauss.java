
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

public class Gauss {
    static Path datei = Paths.get("./input1.txt");
    static double[][] koeff = new double[3][3];
    static double[] tuple = new double[3];

    public static void main(String[] args) throws IOException {
        einlesen();
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
                if (spaltenIndex < 3) {
                    koeff[zeilenIndex][spaltenIndex] = wert;
                } else {
                    tuple[zeilenIndex] = wert;
                }
            }
            zeilenIndex++;
        }
        System.out.println("Koeffizienten:");
    for (int i = 0; i < koeff.length; i++) {
        for (int j = 0; j < koeff[i].length; j++) {
            System.out.print(koeff[i][j] + " ");
        }
        System.out.println();
    }

    System.out.println("Tupel:");
    for (int i = 0; i < tuple.length; i++) {
        System.out.print(tuple[i] + " ");
    }
    System.out.println();
    }
}













