package server;

import mainlib.CommandNet;

import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveDataHandler  {
    SelectionKey key;
    private static final Logger log = Logger.getLogger(ReceiveDataHandler.class.getName());

    public ReceiveDataHandler(SelectionKey key){
        this.key = key;
    }

    public  void receiveData() {
        DataHolder data = (DataHolder) key.attachment();
        try {
            data.channel = (DatagramChannel) key.channel();
            data.channel.configureBlocking(false);
            data.getBuffer().clear();
            DataManager.getLock().readLock().lock();
            data.setClientAdr(data.channel.receive(data.getBuffer()));
            CommandNet receivedCommand = data.getReceivedCommand();
            log.info("the server received the command from the client: " + receivedCommand.getEnteredCommand()[0]);
        } catch (IOException e) {
            log.log(Level.SEVERE, "error in configure blocking or data receiving");
        } finally {
            DataManager.getLock().readLock().unlock();
        }
        if (data.getClientAdr() != null) {
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    public SelectionKey getKey() {
        return key;
    }
}
