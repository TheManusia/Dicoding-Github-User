package com.ian.favoriteuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.ian.favoriteuser.DatabaseContract.NoteColumns.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoadUserCallback {
    private RecyclerView rvFavorite;
    private UserAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar2);
        rvFavorite = findViewById(R.id.rvFavorite);
        rvFavorite.setHasFixedSize(true);
        rvFavorite.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter();
        rvFavorite.setAdapter(adapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver observer = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, observer);

        if (savedInstanceState == null) {
            new LoadUserAsync(this, this).execute();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.favorite));
        }
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
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadUserCallback> weakCallback;

        private LoadUserAsync(Context context, LoadUserCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            //Cursor dataCursor = weakFavoriteHelper.get().queryAll();
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
            return MappingHelper.mapCursorToArrayList(Objects.requireNonNull(dataCursor));
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);
            weakCallback.get().postExecute(users);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadUserAsync(context, (LoadUserCallback) context).execute();
        }
    }
}

interface LoadUserCallback {
    void preExecute();
    void postExecute(ArrayList<User> users);
}