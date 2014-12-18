package com.ael_bd.starlinkdemo;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ael_bd.starlinkdemo.utils.DatabaseHelper;


public class HomeActivity extends ActionBarActivity implements View.OnClickListener {
    //Views
    TextView tvHeadLabel;
    ImageView ivArmStatus;
    Button btnArmInit;
    //Utils
    AnimationDrawable animationDrawable;


    //flags
    boolean isArmed = false;
    boolean isArming = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViewsAndUtils();
        final String des = getIntent().getExtras().getString(DatabaseHelper._DESCRIPTION);
        if (des != null) {
            des.trim();
            if (des.length() == 0) {
                tvHeadLabel.setText("No Description");
            } else {
                tvHeadLabel.setText(des);
            }
        } else
            tvHeadLabel.setText("No Description");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViewsAndUtils() {
        //initViews
        tvHeadLabel = (TextView) findViewById(R.id.tvHeaderLabel);
        ivArmStatus = (ImageView) findViewById(R.id.ivArmStutus);
        btnArmInit = (Button) findViewById(R.id.btnArmInit);

        //initUtils
        btnArmInit.setOnClickListener(this);
        setArmStatBtn(isArmed);
        setArmStatView(isArmed);

    }

    private void setStateAnim(boolean isArming) {
        if (isArming)
            ivArmStatus.setBackgroundResource(R.drawable.animlist_arming);
        else
            ivArmStatus.setBackgroundResource(R.drawable.animlist_disarming);

        animationDrawable = (AnimationDrawable) ivArmStatus.getBackground();
        animationDrawable.setOneShot(false);
        animationDrawable.start();

    }

    private void setArmStatView(boolean isArmed) {
        if (isArmed) {
            ivArmStatus.setBackgroundResource(R.drawable.armed);
        } else {
            ivArmStatus.setBackgroundResource(R.drawable.disarmed);
        }
    }

    private void setArmStatBtn(boolean isArmed) {
        if (isArmed) {
            btnArmInit.setBackgroundResource(R.drawable.btn_ptd);
        } else {
            btnArmInit.setBackgroundResource(R.drawable.btn_pta);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnArmInit:
                new StateInitatorAsync().execute();
                break;
        }
    }

    private class StateInitatorAsync extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            if (isArmed)
                isArming = false;
            else
                isArming = true;
            setStateAnim(isArming);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (isArming)
                isArmed = true;
            else
                isArmed = false;
            setArmStatView(isArmed);
            setArmStatBtn(isArmed);
        }
    }
}
