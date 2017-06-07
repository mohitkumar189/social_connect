package in.squarei.socialconnect.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.interfaces.ItemClickListener;

/**
 * Created by mohit kumar on 5/23/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<String> chatData;
    private Context context;
    private ItemClickListener itemClickListener;

    public ChatAdapter(List<String> chatData, Context context, ItemClickListener itemClickListener) {
        this.chatData = chatData;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType % 2 == 0) {
            itemView = LayoutInflater.from(context)
                    .inflate(R.layout.row_chat_sender, parent, false);
        } else {
            itemView = LayoutInflater.from(context)
                    .inflate(R.layout.row_chat_user, parent, false);
        }
        return new ChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.MyViewHolder holder, int position) {
        holder.bind(chatData.get(position), position, itemClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chatData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvChatText;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvChatText = (TextView) itemView.findViewById(R.id.tvChatText);
        }

        public void bind(String chatData, int position, ItemClickListener itemClickListener) {
            // tvUsrName.setText(notificationData.getCommentByName());
            // tvUsrComment.setText(notificationData.getPostComment());
            // tvCommentTime.setText(notificationData.getCommentOn());
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            }
        }
    }
}

