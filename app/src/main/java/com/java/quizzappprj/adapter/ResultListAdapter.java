package com.java.quizzappprj.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.quizzappprj.R;
import com.java.quizzappprj.model.ResultModel;

import java.util.List;

public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ResultViewHolder>{

    private List<ResultModel> resultModels;

    public void setResultModels(List<ResultModel> resultModels) {
        this.resultModels = resultModels;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_result , parent , false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        ResultModel resultModel = resultModels.get(position);

        holder.title.setText(resultModel.getTitle());
        holder.totalTime.setText(resultModel.getTotalTime()+"s");
        holder.correctAnswer.setText(String.valueOf(resultModel.getCorrect()));
        holder.wrongAnswer.setText(String.valueOf(resultModel.getWrong()));
        holder.notAnswer.setText(String.valueOf(resultModel.getNotAnswered()));
    }

    @Override
    public int getItemCount() {
        if (resultModels == null){
            return 0;
        }else{
            return resultModels.size();
        }
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        private TextView title, correctAnswer, wrongAnswer, notAnswer, totalTime ;
        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            totalTime = itemView.findViewById(R.id.tvTotalTime);
            correctAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            wrongAnswer = itemView.findViewById(R.id.tvWrongAnswer);
            notAnswer = itemView.findViewById(R.id.tvNotAnswer);
        }

    }

}
