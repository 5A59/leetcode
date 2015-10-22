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

import value.Discuss;
import zy.com.leetcode.R;
import zy.com.leetcode.ReadDiscussActivity;

/**
 * Created by zy on 15-10-18.
 */
public class MyDissAdapter extends RecyclerView.Adapter<MyDissAdapter.MyHoldView>{

    private List<Discuss> discussList;
    private Context context;

    public MyDissAdapter(Context context,List<Discuss> discussList){
        this.context = context;
        this.discussList = discussList;
    }

    @Override
    public MyHoldView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_item,null);

        MyHoldView holdView = new MyHoldView(view);
        return holdView;
    }

    @Override
    public void onBindViewHolder(MyHoldView myHoldView, final int i) {

        myHoldView.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadDiscussActivity.class);
                intent.putExtra("dis", discussList.get(i));
                context.startActivity(intent);
            }
        });

        myHoldView.titleText.setText(discussList.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return discussList.size();
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
