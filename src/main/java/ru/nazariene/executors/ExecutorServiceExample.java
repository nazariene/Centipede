package ru.nazariene.executors;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

public class ExecutorServiceExample {

    private static final String SEPARATOR = "-----------------------------------------------";
    private static final ExecutorService executorServiceAuto = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
       /* simpleRunnableSubmit();
        System.out.println(SEPARATOR);
        simpleCallableSubmit();
        System.out.println(SEPARATOR);
        simpleRunnableWithResultSubmit();
        System.out.println(SEPARATOR);
        simpleRunnableCancelSubmit();
        System.out.println(SEPARATOR);
        exceptionInFuture();
        System.out.println(SEPARATOR);*/
        //shutdownDemo();
        shutdownNowDemo();
    }

    /**
     * Demo of Submitting a runnable
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void shutdownDemo() throws ExecutionException, InterruptedException {
        Runnable runnableInstance = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Runnable instance");
        };

        executorServiceAuto.submit(runnableInstance);
        executorServiceAuto.submit(runnableInstance);
        executorServiceAuto.submit(runnableInstance);
        executorServiceAuto.submit(runnableInstance);
        System.out.println("Submitted runnables");
        executorServiceAuto.shutdown();

        //This will generate RejectedExecutionException as pool is shutdown
        try {
            executorServiceAuto.submit(runnableInstance);
        }
        catch (RejectedExecutionException e) {
            System.out.println("Got Rejected! :(");
        }

    }

    /**
     * Demo of Submitting a runnable
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void shutdownNowDemo() throws ExecutionException, InterruptedException {
        Runnable runnableInstance = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Runnable instance");
        };

        executorServiceAuto.submit(runnableInstance);
        executorServiceAuto.submit(runnableInstance);
        executorServiceAuto.submit(runnableInstance);
        executorServiceAuto.submit(runnableInstance);
        System.out.println("Submitted runnables");
        executorServiceAuto.shutdownNow();

        //This will generate RejectedExecutionException as pool is shutdown
        try {
            executorServiceAuto.submit(runnableInstance);
        }
        catch (RejectedExecutionException e) {
            System.out.println("Got Rejected! :(");
        }
    }

    /**
     * Demo of Submitting a runnable
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */

    private static void simpleRunnableSubmit() throws ExecutionException, InterruptedException {
        Future futureRunnableAuto = executorServiceAuto.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I'm Runnable!");
        });
        System.out.println("Submitted runnable 1");
        //This is null
        System.out.println(futureRunnableAuto.get());
    }

    /**
     * Demo of submitting a callable
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void simpleCallableSubmit() throws ExecutionException, InterruptedException {
        Future futureRunnableAuto = executorServiceAuto.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I'm Callable!");
            return "Callable Result!";
        });
        System.out.println("Submitted Callable 1");
        //This is null
        System.out.println(futureRunnableAuto.get());
    }

    /**
     * Demo of submitting a Runnable AND a Result. Runnable gets executed, and Result is returned in Future
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void simpleRunnableWithResultSubmit() throws ExecutionException, InterruptedException {
        Future futureRunnableAuto = executorServiceAuto.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I'm Runnable!");
        }, "Runnable result");
        System.out.println("Submitted Runnable 2");
        //This is null
        System.out.println(futureRunnableAuto.get());
    }

    /**
     * Demo of cancelling a task. Cancellation Exception is thrown from Future
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void simpleRunnableCancelSubmit() throws ExecutionException, InterruptedException {
        Future futureRunnableAuto = executorServiceAuto.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I'm Runnable!");
        });
        System.out.println("Submitted runnable 1");
        futureRunnableAuto.cancel(true);
        try {
            System.out.println(futureRunnableAuto.get());
        } catch (CancellationException ex) {
            System.out.println("Cancelled!");
        }
    }

    /**
     * Demo of submitting a callable that has an exception thrown.
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void exceptionInFuture() throws ExecutionException, InterruptedException {

        Future futureRunnableAuto = executorServiceAuto.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I'm Callable!");
            throw new RuntimeException("I'm an exception!");
        });
        System.out.println("Submitted Callable 2");
        try {
            futureRunnableAuto.get();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
