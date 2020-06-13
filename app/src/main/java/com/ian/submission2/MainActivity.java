package com.ian.submission2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ian.submission2.activity.DetailActivity;
import com.ian.submission2.activity.FavoriteActivity;
import com.ian.submission2.activity.SettingActivity;
import com.ian.submission2.adapter.UserAdapter;
import com.ian.submission2.model.User;
import com.ian.submission2.viewmodel.MainViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private ProgressBar progressBar;
    private UserAdapter adapter;
    private TextView tvSearch;

    public static final String EXTRA_USERNAME = "extra_username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSearch = findViewById(R.id.tvSearch);

        RecyclerView rvList = findViewById(R.id.rvMain);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter();
        adapter.notifyDataSetChanged();
        rvList.setAdapter(adapter);

        adapter.setOnItemClickCallBack(new UserAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClicked(User user) {
                showSelectedUser(user);
            }
        });

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mainViewModel.setListUser(query);
                    progressBar.setVisibility(View.VISIBLE);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!(newText.equals(""))) {
                        tvSearch.setVisibility(View.INVISIBLE);
                        mainViewModel.setListUser(newText);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
            });

            mainViewModel.getListUser().observe(this, new Observer<ArrayList<User>>() {
                @Override
                public void onChanged(ArrayList<User> users) {
                    if (users != null) {
                        adapter.setData(users);
                    } else {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.notfound), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuFavorite) {
            startActivity(new Intent(this, FavoriteActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuSetting) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSelectedUser(User user) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_USERNAME, user.getUsername());
        startActivity(intent);
    }
}
