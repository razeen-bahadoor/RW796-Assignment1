
public class IntTimSort {

    private static int MIN_RUN_LENGTH = 1200;
    private static  int G_THRESHOLD = 7;
    private static int STACK_SIZE = 20;

    private int gthreshold;
    private Stack stack;
    private int[] array;



    public IntTimSort(int[] array) {
        this.array = array;
        stack = new Stack(STACK_SIZE);
        gthreshold = G_THRESHOLD;

    }


    /**
     * sort the given array
     * @param array work array
     */
    public static void sort(int[] array) {
        MIN_RUN_LENGTH = 1200;
        G_THRESHOLD = 7;
        if (array.length <= MIN_RUN_LENGTH && array.length > 1) {
            binaryInsertionSort(array, 0, array.length - 1, 0);
        } else {
            IntTimSort sort = new IntTimSort(array);
            sort(sort, array);
            int mid = 0;
            while (sort.stack.size() > 1) {
                mid = sort.stack.size() - 2;
                if(mid > 0 && sort.stack.length(mid - 1)  < sort.stack.length(mid) ) {
                    sort.premergeProcedure(mid, mid - 1, true);
                } else {
                    sort.premergeProcedure(mid, mid + 1, true);
                }
            }
        }
    }

    /**
     * sort the array
     * @param sort timsort instance
     * @param array work array
     */
    private static void sort(IntTimSort sort, int[] array) {
        int runLength = 0;
        int start = 0;
        int end = 0;
        int length = array.length;
        int minrun = getRunLength(length);
        while (length != 0) {
            if (end + 1 >= array.length) {
                break;
            }
            while (array[end] == array[end + 1]) {
                end++;
                if (end + 1 >= array.length) {
                    break;
                }
            }
            if (end + 1 >= array.length) {
                runLength = (end - start) + 1;
                runLength = makeUpRun(array, runLength, start, length,minrun);
            } else if (array[end] > array[end + 1]) {
                while (array[end] >= array[end + 1]) {

                    end++;
                    if (end + 1 >= array.length) {
                        break;
                    }
                }
                reverse(array, start, end);
                runLength = (end - start) + 1;
                runLength = makeUpRun(array, runLength, start, length,minrun);
            } else if (array[end] < array[end + 1]) {

                while (array[end] <= array[end + 1]) {

                    end++;
                    if (end + 1 >= array.length) {
                        break;
                    }
                }
                runLength = (end - start) + 1;
                runLength = makeUpRun(array, runLength, start, length,minrun);
            }
            sort.stack.pushRun(start, runLength);
            start += runLength;
            end = start;
            length -= runLength;
            sort.invariant();

        }
    }

    /**
     * add elemnts to degenerate runs
     * @param array work array
     * @param runLength current length of the run
     * @param start starting index
     * @param length length of the run
     * @param minrun minrun length
     * @return new runlength
     */
    private static int makeUpRun(int[] array, int runLength, int start, int length, int minrun) {
        if (runLength < minrun) {
            int temp = runLength;
            if (length <= minrun) {
                runLength = length;
            } else {
                runLength = minrun;
            }
            binaryInsertionSort(array, start, start + runLength - 1, start + temp);
        }
        return runLength;
    }

