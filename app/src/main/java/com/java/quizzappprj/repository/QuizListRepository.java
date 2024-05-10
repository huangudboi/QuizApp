package com.java.quizzappprj.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.java.quizzappprj.model.QuizListModel;
import com.java.quizzappprj.model.SetQuizModel;

import java.util.List;

public class QuizListRepository {

    private OnFireStoreTaskComplete onFireStoreTaskComplete;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public QuizListRepository(OnFireStoreTaskComplete onFireStoreTaskComplete){
        this.onFireStoreTaskComplete = onFireStoreTaskComplete;
    }
    public void getQuizData(){
        firebaseFirestore.collection("Quiz")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    onFireStoreTaskComplete.quizDataLoaded(task.getResult()
                            .toObjects(QuizListModel.class));
                }else{
                    onFireStoreTaskComplete.onError(task.getException());
                }
            }
        });
    }

    public void getSetQuizData(){
        firebaseFirestore.collection("SetQuiz")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            onFireStoreTaskComplete.setQuizLoaded(task.getResult()
                                    .toObjects(SetQuizModel.class));
                        }else{
                            onFireStoreTaskComplete.onError(task.getException());
                        }
                    }
                });
    }

    public interface OnFireStoreTaskComplete{
        void quizDataLoaded(List<QuizListModel> quizListModels);
        void setQuizLoaded(List<SetQuizModel> setQuizModels);
        void onError(Exception e);
    }
}
