import java.util.Scanner;

public class GaussElimination {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Anzahl der Unbekannten: ");
        int n = sc.nextInt();
        double[][] A = new double[n][n];
        double[] b = new double[n];
        System.out.println("Geben Sie die Koeffizienten der Gleichungen ein:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char var = (char)('a' + j);
                System.out.print(var + "" + (i+1) + ": ");
                A[i][j] = sc.nextDouble();
            }
            System.out.print("b" + (i+1) + ": ");
            b[i] = sc.nextDouble();
        }
        sc.close();
        gaussElimination(A, b);
    }

    public static void gaussElimination(double[][] A, double[] b) {
        int n = A.length;
        for (int k = 0; k < n - 1; k++) {
            for (int i = k + 1; i < n; i++) {
                double factor = A[i][k] / A[k][k];
                b[i] -= factor * b[k];
                for (int j = k; j < n; j++) {
                    A[i][j] -= factor * A[k][j];
                }
            }
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = b[i];
            for (int j = i + 1; j < n; j++) {
                x[i] -= A[i][j] * x[j];
            }
            x[i] = x[i] / A[i][i];
        }

        System.out.println("\nLösung:");
        for (int i = 0; i < n; i++) {
            char var = (char)('a' + i);
            System.out.println(var + " = " + x[i]);
        }
    }
}
