package com.hunfrit.geolocation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sPref;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv = (TextView) findViewById(R.id.textView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("lit", getIntent().getSerializableExtra("lit"));
                intent.putExtra("lon", getIntent().getSerializableExtra("lon"));
                startActivity(intent);
            }
        });
    }

    public void SAVE (View view){
        String first = (String) getIntent().getSerializableExtra("lit");
        String second = (String) getIntent().getSerializableExtra("lon");
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("LIT", first);
        ed.putString("LON", second);
        ed.commit();
    }

    public void LOAD(View view){
        sPref = getPreferences(MODE_PRIVATE);
        String firstSaved = sPref.getString("LIT", "");
        String secondSaved = sPref.getString("LON", "");
        tv.setText(firstSaved + ", " + secondSaved);
    }

}
