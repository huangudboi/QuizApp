package com.java.quizzappprj.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.java.quizzappprj.model.ResultModel;
import com.java.quizzappprj.repository.ResultRepository;

import java.util.HashMap;
import java.util.List;

public class ResultViewModel extends ViewModel implements ResultRepository.OnResultAdded, ResultRepository.OnResultLoad {

    private MutableLiveData<List<ResultModel>> resultListMutableLiveData;
    private ResultRepository resultRepository;
    private MutableLiveData<HashMap<String, Object>> resultMutableLiveData;

    public MutableLiveData<List<ResultModel>> getResultListMutableLiveData() {
        return resultListMutableLiveData;
    }

    public void getResults() {
        resultRepository.getResults();
    }

    public void getListResult() {
        resultRepository.getListResult();
    }

    public void getListResultRanking() {
        resultRepository.getListResultRanking();
    }

    public void addResults(HashMap<String, Object> resultMap) {
        resultRepository.addResults(resultMap);
    }

    public void setQuizId(String quizId) {
        resultRepository.setQuizId(quizId);
    }

    public MutableLiveData<HashMap<String, Object>> getResultMutableLiveData() {
        return resultMutableLiveData;
    }

    public ResultViewModel() {
        resultListMutableLiveData = new MutableLiveData<>();
        resultMutableLiveData = new MutableLiveData<>();
        resultRepository = new ResultRepository(this, this);
    }

    @Override
    public void onResultLoad(HashMap<String, Object> resultMap) {
        resultMutableLiveData.setValue(resultMap);
    }

    @Override
    public void onLoad(List<ResultModel> resultModels) {
        resultListMutableLiveData.setValue(resultModels);
    }

    @Override
    public boolean onSubmit() {
        return true;
    }

    @Override
    public void onError(Exception e) {
        Log.d("QuizError", "onError: " + e.getMessage());
    }
}
