import java.util.*;

public class KT1 {

    //Задача 1
    public static int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        Set<Integer> set = new HashSet<>();
        for (int num : nums) set.add(num);

        int longest = 0;

        for (int num : set) {
            if (!set.contains(num - 1)) {
                int current = num;
                int length = 1;

                while (set.contains(current + 1)) {
                    current++;
                    length++;
                }

                longest = Math.max(longest, length);
            }
        }

        return longest;
    }


    //Задача 2
    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) return result;

        Arrays.sort(nums);
        int n = nums.length;

        for (int i = 0; i < n - 2; i++) {
            if (nums[i] > 0) break;

            if (i > 0 && nums[i] == nums[i - 1]) continue;

            int left = i + 1;
            int right = n - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];

                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));

                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;

                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }

        return result;
    }


    //Задача 3
    public static int totalFruit(int[] items) {
        if (items == null || items.length == 0) return 0;

        Map<Integer, Integer> window = new HashMap<>();
        int left = 0;
        int maxLength = 0;

        for (int right = 0; right < items.length; right++) {
            window.put(items[right], window.getOrDefault(items[right], 0) + 1);

            while (window.size() > 2) {
                int leftItem = items[left];
                int count = window.get(leftItem);

                if (count == 1) {
                    window.remove(leftItem);
                } else {
                    window.put(leftItem, count - 1);
                }
                left++;
            }

            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }


    //Задача 4
    public static int subarraySum(int[] nums, int k) {
        if (nums == null || nums.length == 0) return 0;

        Map<Long, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0L, 1);

        long prefixSum = 0;
        int count = 0;

        for (int num : nums) {
            prefixSum += num;
            count += prefixCount.getOrDefault(prefixSum - k, 0);
            prefixCount.put(prefixSum, prefixCount.getOrDefault(prefixSum, 0) + 1);
        }

        return count;
    }


    //Задача 5
    public static long maxArea(int[] height) {
        if (height == null || height.length < 2) return 0;

        int left = 0;
        int right = height.length - 1;
        long maxArea = 0;

        while (left < right) {
            long h = Math.min(height[left], height[right]);
            long width = right - left;
            long area = h * width;

            if (area > maxArea) maxArea = area;

            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }


    //Задача 6
    public static String minWindow(String source, String target) {
        if (source == null || target == null
                || source.isEmpty() || target.isEmpty()
                || source.length() < target.length()) {
            return "";
        }

        int[] need = new int[128];
        for (char c : target.toCharArray()) need[c]++;

        int required = 0;
        for (int count : need) if (count > 0) required++;

        int[] window = new int[128];
        int formed = 0;

        int left = 0;
        int bestLen = Integer.MAX_VALUE;
        int bestStart = 0;

        for (int right = 0; right < source.length(); right++) {
            char c = source.charAt(right);
            window[c]++;

            if (need[c] > 0 && window[c] == need[c]) formed++;

            while (formed == required) {
                int currentLen = right - left + 1;
                if (currentLen < bestLen) {
                    bestLen = currentLen;
                    bestStart = left;
                }

                char leftChar = source.charAt(left);
                window[leftChar]--;

                if (need[leftChar] > 0 && window[leftChar] < need[leftChar]) {
                    formed--;
                }
                left++;
            }
        }

        return bestLen == Integer.MAX_VALUE
                ? ""
                : source.substring(bestStart, bestStart + bestLen);
    }


    //Тесты
    public static void main(String[] args) {
        System.out.println("Задача 1");
        System.out.println(longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
        System.out.println(longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}));
        System.out.println(longestConsecutive(new int[]{}));

        System.out.println("\nЗадача 2");
        System.out.println(threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
        System.out.println(threeSum(new int[]{0, 0, 0, 0}));
        System.out.println(threeSum(new int[]{1, 2, -2, -1}));

        System.out.println("\nЗадача 3");
        System.out.println(totalFruit(new int[]{1, 2, 1}));
        System.out.println(totalFruit(new int[]{0, 1, 2, 2}));
        System.out.println(totalFruit(new int[]{1, 2, 3, 2, 2}));

        System.out.println("\nЗадача 4");
        System.out.println(subarraySum(new int[]{1, 1, 1}, 2));
        System.out.println(subarraySum(new int[]{1, -1, 0}, 0));

        System.out.println("\nЗадача 5");
        System.out.println(maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}));
        System.out.println(maxArea(new int[]{1, 1}));

        System.out.println("\nЗадача 6");
        System.out.println("\"" + minWindow("ADOBECODEBANC", "ABC") + "\"");
        System.out.println("\"" + minWindow("a", "aa") + "\"");
        System.out.println("\"" + minWindow("AAABBC", "AABC") + "\"");
    }
}