package ru.nazariene.threads;

/**
 * aThreadInstance.interrupt() - in case Thread is in (wait, join) - it will get InterruptedException AND its interrupted flag will be cleared
 In case thread is blocked by IO channel - it will get ClosedByInterruptException and channel will be closed
 If none of the above - its interrupted flag will be set (NO EXCEPTIONS THROWN)
 If thread is not alive - nothing happens
 */
public class InterruptExample {

    public static void main(String[] args) throws InterruptedException {
        Thread notBusyThread = new Thread(() -> {
            while(!Thread.interrupted()) {
                System.out.println("I'm not busy and having no issues with that");
            }

            System.out.println("I've got interrupted!");
        });

        Thread sleepingThread = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("I've got interrupted while sleeping!");
            }
        });


        Thread deadThread = new Thread(() -> System.out.println("I'm dead"));

        notBusyThread.start();
        sleepingThread.start();

        Thread.sleep(10);
        sleepingThread.interrupt();
        notBusyThread.interrupt();
        deadThread.interrupt();
    }
}
