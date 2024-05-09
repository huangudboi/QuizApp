package com.java.quizzappprj.views;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.java.quizzappprj.model.QuestionModel;
import com.java.quizzappprj.R;
import com.java.quizzappprj.viewmodel.QuestionViewModel;
import com.java.quizzappprj.viewmodel.ResultViewModel;

import java.util.HashMap;
import java.util.List;

public class QuizFragment extends Fragment implements View.OnClickListener {

    private QuestionViewModel viewModel;
    private ResultViewModel resultViewModel;
    private NavController navController;
    private ProgressBar progressBar;
    private Button option1Btn , option2Btn , option3Btn , nextQueBtn;
    private TextView questionTv , ansFeedBackTv , questionNumberTv , timerCountTv;
    private String quizId, title;
    private long totalQuestions;
    private int currentQueNo = 0;
    private boolean canAnswer = false;
    private long timer, totalTime = 0L;
    private CountDownTimer countDownTimer;
    private int notAnswer = 0;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private String answer = "";
    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuizFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizFragment newInstance(String param1, String param2) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuestionViewModel.class);

        resultViewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(ResultViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        ImageView closeQuizBtn = view.findViewById(R.id.imageView3);
        option1Btn = view.findViewById(R.id.option1Btn);
        option2Btn = view.findViewById(R.id.option2Btn);
        option3Btn = view.findViewById(R.id.option3Btn);
        nextQueBtn = view.findViewById(R.id.nextQueBtn);
        ansFeedBackTv = view.findViewById(R.id.ansFeedbackTv);
        questionTv = view.findViewById(R.id.quizQuestionTv);
        timerCountTv = view.findViewById(R.id.countTimeQuiz);
        questionNumberTv = view.findViewById(R.id.quizQuestionsCount);
        progressBar = view.findViewById(R.id.quizCoutProgressBar);

        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();
        title = QuizFragmentArgs.fromBundle(getArguments()).getTitle();
        totalQuestions = QuizFragmentArgs.fromBundle(getArguments()).getTotalQueCount();
        viewModel.setQuizId(quizId);
        resultViewModel.setQuizId(quizId);
        viewModel.getQuestions();;

        option1Btn.setOnClickListener(this);
        option2Btn.setOnClickListener(this);
        option3Btn.setOnClickListener(this);
        nextQueBtn.setOnClickListener(this);

        closeQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_quizFragment_to_listFragment);
            }
        });

        loadData();
    }

    private void loadData(){
        enableOptions();
        loadQuestions(1);
    }

    private void enableOptions(){
        option1Btn.setVisibility(View.VISIBLE);
        option2Btn.setVisibility(View.VISIBLE);
        option3Btn.setVisibility(View.VISIBLE);

        //enable buttons , hide feedback tv , hide nextQuiz btn

        option1Btn.setEnabled(true);
        option2Btn.setEnabled(true);
        option3Btn.setEnabled(true);

        ansFeedBackTv.setVisibility(View.INVISIBLE);
        nextQueBtn.setVisibility(View.INVISIBLE);
    }

    private void loadQuestions(int i){

        currentQueNo = i;
        viewModel.getQuestionMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuestionModel>>() {
            @Override
            public void onChanged(List<QuestionModel> questionModels) {
                questionTv.setText(currentQueNo + ") " + questionModels.get(i - 1).getQuestion());
                option1Btn.setText(questionModels.get(i - 1).getOption_a());
                option2Btn.setText(questionModels.get(i - 1).getOption_b());
                option3Btn.setText(questionModels.get(i - 1).getOption_c());
                timer = questionModels.get(i - 1).getTimer();
                answer = questionModels.get(i - 1).getAnswer();

                //todo set current que no, to que number tv
                questionNumberTv.setText(String.valueOf(currentQueNo));
                startTimer();
            }
        });

        canAnswer = true;
    }

    private void startTimer(){
        timerCountTv.setText(String.valueOf(timer));
        progressBar.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(timer * 1000 , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // update time
                timerCountTv.setText(millisUntilFinished / 1000 + "");

                // up totalTime
                totalTime++;

                long percent = millisUntilFinished/(timer*10);
                progressBar.setProgress((int) percent);
            }

            @Override
            public void onFinish() {
                canAnswer = false;
                ansFeedBackTv.setTextColor(Color.YELLOW);
                ansFeedBackTv.setText("Times Up !! No answer selected");
                notAnswer++;
                showNextBtn();
            }
        }.start();
    }

    private void showNextBtn() {
        if (currentQueNo == totalQuestions){
            ansFeedBackTv.setVisibility(View.VISIBLE);
            nextQueBtn.setText("Submit");
            nextQueBtn.setEnabled(true);
            nextQueBtn.setVisibility(View.VISIBLE);
        }else{
            nextQueBtn.setVisibility(View.VISIBLE);
            nextQueBtn.setEnabled(true);
            ansFeedBackTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.option1Btn) {
            verifyAnswer(option1Btn);
        } else if (v.getId() == R.id.option2Btn) {
            verifyAnswer(option2Btn);
        } else if (v.getId() == R.id.option3Btn) {
            verifyAnswer(option3Btn);
        } else if (v.getId() == R.id.nextQueBtn) {
            if (currentQueNo == totalQuestions){
                submitResults();
            }else{
                currentQueNo++;
                loadQuestions(currentQueNo);
                resetOptions();
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private void resetOptions(){
        ansFeedBackTv.setVisibility(View.INVISIBLE);
        nextQueBtn.setVisibility(View.INVISIBLE);
        nextQueBtn.setEnabled(false);
        option1Btn.setBackgroundColor(R.color.button);
        option2Btn.setBackground(ContextCompat.getDrawable(getContext() , R.color.button));
        option3Btn.setBackground(ContextCompat.getDrawable(getContext() , R.color.button));
    }

    private void submitResults() {
        HashMap<String , Object> resultMap = new HashMap<>();
        resultMap.put("userId" , currentUserId);
        resultMap.put("title" , title);
        resultMap.put("totalTime" , totalTime);
        resultMap.put("correct" , correctAnswer);
        resultMap.put("wrong" , wrongAnswer);
        resultMap.put("notAnswered" , notAnswer);

        resultViewModel.addResults(resultMap);

        QuizFragmentDirections.ActionQuizFragmentToResultFragment action =
                QuizFragmentDirections.actionQuizFragmentToResultFragment();
        action.setQuizId(quizId);
        navController.navigate(action);

    }

    @SuppressLint("ResourceAsColor")
    private void verifyAnswer(Button button){
        if (canAnswer){
            if (answer.contentEquals(button.getText())){
                button.setBackgroundColor(Color.GREEN);
                correctAnswer++;
                ansFeedBackTv.setTextColor(Color.GREEN);
                ansFeedBackTv.setText("Correct Answer.");
            } else {
                button.setBackground(ContextCompat.getDrawable(getContext() , R.color.red));
                wrongAnswer++;
                ansFeedBackTv.setTextColor(Color.RED);
                ansFeedBackTv.setText("Wrong Answer. \nCorrect Answer : " + answer);
            }
        }
        canAnswer=false;
        countDownTimer.cancel();
        showNextBtn();
    }
}