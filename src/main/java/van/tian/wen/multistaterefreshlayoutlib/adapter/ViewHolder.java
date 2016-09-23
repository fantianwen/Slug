package van.tian.wen.multistaterefreshlayoutlib.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用的ViewHolder
 *
 * @author Administrator
 */
public class ViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;
    public int position = -1;

    private ViewHolder(Context context, ViewGroup parent, int layoutId) {
        this.mViews = new SparseArray<View>();
        if (parent == null) {
            this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        } else {
            this.mConvertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }
        this.mConvertView.setTag(this);

    }

    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder(context, parent, layoutId);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return viewHolder;
    }

    /**
     * 通过id获取组件
     * 注意不要将组件声明到全局，否则永远获取的是最后一个组件
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }


    public <T extends View> T getView(int viewId, boolean shouldReuse) {
        if (shouldReuse) {
            return getView(viewId);
        } else {
            return (T) mConvertView.findViewById(viewId);
        }
    }


    public View getConvertView() {
        return this.mConvertView;
    }

    public ViewHolder setTextForTextView(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text == null ? "" : text);

        return this;
    }

    public ViewHolder setTextForTextView(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text == null ? "" : text);

        return this;
    }

    public ViewHolder setImageForImageView(int viewId, int resId) {
        ImageView image = getView(viewId);
        image.setImageResource(resId);
        return this;
    }
}
