package ba.unsa.etf.diplomski;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Unesite lokaciju input fajla: ");
        Scanner scanner = new Scanner(System.in);
        String ulaz = scanner.nextLine();
        System.out.println("Unesite lokaciju output fajla: ");
        String izlaz = scanner.nextLine();

        Christofides christofidesAlgorithm = new Christofides(ulaz);
        christofidesAlgorithm.kruskalMst();
        christofidesAlgorithm.greedyPerfectMatching();
        christofidesAlgorithm.eulersPath();
        christofidesAlgorithm.eulerToHamilton();
        ArrayList<Integer> tsp = christofidesAlgorithm.getHamiltonCycle();
        tsp.forEach(grad -> System.out.println(" " + grad + " "));

    }
}
