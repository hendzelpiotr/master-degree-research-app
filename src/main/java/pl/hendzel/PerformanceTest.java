package pl.hendzel;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

class PerformanceTest {

    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            people.add(new Person(RandomStringUtils.randomAlphabetic(5), RandomStringUtils.randomAlphabetic(7)));
        }

        ArrayList<String> randomWords = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            String generatedString = RandomStringUtils.randomAlphabetic(10);
            randomWords.add(generatedString);
        }

        System.out.println("Size of people list: " + people.size());
        System.out.println("Size of randomWords list: " + randomWords.size());

        Runtime runtime = Runtime.getRuntime();
//
//        System.gc();
//
//        long memory = runtime.totalMemory() - runtime.freeMemory();
//        System.out.println("Used memory is bytes: " + memory);
//        System.out.println("getCurrentlyUsedMemory: " + getCurrentlyUsedMemory());
//        System.out.println("getPossiblyReallyUsedMemory: " + getPossiblyReallyUsedMemory());
        System.out.println("getReallyUsedMemory: " + getReallyUsedMemory());
        System.out.println("getGcCount: " + getGcCount());
        System.out.println("Collection time:  " + getGarbageCollectionTime() + "ms");
    }

    static long getCurrentlyUsedMemory() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed(); //+ ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
    }

    static long getPossiblyReallyUsedMemory() {
        System.gc();
        return getCurrentlyUsedMemory();
    }

    static long getGcCount() {
        long sum = 0;
        for (GarbageCollectorMXBean b : ManagementFactory.getGarbageCollectorMXBeans()) {
            long count = b.getCollectionCount();
            if (count != -1) {
                sum += count;
            }
        }
        return sum;
    }

    static long getReallyUsedMemory() {
        long before = getGcCount();
        System.gc();
        while (getGcCount() == before) ;
        return getCurrentlyUsedMemory();
    }

    private static long getGarbageCollectionTime() {
        long collectionTime = 0;
        for (GarbageCollectorMXBean garbageCollectorMXBean : ManagementFactory.getGarbageCollectorMXBeans()) {
            collectionTime += garbageCollectorMXBean.getCollectionTime();
        }
        return collectionTime;
    }

}
