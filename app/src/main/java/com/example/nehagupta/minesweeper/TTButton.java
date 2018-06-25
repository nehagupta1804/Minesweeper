package com.example.nehagupta.minesweeper;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

public class TTButton extends AppCompatButton {

    private int mines;
    private int x;
    private int y;

    public TTButton(Context context)
    {
        super(context);
    }
    public void setMines(int x)
    {
        mines=x;
    }
    public int getMines()
    {
        return mines ;
    }
    public void set_coordinates(int x,int y)
    {
        this.x=x;
        this.y=y;
    }
    public int get_X()
    {
        return(this.x);
    }
    public int get_Y()
    {
        return(this.y);
    }


}
