package net.maesierra.adventOfCode2025.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionHelpers {

        // Helper function to find all combinations
        // of size r in an array of size n
        // data Temporary array to store current combination
        private static <E> void combinationUtil(List<E> items, int index, int n, List<E> data,
                                            List<List<E>> result) {

            // If size of current combination is r
            if (data.size() == n) {
                result.add(new ArrayList<>(data));
                return;
            }

            // Replace index with all possible elements
            for (int i = index; i < items.size(); i++) {
                // Current element is included
                data.add(items.get(i));
                // Recur for next elements
                combinationUtil(items, i + 1, n, data, result);
                // Backtrack to find other combinations
                data.removeLast();
            }
        }

        // Function to find all combinations of size r
        // in an array of size n
        public static <E> List<List<E>> findCombinations(List<E> items, int n) {

            // to store the result
            List<List<E>> result = new ArrayList<>();
            combinationUtil(items, 0, n, new ArrayList<>(), result);
            return result;
        }

}
