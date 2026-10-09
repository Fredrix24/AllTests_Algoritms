import java.io.*;
import java.util.*;

public class KT5 {

    //Задача 1
    public static void kt5Task1(int n) {
        StringBuilder sb = new StringBuilder();
        kt5Generate(n, 0, 0, sb);
    }

    private static void kt5Generate(int n, int open, int close, StringBuilder sb) {
        if (sb.length() == 2 * n) {
            System.out.println(sb);
            return;
        }
        if (open < n) {
            sb.append('(');
            kt5Generate(n, open + 1, close, sb);
            sb.deleteCharAt(sb.length() - 1);
        }
        if (close < open) {
            sb.append(')');
            kt5Generate(n, open, close + 1, sb);
            sb.deleteCharAt(sb.length() - 1);
        }
    }


    //Задача 2
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        int count = 0;
    }

    static class Trie {
        TrieNode root = new TrieNode();

        void add(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node = node.children.computeIfAbsent(c, k -> new TrieNode());
                node.count++;
            }
        }

        void remove(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node = node.children.get(c);
                node.count--;
            }
        }

        int countPrefix(String prefix) {
            TrieNode node = root;
            for (char c : prefix.toCharArray()) {
                node = node.children.get(c);
                if (node == null) return 0;
            }
            return node.count;
        }
    }

    public static void kt5Task2() {
        Trie trie = new Trie();
        trie.add("apple");
        trie.add("application");
        trie.add("apply");
        trie.add("banana");
        System.out.println(trie.countPrefix("app"));
        System.out.println(trie.countPrefix("appl"));
        System.out.println(trie.countPrefix("ban"));
        System.out.println(trie.countPrefix("xyz"));
        trie.remove("apply");
        System.out.println(trie.countPrefix("appl"));
    }


    //Задача 3
    public static int[] kt5Task3(int[] a) {
        int xorAll = 0;
        for (int x : a) xorAll ^= x;

        int diffBit = xorAll & (-xorAll);

        int x = 0, y = 0;
        for (int num : a) {
            if ((num & diffBit) == 0) x ^= num;
            else y ^= num;
        }

        return x < y ? new int[]{x, y} : new int[]{y, x};
    }


    //Задача 4
    public static long kt5Task4(int n, boolean[][] forbidden) {
        boolean[] cols = new boolean[n];
        boolean[] diag1 = new boolean[2 * n - 1];
        boolean[] diag2 = new boolean[2 * n - 1];
        return kt5PlaceQueens(0, n, cols, diag1, diag2, forbidden);
    }

    private static long kt5PlaceQueens(int row, int n,
                                       boolean[] cols, boolean[] diag1, boolean[] diag2,
                                       boolean[][] forbidden) {
        if (row == n) return 1;

        long count = 0;
        for (int col = 0; col < n; col++) {
            if (forbidden[row][col]) continue;
            int d1 = row + col;
            int d2 = row - col + (n - 1);

            if (cols[col] || diag1[d1] || diag2[d2]) continue;

            cols[col] = diag1[d1] = diag2[d2] = true;
            count += kt5PlaceQueens(row + 1, n, cols, diag1, diag2, forbidden);
            cols[col] = diag1[d1] = diag2[d2] = false;
        }
        return count;
    }


    //Задача 5
    static class TrieNode5 {
        Map<Character, TrieNode5> children = new TreeMap<>();
        int count = 0;
        int wordEnd = 0;
    }

    static class Trie5 {
        TrieNode5 root = new TrieNode5();

        void add(String word) {
            TrieNode5 node = root;
            node.count++;
            for (char c : word.toCharArray()) {
                node = node.children.computeIfAbsent(c, k -> new TrieNode5());
                node.count++;
            }
            node.wordEnd++;
        }

        void remove(String word) {
            TrieNode5 node = root;
            node.count--;
            for (char c : word.toCharArray()) {
                node = node.children.get(c);
                node.count--;
            }
            node.wordEnd--;
        }

        String kth(String prefix, int k) {
            TrieNode5 node = root;
            for (char c : prefix.toCharArray()) {
                node = node.children.get(c);
                if (node == null) return "NONE";
            }
            if (node.count < k) return "NONE";

            StringBuilder sb = new StringBuilder(prefix);
            return kt5FindKth(node, k, sb);
        }

        private String kt5FindKth(TrieNode5 node, int k, StringBuilder sb) {
            if (node.wordEnd > 0) {
                if (k <= node.wordEnd) return sb.toString();
                k -= node.wordEnd;
            }

            for (Map.Entry<Character, TrieNode5> e : node.children.entrySet()) {
                TrieNode5 child = e.getValue();
                if (child.count >= k) {
                    sb.append(e.getKey());
                    return kt5FindKth(child, k, sb);
                } else {
                    k -= child.count;
                }
            }

            return "NONE";
        }
    }

    public static void kt5Task5() {
        Trie5 trie = new Trie5();
        trie.add("apple");
        trie.add("app");
        trie.add("application");
        trie.add("apply");
        trie.add("banana");

        System.out.println(trie.kth("app", 1));
        System.out.println(trie.kth("app", 2));
        System.out.println(trie.kth("app", 3));
        System.out.println(trie.kth("app", 4));
        System.out.println(trie.kth("app", 5));
        System.out.println(trie.kth("ban", 1));
        trie.remove("app");
        System.out.println(trie.kth("app", 1));
    }


    //Задача 6
    static class BitTrie {
        BitTrie[] children = new BitTrie[2];

        void insert(int value) {
            BitTrie node = this;
            for (int bit = 29; bit >= 0; bit--) {
                int b = (value >> bit) & 1;
                if (node.children[b] == null) node.children[b] = new BitTrie();
                node = node.children[b];
            }
        }

        int maxXor(int value) {
            BitTrie node = this;
            int result = 0;
            for (int bit = 29; bit >= 0; bit--) {
                int b = (value >> bit) & 1;
                int preferred = 1 - b;
                if (node.children[preferred] != null) {
                    result |= (1 << bit);
                    node = node.children[preferred];
                } else {
                    node = node.children[b];
                }
            }
            return result;
        }
    }

    public static int kt5Task6(int[] a) {
        BitTrie trie = new BitTrie();
        trie.insert(0);

        int prefixXor = 0;
        int maxXor = 0;

        for (int x : a) {
            prefixXor ^= x;
            maxXor = Math.max(maxXor, trie.maxXor(prefixXor));
            trie.insert(prefixXor);
        }

        return maxXor;
    }


    //Тесты
    public static void main(String[] args) {
        System.out.println("Задача 1");
        kt5Task1(3);

        System.out.println("\nЗадача 2");
        kt5Task2();

        System.out.println("\nЗадача 3");
        System.out.println(Arrays.toString(kt5Task3(new int[]{2, 4, 3, 2, 1, 4})));
        System.out.println(Arrays.toString(kt5Task3(new int[]{5, 1, 5, 7})));
        System.out.println(Arrays.toString(kt5Task3(new int[]{10, 20})));

        System.out.println("\nЗадача 4");
        boolean[][] fb1 = new boolean[4][4];
        System.out.println(kt5Task4(4, fb1));

        boolean[][] fb2 = new boolean[8][8];
        System.out.println(kt5Task4(8, fb2));

        boolean[][] fb3 = new boolean[4][4];
        fb3[0][0] = true;
        System.out.println(kt5Task4(4, fb3));

        System.out.println("\nЗадача 5");
        kt5Task5();

        System.out.println("\nЗадача 6");
        System.out.println(kt5Task6(new int[]{1, 2, 3, 4}));
        System.out.println(kt5Task6(new int[]{8, 1, 2, 12, 7, 6}));
        System.out.println(kt5Task6(new int[]{5, 1, 7, 3}));
    }
}