package com.hcodez.android.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hcodez.android.R;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.ui.callback.CodeClickCallback;
import com.hcodez.codeengine.model.CodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CodeAdapter extends RecyclerView.Adapter<CodeAdapter.CodeViewHolder> {

    /**
     * The adapter's item list
     */
    private List<CodeEntity> codeList = new ArrayList<>();

    /**
     * Callback used when a code item is clicked
     */
    private CodeClickCallback mCodeClickCallback;


    public CodeAdapter(@Nullable CodeClickCallback codeClickCallback) {
        setHasStableIds(true);
        mCodeClickCallback = codeClickCallback;
    }

    /**
     * Update the list
     * @param newCodeList the new newCodeList to use
     */
    public void updateList(List<CodeEntity> newCodeList) {
        if (newCodeList == null)
            return;

        if (codeList == null) {
            codeList = newCodeList;
            notifyItemRangeInserted(0, codeList.size());
            return;
        }

        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return codeList.size();
            }

            @Override
            public int getNewListSize() {
                return newCodeList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return codeList.get(oldItemPosition).getId()
                        .equals(newCodeList.get(newItemPosition).getId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                CodeEntity newCodeEntity = newCodeList.get(newItemPosition);
                CodeEntity oldCodeEntity = codeList.get(oldItemPosition);

                return newCodeEntity.getId().equals(oldCodeEntity.getId()) &&
                        Objects.equals(newCodeEntity.getCodeType(), oldCodeEntity.getCodeType()) &&
                        Objects.equals(newCodeEntity.getIdentifier(), oldCodeEntity.getIdentifier()) &&
                        Objects.equals(newCodeEntity.getOwner(), oldCodeEntity.getOwner()) &&
                        Objects.equals(newCodeEntity.getName(), oldCodeEntity.getName()) &&
                        Objects.equals(newCodeEntity.getPasscode(), oldCodeEntity.getPasscode()) &&
                        newCodeEntity.getCreateTime().getMillis() == oldCodeEntity.getCreateTime().getMillis() &&
                        newCodeEntity.getUpdateTime().getMillis() == oldCodeEntity.getUpdateTime().getMillis();
            }
        });
        codeList = newCodeList;
        result.dispatchUpdatesTo(this);

//        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_code_list, parent, false);
        return new CodeViewHolder(view, mCodeClickCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeViewHolder holder, int position) {
        holder.bind(codeList.get(position));
    }

    @Override
    public int getItemCount() {
        return codeList != null ? codeList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return codeList.get(0).getId();
    }

    /**
     * ViewHolder for a CodeEntity
     */
    static class CodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView          itemName;
        private TextView          itemPublicStatus;
        private TextView          itemPasscodeProtected;
        private CodeClickCallback callback;
        private CodeEntity        entity;

        public CodeViewHolder(@NonNull View itemView, CodeClickCallback callback) {
            super(itemView);

            itemName = itemView.findViewById(R.id.code_list_item_name);
            itemPublicStatus = itemView.findViewById(R.id.code_list_item_public_status);
            itemPasscodeProtected = itemView.findViewById(R.id.code_list_item_passcode_protected);

            this.callback = callback;
        }

        public void bind(CodeEntity codeEntity) {
            entity = codeEntity;
            itemName.setText(entity.toString());
            itemPublicStatus.setText(entity.getCodeType() != null ?
                    entity.getCodeType() != CodeType.PRIVATE ?
                            "Public" : "Private"
                    : "?");
            itemPasscodeProtected.setText(entity.getPasscode() != null ?
                    entity.getPasscode().length() > 0 ?
                            "Has passcode" : "No passcode"
                    : "No passcode");
        }

        @Override
        public void onClick(View view) {
            callback.onClick(entity);
        }
    }
}
