package server;

import commands.Executor;
import lib.Wrapper;
import mainlib.Answer;
import mainlib.CommandNet;
import models.Ticket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class DataManager {
    Selector selector;
    LinkedList<DataHolder> queue = new LinkedList<>();
    SelectionKey key = null;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(6);
    private static final Logger log = Logger.getLogger(DataManager.class.getName());


    public void manageData() {

        selector = Server.getSelector();
        while (Server.running) {
            try {
                selector.select(50);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                ExecutorService serviceSender = Executors.newFixedThreadPool(selectedKeys.size()); //need to test!
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    key = keyIterator.next();
                    keyIterator.remove();
                    if (key.isValid()) {
                        if (key.isReadable()) {
                            new Thread(new ReceiveDataHandler(key, queue)).start();
                        }
                    }
                }
                while (!queue.isEmpty()) {

                    //TODO: do sth with future result
                    serviceSender.submit(() -> new RequestDataHandler(queue.poll(), key).requestData());
                }
                serviceSender.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }
}
