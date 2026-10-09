import java.io.*;
import java.util.*;

public class KT6 {

    //Задача 1
    static class ListNode {
        int value;
        ListNode next;
        ListNode(int value) { this.value = value; }
    }

    public static ListNode kt6Task1(int[] a) {
        ListNode head = null, tail = null;
        for (int x : a) {
            ListNode node = new ListNode(x);
            if (head == null) {
                head = tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
        }

        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        return prev;
    }


    //Задача 2
    static class DSU {
        int[] parent;
        int[] rank;

        DSU(int n) {
            parent = new int[n + 1];
            rank = new int[n + 1];
            for (int i = 1; i <= n; i++) parent[i] = i;
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry) return;
            if (rank[rx] < rank[ry]) parent[rx] = ry;
            else if (rank[rx] > rank[ry]) parent[ry] = rx;
            else { parent[ry] = rx; rank[rx]++; }
        }

        boolean connected(int x, int y) {
            return find(x) == find(y);
        }
    }

    public static void kt6Task2(int n, List<int[]> operations) {
        DSU dsu = new DSU(n);
        StringBuilder sb = new StringBuilder();

        for (int[] op : operations) {
            int type = op[0], u = op[1], v = op[2];
            if (type == 1) {
                dsu.union(u, v);
            } else {
                sb.append(dsu.connected(u, v) ? "YES" : "NO").append('\n');
            }
        }

        System.out.print(sb);
    }


    //Задача 3
    public static int[] kt6Task3(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
        int[] indegree = new int[n + 1];

        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            indegree[e[1]]++;
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 1; i <= n; i++) {
            if (indegree[i] == 0) pq.add(i);
        }

        int[] result = new int[n];
        int idx = 0;

        while (!pq.isEmpty()) {
            int u = pq.poll();
            result[idx++] = u;

            for (int v : adj.get(u)) {
                if (--indegree[v] == 0) {
                    pq.add(v);
                }
            }
        }

        if (idx != n) return new int[]{-1};
        return result;
    }


    //Задача 4
    public static ListNode kt6Task4(int[] a, int k) {
        ListNode head = null, tail = null;
        for (int x : a) {
            ListNode node = new ListNode(x);
            if (head == null) {
                head = tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
        }

        if (k <= 1) return head;

        ListNode dummy = new ListNode(0);
        ListNode prevTail = dummy;
        ListNode curr = head;

        while (curr != null) {
            ListNode check = curr;
            int count = 0;
            while (check != null && count < k) {
                check = check.next;
                count++;
            }

            if (count < k) {
                prevTail.next = curr;
                break;
            }

            ListNode blockHead = curr;
            ListNode prev = null;
            for (int i = 0; i < k; i++) {
                ListNode next = curr.next;
                curr.next = prev;
                prev = curr;
                curr = next;
            }

            prevTail.next = prev;
            prevTail = blockHead;
        }

        return dummy.next;
    }


    //Задача 5
    static class DSUParity {
        int[] parent;
        int[] rank;
        int[] parity;

        DSUParity(int n) {
            parent = new int[n + 1];
            rank = new int[n + 1];
            parity = new int[n + 1];
            for (int i = 1; i <= n; i++) parent[i] = i;
        }

        int find(int x) {
            if (parent[x] == x) return x;
            int root = find(parent[x]);
            parity[x] ^= parity[parent[x]];
            parent[x] = root;
            return root;
        }

        boolean union(int u, int v, int r) {
            int ru = find(u), rv = find(v);
            int pu = parity[u], pv = parity[v];

            if (ru == rv) {
                return (pu ^ pv) == r;
            }

            int newParity = pu ^ pv ^ r;

            if (rank[ru] < rank[rv]) {
                parent[ru] = rv;
                parity[ru] = newParity;
            } else if (rank[ru] > rank[rv]) {
                parent[rv] = ru;
                parity[rv] = newParity;
            } else {
                parent[rv] = ru;
                parity[rv] = newParity;
                rank[ru]++;
            }
            return true;
        }
    }

    public static void kt6Task5(int n, List<int[]> statements) {
        DSUParity dsu = new DSUParity(n);
        boolean ok = true;
        StringBuilder sb = new StringBuilder();

        for (int[] st : statements) {
            if (!ok) {
                sb.append("NO\n");
                continue;
            }
            if (!dsu.union(st[0], st[1], st[2])) {
                ok = false;
                sb.append("NO\n");
            } else {
                sb.append("YES\n");
            }
        }

        System.out.print(sb);
    }


    //Задача 6
    public static String kt6Task6(int k, String[] words) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < k; i++) adj.add(new ArrayList<>());
        int[] indegree = new int[k];
        boolean[][] edge = new boolean[k][k];

        for (int i = 0; i + 1 < words.length; i++) {
            String a = words[i];
            String b = words[i + 1];

            if (a.length() > b.length() && a.startsWith(b)) {
                return "IMPOSSIBLE";
            }

            int len = Math.min(a.length(), b.length());
            for (int j = 0; j < len; j++) {
                int ca = a.charAt(j) - 'a';
                int cb = b.charAt(j) - 'a';
                if (ca != cb) {
                    if (!edge[ca][cb]) {
                        edge[ca][cb] = true;
                        adj.get(ca).add(cb);
                        indegree[cb]++;
                    }
                    break;
                }
            }
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < k; i++) {
            if (indegree[i] == 0) pq.add(i);
        }

        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            int u = pq.poll();
            sb.append((char) ('a' + u));
            for (int v : adj.get(u)) {
                if (--indegree[v] == 0) pq.add(v);
            }
        }

        if (sb.length() != k) return "IMPOSSIBLE";
        return sb.toString();
    }


    //Тесты
    public static void main(String[] args) {
        System.out.println("Задача 1");
        ListNode reversed = kt6Task1(new int[]{1, 2, 3, 4, 5});
        printList(reversed);

        System.out.println("\nЗадача 2");
        List<int[]> ops2 = List.of(
                new int[]{1, 1, 2},
                new int[]{2, 1, 2},
                new int[]{1, 2, 3},
                new int[]{2, 1, 3},
                new int[]{2, 4, 1}
        );
        kt6Task2(4, ops2);

        System.out.println("\nЗадача 3");
        int[] topo = kt6Task3(3, new int[][]{{1, 3}, {2, 3}});
        System.out.println(Arrays.toString(topo));

        int[] topo2 = kt6Task3(2, new int[][]{{1, 2}, {2, 1}});
        System.out.println(Arrays.toString(topo2));

        System.out.println("\nЗадача 4");
        ListNode blocks = kt6Task4(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 3);
        printList(blocks);

        ListNode blocks2 = kt6Task4(new int[]{1, 2, 3, 4, 5}, 2);
        printList(blocks2);

        System.out.println("\nЗадача 5");
        List<int[]> st5 = List.of(
                new int[]{1, 2, 0},
                new int[]{2, 3, 1},
                new int[]{1, 3, 1},
                new int[]{1, 3, 0}
        );
        kt6Task5(3, st5);

        System.out.println("\nЗадача 6.");

        System.out.println(kt6Task6(3, new String[]{"baa", "bca", "cab"}));
        System.out.println(kt6Task6(3, new String[]{"caa", "aaa", "aab"}));
        System.out.println(kt6Task6(3, new String[]{"abc", "ab"}));
        System.out.println(kt6Task6(3, new String[]{"a", "b", "a"}));
    }

    private static void printList(ListNode head) {
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            sb.append(head.value).append(' ');
            head = head.next;
        }
        System.out.println(sb.toString().trim());
    }
}