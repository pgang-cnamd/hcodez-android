package com.hcodez.android.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    private static final String TAG = "CodeAdapter";

    /**
     * The adapter's item list
     */
    private List<CodeEntity> codeList = new ArrayList<>();

    /**
     * Callback used when a code item is clicked
     */
    private CodeClickCallback mCodeClickCallback;


    public CodeAdapter(@Nullable CodeClickCallback codeClickCallback) {
        Log.d(TAG, "CodeAdapter() called with: codeClickCallback = [" + codeClickCallback + "]");
        setHasStableIds(true);
        mCodeClickCallback = codeClickCallback;
    }

    /**
     * Update the list
     * @param newCodeList the new newCodeList to use
     */
    public void updateList(List<CodeEntity> newCodeList) {
        Log.d(TAG, "updateList() called with: newCodeList = [" + newCodeList + "]");
        if (newCodeList == null) {
            Log.d(TAG, "updateList: null new code list");
            return;
        }

        if (codeList == null) {
            Log.d(TAG, "updateList: null old code list");
            codeList = newCodeList;
            notifyItemRangeInserted(0, codeList.size());
            return;
        }

        Log.d(TAG, "updateList: calculating differences between new code list and old code list");
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
        Log.d(TAG, "updateList: dispatches updates to adapter");
    }


    @NonNull
    @Override
    public CodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder() called with: parent = [" + parent + "], viewType = [" + viewType + "]");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_code_list, parent, false);
        return new CodeViewHolder(view, mCodeClickCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");
        holder.bind(codeList.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() called");
        return codeList != null ? codeList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "getItemId() called with: position = [" + position + "]");
        return codeList.get(position).getId();
    }

    /**
     * ViewHolder for a CodeEntity
     */
    static class CodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "CodeViewHolder";

        private TextView          itemName;
        private ImageView         itemPublicStatus;
        private ImageView         itemPasscodeProtected;
        private CodeClickCallback callback;
        private CodeEntity        entity;

        public CodeViewHolder(@NonNull View itemView, CodeClickCallback callback) {
            super(itemView);
            Log.d(TAG, "CodeViewHolder() called with: itemView = [" + itemView + "], callback = [" + callback + "]");

            itemName = itemView.findViewById(R.id.code_list_item_name);
            itemPublicStatus = itemView.findViewById(R.id.code_list_item_public_status);
            itemPasscodeProtected = itemView.findViewById(R.id.code_list_item_passcode_protected);

            this.callback = callback;
            itemView.setOnClickListener(this);
        }

        public void bind(CodeEntity codeEntity) {
            Log.d(TAG, "bind() called with: codeEntity = [" + codeEntity + "]");
            entity = codeEntity;
            itemName.setText(entity.getName());

            itemPublicStatus.setImageResource(
                    codeEntity.getCodeType() != CodeType.PRIVATE ?
                            R.drawable.public_code
                            : R.drawable.private_code);
            itemPasscodeProtected.setImageResource(
                    codeEntity.getCodeType() != CodeType.PRIVATE ?
                            codeEntity.getPasscode() != null ?
                                    codeEntity.getPasscode().length() > 0 ?
                                            R.drawable.passcode_protected_code
                                            : R.drawable.no_passcode_protected_code
                                    : R.drawable.no_passcode_protected_code
                            : R.drawable.passcode_protected_code);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick() called with: view = [" + view + "]");
            callback.onClick(entity);
        }
    }
}
