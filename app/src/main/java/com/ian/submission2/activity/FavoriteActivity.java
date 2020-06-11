package com.ian.submission2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ian.submission2.R;
import com.ian.submission2.adapter.UserAdapter;
import com.ian.submission2.db.FavoriteHelper;
import com.ian.submission2.helper.MappingHelper;
import com.ian.submission2.model.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.ian.submission2.MainActivity.EXTRA_USERNAME;

public class FavoriteActivity extends AppCompatActivity implements LoadUserCallback {
    private RecyclerView rvFavorite;
    private UserAdapter adapter;
    private FavoriteHelper favoriteHelper;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        progressBar = findViewById(R.id.progressBar2);
        rvFavorite = findViewById(R.id.rvFavorite);
        rvFavorite.setHasFixedSize(true);
        rvFavorite.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter();
        rvFavorite.setAdapter(adapter);
        adapter.setOnItemClickCallBack(new UserAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClicked(User user) {
                showSelectedUser(user);
            }
        });

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        if (savedInstanceState == null) {
            new LoadUserAsync(favoriteHelper, this).execute();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Favorite");
        }
    }

    private void showSelectedUser(User user) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_USERNAME, user.getUsername());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<User> users) {
        progressBar.setVisibility(View.GONE);
        if (users.size() > 0) {
            adapter.setData(users);
        } else {
            adapter.setData(new ArrayList<User>());
            Snackbar.make(rvFavorite, "Tidak ada data", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    private static class LoadUserAsync extends AsyncTask<Void, Void, ArrayList<User>> {
        private final WeakReference<FavoriteHelper> weakFavoriteHelper;
        private final WeakReference<LoadUserCallback> weakCallback;

        private LoadUserAsync(FavoriteHelper favoriteHelper, LoadUserCallback callback) {
            weakFavoriteHelper = new WeakReference<>(favoriteHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            Cursor dataCursor = weakFavoriteHelper.get().queryAll();
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);
            weakCallback.get().postExecute(users);
        }
    }
}

interface LoadUserCallback {
    void preExecute();
    void postExecute(ArrayList<User> users);
}