package com.ian.submission2.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.ian.submission2.R;
import com.ian.submission2.adapter.SectionsPagerAdapter;
import com.ian.submission2.db.FavoriteHelper;
import com.ian.submission2.model.User;
import com.ian.submission2.viewmodel.DetailViewModel;
import com.ian.submission2.viewmodel.FollowerViewModel;
import com.ian.submission2.viewmodel.FollowingViewModel;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ian.submission2.MainActivity.EXTRA_USERNAME;
import static com.ian.submission2.db.DatabaseContract.NoteColumns.AVATAR;
import static com.ian.submission2.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.ian.submission2.db.DatabaseContract.NoteColumns.USERNAME;

public class DetailActivity extends AppCompatActivity {
    private TextView tvUsername, tvName, tvLocation, tvRepository, tvCompany;
    private CircleImageView civAvatar;
    private DetailViewModel model;
    private ProgressBar progressBar;
    private TabLayout tabs;
    private FloatingActionButton btnFav;
    private FavoriteHelper favoriteHelper;
    private Uri uriWithId;

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
        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        tvUsername = findViewById(R.id.tvUsername);
        tvName = findViewById(R.id.tvName);
        tvLocation = findViewById(R.id.tvLocation);
        tvRepository = findViewById(R.id.tvRepository);
        tvCompany = findViewById(R.id.tvCompany);
        civAvatar = findViewById(R.id.civAvatar);
        btnFav = findViewById(R.id.btnFavorite);

        model = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DetailViewModel.class);

        String username = getIntent().getStringExtra(EXTRA_USERNAME);
        model.setListUser(username);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(username);
        }

        if (isFavorite(username)) {
            btnFav.setImageResource(R.drawable.ic_baseline_favorite_black_48);
        }

        Log.d("Submission 2", "onCreate: " + username);

        setData();
    }

    private void setData() {
        Log.d("Submission 2", "setData: memulai..");
        model.getListUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(final User user) {
                progressBar.setVisibility(View.GONE);
                String name = !user.getName().equals("null") ? user.getName() : "";
                String location = !user.getLocation().equals("null") ? user.getLocation() : "";
                String follower = String.format(getResources().getString(R.string.follower), user.getFollower());
                String following = String.format(getResources().getString(R.string.following), user.getFollowing());
                String repository = String.format(getResources().getString(R.string.repo), user.getRepository());
                String company = !user.getCompany().equals("null") ? user.getCompany() : "";

                Objects.requireNonNull(tabs.getTabAt(0)).setText(follower);
                Objects.requireNonNull(tabs.getTabAt(1)).setText(following);

                tvName.setText(name);
                tvUsername.setText(user.getUsername());
                tvLocation.setText(location);
                tvRepository.setText(repository);
                tvCompany.setText(company);
                Glide.with(DetailActivity.this)
                        .load(user.getAvatar())
                        .apply(new RequestOptions().override(150, 150))
                        .into(civAvatar);
                FollowerViewModel followerViewModel = new ViewModelProvider(DetailActivity.this, new ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel.class);
                followerViewModel.setListUser(user.getUsername());

                FollowingViewModel followingViewModel = new ViewModelProvider(DetailActivity.this, new ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel.class);
                followingViewModel.setListUser(user.getUsername());

                btnFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFavorite(user.getUsername())) {
//                            int result = favoriteHelper.deleteByUsername(user.getUsername());
//                            if (result > 0) {
//                            }
                            uriWithId = Uri.parse(CONTENT_URI + "/" + user.getUsername());
                            getContentResolver().delete(uriWithId, null, null);
                            Toast.makeText(DetailActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                            btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_48);
                        } else {
                            ContentValues value = new ContentValues();
                            value.put(USERNAME, user.getUsername());
                            value.put(AVATAR, user.getAvatar());
                            getContentResolver().insert(CONTENT_URI, value);
                            Toast.makeText(DetailActivity.this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            btnFav.setImageResource(R.drawable.ic_baseline_favorite_black_48);
//                            long result = favoriteHelper.insert(value);
//                            if (result > 0) {}
                        }
                    }
                });

                Log.d("Submission 2", "showSelectedUser: " + EXTRA_USERNAME + "/" + user.getUsername());
            }
        });
    }

    boolean isFavorite(String username) {
        Cursor result = favoriteHelper.queryByUsername(username);
        return result.getCount() > 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }
}
