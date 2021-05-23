package ba.unsa.etf.diplomski;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        ArrayList<Integer> l = new ArrayList<>(Arrays.asList(1, 2, 3));
        System.out.println(l.get(0));
        System.out.println(l.get(2));
        System.out.println("*************************");
        l.remove(2);
        System.out.println(l.get(0));
        System.out.println("*************************");
        l.remove(0);
        System.out.println(l.get(0));


    }
}
