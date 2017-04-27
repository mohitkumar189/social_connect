package in.squarei.socialconnect.activities.useraccesspackage;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import com.github.paolorotolo.appintro.AppIntro;

import in.squarei.socialconnect.fragments.appIntro.FragmentOneAppIntro;
import in.squarei.socialconnect.fragments.appIntro.FragmentTwoAppIntro;

public class AppIntroActivity extends AppIntro {

    FragmentOneAppIntro fragmentOneAppIntro;
    FragmentTwoAppIntro fragmentTwoAppIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentOneAppIntro = new FragmentOneAppIntro();
        fragmentTwoAppIntro = new FragmentTwoAppIntro();
        addSlide(fragmentOneAppIntro);
        addSlide(fragmentTwoAppIntro);

        showSkipButton(true);
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

    }
}
