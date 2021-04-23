package ru.nazariene.executors;

import java.util.concurrent.*;

public class FutureAndCallableSubmissionExamples {

    private static final ExecutorService executorServiceAuto = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        runnableDemo();
        shutdownDemo();
        shutdownNowDemo();
        submitCallable();
        cancelExecution();
        exceptionInFuture();
    }

    private static final Runnable RUNNABLE = () -> {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Runnable instance");
    };

    private static final Callable<String> CALLABLE = () -> {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Callable instance");
        return "Hello from Callable! " + Thread.currentThread().getName();
    };

    private static void runnableDemo() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        //No return value
        executorService.execute(RUNNABLE);
        //Returns a Future with void
        Future result = executorService.submit(RUNNABLE);
        result.get();
        executorService.shutdown();
    }

    /**
     * Demo of shutdown() behavior. Submit a runnable, call shutdown and try to submit another runnable
     * Current runnable is finished, but new are rejected with an exception.
     */
    private static void shutdownDemo() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(RUNNABLE);
        executorService.submit(RUNNABLE);
        executorService.submit(RUNNABLE);
        executorService.submit(RUNNABLE);
        System.out.println("Submitted runnables");
        executorService.shutdown();

        //This will generate RejectedExecutionException as pool is shutdown
        try {
            executorServiceAuto.submit(RUNNABLE);
        } catch (RejectedExecutionException e) {
            System.out.println("Got Rejected! :(");
        }
    }

    /**
     * Demo of shutdownNow() behavior. Submit a runnable, call shutdown and try to submit another runnable
     * Current runnable are cancelled, new submissions are rejected with an exception.
     */
    private static void shutdownNowDemo() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(RUNNABLE);
        executorService.submit(RUNNABLE);
        executorService.submit(RUNNABLE);
        executorService.submit(RUNNABLE);
        System.out.println("Submitted runnables");
        executorService.shutdownNow();

        //This will generate RejectedExecutionException as pool is shutdown
        try {
            executorService.submit(RUNNABLE);
        } catch (RejectedExecutionException e) {
            System.out.println("Got Rejected! :(");
        }
    }

    /**
     * Submit callable, get result from Future
     */
    private static void submitCallable() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        //Returns a Future with void
        Future<String> result = executorService.submit(CALLABLE);
        System.out.println(result.get());
        executorService.shutdown();
    }

    /**
     * Submit a callable, then cancel it via Future.cancel
     * Observe CancellationException when trying to get Future results.
     */
    private static void cancelExecution() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        //Returns a Future with void
        Future<String> result = executorService.submit(CALLABLE);
        result.cancel(true);

        try {
            result.get();
        } catch (CancellationException ce) {
            System.out.println("Cancelled!");
        }
        executorService.shutdown();
    }

    /**
     * Demo of submitting a callable that has an exception thrown.
     */
    private static void exceptionInFuture() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted!");
            }
            throw new RuntimeException("I'm an exception!");
        });

        try {
            future.get();
        } catch (Exception e) {
            System.out.println("Got exception from Future: " + e.getMessage());
        }
        executorService.shutdown();
    }
}
