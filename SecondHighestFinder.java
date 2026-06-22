import java.util.*;
import java.util.stream.Collectors;

public class SecondHighestFinder {
    
    /**
     * Finds the second highest number from each sublist
     * @param listOfLists - List containing multiple sublists of numbers
     * @return List of second highest numbers from each sublist
     */
    public static List<Integer> findSecondHighestFromEachSublist(List<List<Integer>> listOfLists) {
        return listOfLists.stream()
                .map(sublist -> sublist.stream()
                        .sorted(Comparator.reverseOrder())
                        .skip(1)
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    /**
     * Alternative approach: Find second highest from all numbers across all sublists
     * @param listOfLists - List containing multiple sublists of numbers
     * @return The second highest number from all elements
     */
    public static Integer findSecondHighestOverall(List<List<Integer>> listOfLists) {
        return listOfLists.stream()
                .flatMap(List::stream)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst()
                .orElse(null);
    }
    
    public static void main(String[] args) {
        // Test Case 1: Find second highest from each sublist
        List<List<Integer>> listOfLists = Arrays.asList(
                Arrays.asList(5, 10, 3, 8),
                Arrays.asList(20, 15, 25, 12),
                Arrays.asList(7, 2, 9, 4)
        );
        
        System.out.println("Original List of Lists:");
        listOfLists.forEach(System.out::println);
        
        System.out.println("\nSecond Highest from each sublist:");
        List<Integer> result = findSecondHighestFromEachSublist(listOfLists);
        System.out.println(result);
        
        // Test Case 2: Find second highest overall
        System.out.println("\nSecond Highest Overall (from all elements):");
        Integer secondHighestOverall = findSecondHighestOverall(listOfLists);
        System.out.println(secondHighestOverall);
        
        // Test Case 3: With duplicates
        List<List<Integer>> listWithDuplicates = Arrays.asList(
                Arrays.asList(5, 10, 10, 3),
                Arrays.asList(20, 20, 25, 12)
        );
        
        System.out.println("\n\nWith Duplicates:");
        listWithDuplicates.forEach(System.out::println);
        System.out.println("Second Highest from each sublist:");
        System.out.println(findSecondHighestFromEachSublist(listWithDuplicates));
        System.out.println("Second Highest Overall (distinct values):");
        System.out.println(findSecondHighestOverall(listWithDuplicates));
    }
}
