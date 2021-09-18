package server;

import commands.Executor;
import lib.Wrapper;
import mainlib.Answer;
import mainlib.CommandNet;
import models.Ticket;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class RequestDataHandler {
    DataHolder dataHolder;
    SelectionKey key;
    private static final Logger log = Logger.getLogger(ReceiveDataHandler.class.getName());

    public RequestDataHandler(DataHolder dataHolder, SelectionKey key) {
        this.dataHolder = dataHolder;
        this.key = key;
    }

    public void requestData() {
        dataHolder.getBuffer().flip();
        Future<CommandNet> res = DataManager.getExecutorService().submit(() -> dataHolder.getReceivedCommand());

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(out)) {

            CommandNet receivedCommand = res.get();
            Wrapper wrapper = new Wrapper();

            String command = wrapper.getWrappedCommand(receivedCommand);
            String argument = wrapper.getArgument();
            Ticket tick = wrapper.getWrappedTicket(receivedCommand);
            Integer id = wrapper.getWrappedId(receivedCommand);
            Answer answer = new Executor().execute(command, argument, tick, id);
            oos.writeObject(answer);
            byte[] b = out.toByteArray();
            ByteBuffer buff = ByteBuffer.wrap(b);
            dataHolder.channel.send(buff, dataHolder.getClientAdr());
            log.info("send answer " + b.length + " bytes");
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (key != null) {
            key.interestOps(SelectionKey.OP_READ);
        }
    }

}
