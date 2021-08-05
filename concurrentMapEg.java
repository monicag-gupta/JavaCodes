//ReaderThread.java
import java.util.*;
import java.util.concurrent.*;
public class ReaderThread extends Thread {
    private ConcurrentHashMap<Integer, String> map;
    private String name;
    public ReaderThread(ConcurrentHashMap<Integer, String> map, String threadName) {
        this.map = map;
        this.name = threadName;
    }
    public void run() {
        while (true) {
            ConcurrentHashMap.KeySetView<Integer, String> keySetView = map.keySet();
            Iterator<Integer> iterator = keySetView.iterator();
            long time = System.currentTimeMillis();
            String output = time + ": " + name + ": ";
            while (iterator.hasNext()) {
                Integer key = iterator.next();
                String value = map.get(key);
                output += key + "=>" + value + "; ";
            }
            System.out.println(output);
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }	        }	    }	}


//WriterThread.java

import java.util.*;
import java.util.concurrent.*;
public class WriterThread extends Thread {
    private ConcurrentMap<Integer, String> map;
    private Random random;
    private String name;
    public WriterThread(ConcurrentMap<Integer, String> map, String threadName, long randomSeed) {
        this.map = map;
        this.random = new Random(randomSeed);
        this.name = threadName;
    }
     public void run() {
        while (true) {
            Integer key = random.nextInt(10);
            String value = name;
             if(map.putIfAbsent(key, value) == null) {
                long time = System.currentTimeMillis();
                String output = String.format("%d: %s has put [%d => %s]", time, name, key, value);
                System.out.println(output);
            }
             Integer keyToRemove = random.nextInt(10);
             if (map.remove(keyToRemove, value)) {
                long time = System.currentTimeMillis();
                String output = String.format("%d: %s has removed [%d => %s]", time, name, keyToRemove, value);
                System.out.println(output);
            }  try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {      ex.printStackTrace();      }
        }	    }	}


//ConcurrentHashMapExamples.java

import java.util.*;
import java.util.concurrent.*;
public class ConcurrentHashMapExamples {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();
        new WriterThread(map, "Writer-1", 1).start();
        new WriterThread(map, "Writer-2", 2).start();
        for (int i = 1; i <= 5; i++) {
            new ReaderThread(map, "Reader-" + i).start();
        }
    }
}
/*
1537036113306: Writer-1 has put [5 => Writer-1]
1537036113440: Reader-4: 5=>Writer-1; 8=>Writer-2;
1537036113442: Reader-5: 5=>Writer-1; 8=>Writer-2;
1537036113441: Reader-1: 5=>Writer-1; 8=>Writer-2;
1537036113440: Reader-3: 5=>Writer-1; 8=>Writer-2;
1537036113440: Reader-2: 5=>Writer-1; 8=>Writer-2;
1537036113916: Writer-1 has put [7 => Writer-1]
1537036113916: Writer-2 has put [0 => Writer-2]
1537036114053: Reader-4: 0=>Writer-2; 5=>Writer-1; 7=>Writer-1; 8=>Writer-2;
1537036114054: Reader-2: 0=>Writer-2; 5=>Writer-1; 7=>Writer-1; 8=>Writer-2;
1537036114054: Reader-3: 0=>Writer-2; 5=>Writer-1; 7=>Writer-1; 8=>Writer-2;
1537036114054: Reader-1: 0=>Writer-2; 5=>Writer-1; 7=>Writer-1; 8=>Writer-2;
1537036114053: Reader-5: 0=>Writer-2; 5=>Writer-1; 7=>Writer-1; 8=>Writer-2;
1537036114355: Reader-4: 0=>Writer-2; 5=>Writer-1; 7=>Writer-1; 8=>Writer-2;
1537036114356: Reader-2: 0=>Writer-2; 5=>Writer-1; 7=>Writer-1; 8=>Writer-2;
1537036114363: Reader-5: 0=>Writer-2; 5=>Writer-1; 7=>Writer-1; 8=>Writer-2;
1537036114361: Reader-1: 0=>Writer-2; 5=>Writer-1; 7=>Writer-1; 8=>Writer-2;
1537036114359: Reader-3: 0=>Writer-2; 5=>Writer-1; 7=>Writer-1; 8=>Writer-2;
1537036114418: Writer-1 has put [4 => Writer-1]
1537036114419: Writer-2 has put [9 => Writer-2]
1537036114419: Writer-1 has removed [4 => Writer-1]
1537036114421: Writer-2 has removed [0 => Writer-2]
…..
*/

