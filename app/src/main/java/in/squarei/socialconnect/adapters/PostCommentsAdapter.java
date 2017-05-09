package in.squarei.socialconnect.adapters;

import android.content.Context;
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
import in.squarei.socialconnect.modals.PostCommentsData;
import in.squarei.socialconnect.modals.UserFeedsData;

/**
 * Created by mohit kumar on 5/9/2017.
 */

public class PostCommentsAdapter extends RecyclerView.Adapter<PostCommentsAdapter.MyViewHolder> {

    private List<PostCommentsData> postCommentsData;
    private Context context;
    private ItemClickListener itemClickListener;

    public PostCommentsAdapter(List<PostCommentsData> postCommentsData, Context context, ItemClickListener itemClickListener) {
        this.postCommentsData = postCommentsData;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public PostCommentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.top_view_user_for_comments, parent, false);

        return new PostCommentsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostCommentsAdapter.MyViewHolder holder, int position) {
        holder.bind(postCommentsData.get(position), position, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return postCommentsData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUsrName, tvUsrComment, tvCommentTime;
        ImageView ivProfilePic;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvUsrName = (TextView) itemView.findViewById(R.id.tvUsrName);
            tvUsrComment = (TextView) itemView.findViewById(R.id.tvUsrComment);
            tvCommentTime = (TextView) itemView.findViewById(R.id.tvCommentTime);
            ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);

        }

        public void bind(PostCommentsData postCommentsData, int position, ItemClickListener itemClickListener) {
            tvUsrName.setText(postCommentsData.getCommentByName());
            tvUsrComment.setText(postCommentsData.getPostComment());
            tvCommentTime.setText(postCommentsData.getCommentOn());

            Picasso.with(context)
                    .load(postCommentsData.getProfilePic())
                    .placeholder(context.getResources().getDrawable(R.drawable.picture)) //this is optional the image to display while the url image is downloading
                    .error(context.getResources().getDrawable(R.drawable.picture))         //this is also optional if some error has occurred in downloading the image this image would be displayed
                    .into(ivProfilePic);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            }
        }
    }
}
