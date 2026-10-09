import java.io.*;
import java.util.*;

public class KT10 {

    //Задача 1
    public static int kt10Task1(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));

        int count = 0;
        long lastEnd = Long.MIN_VALUE;

        for (int[] iv : intervals) {
            if (iv[0] >= lastEnd) {
                count++;
                lastEnd = iv[1];
            }
        }

        return count;
    }


    //Задача 2
    static class MinMaxStack {
        private final Deque<Integer> values = new ArrayDeque<>();
        private final Deque<Integer> mins = new ArrayDeque<>();
        private final Deque<Integer> maxs = new ArrayDeque<>();

        void push(int x) {
            values.push(x);
            mins.push(mins.isEmpty() ? x : Math.min(x, mins.peek()));
            maxs.push(maxs.isEmpty() ? x : Math.max(x, maxs.peek()));
        }

        int pop() {
            mins.pop();
            maxs.pop();
            return values.pop();
        }

        int min() { return mins.peek(); }
        int max() { return maxs.peek(); }
        boolean isEmpty() { return values.isEmpty(); }
    }

    public static void kt10Task2(String[] commands) {
        MinMaxStack stack = new MinMaxStack();
        StringBuilder sb = new StringBuilder();

        for (String cmd : commands) {
            String[] parts = cmd.split(" ");
            switch (parts[0]) {
                case "PUSH" -> stack.push(Integer.parseInt(parts[1]));
                case "POP" -> stack.pop();
                case "MIN" -> sb.append(stack.min()).append('\n');
                case "MAX" -> sb.append(stack.max()).append('\n');
            }
        }

        System.out.print(sb);
    }


    //Задача 3
    public static int kt10Task3(int[] a, int k) {
        return kt10Quickselect(a, 0, a.length - 1, k - 1);
    }

    private static int kt10Quickselect(int[] a, int left, int right, int k) {
        while (left < right) {
            int pivotIndex = kt10Partition(a, left, right);

            if (pivotIndex == k) {
                return a[k];
            } else if (pivotIndex < k) {
                left = pivotIndex + 1;
            } else {
                right = pivotIndex - 1;
            }
        }
        return a[left];
    }

    private static int kt10Partition(int[] a, int left, int right) {
        int pivotIdx = left + (int) (Math.random() * (right - left + 1));
        int pivot = a[pivotIdx];
        kt10Swap(a, pivotIdx, right);

        int store = left;
        for (int i = left; i < right; i++) {
            if (a[i] < pivot) {
                kt10Swap(a, i, store++);
            }
        }
        kt10Swap(a, store, right);
        return store;
    }

    private static void kt10Swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }


    //Задача 4
    public static long kt10Task4(int[][] orders) {
        int n = orders.length;
        Arrays.sort(orders, (a, b) -> Long.compare(b[1], a[1]));

        int[] deadlines = new int[n];
        for (int i = 0; i < n; i++) deadlines[i] = orders[i][0];
        int[] sorted = deadlines.clone();
        Arrays.sort(sorted);
        int uniqueCount = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) sorted[uniqueCount++] = sorted[i];
        }
        int[] unique = Arrays.copyOf(sorted, uniqueCount);
        int maxSlot = uniqueCount;
        int[] parent = new int[maxSlot + 1];
        for (int i = 0; i <= maxSlot; i++) parent[i] = i;

        long profit = 0;

        for (int[] order : orders) {
            int d = order[0];
            long p = order[1];

            int pos = Arrays.binarySearch(unique, d) + 1;

            int slot = kt10Find(parent, pos);
            if (slot == 0) continue;

            profit += p;
            parent[slot] = kt10Find(parent, slot - 1);
        }

        return profit;
    }

    private static int kt10Find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = kt10Find(parent, parent[x]);
        return parent[x];
    }


    //Задача 5
    static class LFUCache {
        static class Node {
            int key, value, freq;
            Node prev, next;
            Node(int k, int v) { key = k; value = v; freq = 1; }
        }

        static class DoublyList {
            Node head = new Node(-1, -1), tail = new Node(-1, -1);
            int size = 0;
            DoublyList() {
                head.next = tail;
                tail.prev = head;
            }
            void addFirst(Node node) {
                node.next = head.next;
                node.prev = head;
                head.next.prev = node;
                head.next = node;
                size++;
            }
            void remove(Node node) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                size--;
            }
            Node removeLast() {
                if (size == 0) return null;
                Node last = tail.prev;
                remove(last);
                return last;
            }
        }

        private final int capacity;
        private final Map<Integer, Node> keyToNode = new HashMap<>();
        private final Map<Integer, DoublyList> freqToList = new HashMap<>();
        private int minFreq = 0;

        LFUCache(int capacity) {
            this.capacity = capacity;
        }

        int get(int key) {
            if (!keyToNode.containsKey(key)) return -1;
            Node node = keyToNode.get(key);
            kt10IncreaseFreq(node);
            return node.value;
        }

        void put(int key, int value) {
            if (capacity == 0) return;

            if (keyToNode.containsKey(key)) {
                Node node = keyToNode.get(key);
                node.value = value;
                kt10IncreaseFreq(node);
            } else {
                if (keyToNode.size() == capacity) {
                    DoublyList list = freqToList.get(minFreq);
                    Node evict = list.removeLast();
                    keyToNode.remove(evict.key);
                }
                Node node = new Node(key, value);
                keyToNode.put(key, node);
                freqToList.computeIfAbsent(1, k -> new DoublyList()).addFirst(node);
                minFreq = 1;
            }
        }

        private void kt10IncreaseFreq(Node node) {
            int oldFreq = node.freq;
            DoublyList oldList = freqToList.get(oldFreq);
            oldList.remove(node);

            if (oldList.size == 0) {
                freqToList.remove(oldFreq);
                if (minFreq == oldFreq) minFreq++;
            }

            node.freq++;
            freqToList.computeIfAbsent(node.freq, k -> new DoublyList()).addFirst(node);
        }
    }

    public static void kt10Task5() {
        LFUCache cache = new LFUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));
        cache.put(3, 3);
        System.out.println(cache.get(2));
        System.out.println(cache.get(3));
        cache.put(4, 4);
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));
    }


    //Задача 6
    public static void kt10Task6(int n, int k, long[][] points) {
        Comparator<long[]> cmp = (a, b) -> {
            long da = a[0] * a[0] + a[1] * a[1];
            long db = b[0] * b[0] + b[1] * b[1];
            if (da != db) return Long.compare(da, db);
            if (a[0] != b[0]) return Long.compare(a[0], b[0]);
            if (a[1] != b[1]) return Long.compare(a[1], b[1]);
            return Long.compare(a[2], b[2]);
        };

        PriorityQueue<long[]> heap = new PriorityQueue<>((a, b) -> cmp.compare(b, a));

        for (int i = 0; i < n; i++) {
            long[] point = {points[i][0], points[i][1], i + 1};
            heap.add(point);
            if (heap.size() > k) heap.poll();
        }

        List<long[]> result = new ArrayList<>(heap);
        result.sort(cmp);

        StringBuilder sb = new StringBuilder();
        for (long[] p : result) {
            sb.append(p[0]).append(' ').append(p[1]).append(' ').append(p[2]).append('\n');
        }
        System.out.print(sb);
    }


    //Тесты
    public static void main(String[] args) {
        System.out.println("Задача 1");
        System.out.println(kt10Task1(new int[][]{{1, 3}, {2, 5}, {3, 6}, {5, 7}}));
        System.out.println(kt10Task1(new int[][]{{1, 2}, {2, 3}, {3, 4}}));
        System.out.println(kt10Task1(new int[][]{{1, 10}, {2, 3}, {4, 5}}));

        System.out.println("\nЗадача 2");
        kt10Task2(new String[]{
                "PUSH 5", "PUSH 2", "PUSH 8", "MIN", "MAX", "POP", "MIN", "MAX"
        });

        System.out.println("\nЗадача 3");
        System.out.println(kt10Task3(new int[]{3, 1, 4, 1, 5, 9, 2, 6}, 3));
        System.out.println(kt10Task3(new int[]{5, 4, 3, 2, 1}, 1));
        System.out.println(kt10Task3(new int[]{5, 4, 3, 2, 1}, 5));

        System.out.println("\nЗадача 4");
        System.out.println(kt10Task4(new int[][]{
                {4, 20}, {1, 10}, {1, 40}, {1, 30}
        }));

        System.out.println(kt10Task4(new int[][]{
                {2, 100}, {1, 19}, {2, 27}, {1, 25}, {3, 15}
        }));

        System.out.println("\nЗадача 5");
        kt10Task5();

        System.out.println("\nЗадача 6");
        kt10Task6(5, 3, new long[][]{
                {1, 1}, {3, 3}, {2, 2}, {0, 0}, {5, 5}
        });
    }
}