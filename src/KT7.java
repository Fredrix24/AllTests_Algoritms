import java.io.*;
import java.util.*;

public class KT7 {

    static final long INF = Long.MAX_VALUE / 4;

    //Задача 1
    public static long[] kt7Task1(int n, List<int[]>[] adj, int s) {
        long[] dist = new long[n + 1];
        Arrays.fill(dist, INF);
        dist[s] = 0;

        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.add(new long[]{0, s});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int u = (int) cur[1];

            if (d > dist[u]) continue;

            for (int[] e : adj[u]) {
                int v = e[0];
                long w = e[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.add(new long[]{dist[v], v});
                }
            }
        }

        return dist;
    }


    //Задача 2
    public static long[] kt7Task2(int n, int[][] edges, int s) {
        long[] dist = new long[n + 1];
        Arrays.fill(dist, INF);
        dist[s] = 0;

        for (int iter = 0; iter < n - 1; iter++) {
            boolean updated = false;
            for (int[] e : edges) {
                int u = e[0], v = e[1], w = e[2];
                if (dist[u] != INF && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    updated = true;
                }
            }
            if (!updated) break;
        }

        return dist;
    }


    //Задача 3
    public static long[][] kt7Task3(int n, List<int[]>[] adj, int[] sources) {
        long[] dist = new long[n + 1];
        int[] src = new int[n + 1];
        Arrays.fill(dist, INF);
        Arrays.fill(src, -1);

        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) return Long.compare(a[0], b[0]);
            return Long.compare(a[2], b[2]);
        });

        for (int s : sources) {
            dist[s] = 0;
            src[s] = s;
            pq.add(new long[]{0, s, s});
        }

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int u = (int) cur[1];
            int source = (int) cur[2];

            if (d > dist[u] || (d == dist[u] && source > src[u])) continue;

            for (int[] e : adj[u]) {
                int v = e[0];
                long w = e[1];
                long nd = d + w;

                if (nd < dist[v] || (nd == dist[v] && source < src[v])) {
                    dist[v] = nd;
                    src[v] = source;
                    pq.add(new long[]{nd, v, source});
                }
            }
        }

        long[][] result = new long[n + 1][2];
        for (int i = 1; i <= n; i++) {
            if (dist[i] == INF) {
                result[i][0] = -1;
                result[i][1] = -1;
            } else {
                result[i][0] = src[i];
                result[i][1] = dist[i];
            }
        }
        return result;
    }


    //Задача 4
    public static long kt7Task4(int n, List<int[]>[] adj, int s, int t) {
        long[][] dist = new long[n + 1][2];
        for (long[] row : dist) Arrays.fill(row, INF);
        dist[s][0] = 0;

        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.add(new long[]{0, s, 0});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int u = (int) cur[1];
            int used = (int) cur[2];

            if (d > dist[u][used]) continue;

            for (int[] e : adj[u]) {
                int v = e[0];
                long w = e[1];

                if (d + w < dist[v][used]) {
                    dist[v][used] = d + w;
                    pq.add(new long[]{dist[v][used], v, used});
                }

                if (used == 0) {
                    long discounted = d + w / 2;
                    if (discounted < dist[v][1]) {
                        dist[v][1] = discounted;
                        pq.add(new long[]{dist[v][1], v, 1});
                    }
                }
            }
        }

        long result = Math.min(dist[t][0], dist[t][1]);
        return result == INF ? -1 : result;
    }


    //Задача 5
    public static long[] kt7Task5(int n, int[][] edges, int s) {
        long[] dist = new long[n + 1];
        Arrays.fill(dist, INF);
        dist[s] = 0;

        for (int iter = 0; iter < n - 1; iter++) {
            boolean updated = false;
            for (int[] e : edges) {
                int u = e[0], v = e[1], w = e[2];
                if (dist[u] != INF && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    updated = true;
                }
            }
            if (!updated) break;
        }

        boolean[] negInf = new boolean[n + 1];
        for (int iter = 0; iter < n; iter++) {
            for (int[] e : edges) {
                int u = e[0], v = e[1], w = e[2];
                if (dist[u] != INF && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    negInf[v] = true;
                }
                if (negInf[u]) negInf[v] = true;
            }
        }

        long[] result = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            if (dist[i] == INF && !negInf[i]) result[i] = Long.MIN_VALUE;
            else if (negInf[i]) result[i] = Long.MIN_VALUE + 1;
            else result[i] = dist[i];
        }
        return result;
    }


    //Задача 6
    public static long[] kt7Task6(int n, int[][] edges, int s, int t, int K) {
        long MOD = 1_000_000_007L;

        long[] cost = new long[n + 1];
        long[] ways = new long[n + 1];
        Arrays.fill(cost, INF);
        cost[s] = 0;
        ways[s] = 1;

        long bestCost = (s == t) ? 0 : INF;
        long bestWays = (s == t) ? 1 : 0;

        for (int step = 1; step <= K; step++) {
            long[] newCost = new long[n + 1];
            long[] newWays = new long[n + 1];
            Arrays.fill(newCost, INF);

            for (int[] e : edges) {
                int u = e[0], v = e[1], w = e[2];
                if (cost[u] == INF) continue;

                long nc = cost[u] + w;
                if (nc < newCost[v]) {
                    newCost[v] = nc;
                    newWays[v] = ways[u];
                } else if (nc == newCost[v]) {
                    newWays[v] = (newWays[v] + ways[u]) % MOD;
                }
            }

            cost = newCost;
            ways = newWays;

            if (cost[t] != INF) {
                if (cost[t] < bestCost) {
                    bestCost = cost[t];
                    bestWays = ways[t];
                } else if (cost[t] == bestCost) {
                    bestWays = (bestWays + ways[t]) % MOD;
                }
            }
        }

        if (bestCost == INF) return new long[]{-1};
        return new long[]{bestCost, bestWays};
    }


    //Тесты
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        System.out.println("Задача 1");
        List<int[]>[] adj1 = new List[6];
        for (int i = 0; i <= 5; i++) adj1[i] = new ArrayList<>();
        adj1[1].add(new int[]{2, 4});
        adj1[1].add(new int[]{3, 1});
        adj1[3].add(new int[]{2, 2});
        adj1[2].add(new int[]{4, 5});
        adj1[4].add(new int[]{5, 3});
        long[] d1 = kt7Task1(5, adj1, 1);
        for (int i = 1; i <= 5; i++) System.out.print((d1[i] == INF ? "INF" : d1[i]) + " ");
        System.out.println();

        System.out.println("\nЗадача 2");
        int[][] edges2 = {{1, 2, 4}, {2, 3, -2}, {3, 4, 3}, {4, 5, -1}};
        long[] d2 = kt7Task2(5, edges2, 1);
        for (int i = 1; i <= 5; i++) System.out.print((d2[i] == INF ? "INF" : d2[i]) + " ");
        System.out.println();

        System.out.println("\nЗадача 3");
        List<int[]>[] adj3 = new List[6];
        for (int i = 0; i <= 5; i++) adj3[i] = new ArrayList<>();
        adj3[1].add(new int[]{2, 1});
        adj3[2].add(new int[]{1, 1});
        adj3[2].add(new int[]{3, 1});
        adj3[3].add(new int[]{2, 1});
        adj3[3].add(new int[]{4, 1});
        adj3[4].add(new int[]{3, 1});
        adj3[4].add(new int[]{5, 1});
        adj3[5].add(new int[]{4, 1});
        long[][] r3 = kt7Task3(5, adj3, new int[]{2, 4});
        for (int i = 1; i <= 5; i++) {
            System.out.println(i + ": " + r3[i][0] + " " + r3[i][1]);
        }

        System.out.println("\nЗадача 4");
        List<int[]>[] adj4 = new List[5];
        for (int i = 0; i <= 4; i++) adj4[i] = new ArrayList<>();
        adj4[1].add(new int[]{2, 10});
        adj4[1].add(new int[]{3, 4});
        adj4[3].add(new int[]{2, 1});
        adj4[2].add(new int[]{4, 6});
        System.out.println(kt7Task4(4, adj4, 1, 4));

        System.out.println("\nЗадача 5");
        int[][] edges5 = {{1, 2, 1}, {2, 3, -1}, {3, 2, -1}, {3, 4, 1}};
        long[] r5 = kt7Task5(4, edges5, 1);
        for (int i = 1; i <= 4; i++) {
            if (r5[i] == Long.MIN_VALUE) System.out.println(i + ": *");
            else if (r5[i] == Long.MIN_VALUE + 1) System.out.println(i + ": -");
            else System.out.println(i + ": " + r5[i]);
        }

        System.out.println("\nЗадача 6");
        int[][] edges6 = {{1, 2, 1}, {1, 3, 5}, {2, 3, 1}, {3, 4, 2}};
        long[] r6 = kt7Task6(4, edges6, 1, 4, 3);
        if (r6[0] == -1) System.out.println("IMPOSSIBLE");
        else System.out.println(r6[0] + " " + r6[1]);
    }
}