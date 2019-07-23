package com.hcodez.android.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CodeAdapter extends RecyclerView.Adapter<CodeAdapter.CodeViewHolder> {


    @NonNull
    @Override
    public CodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CodeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class CodeViewHolder extends RecyclerView.ViewHolder {

        public CodeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
