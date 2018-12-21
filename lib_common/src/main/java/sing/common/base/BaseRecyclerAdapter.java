package sing.common.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter {

    protected Context mContext;
    protected List<T> mList;
    protected int layoutId;

    public BaseRecyclerAdapter(Context context, List<T> list, int layoutId) {
        this.mContext = context;
        this.mList = list;
        this.layoutId = layoutId;
    }
    protected MultiTypeSupport<T> multiTypeSupport;
    @Override
    public int getItemViewType(int position) {
        if (multiTypeSupport != null) {
            return multiTypeSupport.getLayoutId(mList.get(position), position);
        }
        return super.getItemViewType(position);
    }
    public interface MultiTypeSupport<T> {
        int getLayoutId(T item, int position);
    }
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void refreshData(List<T> list){
        if (mList == null){
            mList = new ArrayList<>();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (multiTypeSupport != null) {
            layoutId = viewType;
        }
        B bing = DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId, parent, false);
        return new RecyclerHolder(bing.getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        B binding = DataBindingUtil.getBinding(holder.itemView);
        final T t = mList.get(position);
        onBindItem(binding, t, position);
    }


    protected abstract void onBindItem(B binding, T t, int position);

    static class RecyclerHolder extends RecyclerView.ViewHolder {

        public RecyclerHolder(View itemView) {
            super(itemView);
        }
    }
}