package server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataManager {
    Selector selector;
    SelectionKey key = null;
//    private static final ExecutorService executorService = Executors.newFixedThreadPool(6);
    private static final  Logger log = Logger.getLogger(DataManager.class.getName());
    volatile LinkedList<DataHolder> queue = new LinkedList<DataHolder>();
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();



    public void manageData() {

        selector = Server.getSelector();
        ExecutorService serviceSender = null;
        while (Server.running) {
            try {
                selector.select(50);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
//                serviceSender = Executors.newFixedThreadPool(6);
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    key = keyIterator.next();
                    keyIterator.remove();
                    if (key.isValid()) {
                        if (key.isReadable()) {
                            new ReceiveDataHandler(key).receiveData();
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

//        if (serviceSender != null) {
//            serviceSender.shutdown();
//        }

//        executorService.shutdown();
    }
//
//    public static ExecutorService getExecutorService() {
//        return executorService;
//    }
    public static ReentrantReadWriteLock getLock() {
        return lock;
    }
}
