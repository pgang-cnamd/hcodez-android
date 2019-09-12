package com.hcodez.android.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hcodez.android.R;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.ui.callback.CodeClickCallback;
import com.hcodez.android.ui.callback.CodeLongClickCallback;
import com.hcodez.codeengine.model.CodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CodeAdapter extends RecyclerView.Adapter<CodeAdapter.CodeViewHolder> implements Filterable {

    private static final String TAG = "CodeAdapter";

    /**
     * The adapter's item list
     */
    private List<CodeEntity> mCodeList;

    private List<CodeEntity> mCodeListFull = new ArrayList<>();

    /**
     * Callback used when a code item is clicked
     */
    private CodeClickCallback mCodeClickCallback;

    /**
     * Callback used when a code item is long clicked
     */
    private CodeLongClickCallback mCodeLongClickCallback;


    public CodeAdapter(@Nullable CodeClickCallback codeClickCallback,
                       @Nullable CodeLongClickCallback codeLongClickCallback) {
        Log.d(TAG, "CodeAdapter() called with: codeClickCallback = [" + codeClickCallback + "]");
        setHasStableIds(true);
        mCodeClickCallback = codeClickCallback;
        mCodeLongClickCallback = codeLongClickCallback;
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

        if (mCodeList == null) {
            Log.d(TAG, "updateList: null old code list");
            mCodeList = newCodeList;
            mCodeListFull = new ArrayList<>(newCodeList);
            notifyItemRangeInserted(0, mCodeList.size());
            return;
        }

        Log.d(TAG, "updateList: calculating differences between new code list and old code list");
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return mCodeList.size();
            }

            @Override
            public int getNewListSize() {
                return newCodeList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return mCodeList.get(oldItemPosition).getId()
                        .equals(newCodeList.get(newItemPosition).getId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                CodeEntity newCodeEntity = newCodeList.get(newItemPosition);
                CodeEntity oldCodeEntity = mCodeList.get(oldItemPosition);

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
        mCodeList = newCodeList;
        mCodeListFull = new ArrayList<>(newCodeList);
        result.dispatchUpdatesTo(this);
        Log.d(TAG, "updateList: dispatches updates to adapter");
    }


    @NonNull
    @Override
    public CodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder() called with: parent = [" + parent + "], viewType = [" + viewType + "]");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_code_list, parent, false);
        return new CodeViewHolder(view, mCodeClickCallback, mCodeLongClickCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");
        holder.bind(mCodeList.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() called");
        return mCodeList != null ? mCodeList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "getItemId() called with: position = [" + position + "]");
        return mCodeList.get(position).getId();
    }

    /**
     * ViewHolder for a CodeEntity
     */
    static class CodeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        private static final String TAG = "CodeViewHolder";

        private TextView              itemName;
        private ImageView             itemPublicStatus;
        private ImageView             itemPasscodeProtected;
        private CodeClickCallback     clickCallback;
        private CodeLongClickCallback longClickCallback;
        private CodeEntity            entity;

        public CodeViewHolder(@NonNull View itemView,
                              CodeClickCallback clickCallback,
                              CodeLongClickCallback longClickCallback) {
            super(itemView);
            Log.d(TAG, "CodeViewHolder() called with: itemView = [" + itemView + "], clickCallback = [" + clickCallback + "]");

            itemName = itemView.findViewById(R.id.code_list_item_name);
            itemPublicStatus = itemView.findViewById(R.id.code_list_item_public_status);
            itemPasscodeProtected = itemView.findViewById(R.id.code_list_item_passcode_protected);

            this.clickCallback = clickCallback;
            this.longClickCallback = longClickCallback;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
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
            clickCallback.onClick(entity);
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d(TAG, "onLongClick: ");
            longClickCallback.onLongClick(entity);
            return true;
        }
    }

    /**
     * Method for searching a code or code name in the searchView
     */

    public Filter getFilter(){
        return codeFilter;
    }

    /**
     * Filter the code list
     */

    private Filter codeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CodeEntity> filteredCodeList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredCodeList.addAll(mCodeListFull);
            } else {
                String filterCodeName       = constraint.toString().toLowerCase().trim();
                String filterCodeIdentifier = constraint.toString().trim();

                for(CodeEntity entity : mCodeListFull){
                    if(entity.getName().toLowerCase().contains(filterCodeName)){
                        filteredCodeList.add(entity);
                    } else if(entity.getIdentifier().contains(filterCodeIdentifier)){
                            filteredCodeList.add(entity);
                        }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredCodeList;

            return results;
        }

        /**
         * Publish the filtered result to the recyclerView
         * @param constraint Searched CharSequence
         * @param results Results for the searched string
         */

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mCodeList.clear();
            mCodeList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
