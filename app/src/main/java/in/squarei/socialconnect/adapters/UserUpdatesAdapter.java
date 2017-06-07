package in.squarei.socialconnect.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.interfaces.ItemClickListener;
import in.squarei.socialconnect.modals.UpdatesData;

/**
 * Created by mohit kumar on 5/23/2017.
 */

public class UserUpdatesAdapter extends RecyclerView.Adapter<UserUpdatesAdapter.MyViewHolder> {

    private List<UpdatesData> updatesData;
    private Context context;
    private ItemClickListener itemClickListener;

    public UserUpdatesAdapter(List<UpdatesData> updatesData, Context context, ItemClickListener itemClickListener) {
        this.updatesData = updatesData;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public UserUpdatesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_update, parent, false);

        return new UserUpdatesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserUpdatesAdapter.MyViewHolder holder, int position) {
        holder.bind(updatesData.get(position), position, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return updatesData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUpdateLocation, tvUpdateTitle;
        ImageView ivUpdateImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvUpdateLocation = (TextView) itemView.findViewById(R.id.tvUpdateLocation);
            tvUpdateTitle = (TextView) itemView.findViewById(R.id.tvUpdateTitle);
            ivUpdateImage = (ImageView) itemView.findViewById(R.id.ivUpdateImage);

        }

        public void bind(UpdatesData notificationData, int position, ItemClickListener itemClickListener) {
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
