package com.ian.submission2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ian.submission2.activity.DetailActivity;
import com.ian.submission2.adapter.UserAdapter;
import com.ian.submission2.data.User;
import com.ian.submission2.model.MainModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MainModel mainModel;
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

        mainModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainModel.class);
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
                    mainModel.setListUser(query);
                    progressBar.setVisibility(View.VISIBLE);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!(newText.equals(""))) {
                        tvSearch.setVisibility(View.INVISIBLE);
                        mainModel.setListUser(newText);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
            });

            mainModel.getListUser().observe(this, new Observer<ArrayList<User>>() {
                @Override
                public void onChanged(ArrayList<User> users) {
                    if (users != null) {
                        adapter.setData(users);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.notfound), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }

        return true;
    }

    private void showSelectedUser(User user) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_USERNAME, user.getUsername());
        startActivity(intent);
    }
}
