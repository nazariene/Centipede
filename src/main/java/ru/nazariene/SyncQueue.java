package ru.nazariene;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SyncQueue<T> implements BlockingQueue<T> {


    public static void main(String[] args) {
        SyncQueue<String> queue = new SyncQueue<>();

        new Thread(new SyncQueueTestRunnable(queue), "Thr111").start();
        new Thread(new SyncQueueTestRunnable(queue), "Thr222").start();
        new Thread(new SyncQueueTestRunnable(queue), "Thr333").start();

    }

    private static class SyncQueueTestRunnable implements Runnable {
        private SyncQueue<String> queue;

        public SyncQueueTestRunnable(SyncQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while(true) {
                String str = "String:" + (Math.random()*100);
                System.out.println(Thread.currentThread().getName() + ": adding " + str);
                queue.add(str);
                try {
                    Thread.sleep((long) (Math.random()*1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + ": got " + queue.remove());
            }
        }
    }

    private volatile T element;

    public synchronized boolean add(T t) {
        while (!offer(t)) {
            try {
                System.out.println(Thread.currentThread().getName() + " sleeping on add");
                wait();
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrupted on add");
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public synchronized boolean offer(T t) {
        if (element == null) {
            addAndNotify(t);
            return true;
        }
        return false;
    }

    private synchronized void addAndNotify(T t) {
        element = t;
        notifyAll();
    }

    public synchronized T remove() {
        while (element == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return removeAndNotify();
    }

    private synchronized T removeAndNotify() {
        T result = element;
        element = null;
        notifyAll();
        return result;
    }

    public T poll() {
        return element;
    }

    public T element() {
        return null;
    }

    public T peek() {
        return element;
    }

    public void put(T t) throws InterruptedException {

    }

    public boolean offer(T t, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public T take() throws InterruptedException {
        return null;
    }

    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    public int remainingCapacity() {
        return 0;
    }

    public boolean remove(Object o) {
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        return false;
    }

    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean removeIf(Predicate<? super T> filter) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {

    }

    public Spliterator<T> spliterator() {
        return null;
    }

    public Stream<T> stream() {
        return null;
    }

    public Stream<T> parallelStream() {
        return null;
    }

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean contains(Object o) {
        return false;
    }

    public Iterator<T> iterator() {
        return null;
    }

    public void forEach(Consumer<? super T> action) {

    }

    public Object[] toArray() {
        return new Object[0];
    }

    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    public int drainTo(Collection<? super T> c) {
        return 0;
    }

    public int drainTo(Collection<? super T> c, int maxElements) {
        return 0;
    }
}
