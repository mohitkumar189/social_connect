package in.squarei.socialconnect.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.modals.Users;


/**
 * Created by mohit kumar on 6/6/2017.
 */

public class UserChatADapter extends RecyclerView.Adapter<UserChatADapter.CustomViewHolder> {

    List<Users> detail;
    Context mContext;


    public UserChatADapter(Context context,  List<Users> list) {

        this.detail = list;
        this.mContext = context;


    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_message, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        Log.e("onBindViewHolder", "onBindViewHolderCalled");
        customViewHolder.usermessage.setText(detail.get(i).getFull_name());

    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView usermessage;
        LinearLayout layout;

        public CustomViewHolder(View view) {
            super(view);
            Log.e("CustomViewHolder", "CustomViewHolderCalled");
            this.usermessage = (TextView) view.findViewById(R.id.usermessage);
            this.layout = (LinearLayout) view.findViewById(R.id.layout);

        }

    }

}

