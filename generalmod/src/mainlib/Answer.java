package mainlib;

import db.UserState;

import java.io.Serializable;
import java.util.ArrayList;

import static db.UserState.NOT_REGISTERED;

public class Answer implements Serializable {
    private final ArrayList<String> answer;
    AnswerType answerType;
    UserState userState;
    static String username;

    public Answer(ArrayList<String> answer, AnswerType answerType) {
        this.answer = answer;
        this.answerType = answerType;
        this.userState = NOT_REGISTERED;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void printAnswer() {
        answer.forEach(System.out::println);
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public static String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Answer {" +
                "answer=" + answer +
                '}';
    }
}
