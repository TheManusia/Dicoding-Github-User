package com.ian.submission2.viewmodel;

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

public class FollowingViewModel extends ViewModel {
    private MutableLiveData<ArrayList<User>> listUser = new MutableLiveData<>();

    public MutableLiveData<ArrayList<User>> getListUser() {
        return listUser;
    }

    public void setListUser(String username) {
        final ArrayList<User> listItem = new ArrayList<>();

        String url = "https://api.github.com/users/" + username + "/following";

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", BuildConfig.TOKEN);
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONArray responseArray = new JSONArray(result);

                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject list = responseArray.getJSONObject(i);
                        User user = new User();
                        user.setUsername(list.getString("login"));
                        user.setAvatar(list.getString("avatar_url"));
                        listItem.add(user);
                    }
                    listUser.postValue(listItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
