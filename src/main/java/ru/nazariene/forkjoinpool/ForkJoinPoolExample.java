package ru.nazariene.forkjoinpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinPoolExample {

    private static final int THRESHOLD = 5;

    //This is a java-8 way
    public static ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    //In java 7 same can be achieved by
    //public static ForkJoinPool forkJoinPool = new ForkJoinPool(2);
    //Another java-8 way, will detect availableProcessors for parallelism (max number of threads)
    //public static ForkJoinPool forkJoinPool = Executors.newWorkStealingPool();
    //Another java-8 way, manually configured parallelism
    //public static ForkJoinPool forkJoinPool = Executors.newWorkStealingPool(2);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Comparable[] array = {5, 7, 2, 3, 9, 7, 1, 0, 6, 4};
        int i,j;
        for (i = 10, j=0 ; i > 0; i--, j ++) {
            array[j] = i;
        }
        ForkJoinTask task = forkJoinPool.submit(new SorterTask(array, 0, 9));
        task.join();
        System.out.println(array);
        for (i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }

    public static class SorterTask extends RecursiveAction {

        private int lo;
        private int hi;
        private Comparable[] array;

        public SorterTask(Comparable[] array, int lo, int hi) {
            this.array = array;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected void compute() {
            if (hi - lo < THRESHOLD) {
                insertionSort(lo, hi);
                return;
            }

            int mid = (lo + hi) >>> 1;

            invokeAll(new SorterTask(array, lo, mid), new SorterTask(array, mid + 1, hi));
            //OR
            //SorterTask child1 = new SorterTask(array, lo, mid);
            //SorterTask child2 = new SorterTask(array, mid + 1, hi);
            //child1.fork();
            //child2.fork();
            //child2.join();
            //child1.join();

            merge(lo, mid, hi);
        }

        // sort from a[lo] to a[hi] using insertion sort
        private void insertionSort(int lo, int hi) {
            for (int i = lo; i <= hi; i++)
                for (int j = i; j > lo && (array[j].compareTo(array[j-1]) < 0); j--)
                    exch(array, j, j-1);
        }

        void exch(Comparable[] inputArray, int i, int curMin) {
            Comparable curMinValue = inputArray[curMin];
            inputArray[curMin] = inputArray[i];
            inputArray[i] = curMinValue;
        }

        private void merge(int lo, int mid, int hi) {
            int leftSize = mid - lo + 1;
            int rightSize = hi - mid;

            Comparable[] left = new Comparable[leftSize];
            Comparable[] right = new Comparable[rightSize];

            for (int i = 0; i < leftSize; i++) {
                left[i] = array[lo + i];
            }
            for (int i = 0; i < rightSize; i++) {
                right[i] = array[mid + i + 1];
            }

            int i = 0;
            int j = 0;
            for (int k = lo; k <= hi; k ++) {
                if (i >= leftSize) {
                    array[k] = right[j++];
                }
                else if (j >= rightSize) {
                    array[k] = left[i++];
                }
                else if (left[i].compareTo(right[j]) < 0) {
                    array[k] = left[i++];
                }
                else {
                    array[k] = right[j++];
                }
            }
        }
    }
}
