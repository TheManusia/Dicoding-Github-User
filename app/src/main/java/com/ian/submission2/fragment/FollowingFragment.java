package com.ian.submission2.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ian.submission2.MainActivity;
import com.ian.submission2.R;
import com.ian.submission2.activity.DetailActivity;
import com.ian.submission2.adapter.UserAdapter;
import com.ian.submission2.data.User;
import com.ian.submission2.model.FollowingModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {
    private RecyclerView rvFollowing;
    private FollowingModel followingModel;
    private UserAdapter adapter;
    private ProgressBar progressBar;

    public FollowingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFollowing = view.findViewById(R.id.rvFollowing);
        progressBar = view.findViewById(R.id.pbFollowing);

        adapter = new UserAdapter();
        adapter.setOnItemClickCallBack(new UserAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClicked(User user) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(MainActivity.EXTRA_USERNAME, user.getUsername());
                startActivity(intent);
            }
        });

        followingModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.NewInstanceFactory()).get(FollowingModel.class);

        getDataUser();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        adapter.notifyDataSetChanged();
        rvFollowing.setHasFixedSize(true);
        rvFollowing.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFollowing.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void getDataUser() {
        followingModel.getListUser().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                progressBar.setVisibility(View.GONE);
                if (users != null) {
                    adapter.setData(users);
                }
            }
        });
    }
}
