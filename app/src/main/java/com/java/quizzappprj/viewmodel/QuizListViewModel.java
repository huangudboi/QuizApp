package com.java.quizzappprj.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.java.quizzappprj.model.QuizListModel;
import com.java.quizzappprj.model.SetQuizModel;
import com.java.quizzappprj.repository.QuizListRepository;

import java.util.List;

public class QuizListViewModel extends ViewModel implements QuizListRepository.OnFireStoreTaskComplete {

    private MutableLiveData<List<QuizListModel>> quizListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<SetQuizModel>> setQuizListLiveData = new MutableLiveData<>();

    private QuizListRepository quizListRepository = new QuizListRepository(this);

    public MutableLiveData<List<QuizListModel>> getQuizListLiveData() {
        return quizListLiveData;
    }

    public MutableLiveData<List<SetQuizModel>> getSetQuizListLiveData() {
        return setQuizListLiveData;
    }

    public QuizListViewModel() {
        quizListRepository.getQuizData();
        quizListRepository.getSetQuizData();
    }

    @Override
    public void quizDataLoaded(List<QuizListModel> quizListModels) {
        quizListLiveData.setValue(quizListModels);
    }

    @Override
    public void setQuizLoaded(List<SetQuizModel> setQuizModels) {
        setQuizListLiveData.setValue(setQuizModels);
    }

    @Override
    public void onError(Exception e) {
        Log.d("QuizERROR", "onError: " + e.getMessage());
    }
}
