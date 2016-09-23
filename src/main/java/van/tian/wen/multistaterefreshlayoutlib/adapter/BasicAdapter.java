package van.tian.wen.multistaterefreshlayoutlib.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据适配器基类
 */
public abstract class BasicAdapter<T> extends BaseAdapter {

    protected List<T> mList;
    protected Context mContext;
    protected boolean refreshVisible; // 是否刷新可视区域

    public BasicAdapter(Context context) {
        mContext = context;
    }

    /**
     * 重置adapter数据数组.
     *
     * @param list
     * @param clearPrevious 是否清空前一个数组数据
     */
    public void setList(List<T> list, boolean clearPrevious) {
        if (mList != null && clearPrevious) {
            mList.clear();
        }
        mList = list;
    }

    /**
     * 设置是否刷新可视区域
     *
     * @param refreshVisible
     */
    public void setRefreshVisible(boolean refreshVisible) {
        this.refreshVisible = refreshVisible;
    }

    public boolean isRefreshVisible() {
        return refreshVisible;
    }

    /**
     * 向adapter中增加新的数据.
     *
     * @param list
     */
    public void addExtraList(List<T> list) {
        if (mList != null) {
            mList.addAll(list);
        } else {
            mList = list;
        }
    }

    /**
     * 向adapter中增加新的数据.
     *
     * @param list
     */
    public void preAddExtraList(List<T> list) {
        if (mList != null) {
            mList.addAll(0, list);
        } else {
            mList = list;
        }
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public void destroyList() {
        if (mList != null) {
            mList.clear();
            mList = null;
        }
    }

    @Override
    public T getItem(int position) {
        if (mList == null || position >= mList.size() || position < 0) {
            return null;
        }
        return mList.get(position);
    }

    public void addItem(T t) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.add(t);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position) {

        if (mList != null && mList.size() > position) {
            mList.remove(position);
        }

    }

    public void clear() {
        if (mList != null && mList.size() > 0) {
            mList.clear();
        }
    }
}
