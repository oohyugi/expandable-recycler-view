package com.ryanbrooks.expandablerecyclerview.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import com.ryanbrooks.expandablerecyclerview.ClickListeners.ParentItemClickListener;
import com.ryanbrooks.expandablerecyclerview.Model.ChildObject;
import com.ryanbrooks.expandablerecyclerview.Model.ExpandingObject;
import com.ryanbrooks.expandablerecyclerview.Model.ParentObject;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ryanbrooks.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

/**
 * Created by Ryan Brooks on 5/27/15.
 */
public abstract class ExpandableRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ParentItemClickListener {
    private final String TAG = this.getClass().getSimpleName();
    public static final int TYPE_PARENT = 0;
    public static final int TYPE_CHILD = 1;

    protected Context mContext;
    protected List<ExpandingObject> mItemList;

    public ExpandableRecyclerAdapter(Context context, List<ExpandingObject> mItemList) {
        this.mContext = context;
        this.mItemList = mItemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PARENT) {
            return onCreateParentViewHolder(viewGroup);
        } else if (viewType == TYPE_CHILD) {
            return onCreateChildViewHolder(viewGroup);
        } else {
            throw new IllegalStateException("Incorrect ViewType found");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mItemList.get(position) instanceof ParentObject) {
            ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
            parentViewHolder.setExpanded(((ParentObject) mItemList.get(position)).isExpanded());
            parentViewHolder.setParentItemClickListener(this);
            onBindParentViewHolder(parentViewHolder, position);
        } else if (mItemList.get(position) instanceof ChildObject) {
            onBindChildViewHolder((ChildViewHolder) holder, position);
        } else {
            throw new IllegalStateException("Incorrect ViewHolder found");
        }
    }

    public abstract ParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup);

    public abstract ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup);

    public abstract void onBindParentViewHolder(ParentViewHolder parentViewHolder, int position);

    public abstract void onBindChildViewHolder(ChildViewHolder childViewHolder, int position);

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItemList.get(position) instanceof ParentObject) {
            return TYPE_PARENT;
        } else if (mItemList.get(position) instanceof ChildObject) {
            return TYPE_CHILD;
        } else if (mItemList.get(position) == null) {
            throw new IllegalStateException("Null object added");
        } else {
            return TYPE_PARENT;
        }
    }

    @Override
    public void onParentItemClickListener(int position) {
        if (mItemList.get(position) instanceof ParentObject) {
            ParentObject parentObject = (ParentObject) mItemList.get(position);
            expandParent(parentObject, position);
        }
    }

    private void expandParent(ParentObject parentObject, int position) {
        if (parentObject.isExpanded()) {
            parentObject.setExpanded(false);
            mItemList.remove(position + 1);
            notifyItemRemoved(position + 1);
        } else {
            parentObject.setExpanded(true);
            mItemList.add(position + 1, parentObject.getChildObject());
            notifyItemInserted(position + 1);
        }
    }
}