package com.rogergcc.mifcmesis;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.spTopics)
    Spinner spTopics;
    @BindView(R.id.tvTopics)
    TextView tvTopics;
    @BindView(R.id.btn_obtener_token)
    Button btnObtenerToken;
    @BindView(R.id.tv_mitoken)
    TextView tvMitoken;
    @BindView(R.id.llTopics)
    LinearLayout llTopics;
    @BindView(R.id.btnSuscribir)
    Button btnSuscribir;
    @BindView(R.id.btnDesuscribir)
    Button btnDesuscribir;
    @BindView(R.id.llButtons)
    LinearLayout llButtons;
    private Set<String> mTopicsSet;
    private SharedPreferences mSharedPreferences;
    private static final String SP_TOPICS = "sharedPreferencesTopics";


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        //region cuando abres la notificacion
        if (extras != null)
            Log.i("mifcm_roger", extras.getString("descuento"));
        else
            Log.i("mifcm_roger", "no hay valores");

//        endregion


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        Log.e("token_Id", instanceIdResult.getToken());
                    }
                });

        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);
        mTopicsSet = mSharedPreferences.getStringSet(SP_TOPICS, new HashSet<String>());
        showTopics();


    }

    private void showTopics() {
        tvTopics.setText(mTopicsSet.toString());
    }

    @OnClick({R.id.btnSuscribir, R.id.btnDesuscribir})
    public void onViewClicked(View view) {
        String topic = getResources().getStringArray(R.array.topicsValues)[spTopics.getSelectedItemPosition()];
        switch (view.getId()) {
            case R.id.btnSuscribir:
                if (!mTopicsSet.contains(topic)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(topic);
                    mTopicsSet.add(topic);
                    saveSharedPreferences();
                }
                break;
            case R.id.btnDesuscribir:
                if (mTopicsSet.contains(topic)) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                    mTopicsSet.remove(topic);
                    saveSharedPreferences();
                }
                break;
        }
    }

    private void saveSharedPreferences() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.putStringSet(SP_TOPICS, mTopicsSet);
        editor.apply();
        showTopics();
    }

    @OnClick(R.id.btn_obtener_token)
    public void onViewClicked() {


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                new OnCompleteListener<InstanceIdResult>() {
                    @Override public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d(TAG, token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        tvMitoken.setText(token);
                    }
                });

    }
}
