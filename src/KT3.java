import java.io.*;
import java.util.*;

public class KT3 {

    //Задача 1
    public static int[] kt3Task1(int[] prices) {
        int n = prices.length;
        int[] result = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && prices[i] > prices[stack.peek()]) {
                int idx = stack.pop();
                result[idx] = i - idx;
            }
            stack.push(i);
        }
        return result;
    }


    //Задача 2
    public static int kt3Task2(char[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int[][] dist = new int[n][m];
        for (int[] row : dist) Arrays.fill(row, -1);

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        int[] target = null;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 'S') {
                    dist[i][j] = 0;
                    queue.add(new int[]{i, j});
                } else if (grid[i][j] == 'T') {
                    target = new int[]{i, j};
                }
            }
        }

        int[] di = {-1, 1, 0, 0};
        int[] dj = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int ci = cur[0], cj = cur[1];

            if (target != null && ci == target[0] && cj == target[1]) {
                return dist[ci][cj];
            }

            for (int d = 0; d < 4; d++) {
                int ni = ci + di[d];
                int nj = cj + dj[d];

                if (ni < 0 || ni >= n || nj < 0 || nj >= m) continue;
                if (grid[ni][nj] == '#') continue;
                if (dist[ni][nj] != -1) continue;

                dist[ni][nj] = dist[ci][cj] + 1;
                queue.add(new int[]{ni, nj});
            }
        }

        return -1;
    }


    //Задача 3
    public static boolean kt3Task3(int[] next, int start) {
        int slow = start - 1;
        int fast = start - 1;

        while (true) {
            slow = next[slow];
            if (slow == -1) return false;

            fast = next[fast];
            if (fast == -1) return false;
            fast = next[fast];
            if (fast == -1) return false;

            if (slow == fast) return true;
        }
    }


    //Задача 4
    public static int kt3Task4(char[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[] heights = new int[m];
        int maxArea = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (matrix[i][j] == '1') heights[j]++;
                else heights[j] = 0;
            }
            maxArea = Math.max(maxArea, kt3LargestRectangle(heights));
        }

        return maxArea;
    }

    private static int kt3LargestRectangle(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int maxArea = 0;
        int m = heights.length;

        for (int i = 0; i <= m; i++) {
            int h = (i == m) ? 0 : heights[i];

            while (!stack.isEmpty() && heights[stack.peek()] > h) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }

            stack.push(i);
        }

        return maxArea;
    }


    //Задача 5
    public static int kt3Task5(char[][] grid) {
        int n = grid.length;
        int m = grid[0].length;

        int si = -1, sj = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 'S') {
                    si = i;
                    sj = j;
                }
            }
        }

        int[][][] dist = new int[n][m][1024];
        for (int[][] layer : dist)
            for (int[] row : layer)
                Arrays.fill(row, -1);

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{si, sj, 0});
        dist[si][sj][0] = 0;

        int[] di = {-1, 1, 0, 0};
        int[] dj = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int ci = cur[0], cj = cur[1], mask = cur[2];
            char cell = grid[ci][cj];

            if (cell == 'T') {
                return dist[ci][cj][mask];
            }

            for (int d = 0; d < 4; d++) {
                int ni = ci + di[d];
                int nj = cj + dj[d];

                if (ni < 0 || ni >= n || nj < 0 || nj >= m) continue;

                char nextCell = grid[ni][nj];
                if (nextCell == '#') continue;

                int newMask = mask;

                if (nextCell >= 'a' && nextCell <= 'j') {
                    newMask |= (1 << (nextCell - 'a'));
                }

                if (nextCell >= 'A' && nextCell <= 'J') {
                    int keyBit = 1 << (nextCell - 'A');
                    if ((mask & keyBit) == 0) continue;
                }

                if (dist[ni][nj][newMask] != -1) continue;

                dist[ni][nj][newMask] = dist[ci][cj][mask] + 1;
                queue.add(new int[]{ni, nj, newMask});
            }
        }

        return -1;
    }


    //Задача 6
    public static int kt3Task6(int[] nums) {
        int slow = nums[0];
        int fast = nums[0];

        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);

        slow = nums[0];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }

        return slow;
    }


    //Тесты
    public static void main(String[] args) {
        System.out.println("Задача 1");
        System.out.println(Arrays.toString(kt3Task1(new int[]{73, 74, 75, 71, 69, 72, 76, 73})));
        System.out.println(Arrays.toString(kt3Task1(new int[]{30, 40, 50, 60})));
        System.out.println(Arrays.toString(kt3Task1(new int[]{30, 60, 90})));

        System.out.println("\nЗадача 2");
        char[][] grid2 = {
                {'S', '.', '.', '#'},
                {'.', '#', '.', '.'},
                {'.', '.', '.', 'T'}
        };
        System.out.println(kt3Task2(grid2));

        char[][] grid2b = {
                {'S', '#'},
                {'#', 'T'}
        };
        System.out.println(kt3Task2(grid2b));

        System.out.println("\nЗадача 3");
        int[] next1 = {-1, 2, 3, 4, 2};
        System.out.println(kt3Task3(new int[]{1, 2, 3, 1, -1}, 1) ? "YES" : "NO");
        System.out.println(kt3Task3(new int[]{1, 2, 3, -1, -1}, 1) ? "YES" : "NO");
        System.out.println(kt3Task3(new int[]{1, 1}, 1) ? "YES" : "NO");

        System.out.println("\nЗадача 4");
        char[][] grid4 = {
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}
        };
        System.out.println(kt3Task4(grid4));

        char[][] grid4b = {
                {'1'}
        };
        System.out.println(kt3Task4(grid4b));

        System.out.println("\nЗадача 5");
        char[][] grid5 = {
                {'S', '.', 'a', '.', '.'},
                {'.', '#', '#', '#', '.'},
                {'.', '.', '.', '.', 'A'},
                {'.', '#', '.', '#', '.'},
                {'.', '.', '.', '.', 'T'}
        };
        System.out.println(kt3Task5(grid5));

        char[][] grid5b = {
                {'S', 'A', 'T'}
        };
        System.out.println(kt3Task5(grid5b));

        System.out.println("\nЗадача 6");
        System.out.println(kt3Task6(new int[]{1, 3, 4, 2, 2}));
        System.out.println(kt3Task6(new int[]{3, 1, 3, 4, 2}));
        System.out.println(kt3Task6(new int[]{1, 1, 2}));
    }
}