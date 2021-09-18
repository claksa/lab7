package mainlib;

import java.io.Serializable;
import java.util.ArrayList;

public class Answer implements Serializable {
    private final ArrayList<String> answer;

    public Answer(ArrayList<String> answer){
        this.answer = answer;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public void printAnswer(){
        answer.forEach(System.out::println);
    }

    @Override
    public String toString() {
        return "Answer {" +
                "answer=" + answer +
                '}';
    }
}
