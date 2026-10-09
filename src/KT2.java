import java.io.*;
import java.util.*;

public class KT2 {

    //Задача 1
    public static int kt2Task1(int[] nums, int target) {
        int left = 0, right = nums.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) left = mid + 1;
            else right = mid;
        }
        return left;
    }


    //Задача 2
    public static long[] kt2BuildPrefix(int[] nums) {
        long[] prefix = new long[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        return prefix;
    }

    public static long kt2Task2(long[] prefix, int L, int R) {
        return prefix[R + 1] - prefix[L];
    }


    //Задача 3
    public static List<int[]> kt2Task3(int[][] intervals) {
        List<int[]> result = new ArrayList<>();
        if (intervals.length == 0) return result;

        int[][] sorted = intervals.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[0], b[0]));

        int i = 0;
        while (i < sorted.length) {
            int start = sorted[i][0];
            int end = sorted[i][1];

            while (i + 1 < sorted.length && sorted[i + 1][0] <= end) {
                i++;
                end = Math.max(end, sorted[i][1]);
            }

            result.add(new int[]{start, end});
            i++;
        }

        return result;
    }


    //Задача 4
    public static long kt2Task4(int[] weights, int days) {
        long left = 0, right = 0;
        for (int w : weights) {
            left = Math.max(left, w);
            right += w;
        }

        while (left < right) {
            long mid = left + (right - left) / 2;
            if (kt2CanShip(weights, days, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private static boolean kt2CanShip(int[] weights, int days, long capacity) {
        int daysNeeded = 1;
        long currentLoad = 0;

        for (int w : weights) {
            if (currentLoad + w > capacity) {
                daysNeeded++;
                currentLoad = 0;
            }
            currentLoad += w;
        }

        return daysNeeded <= days;
    }


    //Задача 5
    public static long kt2Task5(int[] nums, long k) {
        Map<Long, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0L, 1);

        long prefixSum = 0;
        long count = 0;

        for (int num : nums) {
            prefixSum += num;
            count += prefixCount.getOrDefault(prefixSum - k, 0);
            prefixCount.put(prefixSum, prefixCount.getOrDefault(prefixSum, 0) + 1);
        }

        return count;
    }


    //Задача 6
    public static int kt2Task6(int[][] meetings) {
        int n = meetings.length;
        if (n == 0) return 0;

        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = meetings[i][0];
            ends[i] = meetings[i][1];
        }

        Arrays.sort(starts);
        Arrays.sort(ends);

        int rooms = 0, maxRooms = 0, j = 0;

        for (int i = 0; i < n; i++) {
            if (starts[i] < ends[j]) {
                rooms++;
            } else {
                j++;
            }
            maxRooms = Math.max(maxRooms, rooms);
        }

        return maxRooms;
    }


    //Тесты
    public static void main(String[] args) {
        System.out.println("Задача 1");
        System.out.println(kt2Task1(new int[]{1, 3, 5, 6}, 5));
        System.out.println(kt2Task1(new int[]{1, 3, 5, 6}, 2));
        System.out.println(kt2Task1(new int[]{1, 3, 5, 6}, 7));
        System.out.println(kt2Task1(new int[]{}, 5));

        System.out.println("\nЗадача 2");
        int[] nums2 = {3, 1, 4, 1, 5, 9};
        long[] prefix2 = kt2BuildPrefix(nums2);
        System.out.println(kt2Task2(prefix2, 0, 5));
        System.out.println(kt2Task2(prefix2, 2, 4));
        System.out.println(kt2Task2(prefix2, 3, 3));

        System.out.println("\nЗадача 3");
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        for (int[] iv : kt2Task3(intervals)) {
            System.out.println(Arrays.toString(iv));
        }

        System.out.println("\nЗадача 4");
        System.out.println(kt2Task4(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5));
        System.out.println(kt2Task4(new int[]{3, 2, 2, 4, 1, 4}, 3));
        System.out.println(kt2Task4(new int[]{1, 2, 3, 1, 1}, 4));

        System.out.println("\nЗадача 5");
        System.out.println(kt2Task5(new int[]{1, 1, 1}, 2));
        System.out.println(kt2Task5(new int[]{1, -1, 0}, 0));
        System.out.println(kt2Task5(new int[]{1, 2, 3}, 3));

        System.out.println("\nЗадача 6");
        System.out.println(kt2Task6(new int[][]{{0, 30}, {5, 10}, {15, 20}}));
        System.out.println(kt2Task6(new int[][]{{7, 10}, {2, 4}}));
        System.out.println(kt2Task6(new int[][]{{1, 5}, {5, 10}, {10, 15}}));
    }
}