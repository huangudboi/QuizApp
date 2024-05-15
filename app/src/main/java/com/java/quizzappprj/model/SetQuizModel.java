package com.java.quizzappprj.model;

import com.google.firebase.firestore.DocumentId;

public class SetQuizModel {

    @DocumentId
    private String quizId;
    private String title, manufacture, difficulty;
    private long questions;
    private float fees;

    public SetQuizModel() {
    }

    public SetQuizModel(String quizId, String title, String manufacture, String difficulty, long questions, float fees) {
        this.quizId = quizId;
        this.title = title;
        this.manufacture = manufacture;
        this.difficulty = difficulty;
        this.questions = questions;
        this.fees = fees;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public long getQuestions() {
        return questions;
    }

    public void setQuestions(long questions) {
        this.questions = questions;
    }

    public float getFees() {
        return fees;
    }

    public void setFees(float fees) {
        this.fees = fees;
    }
}
