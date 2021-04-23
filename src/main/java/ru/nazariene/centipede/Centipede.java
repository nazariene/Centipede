package ru.nazariene.centipede;

//Can't remember WTF is this...
public class Centipede {

    public static void main(String[] args) {
        Brain brain = new Brain();

        for (int i = 0; i < 100; i ++) {
            new Thread(new Leg(brain, i)).start();
        }
    }

    private static class Leg implements Runnable {

        private Brain brain;
        private int legNumber;

        public Leg(Brain brain, int legNumber) {
            this.brain = brain;
            this.legNumber = legNumber;
        }

        @Override
        public void run() {
            while(true) {
                brain.doStep(legNumber);
            }
        }
    }

    private static class Brain {

        private volatile int legToStep = 0;

        private long averageTimeForSteps = 0;
        private long lastStepCycleStartTime = 0;
        private int stepCyclesCounter = 0;

        public Brain() {
            lastStepCycleStartTime = System.currentTimeMillis();
        }

        public synchronized boolean doStep(int legNum) {
            while (legNum != legToStep) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (legToStep == legNum) {
                legToStep++;

                if (legToStep >= 100) {
                    legToStep = 0;
                    stepCyclesCounter++;
                    long currentTime = System.currentTimeMillis();
                    System.out.println(currentTime - lastStepCycleStartTime);
                    lastStepCycleStartTime = currentTime;
                }
                //System.out.println("Stepping " + legNum);

            }

            notifyAll();
            return true;
        }
    }
}
