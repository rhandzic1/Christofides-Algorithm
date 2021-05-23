package ba.unsa.etf.diplomski;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Christofides {

    private List<City> cities;
    private ArrayList<ArrayList<Double>> adjMatrix;
    private ArrayList<ArrayList<Double>> e;
    private ArrayList<ArrayList<Integer>> mstAdjList;
    private double pathLength;

    public Christofides(String input, String output) {

        this.cities = readLocations(input);
        this.adjMatrix = new ArrayList<>();
        this.e = new ArrayList<>();
        this.pathLength = -1d;

        for(int i = 0; i < this.cities.size(); i++) {
            this.adjMatrix.add(new ArrayList<Double>(this.cities.size()));
        }
        for(int i = 0; i < cities.size(); i++) {
            for(int j = i; j < cities.size() ; j++) {
                if(i == j) {
                    adjMatrix.get(i).set(j, 0d);
                    continue;
                }
                double d = cities.get(i).getDistance(cities.get(j));
                adjMatrix.get(i).set(j, d);
                adjMatrix.get(j).set(i, d);
                e.add(new ArrayList<Double>(Arrays.asList(Double.valueOf(i), Double.valueOf(i), d)));
            }
        }
    }

    /**
     * Hierholzerov algoritam
     * void printEuler(int v){
     *
     *             stack<int> cpath;    // current path
     *             stack<int> epath;    // euler path
     *
     *             cpath.push(v);        // euler path starts from v
     *
     *             while(!cpath.empty()){
     *                 int u = cpath.top();
     *
     *                 if(adj[u].size()==0){
     *                     // if all edges from u are visited
     *                     // pop u and push it to euler path
     *                     epath.push(u);
     *                     cpath.pop();
     *                 }
     *                 else{
     *                     // if all edges from u are not visited
     *                     // select any edge (u, v)
     *                     // push v to current path and remove edge (u, v) from the graph
     *                     cpath.push(adj[u].begin()->first);
     *                     removeEdge(u,adj[u].begin()->first);
     *                 }
     *             }
     *
     *             while(!epath.empty()){
     *                 cout<<" "<<epath.top()<<" ";
     *                 epath.pop();
     *             }
     *
     *         }
     */
    public void eulersPath() {

    }
    public void eulerToHamilton() {

    }
    public void greedyPerfectMatching() {
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
        List<Double> weights = new ArrayList<>(Collections.nCopies(this.adjMatrix.size(), 0d));
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
        this.mstAdjList = new ArrayList<>();
        for(int i = 0; i < this.adjMatrix.size(); i++) {
            mstAdjList.add(new ArrayList<>());
        }
        for(int i = 0; i < res.size(); i++) {
            mstAdjList.get(res.get(i).get(0)).add(res.get(i).get(1));
            mstAdjList.get(res.get(i).get(1)).add(res.get(i).get(0));
        }
        this.mstAdjList = res;
    }

    public List<City> readLocations(String inputFile) {
        List<City> res = new ArrayList<>();
        try {
            File myObj = new File("filename.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] coordinates = data.split("\\s+");
                res.add(new City(Double.parseDouble(coordinates[0].trim()), Double.parseDouble(coordinates[1].trim())));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return res;
    }
}
