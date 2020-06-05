package com.ian.submission2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ian.submission2.MainActivity;
import com.ian.submission2.R;
import com.ian.submission2.activity.DetailActivity;
import com.ian.submission2.adapter.UserAdapter;
import com.ian.submission2.model.User;
import com.ian.submission2.viewmodel.FollowerViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowerFragment extends Fragment {
    private RecyclerView rvFollower;
    private FollowerViewModel followerViewModel;
    private UserAdapter adapter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_follower, container, false);
    }

    public FollowerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFollower = view.findViewById(R.id.rvFollower);
        progressBar = view.findViewById(R.id.pbFollower);
        adapter = new UserAdapter();
        adapter.setOnItemClickCallBack(new UserAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClicked(User user) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(MainActivity.EXTRA_USERNAME, user.getUsername());
                startActivity(intent);
            }
        });

        followerViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel.class);

        getDataUser();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter.notifyDataSetChanged();
        rvFollower.setHasFixedSize(true);
        rvFollower.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFollower.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void getDataUser() {
        followerViewModel.getListUser().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                progressBar.setVisibility(View.GONE);
                Log.d("Submission 2", "onChanged: " + users);
                if (users != null) {
                    adapter.setData(users);
                    Log.d("Submission 2", "onChanged: " + users);
                }
            }
        });
    }
}
