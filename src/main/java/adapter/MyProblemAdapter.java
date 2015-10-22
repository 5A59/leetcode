package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import value.Problem;
import zy.com.leetcode.R;
import zy.com.leetcode.ReadProblemActivity;

/**
 * Created by zy on 15-10-17.
 */
public class MyProblemAdapter extends RecyclerView.Adapter<MyProblemAdapter.MyHoldView>{

    private List<Problem> problemList;
    private Context context;

    public MyProblemAdapter(Context context, List<Problem> problemList){
        this.context = context;
        this.problemList = problemList;
    }

    @Override
    public MyHoldView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_item,null);

        MyHoldView holdView = new MyHoldView(view);
        return holdView;
    }

    @Override
    public void onBindViewHolder(MyHoldView myHoldView, int i) {
        final int finalI = i;
        myHoldView.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadProblemActivity.class);
                intent.putExtra("pro",problemList.get(finalI));

                context.startActivity(intent);
            }
        });

        myHoldView.difText.setText(problemList.get(i).getDifficulty());
        myHoldView.idText.setText(problemList.get(i).getId());
        myHoldView.titleText.setText(problemList.get(i).getTitle());
        myHoldView.accText.setText(problemList.get(i).getAcc());

        if (problemList.get(i).isAc()){
            myHoldView.imgAC.setImageResource(R.mipmap.ac);
        }else {
            myHoldView.imgAC.setImageResource(R.mipmap.none);
        }
    }

    @Override
    public int getItemCount() {
        return problemList.size();
    }

    public class MyHoldView extends RecyclerView.ViewHolder{

        View view;
        ImageView imgAC;
        TextView titleText;
        TextView idText;
        TextView difText;
        TextView accText;

        public MyHoldView(View itemView) {
            super(itemView);
            this.view = itemView;

            imgAC = (ImageView) view.findViewById(R.id.img_ac);
            titleText = (TextView) view.findViewById(R.id.text_title);
            idText = (TextView) view.findViewById(R.id.text_id);
            difText = (TextView) view.findViewById(R.id.text_dif);
            accText = (TextView) view.findViewById(R.id.text_acc);
        }
    }

}
