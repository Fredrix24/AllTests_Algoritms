import java.io.*;
import java.util.*;

public class KT8 {

    //Задача 1
    public static long kt8Task1(int[] h) {
        int n = h.length;
        if (n == 1) return 0;

        long prev2 = 0;
        long prev1 = Math.abs((long) h[1] - h[0]);

        for (int i = 2; i < n; i++) {
            long cost1 = prev1 + Math.abs((long) h[i] - h[i - 1]);
            long cost2 = prev2 + Math.abs((long) h[i] - h[i - 2]);
            long cur = Math.min(cost1, cost2);
            prev2 = prev1;
            prev1 = cur;
        }

        return prev1;
    }


    //Задача 2
    public static long kt8Task2(int n, int W, int[] w, long[] v) {
        long[] dp = new long[W + 1];

        for (int i = 0; i < n; i++) {
            for (int j = W; j >= w[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - w[i]] + v[i]);
            }
        }

        long best = 0;
        for (long x : dp) best = Math.max(best, x);
        return best;
    }


    //Задача 3
    public static int kt8Task3(String s, String t) {
        int n = s.length(), m = t.length();
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    cur[j] = prev[j - 1] + 1;
                } else {
                    cur[j] = Math.max(prev[j], cur[j - 1]);
                }
            }
            int[] tmp = prev;
            prev = cur;
            cur = tmp;
            Arrays.fill(cur, 0);
        }

        return prev[m];
    }


    //Задача 4
    public static long[] kt8Task4(int n, int C, int[] w, long[] p) {
        long NEG = Long.MIN_VALUE / 4;
        long[] profit = new long[C + 1];
        long[] count = new long[C + 1];
        Arrays.fill(profit, NEG);
        profit[0] = 0;
        count[0] = 0;

        for (int c = 1; c <= C; c++) {
            for (int i = 0; i < n; i++) {
                if (w[i] <= c && profit[c - w[i]] != NEG) {
                    long newProfit = profit[c - w[i]] + p[i];
                    long newCount = count[c - w[i]] + 1;

                    if (newProfit > profit[c]
                            || (newProfit == profit[c] && newCount < count[c])) {
                        profit[c] = newProfit;
                        count[c] = newCount;
                    }
                }
            }
        }

        if (profit[C] == NEG) return new long[]{-1};
        return new long[]{profit[C], count[C]};
    }


    //Задача 5
    static class Fenwick {
        int n;
        int[] len;
        long[] cnt;
        static final long MOD = 1_000_000_007L;

        Fenwick(int n) {
            this.n = n;
            len = new int[n + 1];
            cnt = new long[n + 1];
        }

        int[] query(int pos) {
            int bestLen = 0;
            long bestCnt = 0;
            while (pos > 0) {
                if (len[pos] > bestLen) {
                    bestLen = len[pos];
                    bestCnt = cnt[pos];
                } else if (len[pos] == bestLen) {
                    bestCnt = (bestCnt + cnt[pos]) % MOD;
                }
                pos -= pos & (-pos);
            }
            return new int[]{bestLen, (int) bestCnt};
        }

        void update(int pos, int newLen, long newCnt) {
            while (pos <= n) {
                if (len[pos] < newLen) {
                    len[pos] = newLen;
                    cnt[pos] = newCnt % MOD;
                } else if (len[pos] == newLen) {
                    cnt[pos] = (cnt[pos] + newCnt) % MOD;
                }
                pos += pos & (-pos);
            }
        }
    }

    public static long[] kt8Task5(int[] a) {
        int n = a.length;

        int[] sorted = a.clone();
        Arrays.sort(sorted);
        int[] compressed = new int[n];
        for (int i = 0; i < n; i++) {
            compressed[i] = Arrays.binarySearch(sorted, a[i]) + 1;
        }

        Fenwick fw = new Fenwick(n);

        int globalBestLen = 0;
        long globalBestCnt = 0;

        for (int i = 0; i < n; i++) {
            int[] prev = fw.query(compressed[i] - 1);
            int curLen = prev[0] + 1;
            long curCnt = (prev[0] == 0) ? 1 : prev[1];

            fw.update(compressed[i], curLen, curCnt);

            if (curLen > globalBestLen) {
                globalBestLen = curLen;
                globalBestCnt = curCnt;
            } else if (curLen == globalBestLen) {
                globalBestCnt = (globalBestCnt + curCnt) % 1_000_000_007L;
            }
        }

        return new long[]{globalBestLen, globalBestCnt};
    }


    //Задача 6
    public static String kt8Task6(String s, String t) {
        int n = s.length(), m = t.length();
        int[][] lcs = new int[n + 1][m + 1];

        for (int i = n - 1; i >= 0; i--) {
            for (int j = m - 1; j >= 0; j--) {
                if (s.charAt(i) == t.charAt(j)) {
                    lcs[i][j] = lcs[i + 1][j + 1] + 1;
                } else {
                    lcs[i][j] = Math.max(lcs[i + 1][j], lcs[i][j + 1]);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        int i = 0, j = 0;

        while (i < n && j < m) {
            char cs = s.charAt(i);
            char ct = t.charAt(j);

            if (cs == ct) {
                sb.append(cs);
                i++;
                j++;
            } else {
                boolean takeS;
                if (lcs[i + 1][j] > lcs[i][j + 1]) {
                    takeS = true;
                } else if (lcs[i + 1][j] < lcs[i][j + 1]) {
                    takeS = false;
                } else {
                    takeS = cs < ct;
                }

                if (takeS) {
                    sb.append(cs);
                    i++;
                } else {
                    sb.append(ct);
                    j++;
                }
            }
        }

        while (i < n) sb.append(s.charAt(i++));
        while (j < m) sb.append(t.charAt(j++));

        return sb.toString();
    }


    //Тесты
    public static void main(String[] args) {
        System.out.println("Задача 1");
        System.out.println(kt8Task1(new int[]{10, 30, 40, 20}));
        System.out.println(kt8Task1(new int[]{10, 10}));
        System.out.println(kt8Task1(new int[]{1, 100, 1, 100}));

        System.out.println("\nЗадача 2");
        System.out.println(kt8Task2(3, 50, new int[]{10, 20, 30}, new long[]{60, 100, 120}));
        System.out.println(kt8Task2(3, 10, new int[]{5, 4, 6}, new long[]{10, 40, 30}));

        System.out.println("\nЗадача 3");
        System.out.println(kt8Task3("abcde", "ace"));
        System.out.println(kt8Task3("abc", "abc"));
        System.out.println(kt8Task3("abc", "def"));

        System.out.println("\nЗадача 4");
        long[] r4a = kt8Task4(2, 8, new int[]{3, 4}, new long[]{5, 6});
        System.out.println(r4a[0] + " " + r4a[1]);

        long[] r4b = kt8Task4(1, 5, new int[]{7}, new long[]{10});
        System.out.println(r4b[0]);

        System.out.println("\nЗадача 5");
        long[] r5a = kt8Task5(new int[]{1, 3, 5, 4, 7});
        System.out.println(r5a[0] + " " + r5a[1]);

        long[] r5b = kt8Task5(new int[]{2, 2, 2});
        System.out.println(r5b[0] + " " + r5b[1]);

        System.out.println("\nЗадача 6");
        System.out.println(kt8Task6("abac", "cab"));
        System.out.println(kt8Task6("abc", "abc"));
        System.out.println(kt8Task6("a", "b"));
    }
}