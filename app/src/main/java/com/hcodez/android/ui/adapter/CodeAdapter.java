package com.hcodez.android.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcodez.android.R;
import com.hcodez.android.db.entity.CodeEntity;

import java.util.ArrayList;
import java.util.List;

public class CodeAdapter extends RecyclerView.Adapter<CodeAdapter.CodeViewHolder> {

    private List<CodeEntity> mCodeItems = new ArrayList<>();
    private OnNoteListener mOnNoteListener;

    public void setItems(List<CodeEntity> items, OnNoteListener onNoteListener) {
        mCodeItems = items;
        notifyDataSetChanged();
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public CodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_codes, parent, false);
        return new CodeViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeViewHolder holder, int position) {
        if (holder instanceof CodeViewHolder) {
            holder.bind(mCodeItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mCodeItems != null ? mCodeItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    static class CodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;
        OnNoteListener   onNoteListener;

        public CodeViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.list_item);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        public void bind(CodeEntity codeEntity) {
            mTextView.setText(codeEntity.toString());
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
