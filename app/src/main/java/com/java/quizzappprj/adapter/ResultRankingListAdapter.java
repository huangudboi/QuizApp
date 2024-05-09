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

public class ResultRankingListAdapter extends RecyclerView.Adapter<ResultRankingListAdapter.ResultViewHolder>{

    private List<ResultModel> resultModels;

    public void setResultModels(List<ResultModel> resultModels) {
        this.resultModels = resultModels;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_ranking_result , parent , false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        ResultModel resultModel = resultModels.get(position);

        long totalQuestion = resultModel.getCorrect()+resultModel.getWrong()+resultModel.getNotAnswered();

        holder.rank.setText("No."+(position+1));
        holder.userId.setText(resultModel.getUserId());
        holder.totalTime.setText(resultModel.getTotalTime()+"s");
        holder.correctAnswer.setText(resultModel.getCorrect()+"/"+totalQuestion);
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

        private TextView userId, totalTime, correctAnswer, rank;
        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.rank);
            userId = itemView.findViewById(R.id.tvUserId);
            totalTime = itemView.findViewById(R.id.tvTotalTimeRanking);
            correctAnswer = itemView.findViewById(R.id.tvCorrectAnswerRanking);
        }

    }

}
