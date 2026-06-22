import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.util.concurrent.*;

/**
 * Comprehensive Java Interview Coding Solutions (80 Questions)
 * For 5-6 years experienced Java developers
 */
public class JavaCodingInterviewSolutions {

    // ============================================
    // LINKED LIST PROBLEMS (1-7)
    // ============================================

    // 1. Reverse a Linked List
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    public static ListNode reverseListIterative(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        while (current != null) {
            ListNode nextTemp = current.next;
            current.next = prev;
            prev = current;
            current = nextTemp;
        }
        return prev;
    }

    public static ListNode reverseListRecursive(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseListRecursive(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    // 2. Detect and Remove Cycle in Linked List
    public static boolean hasCycle(ListNode head) {
        if (head == null) return false;
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    public static void removeCycle(ListNode head) {
        if (head == null) return;
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                ListNode ptr1 = head, ptr2 = slow;
                while (ptr1.next != ptr2.next) {
                    ptr1 = ptr1.next;
                    ptr2 = ptr2.next;
                }
                ptr2.next = null;
                break;
            }
        }
    }

    // 3. Find Middle of Linked List
    public static ListNode findMiddle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // 4. Merge Two Sorted Linked Lists
    public static ListNode mergeSortedLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                current.next = l1;
                l1 = l1.next;
            } else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }
        current.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    // 5. Intersection Point of Two Linked Lists
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode a = headA, b = headB;
        while (a != b) {
            a = (a == null) ? headB : a.next;
            b = (b == null) ? headA : b.next;
        }
        return a;
    }

    // 6. Palindrome Linked List
    public static boolean isPalindromeList(ListNode head) {
        if (head == null) return true;
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode secondHalf = reverseListIterative(slow);
        ListNode firstHalf = head;
        while (secondHalf != null) {
            if (firstHalf.val != secondHalf.val) return false;
            firstHalf = firstHalf.next;
            secondHalf = secondHalf.next;
        }
        return true;
    }

    // 7. Flatten a Multilevel Doubly Linked List
    public static class Node {
        int val;
        Node prev, next, child;
        Node(int x) { val = x; }
    }

    public static Node flatten(Node head) {
        if (head == null) return null;
        Node current = head;
        while (current != null) {
            if (current.child != null) {
                Node next = current.next;
                Node flattened = flatten(current.child);
                current.next = flattened;
                flattened.prev = current;
                Node tail = flattened;
                while (tail.next != null) tail = tail.next;
                tail.next = next;
                if (next != null) next.prev = tail;
                current.child = null;
            }
            current = current.next;
        }
        return head;
    }

    // ============================================
    // ARRAY & STRING PROBLEMS (8-18)
    // ============================================

    // 8. Two Sum
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{};
    }

    // 9. Three Sum
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int left = i + 1, right = nums.length - 1;
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

    // 10. Longest Substring Without Repeating Characters
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int maxLen = 0, start = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (map.containsKey(c) && map.get(c) >= start) {
                start = map.get(c) + 1;
            }
            map.put(c, i);
            maxLen = Math.max(maxLen, i - start + 1);
        }
        return maxLen;
    }

    // 11. Longest Palindromic Substring
    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 1) return "";
        int start = 0, maxLen = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > maxLen) {
                maxLen = len;
                start = i - (len - 1) / 2;
            }
        }
        return s.substring(start, start + maxLen);
    }

    private static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    // 12. Merge Overlapping Intervals
    public static int[][] merge(int[][] intervals) {
        if (intervals.length == 0) return new int[0][0];
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> merged = new ArrayList<>();
        merged.add(intervals[0]);
        for (int i = 1; i < intervals.length; i++) {
            int[] last = merged.get(merged.size() - 1);
            if (intervals[i][0] <= last[1]) {
                last[1] = Math.max(last[1], intervals[i][1]);
            } else {
                merged.add(intervals[i]);
            }
        }
        return merged.toArray(new int[0][0]);
    }

    // 13. Rotate Array by k positions
    public static void rotateArray(int[] nums, int k) {
        k = k % nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    private static void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }

    // 14. Container With Most Water
    public static int maxArea(int[] height) {
        int left = 0, right = height.length - 1, maxArea = 0;
        while (left < right) {
            int area = Math.min(height[left], height[right]) * (right - left);
            maxArea = Math.max(maxArea, area);
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }

    // 15. Trapping Rain Water
    public static int trap(int[] height) {
        if (height == null || height.length == 0) return 0;
        int[] leftMax = new int[height.length];
        int[] rightMax = new int[height.length];
        leftMax[0] = height[0];
        for (int i = 1; i < height.length; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }
        rightMax[height.length - 1] = height[height.length - 1];
        for (int i = height.length - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }
        int water = 0;
        for (int i = 0; i < height.length; i++) {
            water += Math.min(leftMax[i], rightMax[i]) - height[i];
        }
        return water;
    }

    // 16. Word Search in Grid
    public static boolean existWordInGrid(char[][] board, String word) {
        if (board == null || board.length == 0) return false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (dfs(board, word, 0, i, j)) return true;
            }
        }
        return false;
    }

    private static boolean dfs(char[][] board, String word, int index, int i, int j) {
        if (index == word.length()) return true;
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) return false;
        if (board[i][j] != word.charAt(index)) return false;
        char temp = board[i][j];
        board[i][j] = '*';
        boolean result = dfs(board, word, index + 1, i + 1, j) ||
                        dfs(board, word, index + 1, i - 1, j) ||
                        dfs(board, word, index + 1, i, j + 1) ||
                        dfs(board, word, index + 1, i, j - 1);
        board[i][j] = temp;
        return result;
    }

    // 17. Find All Duplicates in Array
    public static List<Integer> findDuplicates(int[] nums) {
        List<Integer> result = new ArrayList<>();
        for (int num : nums) {
            int index = Math.abs(num) - 1;
            if (nums[index] < 0) {
                result.add(Math.abs(num));
            } else {
                nums[index] = -nums[index];
            }
        }
        return result;
    }

    // 18. Remove Duplicates from Sorted Array
    public static int removeDuplicates(int[] nums) {
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }

    // ============================================
    // TREE & GRAPH PROBLEMS (19-28)
    // ============================================

    public static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int x) { val = x; }
    }

    // 19. Binary Tree Level Order Traversal (BFS)
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            result.add(level);
        }
        return result;
    }

    // 20. Binary Tree Inorder, Preorder, Postorder Traversal (DFS)
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private static void inorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.val);
        inorderHelper(node.right, result);
    }

    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }

    private static void preorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        result.add(node.val);
        preorderHelper(node.left, result);
        preorderHelper(node.right, result);
    }

    public static List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorderHelper(root, result);
        return result;
    }

    private static void postorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        postorderHelper(node.left, result);
        postorderHelper(node.right, result);
        result.add(node.val);
    }

    // 21. Lowest Common Ancestor (LCA) of Binary Tree
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) return root;
        return (left != null) ? left : right;
    }

    // 22. Validate Binary Search Tree
    public static boolean isValidBST(TreeNode root) {
        return validateBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static boolean validateBST(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;
        return validateBST(node.left, min, node.val) &&
               validateBST(node.right, node.val, max);
    }

    // 23. Serialize and Deserialize Binary Tree
    public static String serializeTree(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializeHelper(root, sb);
        return sb.toString();
    }

    private static void serializeHelper(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("null,");
            return;
        }
        sb.append(node.val).append(",");
        serializeHelper(node.left, sb);
        serializeHelper(node.right, sb);
    }

    public static TreeNode deserializeTree(String data) {
        List<String> nodes = new ArrayList<>(Arrays.asList(data.split(",")));
        return deserializeHelper(nodes);
    }

    private static TreeNode deserializeHelper(List<String> nodes) {
        if (nodes.isEmpty() || nodes.get(0).equals("null")) {
            if (!nodes.isEmpty()) nodes.remove(0);
            return null;
        }
        TreeNode root = new TreeNode(Integer.parseInt(nodes.remove(0)));
        root.left = deserializeHelper(nodes);
        root.right = deserializeHelper(nodes);
        return root;
    }

    // 24. Diameter of Binary Tree
    public static int diameterOfBinaryTree(TreeNode root) {
        int[] diameter = {0};
        heightOfTree(root, diameter);
        return diameter[0];
    }

    private static int heightOfTree(TreeNode node, int[] diameter) {
        if (node == null) return 0;
        int leftHeight = heightOfTree(node.left, diameter);
        int rightHeight = heightOfTree(node.right, diameter);
        diameter[0] = Math.max(diameter[0], leftHeight + rightHeight);
        return Math.max(leftHeight, rightHeight) + 1;
    }

    // 25. Path Sum
    public static boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) return false;
        if (root.left == null && root.right == null) {
            return root.val == targetSum;
        }
        return hasPathSum(root.left, targetSum - root.val) ||
               hasPathSum(root.right, targetSum - root.val);
    }

    // 26. Word Ladder (BFS)
    public static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> words = new HashSet<>(wordList);
        if (!words.contains(endWord)) return 0;
        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);
        Set<String> visited = new HashSet<>();
        visited.add(beginWord);
        int level = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String current = queue.poll();
                if (current.equals(endWord)) return level;
                for (String neighbor : getNeighbors(current, words)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
            level++;
        }
        return 0;
    }

    private static List<String> getNeighbors(String word, Set<String> words) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char oldChar = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                chars[i] = c;
                String newWord = new String(chars);
                if (words.contains(newWord)) {
                    neighbors.add(newWord);
                }
            }
            chars[i] = oldChar;
        }
        return neighbors;
    }

    // 27. Course Schedule (Detect cycle in directed graph)
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        List<Integer>[] graph = new List[numCourses];
        for (int i = 0; i < numCourses; i++) graph[i] = new ArrayList<>();
        for (int[] pre : prerequisites) graph[pre[1]].add(pre[0]);
        int[] color = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            if (hasCycleDFS(i, graph, color)) return false;
        }
        return true;
    }

    private static boolean hasCycleDFS(int node, List<Integer>[] graph, int[] color) {
        if (color[node] == 1) return true;
        if (color[node] == 2) return false;
        color[node] = 1;
        for (int neighbor : graph[node]) {
            if (hasCycleDFS(neighbor, graph, color)) return true;
        }
        color[node] = 2;
        return false;
    }

    // 28. Clone a Graph
    public static class GraphNode {
        int val;
        List<GraphNode> neighbors;
        GraphNode(int x) { val = x; neighbors = new ArrayList<>(); }
    }

    public static GraphNode cloneGraph(GraphNode node) {
        if (node == null) return null;
        Map<GraphNode, GraphNode> map = new HashMap<>();
        return cloneGraphDFS(node, map);
    }

    private static GraphNode cloneGraphDFS(GraphNode node, Map<GraphNode, GraphNode> map) {
        if (map.containsKey(node)) return map.get(node);
        GraphNode clone = new GraphNode(node.val);
        map.put(node, clone);
        for (GraphNode neighbor : node.neighbors) {
            clone.neighbors.add(cloneGraphDFS(neighbor, map));
        }
        return clone;
    }

    // ============================================
    // HASH MAP & COLLECTIONS PROBLEMS (29-38)
    // ============================================

    // 30. Group Anagrams
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
        }
        return new ArrayList<>(map.values());
    }

    // 31. Valid Anagram
    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        char[] sChars = s.toCharArray();
        char[] tChars = t.toCharArray();
        Arrays.sort(sChars);
        Arrays.sort(tChars);
        return Arrays.equals(sChars, tChars);
    }

    // 32. First Unique Character in String
    public static int firstUniqueChar(String s) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : s.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        for (int i = 0; i < s.length(); i++) {
            if (map.get(s.charAt(i)) == 1) return i;
        }
        return -1;
    }

    // 33. Majority Element
    public static int majorityElement(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        int majority = nums.length / 2;
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
            if (map.get(num) > majority) return num;
        }
        return -1;
    }

    // 34. LRU Cache
    public static class LRUCache {
        private int capacity;
        private LinkedHashMap<Integer, Integer> cache;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry eldest) {
                    return size() > LRUCache.this.capacity;
                }
            };
        }

        public int get(int key) {
            return cache.getOrDefault(key, -1);
        }

        public void put(int key, int value) {
            cache.put(key, value);
        }
    }

    // 35. LFU Cache
    public static class LFUCache {
        private int capacity, minFreq;
        private Map<Integer, Integer> keyToVal;
        private Map<Integer, Integer> keyToFreq;
        private Map<Integer, LinkedHashSet<Integer>> freqToKeys;

        public LFUCache(int capacity) {
            this.capacity = capacity;
            this.minFreq = 0;
            this.keyToVal = new HashMap<>();
            this.keyToFreq = new HashMap<>();
            this.freqToKeys = new HashMap<>();
        }

        public int get(int key) {
            if (!keyToVal.containsKey(key)) return -1;
            updateFreq(key);
            return keyToVal.get(key);
        }

        public void put(int key, int value) {
            if (capacity <= 0) return;
            if (keyToVal.containsKey(key)) {
                keyToVal.put(key, value);
                updateFreq(key);
                return;
            }
            if (keyToVal.size() >= capacity) {
                int evict = freqToKeys.get(minFreq).iterator().next();
                freqToKeys.get(minFreq).remove(evict);
                keyToVal.remove(evict);
                keyToFreq.remove(evict);
            }
            keyToVal.put(key, value);
            keyToFreq.put(key, 1);
            freqToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
            minFreq = 1;
        }

        private void updateFreq(int key) {
            int freq = keyToFreq.get(key);
            keyToFreq.put(key, freq + 1);
            freqToKeys.get(freq).remove(key);
            if (freqToKeys.get(freq).isEmpty() && freq == minFreq) {
                minFreq++;
            }
            freqToKeys.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(key);
        }
    }

    // 36. Design a HashMap
    public static class MyHashMap {
        private static final int SIZE = 10000;
        private LinkedList<int[]>[] buckets;

        public MyHashMap() {
            buckets = new LinkedList[SIZE];
            for (int i = 0; i < SIZE; i++) {
                buckets[i] = new LinkedList<>();
            }
        }

        private int hash(int key) {
            return key % SIZE;
        }

        public void put(int key, int value) {
            int index = hash(key);
            for (int[] pair : buckets[index]) {
                if (pair[0] == key) {
                    pair[1] = value;
                    return;
                }
            }
            buckets[index].offer(new int[]{key, value});
        }

        public int get(int key) {
            int index = hash(key);
            for (int[] pair : buckets[index]) {
                if (pair[0] == key) return pair[1];
            }
            return -1;
        }

        public void remove(int key) {
            int index = hash(key);
            buckets[index].removeIf(pair -> pair[0] == key);
        }
    }

    // 37. Design HashSet
    public static class MyHashSet {
        private static final int SIZE = 10000;
        private LinkedList<Integer>[] buckets;

        public MyHashSet() {
            buckets = new LinkedList[SIZE];
            for (int i = 0; i < SIZE; i++) {
                buckets[i] = new LinkedList<>();
            }
        }

        private int hash(int key) {
            return key % SIZE;
        }

        public void add(int key) {
            int index = hash(key);
            if (!buckets[index].contains(key)) {
                buckets[index].add(key);
            }
        }

        public void remove(int key) {
            int index = hash(key);
            buckets[index].remove((Integer) key);
        }

        public boolean contains(int key) {
            int index = hash(key);
            return buckets[index].contains(key);
        }
    }

    // 38. Frequency of Characters/Numbers in a list
    public static Map<Integer, Integer> getFrequency(List<Integer> list) {
        return list.stream()
                .collect(Collectors.groupingBy(
                    Function.identity(),
                    Collectors.summingInt(e -> 1)
                ));
    }

    // ============================================
    // SORTING & SEARCHING PROBLEMS (39-45)
    // ============================================

    // 39. Merge Sort
    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length == 0) return;
        mergeSortHelper(arr, 0, arr.length - 1);
    }

    private static void mergeSortHelper(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(arr, left, mid);
            mergeSortHelper(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        while (i <= mid) temp[k++] = arr[i++];
        while (j <= right) temp[k++] = arr[j++];
        System.arraycopy(temp, 0, arr, left, temp.length);
    }

    // 40. Quick Sort
    public static void quickSort(int[] arr) {
        if (arr == null || arr.length == 0) return;
        quickSortHelper(arr, 0, arr.length - 1);
    }

    private static void quickSortHelper(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSortHelper(arr, low, pi - 1);
            quickSortHelper(arr, pi + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    // 41. Binary Search
    public static int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    // 42. Search in Rotated Sorted Array
    public static int searchInRotatedArray(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) return mid;
            if (nums[left] <= nums[mid]) {
                if (target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (target > nums[mid] && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }

    // 43. Find K Largest Elements
    public static List<Integer> findKLargest(int[] nums, int k) {
        return Arrays.stream(nums)
                .boxed()
                .sorted(Collections.reverseOrder())
                .limit(k)
                .collect(Collectors.toList());
    }

    // 44. Find Kth Smallest Element
    public static int findKthSmallest(int[] nums, int k) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        for (int num : nums) {
            maxHeap.offer(num);
            if (maxHeap.size() > k) maxHeap.poll();
        }
        return maxHeap.peek();
    }

    // 45. Merge K Sorted Lists
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);
        for (ListNode list : lists) {
            if (list != null) minHeap.offer(list);
        }
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        while (!minHeap.isEmpty()) {
            ListNode node = minHeap.poll();
            current.next = node;
            current = current.next;
            if (node.next != null) minHeap.offer(node.next);
        }
        return dummy.next;
    }

    // ============================================
    // DYNAMIC PROGRAMMING PROBLEMS (46-53)
    // ============================================

    // 46. Fibonacci Series - Memoization & Tabulation
    public static int fibonacciMemoization(int n) {
        Map<Integer, Integer> memo = new HashMap<>();
        return fibHelper(n, memo);
    }

    private static int fibHelper(int n, Map<Integer, Integer> memo) {
        if (n <= 1) return n;
        if (memo.containsKey(n)) return memo.get(n);
        int result = fibHelper(n - 1, memo) + fibHelper(n - 2, memo);
        memo.put(n, result);
        return result;
    }

    public static int fibonacciTabulation(int n) {
        if (n <= 1) return n;
        int[] dp = new int[n + 1];
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    // 47. Coin Change Problem
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // 48. Edit Distance (Levenshtein Distance)
    public static int editDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], 
                               Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }
        return dp[m][n];
    }

    // 49. Longest Common Subsequence (LCS)
    public static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    // 50. Longest Increasing Subsequence (LIS)
    public static int lengthOfLIS(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        return Arrays.stream(dp).max().orElse(1);
    }

    // 51. 0/1 Knapsack Problem
    public static int knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(
                        values[i - 1] + dp[i - 1][w - weights[i - 1]],
                        dp[i - 1][w]
                    );
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        return dp[n][capacity];
    }

    // 52. Jump Game
    public static boolean canJump(int[] nums) {
        int maxReach = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false;
            maxReach = Math.max(maxReach, i + nums[i]);
            if (maxReach >= nums.length - 1) return true;
        }
        return false;
    }

    // 53. Unique Paths in grid
    public static int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) dp[i][0] = 1;
        for (int j = 0; j < n; j++) dp[0][j] = 1;
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }

    // ============================================
    // JAVA STREAMS PROBLEMS (54-70)
    // ============================================

    // 54. Find Second Highest Number from a list
    public static Integer findSecondHighest(List<Integer> list) {
        return list.stream()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst()
                .orElse(null);
    }

    // 55. Group by Department and count employees
    public static class Employee {
        String name, department;
        double salary;
        Employee(String name, String dept, double sal) {
            this.name = name;
            this.department = dept;
            this.salary = sal;
        }
        public String getDepartment() { return department; }
    }

    public static Map<String, Long> groupByDepartment(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                    Employee::getDepartment,
                    Collectors.counting()
                ));
    }

    // 56. Find All Duplicate Elements
    public static List<Integer> findDuplicatesStream(List<Integer> list) {
        return list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 57. Partition a List based on condition
    public static Map<Boolean, List<Integer>> partitionList(List<Integer> list) {
        return list.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
    }

    // 58. Find Maximum and Minimum
    public static Integer findMax(List<Integer> list) {
        return list.stream()
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    public static Integer findMin(List<Integer> list) {
        return list.stream()
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    // 59. Convert List to Map
    public static Map<String, Employee> convertListToMap(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.toMap(e -> e.name, Function.identity()));
    }

    // 60. Filter and Transform
    public static List<Integer> filterAndTransform(List<Integer> list) {
        return list.stream()
                .filter(n -> n > 5)
                .map(n -> n * 2)
                .collect(Collectors.toList());
    }

    // 61. Flatten Nested Lists using flatMap
    public static List<Integer> flattenLists(List<List<Integer>> listOfLists) {
        return listOfLists.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    // 62. Find Common Elements between two lists
    public static List<Integer> findCommonElements(List<Integer> list1, List<Integer> list2) {
        return list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList());
    }

    // 63. Remove Duplicates from list
    public static List<Integer> removeDuplicatesStream(List<Integer> list) {
        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    // 64. Find Top N Highest/Lowest values
    public static List<Integer> findTopN(List<Integer> list, int n) {
        return list.stream()
                .sorted(Comparator.reverseOrder())
                .limit(n)
                .collect(Collectors.toList());
    }

    // 65. Sum, Average, Count using streams
    public static int sumOfList(List<Integer> list) {
        return list.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public static OptionalDouble averageOfList(List<Integer> list) {
        return list.stream()
                .mapToInt(Integer::intValue)
                .average();
    }

    public static long countOfList(List<Integer> list) {
        return list.stream().count();
    }

    // 66. Check if All/Any/None match condition
    public static boolean allMatch(List<Integer> list) {
        return list.stream().allMatch(n -> n > 0);
    }

    public static boolean anyMatch(List<Integer> list) {
        return list.stream().anyMatch(n -> n > 0);
    }

    public static boolean noneMatch(List<Integer> list) {
        return list.stream().noneMatch(n -> n < 0);
    }

    // 67. Skip and Limit elements
    public static List<Integer> skipAndLimit(List<Integer> list, int skip, int limit) {
        return list.stream()
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toList());
    }

    // 68. Reduce Operation
    public static Integer reduceSum(List<Integer> list) {
        return list.stream()
                .reduce(0, Integer::sum);
    }

    public static Optional<Integer> reduceWithoutInitial(List<Integer> list) {
        return list.stream()
                .reduce(Integer::sum);
    }

    // 69. Sorting with Multiple Criteria
    public static List<Employee> sortByMultipleCriteria(List<Employee> employees) {
        return employees.stream()
                .sorted(Comparator.comparing(Employee::getDepartment)
                                   .thenComparing(e -> e.salary))
                .collect(Collectors.toList());
    }

    // 70. Collecting to Different Collections
    public static void collectingExamples(List<Integer> list) {
        List<Integer> toList = list.stream().collect(Collectors.toList());
        Set<Integer> toSet = list.stream().collect(Collectors.toSet());
        LinkedList<Integer> toLinkedList = list.stream()
                .collect(Collectors.toCollection(LinkedList::new));
        String joined = list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    // ============================================
    // CONCURRENCY PROBLEMS (71-75)
    // ============================================

    // 71. Print "FooBar" alternately using two threads
    public static class FooBar {
        private int n;
        private volatile int state = 0;

        public FooBar(int n) { this.n = n; }

        public void foo() throws InterruptedException {
            for (int i = 0; i < n; i++) {
                synchronized (this) {
                    while (state != 0) this.wait();
                    System.out.print("foo");
                    state = 1;
                    this.notifyAll();
                }
            }
        }

        public void bar() throws InterruptedException {
            for (int i = 0; i < n; i++) {
                synchronized (this) {
                    while (state != 1) this.wait();
                    System.out.print("bar");
                    state = 0;
                    this.notifyAll();
                }
            }
        }
    }

    // 72. Print Numbers in Order using 3 threads
    public static class ZeroEvenOdd {
        private int n;
        private volatile int state = 0;

        public ZeroEvenOdd(int n) { this.n = n; }

        public void zero() throws InterruptedException {
            for (int i = 1; i <= n; i++) {
                synchronized (this) {
                    while (state != 0) this.wait();
                    System.out.print("0");
                    state = i % 2 == 1 ? 1 : 2;
                    this.notifyAll();
                }
            }
        }

        public void even() throws InterruptedException {
            for (int i = 2; i <= n; i += 2) {
                synchronized (this) {
                    while (state != 2) this.wait();
                    System.out.print(i);
                    state = 0;
                    this.notifyAll();
                }
            }
        }

        public void odd() throws InterruptedException {
            for (int i = 1; i <= n; i += 2) {
                synchronized (this) {
                    while (state != 1) this.wait();
                    System.out.print(i);
                    state = 0;
                    this.notifyAll();
                }
            }
        }
    }

    // 73. Thread-Safe Counter
    public static class ThreadSafeCounter {
        private int count = 0;

        public synchronized void increment() { count++; }
        public synchronized void decrement() { count--; }
        public synchronized int getCount() { return count; }
    }

    // 74. Producer-Consumer pattern
    public static class ProducerConsumer {
        private BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);

        public void produce(int value) throws InterruptedException {
            queue.put(value);
            System.out.println("Produced: " + value);
        }

        public int consume() throws InterruptedException {
            int value = queue.take();
            System.out.println("Consumed: " + value);
            return value;
        }
    }

    // 75. Design a Thread Pool (simplified)
    public static class SimpleThreadPool {
        private BlockingQueue<Runnable> taskQueue;
        private List<Thread> workers;

        public SimpleThreadPool(int poolSize) {
            taskQueue = new LinkedBlockingQueue<>();
            workers = new ArrayList<>();
            for (int i = 0; i < poolSize; i++) {
                Thread worker = new Thread(() -> {
                    try {
                        while (true) {
                            Runnable task = taskQueue.take();
                            task.run();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
                worker.start();
                workers.add(worker);
            }
        }

        public void execute(Runnable task) throws InterruptedException {
            taskQueue.put(task);
        }
    }

    // ============================================
    // OTHER PRACTICAL PROBLEMS (76-80)
    // ============================================

    // 76. Design a Rate Limiter
    public static class RateLimiter {
        private int maxRequests;
        private long timeWindowMillis;
        private Queue<Long> requestTimes = new LinkedList<>();

        public RateLimiter(int maxRequests, long timeWindowMillis) {
            this.maxRequests = maxRequests;
            this.timeWindowMillis = timeWindowMillis;
        }

        public synchronized boolean allowRequest() {
            long now = System.currentTimeMillis();
            while (!requestTimes.isEmpty() && 
                   requestTimes.peek() + timeWindowMillis <= now) {
                requestTimes.poll();
            }
            if (requestTimes.size() < maxRequests) {
                requestTimes.offer(now);
                return true;
            }
            return false;
        }
    }

    // 77. Find Most Frequent Visitor from log file
    public static String findMostFrequentVisitor(List<String> logs) {
        return logs.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // 79. Design a Key-Value Store
    public static class KeyValueStore {
        private Map<String, String> store = new ConcurrentHashMap<>();

        public void put(String key, String value) {
            store.put(key, value);
        }

        public String get(String key) {
            return store.getOrDefault(key, null);
        }

        public void remove(String key) {
            store.remove(key);
        }

        public boolean exists(String key) {
            return store.containsKey(key);
        }
    }

    // 80. Implement Autocomplete system
    public static class AutocompleteSystem {
        private Map<String, Integer> frequency = new HashMap<>();
        private String currentSentence = "";

        public void input(char c) {
            if (c == '#') {
                frequency.put(currentSentence, 
                    frequency.getOrDefault(currentSentence, 0) + 1);
                currentSentence = "";
            } else {
                currentSentence += c;
            }
        }

        public List<String> getTopSuggestions(int k) {
            return frequency.entrySet().stream()
                    .filter(e -> e.getKey().startsWith(currentSentence))
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .limit(k)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    }

    // ============================================
    // MAIN METHOD - DEMONSTRATION
    // ============================================

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Java Interview Coding Solutions ===");
        System.out.println("Total: 80 Coding Interview Questions with Solutions");
        System.out.println();

        System.out.println("Categories:");
        System.out.println("1. Linked List Problems (7)");
        System.out.println("2. Array & String Problems (11)");
        System.out.println("3. Tree & Graph Problems (10)");
        System.out.println("4. Hash Map & Collections (10)");
        System.out.println("5. Sorting & Searching (7)");
        System.out.println("6. Dynamic Programming (8)");
        System.out.println("7. Java Streams (17)");
        System.out.println("8. Concurrency (5)");
        System.out.println("9. Other Practical Problems (5)");
        System.out.println();

        // Quick Demo
        System.out.println("=== Quick Demo ===");
        
        // Test Streams
        List<Integer> numbers = Arrays.asList(5, 10, 3, 8, 20, 15);
        System.out.println("Numbers: " + numbers);
        System.out.println("Second Highest: " + findSecondHighest(numbers));
        System.out.println("Duplicates: " + findDuplicatesStream(Arrays.asList(1, 2, 2, 3, 3, 4)));
        System.out.println("Top 3: " + findTopN(numbers, 3));
        
        System.out.println("\n=== All 80 solutions ready for interviews! ===");
    }
}
