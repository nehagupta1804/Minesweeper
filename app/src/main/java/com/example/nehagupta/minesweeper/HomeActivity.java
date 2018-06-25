package com.example.nehagupta.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {
    String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void startMainActivity(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("name",level);
        startActivity(intent);
    }
    public void onclick(View view) {
       
        /*(id==R.id.male)
        {
            name="male";
        }
        else
        {
            name="female";
        }
        // Is the button now checked?
        //boolean checked = ((ToggleButton) view).isChecked();*/

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.easy:
                //if (checked)

                level= "easy";
                break;
            case R.id.medium:
                //if (checked)

                level = "medium";
                // Ninjas rule
                break;
            case R.id.hard:
                level ="hard";
                break;
        }
    }


        /*if(id== R.id.easy)
        {
            level="easy";
        }
        else if(id == R.id.medium)
        {
            level="medium";
        }
        else
        {
            level="hard";
        }

    }*/
}
