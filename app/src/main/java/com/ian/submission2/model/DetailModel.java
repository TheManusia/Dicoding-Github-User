package com.ian.submission2.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ian.submission2.data.Token;
import com.ian.submission2.data.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;


import cz.msebera.android.httpclient.Header;

public class DetailModel extends ViewModel {
    private MutableLiveData<User> listUser = new MutableLiveData<>();

    public MutableLiveData<User> getListUser() {
        return listUser;
    }

    public void setListUser(String username) {
        String url = "https://api.github.com/users/" +username;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", Token.TOKEN);
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);

                    User items = new User();
                    items.setAvatar(responseObject.getString("avatar_url"));
                    items.setName(responseObject.getString("name"));
                    items.setUsername(responseObject.getString("login"));
                    items.setLocation(responseObject.getString("location"));
                    items.setFollower(responseObject.getInt("followers"));
                    items.setFollowing(responseObject.getInt("following"));
                    items.setRepository(responseObject.getInt("public_repos"));
                    items.setCompany(responseObject.getString("company"));
                    listUser.setValue(items);
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
