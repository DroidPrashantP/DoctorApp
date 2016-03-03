package com.midoconline.app.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.midoconline.app.MiDocOnlineApplication;
import com.midoconline.app.R;
import com.midoconline.app.Util.Constants;
import com.midoconline.app.Util.NetworkManager;
import com.midoconline.app.Util.SharePreferences;
import com.midoconline.app.Util.Utils;
import com.midoconline.app.api.UserApi;
import com.midoconline.app.api.UserApiParser;
import com.midoconline.app.beans.User;
import com.midoconline.app.ui.adapters.UsersCustomAdapter;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.videochat.core.QBVideoChatController;

import org.jivesoftware.smack.XMPPException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Prashant on 19/11/15.
 */
public class VideoChatActivity  extends Activity implements UserApi.UserEventListener,Constants{
    private ProgressDialog progressDialog;
    private SharePreferences sharePreferences;
    private UserApi mUserApi;
    private RecyclerView mUserRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharePreferences = new SharePreferences(this);
        mUserApi = new UserApi(VideoChatActivity.this, this);
        mUserRecyclerView = (RecyclerView) findViewById(R.id.usersRecyclerView);
        setContentView(R.layout.layout_video_login);
        initChatService();
        if (NetworkManager.isConnectedToInternet(this)) {
            Utils.showProgress(this);
            createSession(MiDocOnlineApplication.FIRST_USER_LOGIN, MiDocOnlineApplication.FIRST_USER_PASSWORD);
        }else {
            Toast.makeText(this, "No internet Connections",Toast.LENGTH_SHORT).show();
        }

        // setup UI
        //

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage(getString(R.string.please_wait));
//        progressDialog.setCancelable(false);

//        findViewById(R.id.loginByFirstUserBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                progressDialog.show();
//            }
//        });
//
//        findViewById(R.id.loginBySecondUserBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                progressDialog.show();
//                createSession(MiDocOnlineApplication.SECOND_USER_LOGIN, MiDocOnlineApplication.SECOND_USER_PASSWORD);
//            }
//        });
    }

    private void sendRequestToAllUserData() {
        if (NetworkManager.isConnectedToInternet(this)) {
            Utils.showProgress(this);
            mUserApi.fetchAllUser(Constants.QuickBox.ALL_USER, Constants.ApiType.ALL_USER_API);
        }else {
            Toast.makeText(this,"No internet connected",Toast.LENGTH_SHORT).show();
        }
    }

    private void createSession(String login, final String password) {
        QBAuth.createSession(login, password, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

                sharePreferences.setVideoSessionToken(qbSession.getToken());

                // Save current user
                //
                MiDocOnlineApplication app = (MiDocOnlineApplication) getApplication();
                app.setCurrentUser(qbSession.getUserId(), password);
                Utils.showProgress(VideoChatActivity.this);
                sendRequestToAllUserData();

                // Login to Chat
                //
                QBChatService.getInstance().login(app.getCurrentUser(), new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess() {
                        try {
                            QBVideoChatController.getInstance().initQBVideoChatMessageListener();


                        } catch (XMPPException e) {
                           // e.printStackTrace();
                        }

                        // show next activity
                       // showCallUserActivity();
                    }

                    @Override
                    public void onError(List errors) {
                        Utils.closeProgress();
                        Toast.makeText(VideoChatActivity.this, "Error when login", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onError(List<String> errors) {
                Utils.closeProgress();
                Toast.makeText(VideoChatActivity.this, "Error when login, check test users login and password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initChatService(){
        QBChatService.setDebugEnabled(true);

        if (!QBChatService.isInitialized()) {
            Log.d("ActivityLogin", "InitChat");
            QBChatService.init(this);
        }else{
            Log.d("ActivityLogin", "InitChat not needed");
        }
    }

    private void showCallUserActivity() {
        progressDialog.dismiss();

        Intent intent = new Intent(this, LiveVideoChatActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onComplete(JSONObject jsonObject, String apiType) {

        try {
            if (apiType.equals(ApiType.ALL_USER_API)) {
                UserApiParser.getInstance().ParseAllUserResponse(jsonObject);
                setUserListLayout();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Utils.closeProgress();
    }

    @Override
    public void onError(VolleyError volleyError, String ApiType) {
        Utils.closeProgress();
    }

    /**
     * set Store layout
     */
    public void setUserListLayout() {
        RecyclerView userRecyclerView = (RecyclerView) findViewById(R.id.usersRecyclerView);
        userRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        userRecyclerView.setLayoutManager(layoutManager);

        List<User> userList= UserApiParser.getInstance().getUserList()  ;
        UsersCustomAdapter adapter = new UsersCustomAdapter(userList, this);
        userRecyclerView.setAdapter(adapter);

    }
}
