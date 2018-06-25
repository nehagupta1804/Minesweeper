package com.example.nehagupta.minesweeper;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener,View.OnLongClickListener {

    LinearLayout rootLayout;
    public int SIZE;
    public int score;
    TextView textview;
    public int t;
    public ArrayList<LinearLayout> rows;
    public TTButton[][] board;
    public int currentStatus;
    public int INCOMPLETE=1;
    public int COMPLETE=2;
    Random random;
    int x[]={-1,-1,-1,0,0,1,1,1};
    int y[]={-1,0,1,-1,1,-1,0,1};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview=findViewById(R.id.score);
        rootLayout = findViewById(R.id.rootLayout);
        Intent intent = getIntent();
        String level=intent.getStringExtra("name");
        if(level.equals("easy"))
        {
            SIZE = 3;
            t=3;
        }
        else if(level.equals("medium"))
        {
            SIZE=4;
            t=4;
        }
        else
        {
            SIZE=5;
            t=5;
        }

        setupBoard();
    }

    public void setupBoard() {

       score=0;
       textview.setText(Integer.toString(score));
       random = new Random();
        currentStatus=INCOMPLETE;
        rows = new ArrayList<>();
        board = new TTButton[SIZE][SIZE];
        rootLayout.removeAllViews();

        for (int i = 0; i < SIZE; i++) {

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
            linearLayout.setLayoutParams(layoutParams);

            rootLayout.addView(linearLayout);
            rows.add(linearLayout);
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                TTButton button = new TTButton(this);
                button.setBackgroundResource(R.drawable.mine);
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(5, Color.BLACK);
                button.setBackgroundDrawable(drawable);
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

                button.setLayoutParams(layoutParams);
                //button.setBackgroundResource(R.drawable.mine3);
                button.setOnClickListener(this);
                button.setOnLongClickListener(this);
                button.set_coordinates(i,j);
                LinearLayout row = rows.get(i);
                row.addView(button);

                board[i][j] = button;

            }
        }

        int x=0;
        while(x<t)
        {
            int m=random.nextInt(SIZE-1);
            int n=random.nextInt(SIZE-1);
            board[m][n].setMines(-1);
            x++;
        }
        calculate_mines();

    }
    public void calculate_mines()
    {
        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {

                if(board[i][j].getMines()!=-1) {
                    int count = 0;
                    for (int k = 0; k < 8; k++) {
                        int left = i + x[k];
                        int right = j + y[k];
                        if (left >= 0 && right >= 0 && left < SIZE && right < SIZE && board[left][right].getMines() == -1)
                            count++;
                    }
                    board[i][j].setMines(count);
                }
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.resetItem){
            setupBoard();
        }else if(id == R.id.size3){
            SIZE = 3;
            t=3;
            setupBoard();
        }
        else if(id == R.id.size4){
            t=4;
            SIZE = 4;
            setupBoard();
        }
        else if(id == R.id.size5){
            t=5;
            SIZE = 5;
            setupBoard();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onLongClick(View view)
    {
        if(currentStatus == INCOMPLETE) {
            TTButton button = (TTButton) view;
            String name = (String) button.getText();
            if (name.equals("danger")) {
                button.setText("");
                button.setClickable(true);
            } else {
                button.setText("danger");
                button.setClickable(false);
            }


            int cnt = 0;
            boolean flag = true;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    int mine = board[i][j].getMines();
                    String nm = (String) board[i][j].getText();
                    if (nm.equals("danger")) {
                        if (mine == -1)
                            cnt++;
                        else
                            flag = false;
                    }
                }
            }
            if (cnt == t && flag) {
                Toast.makeText(this, "you win", Toast.LENGTH_LONG).show();
                currentStatus = COMPLETE;
            }
        }

        return(true);
    }

    @Override
    public void onClick(View view) {

        if(currentStatus == INCOMPLETE)
        {
            TTButton v = (TTButton) view;
            int x = v.get_X();
            int y = v.get_Y();
            if (board[x][y].getMines() == -1) {
                Toast.makeText(this, "GAME_OVER", Toast.LENGTH_LONG).show();
                reveal_mines();
                currentStatus = COMPLETE;
            } else if (board[x][y].getMines() > 0) {
                score++;
                textview.setText(Integer.toString(score));
                int k = board[x][y].getMines();
                board[x][y].setText(Integer.toString(k));
                board[x][y].setEnabled(false);
                board[x][y].setClickable(false);
            } else if (board[x][y].getMines() == 0) {

                reveal_neighbours(x, y);
            }
            int cnt = 0;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j].getMines() != -1 && !board[i][j].isEnabled()) {
                        cnt++;
                    }
                }
            }
            int non_mines=((SIZE*SIZE)-t);
            if (cnt == non_mines) {
                Toast.makeText(this, "you win", Toast.LENGTH_LONG).show();
               // Toast.makeText(this, "SCORE + score_count", Toast.LENGTH_LONG).show();
                currentStatus = COMPLETE;
            }

        }


    }
    void reveal_neighbours(int m,int n)
    {
        if (board[m][n].getMines() > 0) {
            score++;
            textview.setText(Integer.toString(score));
            int k = board[m][n].getMines();
            board[m][n].setText(Integer.toString(k));
            board[m][n].setEnabled(false);
            board[m][n].setClickable(false);
            return;
        } else if (board[m][n].getMines() == -1) {
            Toast.makeText(this, "GAME_OVER", Toast.LENGTH_LONG).show();
            reveal_mines();
            currentStatus=COMPLETE;
            return;
        } else if (board[m][n].getMines() == 0) {

            if (board[m][n].isClickable()) {
                score++;
                textview.setText(Integer.toString(score));
                board[m][n].setText("0");
                board[m][n].setEnabled(false);
                board[m][n].setClickable(false);
            }
            for (int i = 0; i < 8; i++) {
                int left = m + x[i];
                int right = n + y[i];
                if (left >= 0 && right >= 0 && left < SIZE && right < SIZE) {
                    if (board[left][right].isClickable()) {
                        score++;
                        textview.setText(Integer.toString(score));
                        int k = board[left][right].getMines();
                        board[left][right].setText(Integer.toString(k));
                        board[left][right].setEnabled(false);
                        board[left][right].setClickable(false);
                        if (k == 0) {
                            reveal_neighbours(left, right);
                        }
                    }
                }
            }


        }


    }


   /* public void calculate_mines()
    {
        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                int count=0;
                if(board[i][j].getMines()!=-1) {
                    if (i >= 0 && j - 1 >= 0 && i < SIZE && j - 1 < SIZE && board[i][j - 1].getMines() == -1) {
                        count++;
                    }
                    if (i - 1 >= 0 && j >= 0 && i - 1 < SIZE && j < SIZE && board[i - 1][j].getMines() == -1) {
                        count++;
                    }
                    if (i >= 0 && j + 1 >= 0 && i < SIZE && j + 1 < SIZE && board[i][j + 1].getMines() == -1) {
                        count++;
                    }
                    if (i + 1 >= 0 && j >= 0 && i + 1 < SIZE && j < SIZE && board[i + 1][j].getMines() == -1) {
                        count++;
                    }
                    if (i-1 >= 0 && j - 1 >= 0 && i-1 < SIZE && j - 1 < SIZE && board[i-1][j - 1].getMines() == -1) {
                        count++;
                    }
                    if (i - 1 >= 0 && j+1 >= 0 && i - 1 < SIZE && j+1 < SIZE && board[i - 1][j+1].getMines() == -1) {
                        count++;
                    }
                    if (i+1 >= 0 && j + 1 >= 0 && i+1 < SIZE && j + 1 < SIZE && board[i+1][j + 1].getMines() == -1) {
                        count++;
                    }
                    if (i + 1 >= 0 && j-1>= 0 && i + 1 < SIZE && j-1 < SIZE && board[i + 1][j-1].getMines() == -1) {
                        count++;
                    }
                    board[i][j].setMines(count);
                }



            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.resetItem){
            setupBoard();
        }else if(id == R.id.size3){
            SIZE = 3;
            setupBoard();
        }
        else if(id == R.id.size4){
            SIZE = 4;
            setupBoard();
        }
        else if(id == R.id.size5){
            SIZE = 5;
            setupBoard();
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onLongClick(View view)
    {
        Button button = (Button)view;
         button.setText("danger");
         return(true);
    }

    @Override
    public void onClick(View view)
    {
        //optimize it
        int flag=1;
        TTButton button = (TTButton)view;
        if(currentStatus==INCOMPLETE) {

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (button == board[i][j]) {
                        flag = 0;
                        if (button.getMines() == 0) {
                            if (i >= 0 && j - 1 >= 0 && i < SIZE && j - 1 < SIZE) {
                                int x = board[i][j - 1].getMines();
                                board[i][j - 1].setText(Integer.toString(x));
                                board[i][j - 1].setEnabled(false);
                                board[i][j - 1].setClickable(false);
                            }
                            if (i - 1 >= 0 && j >= 0 && i - 1 < SIZE && j < SIZE) {
                                int x = board[i - 1][j].getMines();
                                board[i - 1][j].setText(Integer.toString(x));
                                board[i - 1][j].setEnabled(false);
                                board[i - 1][j].setClickable(false);
                            }
                            if (i >= 0 && j + 1 >= 0 && i < SIZE && j + 1 < SIZE) {
                                int x = board[i][j + 1].getMines();
                                board[i][j + 1].setText(Integer.toString(x));
                                board[i][j + 1].setEnabled(false);
                                board[i][j + 1].setClickable(false);
                            }
                            if (i + 1 >= 0 && j >= 0 && i + 1 < SIZE && j < SIZE) {
                                int x = board[i + 1][j].getMines();
                                board[i + 1][j].setText(Integer.toString(x));
                                board[i + 1][j].setEnabled(false);
                                board[i + 1][j].setClickable(false);
                            }
                            if (i - 1 >= 0 && j - 1 >= 0 && i - 1 < SIZE && j - 1 < SIZE) {
                                int x = board[i - 1][j - 1].getMines();
                                board[i - 1][j - 1].setText(Integer.toString(x));
                                board[i - 1][j - 1].setEnabled(false);
                                board[i - 1][j - 1].setClickable(false);
                            }
                            if (i - 1 >= 0 && j + 1 >= 0 && i - 1 < SIZE && j + 1 < SIZE) {
                                int x = board[i - 1][j + 1].getMines();
                                board[i - 1][j + 1].setText(Integer.toString(x));
                                board[i - 1][j + 1].setEnabled(false);
                                board[i - 1][j + 1].setClickable(false);
                            }
                            if (i + 1 >= 0 && j + 1 >= 0 && i + 1 < SIZE && j + 1 < SIZE) {
                                int x = board[i + 1][j + 1].getMines();
                                board[i + 1][j + 1].setText(Integer.toString(x));
                                board[i + 1][j + 1].setEnabled(false);
                                board[i + 1][j + 1].setClickable(false);
                            }
                            if (i + 1 >= 0 && j - 1 >= 0 && i + 1 < SIZE && j - 1 < SIZE) {
                                int x = board[i + 1][j - 1].getMines();
                                board[i + 1][j - 1].setText(Integer.toString(x));
                                board[i + 1][j - 1].setEnabled(false);
                                board[i + 1][j - 1].setClickable(false);
                            }
                            button.setText("0");
                            button.setEnabled(false);
                            button.setClickable(false);

                        } else if (button.getMines() == -1) {
                            Toast.makeText(this, "GAME OVER", Toast.LENGTH_LONG).show();
                            currentStatus=4;

                        } else {
                            int y = button.getMines();
                            button.setText(Integer.toString(y));
                            button.setEnabled(false);
                            button.setClickable(false);

                        }

                    }
                    if (flag == 0)
                        break;

                }
                if (flag == 0)
                    break;
            }
            flag = 1;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j].getMines() != -1 && board[i][j].isClickable() == false) {
                        flag = 1;
                    } else if (board[i][j].isClickable() && board[i][j].getMines() != -1)
                        flag = 0;
                    if (flag == 0)
                        break;

                }
                if (flag == 0)
                    break;

            }
            if (flag == 1) {
                Toast.makeText(this, "you win", Toast.LENGTH_LONG).show();
                currentStatus=4;
                return;
            }
        }*/


    public  void reveal_mines()
    {
        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                if(board[i][j].getMines()==-1)
                    board[i][j].setBackgroundResource(R.drawable.bomb);

            }
        }
    }






}
