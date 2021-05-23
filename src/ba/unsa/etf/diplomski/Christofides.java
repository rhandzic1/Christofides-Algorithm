package ba.unsa.etf.diplomski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Christofides {

    private List<City> cities;
    private ArrayList<ArrayList<Double>> adjMatrix;
    private ArrayList<ArrayList<Double>> e;
    private ArrayList<ArrayList<Integer>> mstAdjList;
    private ArrayList<Integer> eulersPath;
    private double pathLength;


    public Christofides(String input) {

        List<City> res = new ArrayList<>();
        try {
            File myObj = new File(input);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] coordinates = data.split("\\s+");
                res.add(new City(Double.parseDouble(coordinates[0].trim()), Double.parseDouble(coordinates[1].trim())));
            }
            myReader.close();
            this.cities = res;
            this.adjMatrix = new ArrayList<>();
            this.e = new ArrayList<>();
            this.pathLength = -1d;

            System.out.println("Broj gradova: " + cities.size());
            for(int i = 0; i < this.cities.size(); i++) {
                this.adjMatrix.add(new ArrayList<Double>(Collections.nCopies(this.cities.size(), 0d)));
            }
            for(int i = 0; i < cities.size(); i++) {
                for(int j = i; j < cities.size() ; j++) {
                    if(i == j) {
                        continue;
                    }
                    double d = cities.get(i).getDistance(cities.get(j));
                    adjMatrix.get(i).set(j, d);
                    adjMatrix.get(j).set(i, d);
                    e.add(new ArrayList<Double>(Arrays.asList(Double.valueOf(i), Double.valueOf(j), d)));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void eulersPath() {

        //Koristi se Hierholzerov algoritam, koji ima linearnu kompleksnost
        Stack<Integer> currentPath = new Stack<>();
        Stack<Integer> eulersPath = new Stack<>();
        ArrayList<ArrayList<Integer>> mstCopy = mstAdjList;

      /*  for(int i = 0; i < mstAdjList.size(); i++) {
            if(!mstAdjList.get(i).isEmpty()) {
                mstCopy.get(i).addAll(new ArrayList<>());
                for(int j = 0; j < mstAdjList.get(i).size(); j++) {
                    mstCopy.get(i).add(mstAdjList.get(i).get(j));
                }
            }
        }*/

        currentPath.push(0);
        while(!currentPath.isEmpty()) {

            int u = currentPath.peek();
            if(mstCopy.get(u).size() != 0) {
                eulersPath.push(u);
                currentPath.pop();

            } else {
                currentPath.push(mstCopy.get(u).get(0));
                mstCopy.get(u).remove(0);
            }
        }
        ArrayList<Integer> result = new ArrayList<>();

        while(!eulersPath.isEmpty()) {
            result.add(eulersPath.peek());
            eulersPath.pop();
        }
        this.eulersPath = result;
        System.out.println("Eulerov put: ");
        for (Integer integer : eulersPath) {
            System.out.println(integer);
        }
    }
    public void eulerToHamilton() {

        Set unique = new HashSet(this.eulersPath);
        this.eulersPath.clear();
        this.eulersPath.addAll(unique);

    }
    public void greedyPerfectMatching() {
        mstAdjList.forEach(l -> System.out.println(l));
        System.out.println("-------------------------------");
        ArrayList<Integer> mstOddVertices = new ArrayList<>();
        for(int i = 0; i < this.mstAdjList.size(); i++) {
            if(mstAdjList.get(i).size() % 2 == 1) {
                mstOddVertices.add(i);
            }
        }
        while(!mstOddVertices.isEmpty()) {

            double minDistance = Double.MAX_VALUE;
            int firstNode = mstOddVertices.get(0);
            int minDistanceNodeIndex = -1;

            for(int i = 0; i < mstOddVertices.size(); i++) {
                if(adjMatrix.get(firstNode).get(mstOddVertices.get(i)) < minDistance) {
                    minDistance = adjMatrix.get(firstNode).get(mstOddVertices.get(i));
                    minDistanceNodeIndex = i;
                }
            }
            this.mstAdjList.get(firstNode).add(mstOddVertices.get(minDistanceNodeIndex));
            this.mstAdjList.get(mstOddVertices.get(minDistanceNodeIndex)).add(firstNode);
            mstOddVertices.remove(minDistanceNodeIndex);
            mstOddVertices.remove(0);
        }
        mstAdjList.forEach(l -> System.out.println(l));
    }
    //Izmijeniti metodu, da bude void i bez parametara
    public void kruskalMst() {

        Collections.sort(this.e, (o1, o2) -> {
            if(o1.get(2) > o2.get(2)) {
                return 1;
            } else if(o1.get(2) < o2.get(2)) {
                return -1;
            } else return 0;
        });

        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        //Na početku je svaki čvor referentan samome sebi
        List<Integer> referenceNodes = new ArrayList<>();

        for(int i = 0; i < this.adjMatrix.size(); i++) {
            referenceNodes.add(i);
        }

        //Na početku su sve težine jednake jedinici
        List<Integer> weights = new ArrayList<>(Collections.nCopies(this.adjMatrix.size(), 0));

        for(int i = 0; i < this.e.size(); i++)
        {
            //Prvi čvor
            int firstNode = (int)Math.round(this.e.get(i).get(0));
            //Drugi čvor
            int secondNode = (int)Math.round(this.e.get(i).get(1));
            int referenceNodeOne = referenceNodes.get(firstNode);
            int temp = firstNode;

            while(temp != referenceNodeOne) {
                temp = referenceNodeOne;
                referenceNodeOne = referenceNodes.get(temp);
            }

            int referenceNodeTwo = referenceNodes.get(secondNode);
            temp = secondNode;
            while(temp != referenceNodeTwo) {
                temp = referenceNodeTwo;
                referenceNodeTwo = referenceNodes.get(temp);
            }

            if(referenceNodeOne != referenceNodeTwo) {

                int low = -1;
                int high = -1;
                if(weights.get(referenceNodeOne) < weights.get(referenceNodeTwo)) {
                    low = referenceNodeTwo;
                    high = referenceNodeOne;
                } else {
                    low = referenceNodeOne;
                    high = referenceNodeTwo;
                }
                referenceNodes.set(low, high);
                weights.set(high, weights.get(high) + weights.get(low));
                res.add(new ArrayList<>(Arrays.asList(firstNode, secondNode)));
            }
        }
        this.mstAdjList = new ArrayList<>(this.adjMatrix.size());
        for(int i = 0; i < this.adjMatrix.size(); i++) {
            mstAdjList.add(new ArrayList<>());
        }
        for(int i = 0; i < res.size(); i++) {
            mstAdjList.get(res.get(i).get(0)).add(res.get(i).get(1));
            mstAdjList.get(res.get(i).get(1)).add(res.get(i).get(0));
        }
    }

    public ArrayList<Integer> getHamiltonCycle() {
        return eulersPath;
    }
}
