package com.example.amardeep.groupchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;


public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder> {

    private List<Message> meassageList;

    public MessageRecyclerAdapter(List<Message> messageList){
        this.meassageList = messageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.user_message.setText(meassageList.get(position).getMessage());
        holder.time.setText(meassageList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return meassageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView user_message , time;
        private View mView;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            user_message = (TextView) mView.findViewById(R.id.message_text);
            time = (TextView) mView.findViewById(R.id.time_id);
        }
    }
}
