package com.ian.submission2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.ian.submission2.R;
import com.ian.submission2.adapter.SectionsPagerAdapter;
import com.ian.submission2.data.User;
import com.ian.submission2.model.DetailModel;
import com.ian.submission2.model.FollowerModel;
import com.ian.submission2.model.FollowingModel;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ian.submission2.MainActivity.EXTRA_USERNAME;

public class DetailActivity extends AppCompatActivity {
    private TextView tvUsername, tvName, tvLocation, tvRepository, tvCompany;
    private CircleImageView civAvatar;
    private DetailModel model;
    private ProgressBar progressBar;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBar = findViewById(R.id.pbDetail);
        progressBar.setVisibility(View.VISIBLE);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager pager = findViewById(R.id.view_pager);
        pager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);

        tvUsername = findViewById(R.id.tvUsername);
        tvName = findViewById(R.id.tvName);
        tvLocation = findViewById(R.id.tvLocation);
        tvRepository = findViewById(R.id.tvRepostitory);
        tvCompany = findViewById(R.id.tvCompany);
        civAvatar = findViewById(R.id.civAvatar);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DetailModel.class);

        String username = getIntent().getStringExtra(EXTRA_USERNAME);
        model.setListUser(username);

        Log.d("Submission 2", "onCreate: "+username);

        setData();
    }

    private void setData() {
        Log.d("Submission 2", "setData: memulai..");
        model.getListUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                progressBar.setVisibility(View.GONE);
                String name = !user.getName().equals("null") ? user.getName() : "";
                String location = !user.getLocation().equals("null") ? user.getLocation() : "";
                String follower = String.format(getResources().getString(R.string.follower), user.getFollower());
                String following = String.format(getResources().getString(R.string.following), user.getFollowing());
                String repository = String.format(getResources().getString(R.string.repo), user.getRepository());
                String company = !user.getCompany().equals("null") ? user.getCompany() : "";

                tabs.getTabAt(0).setText(follower);
                tabs.getTabAt(1).setText(following);

                tvName.setText(name);
                tvUsername.setText(user.getUsername());
                tvLocation.setText(location);
                tvRepository.setText(repository);
                tvCompany.setText(company);
                Glide.with(DetailActivity.this)
                        .load(user.getAvatar())
                        .apply(new RequestOptions().override(150,150))
                        .into(civAvatar);
                FollowerModel followerModel = new ViewModelProvider(DetailActivity.this, new ViewModelProvider.NewInstanceFactory()).get(FollowerModel.class);
                followerModel.setListUser(user.getUsername());

                FollowingModel followingModel = new ViewModelProvider(DetailActivity.this, new ViewModelProvider.NewInstanceFactory()).get(FollowingModel.class);
                followingModel.setListUser(user.getUsername());

                Log.d("Submission 2", "showSelectedUser: "+EXTRA_USERNAME+"/"+user.getUsername());
            }
        });
    }
}
