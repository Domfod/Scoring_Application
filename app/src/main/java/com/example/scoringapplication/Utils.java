package com.example.scoringapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class Utils {

    public static void showToast(DialogInterface.OnClickListener mContext, String message){
        Toast.makeText((Context) mContext, message, Toast.LENGTH_SHORT).show();
    }
}