    /**
     * binary search
     * @param array work array
     * @param low starting index
     * @param high ending index
     * @param item item
     * @return position of item
     */
    public static int binarySearch(int[] array, int low, int high, int item) {
        while (low < high) {
            int mid = low + high >>> 1;
            if (array[mid] == item) {
                return mid + 1;
            } else  if (item < array[mid]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return (item > array[low]) ?
                (low + 1) : low;
    }



    /**
     * Binary Insertion Sort done on specified section of the data array
     *
     * @param data       array containing data that gets sorted
     * @param min        the min index of section to be sorted (inclusive)
     * @param max        the max index of section to be sorted (inclusive)
     * @param start      the first index of the unsorted part of the array
     */
    public static void binaryInsertionSort(int[] data, int min, int max, int start) {
        assert min <= start && start <= max;
        for (int i = start; i < max + 1; i++) {
            int temp = data[i];
            int loc = binarySearch(data, min, i, temp);
            for (int j = i - 1; j >= loc; j--) {
                data[j+1] = data[j];
            }
            data[loc] = temp;
        }
    }




    /**
     * Invariant conditions.
     */
    private void invariant() {
        while(true) {
            if(stack.size() > 1) {
                int mid = stack.size()-2;
                if(mid > 0) {
                    if(stack.length(mid -1) <= stack.length(mid) + stack.length(mid+1)) {
                        if(stack.length(mid-1) < stack.length(mid+1)) {
                            premergeProcedure(mid-1 , mid,  true);
                        } else {
                            premergeProcedure(mid, mid+1, false);
                        }
                    } else {
                        if (stack.size() > 2 && stack.length(mid) <= stack.length(mid+1)) {
                            premergeProcedure(mid, mid + 1, false);
                        } else {
                            return;
                        }
                    }
                } else {
                    if (stack.size() > 2 && stack.length(mid) <= stack.length(mid+1)) {
                        premergeProcedure(mid, mid + 1, false);
                    } else {
                        return;
                    }
                }
            } else {
                break;
            }
        }








    }

    /**
     * setup for merge.
     * @param subarray1 index of array on stack
     * @param subarray2 index of array on stack
     * @param merge_flag
     */
    private void premergeProcedure(int subarray1, int subarray2, boolean merge_flag) {
        int bidx1 = stack.base(subarray1);
        int bidx2 = stack.base(subarray2);
        int len1 = stack.length(subarray1);
        int len2 = stack.length(subarray2);
        if(merge_flag) {
            stack.shift(subarray2+1, subarray2);
            stack.updateLength(subarray1, len1+len2);
        } else {
            stack.updateLength(subarray1, len1+len2);
        }
        stack.decrementSize();
        merge(bidx1, len1, bidx2, len2);
    }

    /**
     * Perform initial gallop.
     * @param bidx1 base index of subarray 1
     * @param len1 length of sub array 1
     * @param bidx2 base index of subarray 2
     * @param len2 length of subarray 2
     */
    public void merge(int bidx1, int len1, int bidx2, int len2) {
        int second = rightGallop(array[bidx2], array, bidx1,len1);
        int first = leftGallop(array[bidx1+len1-1], array,bidx2, len2);
        bidx1 = bidx1 + second;
        len1 = len1 - second;
        len2 = first ;
        if (len1 != 0 && len2 != 0) {
            if (len1 <= len2) {
                mergeBegin(array, bidx1, len1, bidx2, len2);
            } else {
                mergeEnd(array, bidx1, len1, bidx2, len2);
            }
        }
    }

    /**
     * Merge end, when len1 < len2
     * @param array work array
     * @param bidx1 base index of subarray 1
     * @param len1 length of sub array 1
     * @param bidx2 base index of subarray 2
     * @param len2 length of subarray 2
     */
    private void mergeBegin(int[] array, int bidx1, int len1, int bidx2, int len2) {
        int[] aux = new int[len1];
        // copy to aux
        copy(array, bidx1, aux, 0, len1);
        array[bidx1] = array[bidx2];
        len2 = len2-1;
        if(len2 == 0) {
            // copy remainder
            copy(aux, 0, array, bidx1+1, len1);
        } else if (len1 == 1) {
            copy(array, bidx2+1, array, bidx1+1, len2-1);
            // insert last of first
            array[bidx2+len2-1] = aux[0];
        } else {
            int aux_idx =0;
            int main = bidx1+1;
            int idx = bidx2+1;
            int threshold = this.gthreshold;
            major:
            while(true) {
                int swin1 =0;
                int swin2 =0;
                while (swin1 <= threshold || swin2 <= threshold) {
                    if (aux[aux_idx] <= array[idx]) {
                        array[main++] = aux[aux_idx++];
                        len1--;
                        swin1++;
                        swin2=0;
                        if (len1 == 1) {
                            copy(array, idx, array, main, len2);
                            array[main + len2] = aux[aux_idx];
                            break major;
                        }
                    } else {
                        array[main++] = array[idx++];
                        len2--;
                        swin2++;
                        swin1 =0;
                        if (len2 == 0) {
                            copy(aux, aux_idx, array, main, len1);
                            break major;
                        }
                    }
                }

                do {
                    // gallop second into first
                    int second = rightGallop(array[idx], aux, aux_idx, len1);
                    if(second != 0) {
                        copy(aux, aux_idx, array, main, second);
                        main = main + second;
                        len1 = len1 - second;
                        aux_idx = aux_idx + second;
                        if(len1 <= 1) {
                            copy(array, idx, array, main, len2);
                            array[main + len2] = aux[aux_idx];
                            break  major;
                        }
                    }
                    // copy second into position
                    array[main] = array[idx];
                    main++;
                    idx++;
                    len2--;
                    if(len2 == 0) {
                        copy(aux, aux_idx, array, main, len1);
                        break major;
                    }
                    // gallop end of first into second
                    int first = leftGallop(aux[aux_idx], array,idx, len2);
                    if(first != 0) {
                        copy(array, idx, array, main,first);
                        main = main + first;
                        len2 = len2 - first;
                        idx = idx + first;

                        if(len2 == 0) {
                            copy(aux, aux_idx, array, main, len1);
                            break  major;
                        }

                    }
                    array[main++] = aux[aux_idx++];
                    len1--;
                    if (len1 == 1) {
                        copy(array, idx, array, main, len2);
                        array[main + len2] = aux[aux_idx];
                        break major;
                    }

                    swin1 = second;
                    swin2 = first;
                    threshold = threshold-1;
                } while(swin1 >= G_THRESHOLD || swin2 >=G_THRESHOLD);

                // penalise g threhold
                if(threshold < 0) {
                    threshold = 0;
                }
                threshold = threshold + 4;

            }
            // refresh g threshold
            if (threshold < 1) {
                this.gthreshold =1;
            } else {
                this.gthreshold = threshold;
            }
        }
    }

    /**
     * Merge end, when len2 <= len1
     * @param array work array
     * @param bidx1 base index of subarray 1
     * @param len1 length of sub array 1
     * @param bidx2 base index of subarray 2
     * @param len2 length of subarray 2
     */
    private void mergeEnd(int[] array, int bidx1, int len1, int bidx2, int len2) {
        int[] aux = new int[len2];
        copy(array, bidx2, aux, 0, len2);
        // copy last of 1 to position
        array[bidx2+len2-1] = array[bidx1+len1-1];
        len1 = len1-1;
        if(len1 == 0) {
            copy(aux,0,array,bidx1,len2);
        } else if(len2 == 1) {
            // copy remaining array
            // move first of aux into place
            copy(array, bidx1,array,bidx1+1, len1);
        } else {
            int aux_idx = aux.length-1;
            int idx = bidx1 + len1-1;
            int main = bidx2+len2-2;
            int threshold = this.gthreshold;
            major:
            while(true) {
                int swin1 =0;
                int swin2 =0;
                while(swin1 <= G_THRESHOLD || swin2 <= G_THRESHOLD) {
                    if(aux[aux_idx] >= array[idx]) {
                        array[main] = aux[aux_idx];
                        main--;
                        aux_idx--;
                        len2--;
                        if(len2 == 1) {
                            // copy first of second
                            copy(array, bidx1, array, bidx1+1,len1);
                            array[bidx1] = aux[aux_idx];
                            break major;
                        }
                        swin2++;
                        swin1=0;
                    } else {
                        array[main--] = array[idx--];
                        len1--;
                        if (len1 == 0){
                            copy(aux,0,array, bidx1, len2);
                            break major;
                        }
                        swin1++;
                        swin2=0;
                    }
                }

                do {
                    // gallop second into position
                    int second = rightGallop(aux[aux_idx], array, bidx1, len1);
                    second = len1 - second ;
                    if(second != 0) {
                        copy(array, idx-second+1, array, main-second+1,second);
                        main = main - second;
                        len1 = len1 - second;
                        idx = idx - second;
                        // end of array 1
                        if(len1 == 0) {
                            copy(aux,0,array, bidx1, len2);
                            break major;
                        }
                    }
                    array[main--] = aux[aux_idx--];
                    len2 = len2-1;
                    if(len2 == 1) {
                        // break out and copy first element of 2 into position
                        copy(array, bidx1, array, bidx1+1,len1);
                        array[bidx1] = aux[aux_idx];
                        break major;
                    }
                    // gallop end of first into second
                    int first = len2 -  leftGallop(array[idx], aux, 0, len2);
                    if(first != 0) {
                        copy(aux, aux_idx-first+1, array, main-first+1, first);
                        main = main - first;
                        aux_idx = aux_idx -first;
                        len2 = len2 - first;
                        if(len2 <= 1) {
                            // break and copy first of 2 into position
                            copy(array, bidx1, array, bidx1+1,len1);
                            array[bidx1] = aux[aux_idx];
                            break  major;
                        }
                    }
                    // copy 1 into position
                    array[main--] = array[idx--];
                    len1 = len1 -1;
                    if(len1 == 0) {
                        copy(aux,0,array, bidx1, len2);
                        break  major;
                    }
                    swin1 = second;
                    swin2 = first;
                    threshold = threshold-1;
                } while(swin1 >= G_THRESHOLD || swin2 >=G_THRESHOLD);
                // penalise g threhold
                if(threshold < 0) {
                    threshold = 0;
                }
                threshold = threshold + 4;

            }
            if (threshold < 1) {
                this.gthreshold =1;
            } else {
                this.gthreshold = threshold;
            }
        }

    }

    /**
     * Left gallop.
     *
     * @param item  to gallop
     * @param array work array
     * @param bidx  base index in array
     * @param len   length of array
     * @return position
     */
    private static int leftGallop(int item, int[] array, int bidx, int len) {
        if(item < array[bidx]) {
            return  0;
        } else if(item > array[bidx+len-1]) {
            return len;
        } else {
            int pidx =0;
            int idx =1;
            while (idx < len && item > array[bidx+idx]) {
                pidx = idx;
                idx = (2*idx)+1;
                if(idx<=0) {
                    idx = len;
                }
            }
            if(idx > len) {
                idx = len;
            }
            while (pidx < idx) {
                int mid  = pidx + idx >> 1;

                if (item > array[bidx+mid]) {
                    pidx = mid + 1;
                } else {
                    idx = mid;
                }
            }
            return idx;


        }

    }

    /**
     * Right gallop.
     *
     * @param item  to gallop
     * @param array work array
     * @param bidx  base index in array
     * @param len   length of array
     * @return position
     */
    private static int rightGallop(int item, int[] array, int bidx, int len) {
        if (item < array[bidx]) {
            return 0;
        } else if (item > array[bidx + (len - 1)]) {
            return len;
        } else {
            int idx = 1;
            int pidx = 0;
            while (idx < len && item >= array[bidx + idx]) {

                pidx = idx;
                idx = (2 * idx) + 1;
                if (idx <= 0) {
                    idx = len;
                }
            }
            if (idx > len) {
                idx = len;
            }
            while (pidx < idx) {
                int mid = (pidx + idx) >> 1;
                if (item >= array[bidx+mid]) {
                    pidx = mid + 1;
                } else {
                    idx = mid;
                }
            }
            return idx;
        }
    }

    /**
     * Wrapper of System.array.copy
     *
     * @param src     source array
     * @param srcPos  source position
     * @param dest    destination array
     * @param destPos destination position
     * @param length  number of elements to copy
     */
    private static void copy(int[] src, int srcPos, int[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    /**
     * Compute the min run length such that size/run length is a power of 2 or approximately
     *
     * @param array_size size of the current array
     * @return min run length
     */
    private static int getRunLength(int array_size) {
        int flag = 0;
        while (array_size >= MIN_RUN_LENGTH) {

            flag = flag | (array_size & 1);
            array_size >>>= 1; // has the ffect of dividing by 2
        }
        return array_size + flag;
    }

    /**
     * Reverse a section of the array.
     *
     * @param array work array.
     * @param low   starting index
     * @param high  ending index
     */
    public static void reverse(int[] array, int low, int high) {
        int mid = low + high >> 1;
        for (int idx = low; idx <= mid; idx++) {
            int aux = array[idx];
            array[idx] = array[high];
            array[high--] = aux;
        }
    }

}
