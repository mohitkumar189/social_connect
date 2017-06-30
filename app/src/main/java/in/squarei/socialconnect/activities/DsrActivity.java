package in.squarei.socialconnect.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import in.squarei.socialconnect.R;

public class DsrActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView iv;
    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsr);
        iv = (ImageView) findViewById(R.id.iv);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                iv.setImageResource(R.drawable.img5);
                break;
            case R.id.btn2:
                iv.setImageResource(R.drawable.img6);
                break;
        }
    }
}
