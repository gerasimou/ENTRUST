package auxiliary;


import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class CPUTest {

    public static void main(String[] args) throws Exception {
        int numThreads = 10;
        long start = System.currentTimeMillis();
        for (int i = 0; i < numThreads; ++i) {
            new MyThread().start();
        }
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] allThreadIds = threadMXBean.getAllThreadIds();
        System.out.println("Total JVM Thread count: " + allThreadIds.length);
        long nano = 0;
        for (long id : allThreadIds) {
            nano += threadMXBean.getThreadCpuTime(id);
        }
        System.out.printf("Total cpu time: %s ms; real time: %s ms", nano / 1E6, (System.currentTimeMillis() - start));
    }

}

class MyThread extends Thread {

    public void run() {
        int sum = 0;
        for (int i = 0; i < 900000000; ++i) {
            sum += i;
        }
        sum = sum + 1;
    }
}