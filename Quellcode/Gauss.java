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

    private void parseCommandLine() {
        // Hier kann der Code für die Verarbeitung der Befehlszeilenargumente eingefügt werden
    }

    private static void einlesen() throws IOException {
        List<String> f = Files.readAllLines(datei, StandardCharsets.UTF_8);
        for(int i = 0; i < f.size(); i++){
            String pointer = f.get(i);
            if(pointer.isEmpty() == true || pointer.charAt(0) == '#'){
                continue;
            }

            String[] pPointer =  pointer.replace(',', '.').split("\\s+");
            for(int j = 0; j < pPointer.length; j++){
                String p = pPointer[j];
                double p1 = Double.parseDouble(p);
                System.out.println(p1);
            }
        }
    }
}
















