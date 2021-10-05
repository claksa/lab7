package server;

import commands.Executor;
import db.UserManager;
import db.UserState;
import lib.Wrapper;
import mainlib.Answer;
import mainlib.CommandNet;
import mainlib.Reader;
import models.Ticket;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.logging.Logger;

public class RequestDataHandler implements Runnable {
    SelectionKey key;
    DataHolder dataHolder;
    private static final Logger log = Logger.getLogger(ReceiveDataHandler.class.getName());

    public RequestDataHandler(SelectionKey key) {
        this.key = key;
    }


    @Override
    public void run() {
        dataHolder = (DataHolder) key.attachment();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(out)) {
            DataManager.getLock().writeLock().lock();

            if (dataHolder == null) {
                throw new ClassNotFoundException();
            }
            System.out.println("IN REQUEST DATA");

//            Future<CommandNet> res = DataManager.getExecutorService().submit(dataHolder::getReceivedCommand);

            dataHolder.getBuffer().flip();
            CommandNet receivedCommand = dataHolder.getReceivedCommand();
            Wrapper wrapper = new Wrapper();

            String command = wrapper.getWrappedCommand(receivedCommand);
            String argument = wrapper.getArgument();
            Ticket tick = wrapper.getWrappedTicket(receivedCommand);
            Integer id = wrapper.getWrappedId(receivedCommand);
            if (UserManager.getUserState().equals(UserState.NOT_REGISTERED) && command.equals("connect")) {
                System.out.println("IN SENDING");
                Answer answer = new Executor().execute(command, argument, tick, id);
                oos.writeObject(answer);
                byte[] b = out.toByteArray();
                ByteBuffer buff = ByteBuffer.wrap(b);
                dataHolder.channel.send(buff, dataHolder.getClientAdr());
                log.info("send answer " + b.length + " bytes");
            }
//            if (key != null) {
//                key.interestOps(SelectionKey.OP_READ);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Reader.PrintErr("there is no way to get a command!");
//            return;
        } finally {
            DataManager.getLock().writeLock().unlock();
        }
    }
}
