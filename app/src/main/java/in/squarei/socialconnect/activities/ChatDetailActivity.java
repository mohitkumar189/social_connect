package in.squarei.socialconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.adapters.UserChatADapter;
import in.squarei.socialconnect.modals.Users;
import in.squarei.socialconnect.utils.Logger;

public class ChatDetailActivity extends SocialConnectBaseActivity {

    private final String TAG="ChatDetailActivity";
    private int chatId;
    private String userName;
    private RecyclerView recyclerView;
    private TextView tvName;
    private EditText editMessage;
    private Button btnSend;
    private UserChatADapter chatADapter;
    private List<Users> chatData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        Intent intent = getIntent();
        String id = intent.getStringExtra("chatId");
        if (id != null && !id.isEmpty())
            chatId = Integer.parseInt(id);
        userName = getIntent().getStringExtra("userName");
        Logger.info(TAG,"===chat id received:=="+chatId);

    }

    @Override
    protected void initViews() {
        chatData = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvName = (TextView) findViewById(R.id.tvName);
        editMessage = (EditText) findViewById(R.id.editMessage);
        btnSend = (Button) findViewById(R.id.btnSend);
    }

    @Override
    protected void initContext() {
        context = this;
        currentActivity = this;
    }

    @Override
    protected void initListners() {
        btnSend.setOnClickListener(this);
    }

    @Override
    protected boolean isActionBar() {
        return false;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
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
        chatData.add(new Users(0, editMessage.getText().toString(), 0));
        chatADapter.notifyDataSetChanged();
    }
}
