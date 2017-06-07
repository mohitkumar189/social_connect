package in.squarei.socialconnect.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.adapters.ChatAdapter;
import in.squarei.socialconnect.interfaces.ItemClickListener;

public class UserChatActivity extends SocialConnectBaseActivity implements ItemClickListener {
    private RecyclerView recyclerView;
    private List<String> chatData;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        prepareData();
        setTitle("Chat");
    }

    @Override
    protected void initViews() {
        chatData = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    protected void initContext() {
        context = UserChatActivity.this;
        currentActivity = UserChatActivity.this;
    }

    @Override
    protected void initListners() {

    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }

    @Override
    protected boolean isNavigationView() {
        return false;
    }

    @Override
    protected boolean isTabs() {
        return false;
    }

    @Override
    protected boolean isFab() {
        return false;
    }

    @Override
    protected boolean isDrawerListener() {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    private void prepareData() {
        chatData.add(null);
        chatData.add(null);
        chatData.add(null);
        chatData.add(null);
        chatData.add(null);
        chatData.add(null);
        chatAdapter = new ChatAdapter(chatData, context, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(chatAdapter);

    }

    @Override
    public void onItemClickCallback(int position, int flag) {

    }
}
