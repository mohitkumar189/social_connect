package in.squarei.socialconnect.activities;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import in.squarei.socialconnect.R;

public class ActivityLandmarkCityDetail extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private LinearLayout btn1, btn2, btn3, btn4, btn5;
    private ImageView image;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark_city_detail);
        init();
        initListener();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Landmark City");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn1 = (LinearLayout) findViewById(R.id.btn1);
        btn2 = (LinearLayout) findViewById(R.id.btn2);
        btn3 = (LinearLayout) findViewById(R.id.btn3);
        btn4 = (LinearLayout) findViewById(R.id.btn4);
        btn5 = (LinearLayout) findViewById(R.id.btn5);
        image = (ImageView) findViewById(R.id.image);

        search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(this);
        search.setQueryHint("Search cafe, laundary,garden...");
        search.setEnabled(true);

        image.setOnClickListener(this);
    }

    private void initListener() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn1:
                image.setImageDrawable(getResources().getDrawable(R.drawable.hospital));
                break;
            case R.id.btn2:
                image.setImageDrawable(getResources().getDrawable(R.drawable.allen));
                break;
            case R.id.btn3:
                image.setImageDrawable(getResources().getDrawable(R.drawable.marriage));
                break;
            case R.id.btn4:
                image.setImageDrawable(getResources().getDrawable(R.drawable.hostel));
                break;
            case R.id.btn5:
                image.setImageDrawable(getResources().getDrawable(R.drawable.city));
                break;
            case R.id.image:
                showDialogForComment();
                // image.setImageDrawable(getResources().getDrawable(R.drawable.pg_mobile_world));
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() >= 3) {
            //    Toast.makeText(this, "query submit", Toast.LENGTH_SHORT).show();
            changeImage(query.toLowerCase().trim());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() >= 3) {
            //  Toast.makeText(this, "newText submit", Toast.LENGTH_SHORT).show();
            //   chabgeImage(newText.toLowerCase().trim());
        }
        return false;
    }

    private void changeImage(String imageName) {
        switch (imageName) {
            case "hospital":
                image.setImageDrawable(getResources().getDrawable(R.drawable.hospital));
                break;
            case "city":
                image.setImageDrawable(getResources().getDrawable(R.drawable.city));
                break;
            case "garden":
                image.setImageDrawable(getResources().getDrawable(R.drawable.marriage));
                break;
            case "hostel":
                image.setImageDrawable(getResources().getDrawable(R.drawable.hostel));
                break;
            case "allen":
                image.setImageDrawable(getResources().getDrawable(R.drawable.allen));
                break;
            case "cafe":
                image.setImageDrawable(getResources().getDrawable(R.drawable.img2));
                break;
            case "laundary":
                image.setImageDrawable(getResources().getDrawable(R.drawable.img1));
                break;
        }
    }

    private void showDialogForComment() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_area_detail, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialog.dismiss();
            }
        });
        /*
        deleteDialogView.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });*/

        deleteDialog.show();
    }
}
