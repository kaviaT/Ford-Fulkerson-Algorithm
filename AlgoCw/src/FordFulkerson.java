import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FordFulkerson {
    final static int INF = 987654321;   //compare and find the bottleneck capacity

    static int V;
    static int[][] capacity, flow;
    static ArrayList<Integer> arrayList = new ArrayList<>();

    public static void main(String[] args) {

        Stopwatch timer = new Stopwatch();

        Scanner input = null;
        try {
            input = new Scanner(new File("bridge_5.txt"));  //reading the text file
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; input.hasNext(); i++) {
            String key = input.next();
            arrayList.add(Integer.valueOf(key));
        }

        V = arrayList.get(0);
        System.out.println("\nV : " + V);

        capacity = new int[V][V];
        int count = 1;
        int i1 = 0;
        int i2 = 0;
        int i3;

        for (int i = 1; i < arrayList.size(); i++) {
            //edges
            if (count == 1) {
                i1 = arrayList.get(i);
                count++;
            } else if (count == 2) {
                i2 = arrayList.get(i);
                count++;
            } else if (count == 3) {
                i3 = arrayList.get(i);
                capacity[i1][i2] = i3;
                count = 1;
            }
        }
        System.out.println("Max capacity in Network Flow : " + networkFlow(0, V - 1));      //calculate max capacity
        System.out.println("Time spent for Max flow: " + timer.elapsedTime()+"s");      //timer
    }

    private static int networkFlow(int source, int sink) {
        flow = new int[V][V];
        int totalFlow = 0;
        while (true) {
            Vector<Integer> parent = new Vector<>(V);   //store the path
            for (int i = 0; i < V; i++)
                parent.add(-1);
            Queue<Integer> q = new LinkedList<>();
            parent.set(source, source);
            q.add(source);      //fill the vector
            while (!q.isEmpty() && parent.get(sink) == -1) {        //if q is empty
                int here = q.peek();   //taking the first one
                q.poll();       //remove it
                for (int there = 0; there < V; ++there)     //checking whether visited or not
                    if (capacity[here][there] - flow[here][there] > 0 && parent.get(there) == -1) {
                        q.add(there);
                        parent.set(there, here);       //going to node and add q
                    }
            }
            if (parent.get(sink) == -1)   //checking whether there's a route for the last node
                break;      //break after visiting everyone

            int amount = INF;
            String printer = " path : ";
            ArrayList arrayList = new ArrayList();

            for (int p = sink; p != source; p = parent.get(p)) {
                amount = Math.min(capacity[parent.get(p)][p] - flow[parent.get(p)][p], amount);     //compare and check the bottleneck
                arrayList.add(p);
            }

            arrayList.add(source);
            for (int p = sink; p != source; p = parent.get(p)) {       //backtrace and check the path
                flow[parent.get(p)][p] += amount;
                flow[p][parent.get(p)] -= amount;
            }

            totalFlow += amount;
            Collections.reverse(arrayList);  //reverse the arraylist according to ascending order
            System.out.print(arrayList);
            System.out.print(printer);

            System.out.println("-->  max flow : " +totalFlow);
        }
        return totalFlow;
    }
}