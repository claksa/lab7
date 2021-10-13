package server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataManager {
    Selector selector;
    SelectionKey key = null;
    private static final  Logger log = Logger.getLogger(DataManager.class.getName());
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private static ForkJoinPool forkJoinPool = new ForkJoinPool(4);



    public void manageData() {

        selector = Server.getSelector();
        while (Server.running) {
            try {
                selector.select(50);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    key = keyIterator.next();
                    keyIterator.remove();
                    if (key.isValid()) {
                        if (key.isReadable()) {
                            forkJoinPool.invoke(new ReceiveDataHandler(key));
                        } else if(key.isWritable()){
                            System.out.println("IN DM THREAD: before launch request thread");
                            new Thread(new RequestDataHandler(key)).start();
                            key.interestOps(SelectionKey.OP_READ);
                        }
                    }
                }

            } catch (IOException e) {
                log.log(Level.SEVERE, "error in getting the selector");
            }
        }
        executorService.shutdown();
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static ReentrantReadWriteLock getLock() {
        return lock;
    }

    public static ForkJoinPool getForkJoinPool() {
        return forkJoinPool;
    }
}
