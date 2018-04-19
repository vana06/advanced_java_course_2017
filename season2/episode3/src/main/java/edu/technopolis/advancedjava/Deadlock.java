package edu.technopolis.advancedjava;

public class Deadlock {
    private static final Object FIRST_LOCK = new Object();
    private static final Object SECOND_LOCK = new Object();
    private static final Object TEMP_LOCK = new Object();

    private volatile static boolean done1 = false, done2 = false;

    public static void main(String[] args) throws Exception {
        Thread ft = new Thread(Deadlock::first);
        Thread st = new Thread(Deadlock::second);
        ft.start();
        st.start();
        ft.join();
        st.join();
        //never going to reach this point
    }

    private static void first() {
        synchronized(FIRST_LOCK) {
            //insert some code here to guarantee a deadlock
            synchronized (TEMP_LOCK){
                try {
                    done1 = true;
                    while (!done2) {
                        TEMP_LOCK.wait();
                    }
                    TEMP_LOCK.notifyAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized(SECOND_LOCK) {
                //unreachable point
            }
        }
    }

    private static void second() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //
        }
        //reverse order of monitors
        synchronized(SECOND_LOCK) {
            //insert some code here to guarantee a deadlock
            synchronized (TEMP_LOCK){
                try {
                    done2 = true;
                    while (!done1) {
                        TEMP_LOCK.wait();
                    }
                    TEMP_LOCK.notifyAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized(FIRST_LOCK) {
                //unreachable point
            }
        }

    }

}