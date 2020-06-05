package com.ian.submission2.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ian.submission2.BuildConfig;
import com.ian.submission2.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<User>> listUser = new MutableLiveData<>();


    public MutableLiveData<ArrayList<User>> getListUser() {
        return listUser;
    }

    public void setListUser(final String username) {
        final ArrayList<User> listItem = new ArrayList<>();

        final String url = "https://api.github.com/search/users?q=" + username;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", BuildConfig.TOKEN);
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    if (responseObject.getInt("total_count") != 0) {
                        JSONArray list = responseObject.getJSONArray("items");

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject user = list.getJSONObject(i);
                            User userItems = new User();
                            userItems.setUsername(user.getString("login"));
                            userItems.setAvatar(user.getString("avatar_url"));
                            listItem.add(userItems);
                        }
                        listUser.postValue(listItem);
                    } else {
                        listUser.postValue(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Submission 2", "onFailure: " + error.getMessage());
            }
        });
    }
}
