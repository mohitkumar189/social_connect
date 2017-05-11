package in.squarei.socialconnect.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.interfaces.ItemClickListener;
import in.squarei.socialconnect.modals.UsersListData;

/**
 * Created by mohit kumar on 5/11/2017.
 */

public class UserFriendsSuggestionAdapter extends RecyclerView.Adapter<UserFriendsSuggestionAdapter.MyViewHolder> {
    ItemClickListener itemClickListener;
    private List<UsersListData> usersListData;
    private Context ctx;

    public UserFriendsSuggestionAdapter(List<UsersListData> usersListData, Context ctx, ItemClickListener itemClickListener) {
        this.usersListData = usersListData;
        this.ctx = ctx;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(ctx)
                .inflate(R.layout.top_view_user_suggest_profile, parent, false);

        return new UserFriendsSuggestionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(usersListData.get(position), position, itemClickListener);
/*        holder.tvUserProfileName.setText(usersListData.get(position).getUserName());
        holder.tvUserProfileStatus.setText(usersListData.get(position).getUserStatus());
        holder.tvUserType.setText(usersListData.get(position).getUserType());
        Picasso.with(ctx)
                .load(usersListData.get(position).getUserProfilePic())
                .placeholder(ctx.getResources().getDrawable(R.drawable.man)) //this is optional the image to display while the url image is downloading
                .error(ctx.getResources().getDrawable(R.drawable.man))         //this is also optional if some error has occurred in downloading the image this image would be displayed
                .into(holder.ivUserProfile);*/
    }

    @Override
    public int getItemCount() {
        return usersListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUserProfileName, tvUserProfileStatus, tvUserType, tvRemoveFriend, tvAddFriend;
        ImageView ivUserProfile;
        LinearLayout linearActionViewHolder, user_view_container;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvUserProfileName = (TextView) itemView.findViewById(R.id.tvUserProfileName);
            tvUserProfileStatus = (TextView) itemView.findViewById(R.id.tvUserProfileStatus);
            //  tvUserType = (TextView) itemView.findViewById(R.id.tvUserType);
            tvAddFriend = (TextView) itemView.findViewById(R.id.tvRemoveFriend);
            tvRemoveFriend = (TextView) itemView.findViewById(R.id.tvAddFriend);
            linearActionViewHolder = (LinearLayout) itemView.findViewById(R.id.linearActionViewHolder);
            //  user_view_container = (LinearLayout) itemView.findViewById(R.id.user_view_container);
            ivUserProfile = (ImageView) itemView.findViewById(R.id.ivUserProfile);
            itemView.setOnClickListener(this);
            tvAddFriend.setOnClickListener(this);
            tvRemoveFriend.setOnClickListener(this);

        }

        public void bind(UsersListData usersListData, int position, ItemClickListener itemClickListener) {
            tvUserProfileName.setText(usersListData.getUserName());
            tvUserProfileStatus.setText(usersListData.getUserStatus());
//            tvUserType.setText(usersListData.getUserType());
            Picasso.with(ctx)
                    .load(usersListData.getUserProfilePic())
                    .placeholder(ctx.getResources().getDrawable(R.drawable.man)) //this is optional the image to display while the url image is downloading
                    .error(ctx.getResources().getDrawable(R.drawable.man))         //this is also optional if some error has occurred in downloading the image this image would be displayed
                    .into(ivUserProfile);

            if (!usersListData.isFriend()) {
                linearActionViewHolder.setVisibility(View.VISIBLE);
            } else {
                if (linearActionViewHolder.getVisibility() == View.VISIBLE) {
                    linearActionViewHolder.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvAddFriend:
                    itemClickListener.onItemClickCallback(getAdapterPosition(), 5); // 5 to accept suggested friend request
                    break;
                case R.id.tvRemoveFriend:
                    itemClickListener.onItemClickCallback(getAdapterPosition(), 6);// 6 to reject suggested friend request
                    break;
                default:
                    itemClickListener.onItemClickCallback(getAdapterPosition(), 1); // 1 is for suggestions

                    break;
            }

        }
    }
}

