import java.io.*;
import java.util.*;

public class KT9 {

    static final long NEG = Long.MIN_VALUE / 4;

    //Задача 1
    public static int kt9Task1(String s) {
        int n = s.length();
        boolean[][] isPal = new boolean[n][n];

        for (int i = 0; i < n; i++) isPal[i][i] = true;

        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    if (len == 2 || isPal[i + 1][j - 1]) {
                        isPal[i][j] = true;
                    }
                }
            }
        }

        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (isPal[j][i - 1] && dp[j] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], dp[j] + 1);
                }
            }
        }

        return dp[n];
    }


    //Задача 2
    public static long kt9Task2(char[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        long MOD = 1_000_000_007L;

        if (grid[0][0] == '#' || grid[n - 1][m - 1] == '#') return 0;

        long[] dp = new long[m];
        dp[0] = 1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == '#') {
                    dp[j] = 0;
                } else if (j > 0) {
                    dp[j] = (dp[j] + dp[j - 1]) % MOD;
                }
            }
        }

        return dp[m - 1];
    }


    //Задача 3
    public static long kt9Task3(int n, long[] val, List<Integer>[] adj) {
        boolean[] visited = new boolean[n + 1];
        long[] res = kt9Dfs3(1, val, adj, visited);
        return Math.max(res[0], res[1]);
    }

    private static long[] kt9Dfs3(int v, long[] val, List<Integer>[] adj, boolean[] visited) {
        visited[v] = true;
        long take = val[v];
        long skip = 0;

        for (int u : adj[v]) {
            if (!visited[u]) {
                long[] child = kt9Dfs3(u, val, adj, visited);
                take += child[1];
                skip += Math.max(child[0], child[1]);
            }
        }

        return new long[]{take, skip};
    }


    //Задача 4
    public static long kt9Task4(int n, long[] a) {
        if (n == 1) return 0;

        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + a[i];

        long[][] dp = new long[n][n];

        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                dp[i][j] = Long.MAX_VALUE;

                long sumIJ = prefix[j + 1] - prefix[i];

                for (int k = i; k < j; k++) {
                    long cost = dp[i][k] + dp[k + 1][j] + sumIJ;
                    if (cost < dp[i][j]) dp[i][j] = cost;
                }
            }
        }

        return dp[0][n - 1];
    }


    //Задача 5
    public static long kt9Task5(int n, int m, int k, long[][] a) {
        long[][][] dp = new long[n][m][k + 1];
        for (long[][] layer : dp)
            for (long[] row : layer)
                Arrays.fill(row, NEG);

        dp[0][0][0] = a[0][0];
        if (a[0][0] < 0 && k > 0) dp[0][0][1] = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == 0 && j == 0) continue;

                for (int t = 0; t <= k; t++) {
                    long from = NEG;
                    if (i > 0) from = Math.max(from, dp[i - 1][j][t]);
                    if (j > 0) from = Math.max(from, dp[i][j - 1][t]);
                    if (from != NEG) {
                        dp[i][j][t] = Math.max(dp[i][j][t], from + a[i][j]);
                    }

                    if (t > 0 && a[i][j] < 0) {
                        long from2 = NEG;
                        if (i > 0) from2 = Math.max(from2, dp[i - 1][j][t - 1]);
                        if (j > 0) from2 = Math.max(from2, dp[i][j - 1][t - 1]);
                        if (from2 != NEG) {
                            dp[i][j][t] = Math.max(dp[i][j][t], from2);
                        }
                    }
                }
            }
        }

        long best = NEG;
        for (int t = 0; t <= k; t++) best = Math.max(best, dp[n - 1][m - 1][t]);
        return best;
    }


    //Задача 6
    public static long kt9Task6(int n, int k, long[] val, List<Integer>[] adj) {
        boolean[] visited = new boolean[n + 1];
        long[] res = kt9Dfs6(1, k, val, adj, visited);
        return res[k];
    }

    private static long[] kt9Dfs6(int v, int k, long[] val, List<Integer>[] adj, boolean[] visited) {
        visited[v] = true;

        long[] dp = new long[k + 1];
        Arrays.fill(dp, NEG);
        dp[1] = val[v];
        int size = 1;

        for (int u : adj[v]) {
            if (visited[u]) continue;

            long[] child = kt9Dfs6(u, k, val, adj, visited);
            int childSize = 0;
            for (int s = k; s >= 1; s--) {
                if (child[s] != NEG) { childSize = s; break; }
            }

            long[] newDp = dp.clone();
            for (int s1 = 1; s1 <= size; s1++) {
                if (dp[s1] == NEG) continue;
                for (int s2 = 1; s2 <= childSize && s1 + s2 <= k; s2++) {
                    if (child[s2] == NEG) continue;
                    newDp[s1 + s2] = Math.max(newDp[s1 + s2], dp[s1] + child[s2]);
                }
            }
            dp = newDp;
            size = Math.min(k, size + childSize);
        }

        return dp;
    }


    //Тесты
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        System.out.println("Задача 1");
        System.out.println(kt9Task1("abacaba"));
        System.out.println(kt9Task1("abc"));
        System.out.println(kt9Task1("aabbaa"));
        System.out.println(kt9Task1("abcd"));

        System.out.println("\nЗадача 2");
        System.out.println(kt9Task2(new char[][]{
                {'.', '.', '.'},
                {'.', '#', '.'},
                {'.', '.', '.'}
        }));
        System.out.println(kt9Task2(new char[][]{
                {'.', '.'},
                {'.', '.'}
        }));

        System.out.println("\nЗадача 3");
        List<Integer>[] adj3 = new List[5];
        for (int i = 0; i <= 4; i++) adj3[i] = new ArrayList<>();
        adj3[1].add(2); adj3[2].add(1);
        adj3[1].add(3); adj3[3].add(1);
        adj3[2].add(4); adj3[4].add(2);
        long[] v3 = {0, 10, 5, 8, 3};
        System.out.println(kt9Task3(4, v3, adj3));

        System.out.println("\nЗадача 4");
        System.out.println(kt9Task4(4, new long[]{1, 2, 3, 4}));
        System.out.println(kt9Task4(1, new long[]{5}));

        System.out.println("\nЗадача 5");
        long[][] a5 = {
                {1, -5, 3},
                {-2, 4, -1},
                {5, -3, 2}
        };
        System.out.println(kt9Task5(3, 3, 1, a5));

        System.out.println("\nЗадача 6");
        System.out.println(kt9Task6(4, 3, v3, adj3));
    }
}