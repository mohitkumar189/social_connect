package in.squarei.socialconnect.activities.useraccesspackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.fragments.appIntro.FragmentOneAppIntro;
import in.squarei.socialconnect.fragments.appIntro.FragmentThreeAppIntro;
import in.squarei.socialconnect.fragments.appIntro.FragmentTwoAppIntro;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;

import static in.squarei.socialconnect.interfaces.AppConstants.IS_INTRO_COMPLETED;

public class AppIntroActivity extends AppIntro {

    FragmentOneAppIntro fragmentOneAppIntro;
    FragmentTwoAppIntro fragmentTwoAppIntro;
    FragmentThreeAppIntro fragmentThreeAppIntro;
    private static final String TAG="AppIntroActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentOneAppIntro = new FragmentOneAppIntro();
        fragmentTwoAppIntro = new FragmentTwoAppIntro();
        fragmentThreeAppIntro = new FragmentThreeAppIntro();
        addSlide(fragmentOneAppIntro);
        addSlide(fragmentTwoAppIntro);
        addSlide(fragmentThreeAppIntro);
        showSkipButton(true);
        View view = (View) doneButton.getParent();
        view.setBackgroundColor(this.getResources().getColor(R.color.green));
        Logger.info(TAG,"height=========="+view.getHeight());
        // setNavBarColor(545454);
        //  setBarColor(555555);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        doSomeWorkOnSkip();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        doSomeWorkOnSkip();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    private void doSomeWorkOnSkip() {
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance(this);
        sharedPreferenceUtils.putBoolean(IS_INTRO_COMPLETED, true);
        startActivity(new Intent(this, UserLoginActivity.class));
    }
}
