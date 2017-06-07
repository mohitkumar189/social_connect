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
import in.squarei.socialconnect.modals.NotificationData;

/**
 * Created by mohit kumar on 5/23/2017.
 */

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.MyViewHolder> {

    private List<NotificationData> notificationData;
    private Context context;
    private ItemClickListener itemClickListener;

    public NotificationsListAdapter(List<NotificationData> notificationData, Context context, ItemClickListener itemClickListener) {
        this.notificationData = notificationData;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public NotificationsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_notification, parent, false);
        if (viewType == 2) {
            itemView = LayoutInflater.from(context)
                    .inflate(R.layout.row_update, parent, false);
        }
        return new NotificationsListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationsListAdapter.MyViewHolder holder, int position) {
        holder.bind(notificationData.get(position), position, itemClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return notificationData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNotificationTitle, tvNotificationTime, tvNotificationDescription;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNotificationTitle = (TextView) itemView.findViewById(R.id.tvNotificationTitle);
            tvNotificationTime = (TextView) itemView.findViewById(R.id.tvNotificationTime);
            tvNotificationDescription = (TextView) itemView.findViewById(R.id.tvNotificationDescription);

        }

        public void bind(NotificationData notificationData, int position, ItemClickListener itemClickListener) {
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

