package in.squarei.socialconnect.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.interfaces.ItemClickListener;
import in.squarei.socialconnect.modals.UserFeedsData;
import in.squarei.socialconnect.modals.UsersListData;

/**
 * Created by mohit kumar on 5/5/2017.
 */

public class UserFeedsAdapter extends RecyclerView.Adapter<UserFeedsAdapter.MyViewHolder> {

    private List<UserFeedsData> userFeedsData;
    private Context context;
    private ItemClickListener itemClickListener;

    public UserFeedsAdapter(List<UserFeedsData> userFeedsData, Context context, ItemClickListener itemClickListener) {
        this.userFeedsData = userFeedsData;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.top_view_users_post, parent, false);

        return new UserFeedsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
/*        holder.tvPostedBy.setText(userFeedsData.get(position).getPostSenderName());
        holder.tvPostTitle.setText(userFeedsData.get(position).getPostTitleComment());
        holder.tvUserLikes.setText(userFeedsData.get(position).getUserLikes() + " likes");
        holder.tvUserComments.setText(userFeedsData.get(position).getUserComments() + " comments");

        Picasso.with(context)
                .load(userFeedsData.get(position).getPostImageUrl())
                .placeholder(context.getResources().getDrawable(R.drawable.picture)) //this is optional the image to display while the url image is downloading
                .error(context.getResources().getDrawable(R.drawable.picture))         //this is also optional if some error has occurred in downloading the image this image would be displayed
                .into(holder.ivPostImage);*/
        holder.bind(userFeedsData.get(position), position, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return userFeedsData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvPostedBy, tvPostTitle, tvUserComments, tvUserLikes, tvUserShares;
        RelativeLayout relativeLikeLayout, relativeCommentLayout, relativeShareLayout;
        ImageView ivUserPostOption, ivPostImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPostedBy = (TextView) itemView.findViewById(R.id.tvPostedBy);
            tvPostTitle = (TextView) itemView.findViewById(R.id.tvPostTitle);
            tvUserComments = (TextView) itemView.findViewById(R.id.tvUserComments);
            tvUserLikes = (TextView) itemView.findViewById(R.id.tvUserLikes);
            // tvUserShares = (TextView) itemView.findViewById(R.id.tvUserShares);
            relativeLikeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLikeLayout);
            relativeCommentLayout = (RelativeLayout) itemView.findViewById(R.id.relativeCommentLayout);
            relativeShareLayout = (RelativeLayout) itemView.findViewById(R.id.relativeShareLayout);
            ivUserPostOption = (ImageView) itemView.findViewById(R.id.ivUserPostOption);
            ivPostImage = (ImageView) itemView.findViewById(R.id.ivPostImage);

            relativeCommentLayout.setOnClickListener(this);
            relativeLikeLayout.setOnClickListener(this);
        }

        public void bind(UserFeedsData userFeedsData, int position, ItemClickListener itemClickListener) {
            tvPostedBy.setText(userFeedsData.getPostSenderName());
            tvPostTitle.setText(userFeedsData.getPostTitleComment());
            tvUserLikes.setText(userFeedsData.getUserLikes() + " likes");
            tvUserComments.setText(userFeedsData.getUserComments() + " comments");

            Picasso.with(context)
                    .load(userFeedsData.getPostImageUrl())
                    .placeholder(context.getResources().getDrawable(R.drawable.picture)) //this is optional the image to display while the url image is downloading
                    .error(context.getResources().getDrawable(R.drawable.picture))         //this is also optional if some error has occurred in downloading the image this image would be displayed
                    .into(ivPostImage);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.relativeCommentLayout:
                    itemClickListener.onItemClickCallback(getAdapterPosition(), 1); // 1 for the comments
                    break;
                case R.id.relativeLikeLayout:
                    itemClickListener.onItemClickCallback(getAdapterPosition(), 2); // 2 for likes
                    break;
                default:
                    break;
            }
        }
    }
}
