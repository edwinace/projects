package com.avans.ronald.snschatapp.models;

import java.io.Serializable;

/**
 * Created by Ronald on 17-5-2015.
 */
public class FAQ implements Serializable {

    private String question;
    private String answer;
    private String id;
    private String category;

    public FAQ(String id, String category, String question, String answer) {
        setAnswer(answer);
        setTitle(question);
        setId(id);
        setCategory(category);
    }

    public String getTitle() {
        return question;
    }

    public void setTitle(String title) {
        this.question = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}