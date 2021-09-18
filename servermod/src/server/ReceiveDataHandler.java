package server;

import mainlib.CommandNet;

import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveDataHandler implements Runnable {
    SelectionKey key;
    CommandNet receivedCommand;
    LinkedList<DataHolder> queue;
    private static final Logger log = Logger.getLogger(ReceiveDataHandler.class.getName());

    ReceiveDataHandler(SelectionKey key, LinkedList<DataHolder> queue) {
        this.key = key;
        this.queue = queue;
    }

    @Override
    public void run() {
        DataHolder data = (DataHolder) key.attachment();
        try {
            data.channel = (DatagramChannel) key.channel();
            data.channel.configureBlocking(false);
            data.getBuffer().clear();
            data.setClientAdr(data.channel.receive(data.getBuffer()));
            receivedCommand = data.getReceivedCommand();
            log.info("the server received the command from the client: " + receivedCommand.getEnteredCommand()[0]);
        } catch (IOException e) {
            log.log(Level.SEVERE, "error in configure blocking or data receiving");
        }
        if (data.getClientAdr() != null) {
            key.interestOps(SelectionKey.OP_WRITE);
        }
        queue.add(data);
    }

}
