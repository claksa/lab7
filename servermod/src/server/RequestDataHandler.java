package server;

import commands.Executor;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

import static db.UserAct.LOG_IN;
import static db.UserAct.REGISTER;
import static db.UserState.AUTHORIZED;
import static mainlib.AnswerType.ERROR;
import static mainlib.AnswerType.WIN;

public class RequestDataHandler implements Runnable {
    SelectionKey key;
    DataHolder dataHolder;
    CommandNet commandNet;
    AnswerType answerType;
    UserState userState;
    Wrapper wrapper = new Wrapper();
    User thisUser;
    UserManager um = Server.getDatabase().getUserManager();
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
            dataHolder.getBuffer().flip();
            Object received = dataHolder.getReceivedData();
            if (received == null) {
                throw new EmptyIOException();
            }
            Answer answer = null;
            if (received instanceof User) {
                thisUser = (User) received;
                ArrayList<String> answerList = new ArrayList<>();
                if (thisUser.getUserAct().equals(REGISTER)) {
                    if (um.register(thisUser)) {
                        answerList.add("registered");
                        userState = AUTHORIZED;
                        answerType = WIN;
                    } else {
                        answerList.add("problems with registration.\nMost likely you are already in the system! try to log in:");
                        answerType = ERROR;
                    }
                } else if (thisUser.getUserAct().equals(LOG_IN)) {
                    if (um.authorize(thisUser)) {
                        answerList.add("authorized");
                        userState = AUTHORIZED;
                        answerType = WIN;
                    } else {
                        answerList.add("problems with authorization.\nWe didn't find you in the system! Try to register:");
                        answerType = ERROR;
                    }
                }
                answer = new Answer(answerList, answerType);
                if (thisUser.getUsername() != null) {
                    answer.setUsername(thisUser.getUsername());
                }
                answer.setUserState(userState);
            }
            if (received instanceof CommandNet) {
                commandNet = (CommandNet) received;
                String command = wrapper.getWrappedCommand(commandNet);
                String argument = wrapper.getArgument();
                String username = wrapper.getWrappedUserName(commandNet);
                Ticket tick = wrapper.getWrappedTicket(commandNet);
                Integer id = wrapper.getWrappedId(commandNet);
                boolean cannotexecute = true;
                if (!um.getUserUtil().checkUserName(username)) {
                    ArrayList<String> answerList = new ArrayList<>();
                    answerList.add("There are no registered users with this name!");
                    answer = new Answer(answerList, ERROR);
                    cannotexecute = false;
                }
                if (!UserManager.getUserState().equals(UserState.NOT_REGISTERED) || command.equals("connect") || !cannotexecute) {
                    answer = new Executor().execute(command, argument, tick, id, username);
                    answerType = WIN;
                }
            }
            oos.writeObject(answer);
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
