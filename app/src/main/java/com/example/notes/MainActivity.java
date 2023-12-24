package com.example.notes;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// MainActivity.java
public class MainActivity extends AppCompatActivity {

    Button addNoteButton;
    AlertDialog alertDialog;
    EditText noteEditText;
    ArrayAdapter<String> notesAdapter;
    ArrayList<String> notesList;
    ListView notesListView;
    NoteDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesListView = findViewById(R.id.notesListView);
        addNoteButton = findViewById(R.id.addNoteButton);
        databaseHelper = new NoteDatabaseHelper(this);

        // Инициализация списка заметок и адаптера
        notesList = databaseHelper.getAllNotes();
        notesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        notesListView.setAdapter(notesAdapter);

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void updateNoteAndRefreshList(int position, String editedNoteText) {
        // Обновляем заметку в базе данных
        databaseHelper.updateNote(position, editedNoteText);

        // Обновляем список заметок и обновляем адаптер
        notesList.set(position, editedNoteText);
        notesAdapter.notifyDataSetChanged();
    }

    private void showEditDialog(final String currentNote, final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_note, null);
        dialogBuilder.setView(dialogView);

        final EditText editNoteEditText = dialogView.findViewById(R.id.editNoteEditText);
        editNoteEditText.setText(currentNote);

        dialogBuilder.setTitle("Редактировать заметку");
        dialogBuilder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String editedNoteText = editNoteEditText.getText().toString();
                if (!editedNoteText.isEmpty()) {
                    updateNoteAndRefreshList(position, editedNoteText);
                }
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog editDialog = dialogBuilder.create();
        editDialog.show();
    }

    @SuppressLint("MissingInflatedId")
    private void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        dialogBuilder.setView(dialogView);

        final EditText noteEditText = dialogView.findViewById(R.id.noteEditText);

        if (noteEditText != null) {
            noteEditText.setText((CharSequence) noteEditText);
        }

        dialogBuilder.setTitle("Введите заметку");
        dialogBuilder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String noteText = noteEditText.getText().toString();
                if (noteEditText != null) {
                    // Редактирование заметки
                    updateNoteAndRefreshList(1, noteText);
                } else {
                    // Добавление новой заметки
                    if (!noteText.isEmpty()) {
                        addNoteAndRefreshList(noteText);
                    }
                }
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void addNoteAndRefreshList(String noteText) {
        // Добавление новой заметки в базу данных
        databaseHelper.addNote(noteText);

        // Обновляем список заметок и адаптер
        notesList.add(noteText);
        notesAdapter.notifyDataSetChanged();
    }



}
