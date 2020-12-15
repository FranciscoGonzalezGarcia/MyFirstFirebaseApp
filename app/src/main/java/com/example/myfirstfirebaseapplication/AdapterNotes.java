package com.example.myfirstfirebaseapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// extends RecyclerView.Adapter<AdapterCalendar.ViewHolderData> implements View.OnClickListener
public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.ViewHolderData> implements View.OnClickListener {
    private View.OnClickListener mListener;
    private ArrayList<NoteClass> notesList;

    public AdapterNotes(ArrayList<NoteClass> notesList) {
        this.notesList = notesList;
    }

    @Override
    public void onClick(View view) {
        if(mListener!=null){
            mListener.onClick(view);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notes, null, false);
        view.setOnClickListener(this);
        return new ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        holder.title.setText(notesList.get(position).getTitle());
        holder.body.setText(notesList.get(position).getBody());

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {
        private TextView title, body;
        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.lblTitle);
            body = itemView.findViewById(R.id.lblBody);
        }
    }
}
