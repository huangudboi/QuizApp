package com.java.quizzappprj.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.java.quizzappprj.R;
import com.java.quizzappprj.model.SetQuizModel;
import com.java.quizzappprj.viewmodel.QuizListViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyNewSetQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyNewSetQuestionFragment extends Fragment {

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton, timeButton, addSetQuestionButton;
    private List<String> setQuizTitle;
    private List<SetQuizModel> quizList;
    private AutoCompleteTextView titleSetQuiz;
    private ArrayAdapter<String> adapterItems;
    private QuizListViewModel viewModel;
    private NavController navController;
    private TextView tvDifficulty, tvManufacture, tvNumberQuestion, tvFees;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuyNewSetQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyNewSetQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyNewSetQuestionFragment newInstance(String param1, String param2) {
        BuyNewSetQuestionFragment fragment = new BuyNewSetQuestionFragment();
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
                .getInstance(getActivity().getApplication())).get(QuizListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_new_set_question, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateButton = view.findViewById(R.id.buttonSelectDate);
        timeButton = view.findViewById(R.id.buttonSelectTime);
        addSetQuestionButton = view.findViewById(R.id.buttonAddSetQuestion);
        tvDifficulty = view.findViewById(R.id.tv_difficulty);
        tvManufacture = view.findViewById(R.id.tv_manufacture);
        tvFees = view.findViewById(R.id.tv_fees);
        tvNumberQuestion = view.findViewById(R.id.tv_questions);
        titleSetQuiz = view.findViewById(R.id.actv_title_set);
        navController = Navigation.findNavController(view);

        viewModel.getSetQuizListLiveData().observe(getViewLifecycleOwner(), new Observer<List<SetQuizModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<SetQuizModel> quizListModels) {
                setQuizTitle = new ArrayList<>();
                quizList = quizListModels;
                for (SetQuizModel model : quizListModels) {
                    setQuizTitle.add(model.getTitle());
                }

                adapterItems = new ArrayAdapter<>(getActivity(), R.layout.each_set_quizz, setQuizTitle);
                titleSetQuiz.setAdapter(adapterItems);
            }
        });

        titleSetQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = parent.getItemAtPosition(position).toString();
                for (SetQuizModel model : quizList) {
                    if (title.equals(model.getTitle())) {
                        tvDifficulty.setText("Difficulty: "+model.getDifficulty());
                        tvFees.setText("Fees: "+model.getFees()+"$");
                        tvManufacture.setText("Manufacture: "+model.getManufacture());
                        tvNumberQuestion.setText("Number question: "+model.getQuestions());
                        break;
                    }
                }

            }
        });

        // datepicker
        initDatePicker();
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        // timepicker
        initTimePicker();
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        addSetQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Add Set Question Successful", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.action_buyNewSetQuestionFragment_to_listFragment);
            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateButton.setText(formatDate(year, month, dayOfMonth));
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        dateButton.setText(formatDate(year, month, day));

        int style = AlertDialog.BUTTON_POSITIVE;
        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis()+86400000);
    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeButton.setText(hourOfDay+":"+minute);
            }
        };

        Calendar cal = Calendar.getInstance();
        int hrs = cal.get(Calendar.HOUR);
        int mins = cal.get(Calendar.MINUTE);
        timeButton.setText(hrs+":"+mins);

        int style = AlertDialog.BUTTON_POSITIVE;
        timePickerDialog = new TimePickerDialog(getContext(), style, timeSetListener, hrs, mins, true);

    }


    private String formatDate(int year, int month, int day) {
        month = month+1;
        if (month<10 && day<10) {
            return "0"+day+"/0"+month+"/"+year;
        } else if (month<10) {
            return day+"/0"+month+"/"+year;
        } else if (day<10) {
            return "0"+day+"/"+month+"/"+year;
        }

        return day+"/"+month+"/"+year;
    }
}