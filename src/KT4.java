import java.io.*;
import java.util.*;

public class KT4 {

    //Задача 1
    public static long[] kt4Task1(int[] value, int[] left, int[] right) {
        int n = value.length;
        List<Long> sums = new ArrayList<>();

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(0);

        while (!queue.isEmpty()) {
            int size = queue.size();
            long levelSum = 0;

            for (int i = 0; i < size; i++) {
                int v = queue.poll();
                levelSum += value[v];

                if (left[v] != -1) queue.add(left[v]);
                if (right[v] != -1) queue.add(right[v]);
            }

            sums.add(levelSum);
        }

        long[] result = new long[sums.size()];
        for (int i = 0; i < sums.size(); i++) result[i] = sums.get(i);
        return result;
    }


    //Задача 2
    public static int[] kt4Task2(int[] a, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int x : a) freq.merge(x, 1, Integer::sum);

        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(freq.entrySet());
        list.sort((e1, e2) -> {
            int cmp = Integer.compare(e2.getValue(), e1.getValue());
            if (cmp != 0) return cmp;
            return Integer.compare(e1.getKey(), e2.getKey());
        });

        int[] result = new int[k];
        for (int i = 0; i < k; i++) result[i] = list.get(i).getKey();
        return result;
    }


    //Задача 3
    public static double[] kt4Task3(int[] a) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        double[] result = new double[a.length];

        for (int i = 0; i < a.length; i++) {
            int x = a[i];

            if (maxHeap.isEmpty() || x <= maxHeap.peek()) {
                maxHeap.add(x);
            } else {
                minHeap.add(x);
            }

            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.add(maxHeap.poll());
            } else if (minHeap.size() > maxHeap.size()) {
                maxHeap.add(minHeap.poll());
            }

            if (maxHeap.size() == minHeap.size()) {
                result[i] = (maxHeap.peek() + (long) minHeap.peek()) / 2.0;
            } else {
                result[i] = maxHeap.peek();
            }
        }

        return result;
    }


    //Задача 4
    private static long kt4Best;

    public static long kt4Task4(int[] value, int[] left, int[] right) {
        kt4Best = Long.MIN_VALUE;
        kt4Dfs(0, value, left, right);
        return kt4Best;
    }

    private static long kt4Dfs(int v, int[] value, int[] left, int[] right) {
        if (v == -1) return 0;

        long leftGain = Math.max(0, kt4Dfs(left[v], value, left, right));
        long rightGain = Math.max(0, kt4Dfs(right[v], value, left, right));

        long throughV = value[v] + leftGain + rightGain;
        kt4Best = Math.max(kt4Best, throughV);

        return value[v] + Math.max(leftGain, rightGain);
    }


    //Задача 5
    public static long kt4Task5(int[] A, int[] B, int k) {
        long lo = (long) A[0] + B[0];
        long hi = (long) A[A.length - 1] + B[B.length - 1];

        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (kt4CountPairs(A, B, mid) >= k) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }

        return lo;
    }

    private static long kt4CountPairs(int[] A, int[] B, long limit) {
        long count = 0;
        int j = B.length - 1;

        for (int i = 0; i < A.length; i++) {
            while (j >= 0 && (long) A[i] + B[j] > limit) {
                j--;
            }
            if (j < 0) break;
            count += j + 1;
        }

        return count;
    }


    //Задача 6
    public static double[] kt4Task6(int[] a, int k) {
        int n = a.length;
        if (k > n) return new double[0];

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        Map<Integer, Integer> toDelete = new HashMap<>();

        double[] result = new double[n - k + 1];

        for (int i = 0; i < k; i++) maxHeap.add(a[i]);
        for (int i = 0; i < k / 2; i++) minHeap.add(maxHeap.poll());

        result[0] = kt4Median(maxHeap, minHeap, k);

        for (int i = k; i < n; i++) {
            int out = a[i - k];
            int in = a[i];

            toDelete.merge(out, 1, Integer::sum);

            int balance = 0;
            if (!maxHeap.isEmpty() && in <= maxHeap.peek()) {
                maxHeap.add(in);
                balance++;
            } else {
                minHeap.add(in);
                balance--;
            }

            if (!maxHeap.isEmpty() && out <= maxHeap.peek()) {
                balance--;
            } else {
                balance++;
            }

            if (balance > 0) {
                minHeap.add(maxHeap.poll());
                balance--;
            } else if (balance < 0) {
                maxHeap.add(minHeap.poll());
                balance++;
            }

            kt4Clean(maxHeap, toDelete);
            kt4Clean(minHeap, toDelete);

            result[i - k + 1] = kt4Median(maxHeap, minHeap, k);
        }

        return result;
    }

    private static void kt4Clean(PriorityQueue<Integer> heap, Map<Integer, Integer> toDelete) {
        while (!heap.isEmpty()) {
            int top = heap.peek();
            Integer cnt = toDelete.get(top);
            if (cnt == null || cnt == 0) break;
            if (cnt == 1) toDelete.remove(top); else toDelete.put(top, cnt - 1);
            heap.poll();
        }
    }

    private static double kt4Median(PriorityQueue<Integer> maxHeap,
                                    PriorityQueue<Integer> minHeap, int k) {
        if (k % 2 == 1) {
            return maxHeap.peek();
        } else {
            return (maxHeap.peek() + (long) minHeap.peek()) / 2.0;
        }
    }


    //Тесты
    public static void main(String[] args) {
        System.out.println("Задача 1");
        int[] v1 = {5, 3, 8, 1, 4};
        int[] l1 = {1, 3, -1, -1, -1};
        int[] r1 = {2, 4, -1, -1, -1};
        System.out.println(Arrays.toString(kt4Task1(v1, l1, r1)));

        System.out.println("\nЗадача 2");
        int[] a2 = {1, 1, 1, 2, 2, 3};
        System.out.println(Arrays.toString(kt4Task2(a2, 2)));
        int[] a2b = {4, 4, 4, 2, 2, 3, 3, 3};
        System.out.println(Arrays.toString(kt4Task2(a2b, 3)));

        System.out.println("\nЗадача 3");
        int[] a3 = {1, 2, 3, 4, 5};
        double[] m3 = kt4Task3(a3);
        for (double d : m3) System.out.printf("%.1f ", d);
        System.out.println();

        System.out.println("\nЗадача 4");
        int[] v4 = {-10, 9, 20, 15, 7};
        int[] l4 = {1, -1, 3, -1, -1};
        int[] r4 = {2, -1, 4, -1, -1};
        System.out.println(kt4Task4(v4, l4, r4));

        System.out.println("\nЗадача 5");
        int[] A5 = {1, 7, 11};
        int[] B5 = {2, 4, 6};
        System.out.println(kt4Task5(A5, B5, 3));
        System.out.println(kt4Task5(A5, B5, 1));
        System.out.println(kt4Task5(A5, B5, 9));

        System.out.println("\nЗадача 6");
        int[] a6 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k6 = 3;
        double[] m6 = kt4Task6(a6, k6);
        for (double d : m6) System.out.printf("%.1f ", d);
        System.out.println();
    }
}