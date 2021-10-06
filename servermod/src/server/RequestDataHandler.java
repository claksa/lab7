package server;

import commands.Executor;
import db.UserAct;
import db.UserManager;
import db.UserState;
import exceptions.EmptyIOException;
import lib.Wrapper;
import mainlib.*;
import mainlib.Reader;
import models.Ticket;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.logging.Logger;

public class RequestDataHandler implements Runnable {
    SelectionKey key;
    DataHolder dataHolder;
    CommandNet commandNet;
    User user;
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
            dataHolder.getBuffer().flip();
            Object received = dataHolder.getReceivedData();
            if (received == null) {
                throw new EmptyIOException();
            }
            Answer answer = null;
            if (received instanceof CommandNet) {
                commandNet = (CommandNet) received;
                Wrapper wrapper = new Wrapper();
                String command = wrapper.getWrappedCommand(commandNet);
                String argument = wrapper.getArgument();
                Ticket tick = wrapper.getWrappedTicket(commandNet);
                Integer id = wrapper.getWrappedId(commandNet);
                if (UserManager.getUserState().equals(UserState.NOT_REGISTERED) && command.equals("connect")) {
                    answer = new Executor().execute(command, argument, tick, id);
                    answer.setAnswerStatus(AnswerType.INT_COMMAND);
                    oos.writeObject(answer);
                }
            } else if (received instanceof User) {
                user = (User) received;
                ArrayList<String> answerList = new ArrayList<>();
                UserManager userManager = new UserManager();
                if (user.getUserAct().equals(UserAct.REGISTER)) {
                    if (userManager.register(user)) {
                        answerList.add("registered");
                    }
                } else if (user.getUserAct().equals(UserAct.LOG_IN)) {
                    if (userManager.authorize(user)) {
                        answerList.add("authorized");
                    }
                }
                answer = new Answer(answerList);
                answer.setAnswerStatus(AnswerType.USER);
                oos.writeObject(new Answer(answerList));
            }
            byte[] b = out.toByteArray();
            ByteBuffer buff = ByteBuffer.wrap(b);
            dataHolder.channel.send(buff, dataHolder.getClientAdr());
            log.info("send answer " + b.length + " bytes");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Reader.PrintErr("there is no way to get a command!");
        } catch (EmptyIOException e) {
            Reader.PrintErr("unavailable received command");
        } finally {
            DataManager.getLock().writeLock().unlock();
        }
    }
}
