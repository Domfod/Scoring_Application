package com.example.scoringapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogHelper extends AppCompatDialogFragment {
    private EditText edit_lPlayerName;
    private EditText edit_rPlayerName;
    private EditText edit_notes;
    private DialogHelperListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Player Names")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String lName = edit_lPlayerName.getText().toString();
                String rName = edit_rPlayerName.getText().toString();
                String notes = edit_notes.getText().toString();
                listener.getTexts(lName, rName, notes);
                MainActivity.myDb.insertData(lName, MainActivity.gameScore1, rName, MainActivity.gameScore2, notes);
            }
        });

        edit_lPlayerName = view.findViewById(R.id.edit_lName);
        edit_rPlayerName = view.findViewById(R.id.edit_rName);
        edit_notes = view.findViewById(R.id.edit_notes);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogHelperListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Implement DialogHelperListener");
        }
    }

    public interface DialogHelperListener {
        void getTexts(String lName, String rName, String notes);
    }
}
