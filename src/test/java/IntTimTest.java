

import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;


public class IntTimTest {


    /**
     * Test random data
     */
    @Test
    public void testRandom() {
        int[] array  = {
            73,83,60,87,93,
                    48,81,64,46,36,
                    21,81,77,73,1,
                    10,84,76,20,93,
                    49,9,33,78,50,
        };
        Hashtable<Integer, Integer> before = getFrequencyTable(array);

        IntTimSort.sort(array);
        sanity(array, before);
    }

    /**
     * Test sorted data
     */
    @Test
    public void testSorted() {
        int[] array = { 1,2,3,4,5,6,7,8,
                9,10,11,12,13,14,
                15,16,17,18,19,20,
                21,22,23,24,25,26,
                27,28,29,30};
        Hashtable<Integer, Integer> before = getFrequencyTable(array);
        IntTimSort.sort(array);
        sanity(array, before);
    }


    /**
     * Test
     */
    @Test
    public void testReversed() {
        int[] array = {120,99,94,83,81
                ,77,72,62,59,58,53
                ,48,46,29,18,16,14
                ,13,2,1};

        Hashtable<Integer, Integer> before = getFrequencyTable(array);
        IntTimSort.sort(array);
        sanity(array, before);
    }


    /**
     * Failing test case
     */
    @Test
    public void fail1() {
        fail();
    }






    private static void sanity(int[] array, Hashtable<Integer, Integer> before) {

        Comparator<Integer> cmp = new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return         i1.compareTo(i2);
            }
        };

        assertTrue(isSorted(array));

        // check frequencies

        Hashtable<Integer, Integer> after = getFrequencyTable(array);
        for (Integer i: array) {
            assertEquals(before.get(i), after.get(i));
        }
    }


    // sanity checks
    private  static  boolean isSorted(int[] array) {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return integer.compareTo(t1);
            }
        };
        for (int i = 0; i < array.length - 1; ++i) {
            if (comparator.compare(array[i], (array[i + 1])) > 0)
                return false;
        }

        return true;

    }

    private static Hashtable<Integer, Integer> getFrequencyTable(int[] array) {

        Hashtable<Integer, Integer> map = new Hashtable<>();
        for (Integer i: array) {


            if(map.containsKey(i)) {
                map.put(i, map.get(i) + 1);
            } else {
                map.put(i, 1);
            }
        }
        return  map;

    }

}
