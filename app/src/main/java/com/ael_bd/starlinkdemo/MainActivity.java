package com.ael_bd.starlinkdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ael_bd.starlinkdemo.utils.AccCred;
import com.ael_bd.starlinkdemo.utils.DatabaseHelper;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public static final ArrayList<AccCred> accList = new ArrayList<>();
    public SharedPreferences sharedPreferences;
    DatabaseHelper db;
    int currentCheckedBtn = -1;
    private ActionBar actionBar;
    private ListView lvAccCred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();
        initviewAndUtils();

        populateList();
        lvAccCred.setAdapter(new AccCredAdapter(accList));

    }

    private void initActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.activity1_name);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.icon);

    }

    private void initviewAndUtils() {
        lvAccCred = (ListView) findViewById(R.id.lvAccCreds);
        //Utils
        sharedPreferences = getSharedPreferences("StarLink", Context.MODE_PRIVATE);
    }

    private void populateList() {

        StartLinkDemo appOb = (StartLinkDemo) getApplication();
        db = appOb.getDatabase();

        Cursor mCursor = db.getAccounts();
        accList.clear();

        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                AccCred accOb = new AccCred();
                accOb._id = mCursor.getString(0);
                accOb.accountNo = mCursor.getString(1);
                accOb.password = mCursor.getString(2);
                accOb.description = mCursor.getString(3);

                String state = mCursor.getString(4);

                if (state != null && state.equalsIgnoreCase("0"))
                    accOb.state = false;
                else if (state != null && state.equalsIgnoreCase("1"))
                    accOb.state = true;

                accList.add(accOb);
            } while (mCursor.moveToNext());

            mCursor.close();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_login:
                final int position = sharedPreferences.getInt("currentlyChecked", -1);
                if (position != -1) {

                    Intent intent = new Intent(this, HomeActivity.class);
                    AccCredAdapter adapter = (AccCredAdapter) lvAccCred.getAdapter();
                    AccCred itemOb = (AccCred) adapter.getItem(position);
                    intent.putExtra(DatabaseHelper._DESCRIPTION, itemOb.description);
                    startActivity(intent);

                } else {
                    Toast.makeText(this, "Select an account first", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.action_addacc:
                db.insertAccounts("", "", "", "0");
                populateList();
                lvAccCred.setAdapter(new AccCredAdapter(accList));
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private class AccCredAdapter extends BaseAdapter {

        ArrayList<AccCred> adpList;


        private AccCredAdapter(ArrayList<AccCred> adpList) {
            this.adpList = adpList;
        }

        @Override
        public int getCount() {
            return adpList.size();
        }

        @Override
        public Object getItem(int position) {
            return adpList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.list_row, null);

            final CheckBox ckState = (CheckBox) convertView.findViewById(R.id.cbState);
            final String _ID = adpList.get(position)._id;

            final EditText etAcc = (EditText) convertView.findViewById(R.id.etAcc);
            final EditText etPass = (EditText) convertView.findViewById(R.id.etPass);
            final EditText etDescription = (EditText) convertView.findViewById(R.id.etDescription);

            if (sharedPreferences.getInt("currentlyChecked", -1) == position/* && (position >= lvAccCred.getFirstVisiblePosition()
                    && position <= lvAccCred.getLastVisiblePosition())*/) {
                ckState.setChecked(true);

            } else /*if (position >= lvAccCred.getFirstVisiblePosition()
                    && position <= lvAccCred.getLastVisiblePosition())*/ {
                ckState.setChecked(false);
            }

            ckState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    sharedPreferences.edit().putInt("currentlyChecked", position).commit();

                    populateList();
                    lvAccCred.setAdapter(new AccCredAdapter(accList));
                }
            });

            etAcc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    db.updateAccounts(_ID, s.toString(), etPass.getText().toString(), etDescription.getText().toString(), adpList.get(position).state + "");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etPass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    db.updateAccounts(_ID, etAcc.getText().toString(), s.toString(), etDescription.getText().toString(), adpList.get(position).state + "");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adpList.get(position).description=s.toString();
                    db.updateAccounts(_ID, etAcc.getText().toString(), etPass.getText().toString(), s.toString(), adpList.get(position).state + "");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etAcc.setText((String) adpList.get(position).accountNo);
            etPass.setText((String) adpList.get(position).password);
            etDescription.setText((String) adpList.get(position).description);


            return convertView;
        }

    }
}
