package com.example.stillhet;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.stillhet.databinding.ActivityNavigationBinding;
import com.example.stillhet.ui.discussion.FindDiscussionFragment;
import com.example.stillhet.ui.discussion.NewDiscussionFragment;
import com.example.stillhet.ui.music.AlbumFragment;
import com.example.stillhet.ui.music.RecommendationsFragment;
import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavController navController;

    RadioButton sunset, sunrize, day, night, white;

    FragmentContainerView fragmentContainerView, fragmentContainerView2;
    LinearLayout linearLayout, linearLayout2;
    RecyclerView recyclerView, recyclerView2;
    EditText editText;

    JcPlayerView jcPlayerView;
    TextView playerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.stillhet.databinding.ActivityNavigationBinding binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        jcPlayerView = findViewById(R.id.player);
        playerButton = findViewById(R.id.playerButton);

        playerButton.setOnClickListener(v -> {
            jcPlayerView.setVisibility(View.GONE);
            playerButton.setVisibility(View.GONE);
            jcPlayerView.pause();
        });

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_setting, R.id.nav_help, R.id.nav_find_discussion, R.id.nav_new_discussion, R.id.nav_album_music, R.id.nav_recommendation_music)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void MyDiscussionClick(View view) {
        fragmentContainerView = findViewById(R.id.fragmentContainerDiscuss);
        linearLayout = findViewById(R.id.linearLayout6);
        recyclerView = findViewById(R.id.myDiscussionReView);

        if (view.getId() == R.id.MyDiscussion) {
            fragmentContainerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (view.getId() == R.id.FindDiscussion) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainerDiscuss, new FindDiscussionFragment());
            ft.commit();

            fragmentContainerView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
        if (view.getId() == R.id.CreateDiscussion) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainerDiscuss, new NewDiscussionFragment());
            ft.commit();

            fragmentContainerView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void MyMusicClick(View view) {
        fragmentContainerView2 = findViewById(R.id.fragmentContainerMusic);
        linearLayout2 = findViewById(R.id.linearLayout4);
        recyclerView2 = findViewById(R.id.My_music);
        editText = findViewById(R.id.findLine2);

        if (view.getId() == R.id.MyMusic) {
            fragmentContainerView2.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
        }
        if (view.getId() == R.id.AlbumMusic) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainerMusic, new AlbumFragment());
            ft.commit();

            fragmentContainerView2.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
        }
        if (view.getId() == R.id.RecommendationsMusic) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainerMusic, new RecommendationsFragment());
            ft.commit();

            fragmentContainerView2.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
        }
    }

    public void SetBackground(View v) {
        sunrize = findViewById(R.id.Rassvet);
        sunset = findViewById(R.id.Zakat);
        day = findViewById(R.id.Day);
        night = findViewById(R.id.Night);
        white = findViewById(R.id.white);
        DrawerLayout view = findViewById(R.id.drawer_layout);
        if (v.getId() == R.id.buttonChangeTheme) {
            if (sunrize.isChecked()) {
                view.setBackgroundResource(R.drawable.sunrise);
            } else if (sunset.isChecked()) {
                view.setBackgroundResource(R.drawable.sunset);
            } else if (day.isChecked()) {
                view.setBackgroundResource(R.drawable.day);
            } else if (night.isChecked()) {
                view.setBackgroundResource(R.drawable.night);
            } else if (white.isChecked()) {
                view.setBackgroundResource(R.color.white);
            }
        }
    }
}