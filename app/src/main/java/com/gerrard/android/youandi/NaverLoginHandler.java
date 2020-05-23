package com.gerrard.android.youandi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NaverLoginHandler extends OAuthLoginHandler {

    private Context mContext;
    private OAuthLogin mOAuthLoginModule;
    private String token;

    public NaverLoginHandler(Context mContext, OAuthLogin mOAuthLoginModule) {
        this.mContext = mContext;
        this.mOAuthLoginModule = mOAuthLoginModule;
    }

    @Override
    public void run(boolean success) {
        if (success) {
            String accessToken = mOAuthLoginModule.getAccessToken(mContext);
            MemberProfileTask task = new MemberProfileTask(mContext);
            task.execute(accessToken);
            /*String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
            long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
            String tokenType = mOAuthLoginModule.getTokenType(mContext);
            mOauthAT.setText(accessToken);
            mOauthRT.setText(refreshToken);
            mOauthExpires.setText(String.valueOf(expiresAt));
            mOauthTokenType.setText(tokenType);
            mOAuthState.setText(mOAuthLoginModule.getState(mContext).toString());*/
        } else {
            String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
            String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
            Toast.makeText(mContext, "errorCode:" + errorCode
                    + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
        }

    }

    class MemberProfileTask extends AsyncTask<String, Void, String> {

        String responseBody;
        Context context;
        private UserInfoListener userInfoListener;

        MemberProfileTask(Context context) {
            this.context = context;
            this.userInfoListener = (UserInfoListener) context;
        }

        @Override
        protected String doInBackground(String... strings) {
            token = strings[0];
            String header = "Bearer " + token;

            String apiURL = "https://openapi.naver.com/v1/nid/me";

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Authorization", header);
            responseBody = get(apiURL,requestHeaders);

            return responseBody;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(responseBody);
                if(object.getString("resultcode").equals("00")) {
                    // should not use this access token. Custom token needed.
                    userInfoListener.passUserInfo(token);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String get(String apiUrl, Map<String, String> requestHeaders) {
            HttpURLConnection con = connect(apiUrl);
            try {
                con.setRequestMethod("GET");
                for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                    con.setRequestProperty(header.getKey(), header.getValue());
                }

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return readBody(con.getInputStream());
                } else { // 에러 발생
                    return readBody(con.getErrorStream());
                }
            } catch (IOException e) {
                throw new RuntimeException("API Response fails", e);
            } finally {
                con.disconnect();
            }

        }

        private HttpURLConnection connect(String apiUrl){
            try {
                URL url = new URL(apiUrl);
                return (HttpURLConnection)url.openConnection();
            } catch (MalformedURLException e) {
                throw new RuntimeException("Wrong API URL :" + apiUrl, e);
            } catch (IOException e) {
                throw new RuntimeException("Connection failed : " + apiUrl, e);
            }
        }

        private String readBody(InputStream body){
            InputStreamReader streamReader = new InputStreamReader(body);

            try (BufferedReader lineReader = new BufferedReader(streamReader)) {
                StringBuilder responseBody = new StringBuilder();

                String line;
                while ((line = lineReader.readLine()) != null) {
                    responseBody.append(line);
                }

                return responseBody.toString();
            } catch (IOException e) {
                throw new RuntimeException("Failed to read API Response", e);
            }
        }

    }

    public interface UserInfoListener {
        void passUserInfo(String token);
    }
}