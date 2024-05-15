package com.java.quizzappprj.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.java.quizzappprj.model.QuestionModel;
import com.java.quizzappprj.repository.QuestionRepository;

import java.util.List;

public class QuestionViewModel extends ViewModel implements QuestionRepository.OnQuestionLoad {

    private MutableLiveData<List<QuestionModel>> questionMutableLiveData;
    private QuestionRepository questionRepository;

    public MutableLiveData<List<QuestionModel>> getQuestionMutableLiveData() {
        return questionMutableLiveData;
    }

    public QuestionViewModel() {
        questionMutableLiveData = new MutableLiveData<>();
        questionRepository = new QuestionRepository(this);
    }

    public void setQuizId(String quizId) {
        questionRepository.setQuizId(quizId);
    }

    public void getQuestions() {
        questionRepository.getQuestions();
    }

    @Override
    public void onLoad(List<QuestionModel> questionModels) {
        questionMutableLiveData.setValue(questionModels);
    }

    @Override
    public void onError(Exception e) {
        Log.d("QuizError", "onError: " + e.getMessage());
    }
}
