package com.java.quizzappprj.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.java.quizzappprj.model.ResultModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ResultRepository {
    private FirebaseFirestore firebaseFirestore;
    private String quizId;
    private HashMap<String, Object> resultMap = new HashMap<>();
    private OnResultAdded onResultAdded;
    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private OnResultLoad onResultLoad;
    private List<ResultModel> resultModels = new ArrayList<>();

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public ResultRepository(OnResultAdded onResultAdded, OnResultLoad onResultLoad) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        this.onResultAdded = onResultAdded;
        this.onResultLoad = onResultLoad;
    }

    public void getResults() {
        firebaseFirestore.collection("Quiz").document(quizId)
                .collection("results").document(currentUserId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            resultMap.put("correct", task.getResult().getLong("correct"));
                            resultMap.put("title", task.getResult().getString("title"));
                            resultMap.put("totalTime", task.getResult().getLong("totalTime"));
                            resultMap.put("wrong", task.getResult().getLong("wrong"));
                            resultMap.put("notAnswered", task.getResult().getLong("notAnswered"));
                            onResultLoad.onResultLoad(resultMap);
                        } else {
                            onResultLoad.onError(task.getException());
                        }
                    }
                });
    }

    public void getListResult() {
        firebaseFirestore.collection("Quiz").document(quizId)
                .collection("results").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            resultModels.addAll(task.getResult().toObjects(ResultModel.class));
                            resultModels = resultModels.stream()
                                    .filter(resultModel -> resultModel.getUserId().equals(currentUserId))
                                    .collect(Collectors.toList());

                            onResultLoad.onLoad(resultModels);
                        } else {
                            onResultLoad.onError(task.getException());
                        }
                    }
                });
    }

    public void getListResultRanking() {
        firebaseFirestore.collection("Quiz").document(quizId)
                .collection("results").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            resultModels = new ArrayList<>();
                            resultModels.addAll(task.getResult().toObjects(ResultModel.class));
                            resultModels.sort(Comparator.comparing(ResultModel::getCorrect).reversed()
                                    .thenComparing(ResultModel::getTotalTime));

                            onResultLoad.onLoad(resultModels);
                        } else {
                            onResultLoad.onError(task.getException());
                        }
                    }
                });
    }

    public void addResults(HashMap<String, Object> resultMap) {
        firebaseFirestore.collection("Quiz").document(quizId)
                .collection("results").document(currentUserId)
                .set(resultMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            onResultAdded.onSubmit();
                        } else {
                            onResultAdded.onError(task.getException());
                        }
                    }
                });
    }

    public interface OnResultLoad {
        void onResultLoad(HashMap<String, Object> resultMap);

        void onLoad(List<ResultModel> resultModels);

        void onError(Exception e);
    }


    public interface OnResultAdded {
        boolean onSubmit();

        void onError(Exception e);
    }

}
