package server;

import mainlib.CommandNet;
import mainlib.User;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveDataHandler {
    SelectionKey key;
    CommandNet receivedCommand;
    User user;
    private static final Logger log = Logger.getLogger(ReceiveDataHandler.class.getName());

    public ReceiveDataHandler(SelectionKey key) {
        this.key = key;
    }

    public void receiveData() {
        DataHolder data = (DataHolder) key.attachment();
        try {
            data.channel = (DatagramChannel) key.channel();
            data.channel.configureBlocking(false);
            data.getBuffer().clear();
            DataManager.getLock().readLock().lock();
            ByteBuffer future = DataManager.getExecutorService().submit(data::getBuffer).get();
            data.setClientAdr(data.channel.receive(future));
            if (data.getReceivedData() instanceof CommandNet) {
                receivedCommand = (CommandNet) data.getReceivedData();
                log.info("the server received the command from the client: " + receivedCommand.getEnteredCommand()[0]);
            } else if (data.getReceivedData() instanceof User) {
                user = (User) data.getReceivedData();
                log.info("the server received a user command!");
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "error in configure blocking or data receiving");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
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
