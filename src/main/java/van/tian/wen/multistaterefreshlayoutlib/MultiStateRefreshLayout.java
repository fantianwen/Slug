/**
 * Copyright (C) 2016 fantianwen <twfan_09@hotmail.com>
 * <p/>
 * also you can see {@link https://github.com/fantianwen/MultiStateRefreshLayout}
 */
package van.tian.wen.multistaterefreshlayoutlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MultiStateRefreshLayout extends SwipeRefreshLayout {

    private final RelativeLayout mRelativeLayout;
    private FrameLayout mOuterContainer;
    private ListView mListView;
    private ScrollView mScrollView;

    // 获取数据为空显示的view
    private View mEmptyView;
    // 获取数据出错时候显示的view
    private View mRequestErrorView;

    private View mLoadingView;
    private View mErrorView;
    private View mSuccessView;
    private View mUnknownView;

    private MultiStateView mFootView;

    private ListView mDefaultListView;

    private View mDefaultLoadingView;
    private View mDefaultSuccessView;
    private View mDefaultErrorView;
    private View mDefaultUnknownView;
    private View mDefaultEmptyView;
    private View mDefaultRequestErrorView;

    private Context mContext;

    private float mFirstTouchY;
    private float mLastTouchY;
    private OnLoadingListener mOnLoadingListener;
    private boolean isLoading;
    private int mTouchSlop;
    private RecyclerView mRecyclerView;
    private boolean isRecyclerViewLoadingMore;

    public MultiStateRefreshLayout(Context context) {
        this(context, null);
    }

    public MultiStateRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        initDefaultViews();

        initRes(attrs);

        mOuterContainer = new FrameLayout(mContext);
        FrameLayout.LayoutParams outerLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mOuterContainer.setLayoutParams(outerLayoutParams);
        mFootView = new MultiStateView(mContext);

        mFootView.setLoadingView(mLoadingView)
                .setErrorView(mErrorView)
                .setSuccessView(mSuccessView)
                .setUnknownView(mUnknownView);

        mListView.addFooterView(mFootView);

        mScrollView = new ScrollView(mContext);
        // that is important
        mScrollView.setFillViewport(true);
        FrameLayout.LayoutParams scrollLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mScrollView.setLayoutParams(scrollLayoutParams);

        mRelativeLayout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mRelativeLayout.setLayoutParams(relativeLayoutParams);

        mRelativeLayout.addView(mEmptyView);
        mRelativeLayout.addView(mRequestErrorView);

        mScrollView.addView(mRelativeLayout);

        mOuterContainer.addView(mListView, mListView.getLayoutParams());
        mOuterContainer.addView(mScrollView, mScrollView.getLayoutParams());
        this.addView(mOuterContainer, mOuterContainer.getLayoutParams());

        this.mScrollView.setVisibility(View.INVISIBLE);

        if (mListView != null) {
            setListView(mListView);
        }

        if (mUnknownView != null) {
            mUnknownView.setClickable(false);
        }

    }

    private void initDefaultViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        mDefaultEmptyView = layoutInflater.inflate(R.layout.default_empty_view, this, false);
        mDefaultListView = (ListView) layoutInflater.inflate(R.layout.default_listview, this, false);
        mDefaultErrorView = layoutInflater.inflate(R.layout.default_foot_error_view, this, false);
        mDefaultLoadingView = layoutInflater.inflate(R.layout.default_foot_loading_view, this, false);
        mDefaultSuccessView = layoutInflater.inflate(R.layout.default_foot_success_view, this, false);
        mDefaultRequestErrorView = layoutInflater.inflate(R.layout.default_request_error_view, this, false);
        mDefaultUnknownView = layoutInflater.inflate(R.layout.default_foot_unknown_view, this, false);

    }

    private void initRes(AttributeSet attrs) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateRefreshLayout);
        int listViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_listView, -1);
        if (listViewId > -1) {
            View listView = layoutInflater.inflate(listViewId, this, false);
            if (listView instanceof AbsListView) {
                mListView = (ListView) listView;
            } else {
                throw new IllegalArgumentException("illegal type of AbsListView");
            }
        }

        int emptyViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_emptyView, -1);
        if (emptyViewId > -1) {
            mEmptyView = layoutInflater.inflate(emptyViewId, this, false);
        } else {
            mEmptyView = mDefaultEmptyView;
        }

        int loadingViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_footLoadingView, -1);
        if (loadingViewId > -1) {
            mLoadingView = layoutInflater.inflate(loadingViewId, this, false);
        } else {
            mLoadingView = mDefaultLoadingView;
        }

        int errorViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_footErrorView, -1);
        if (errorViewId > -1) {
            mErrorView = layoutInflater.inflate(errorViewId, this, false);
        } else {
            mErrorView = mDefaultErrorView;
        }

        int successViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_footSuccessView, -1);
        if (successViewId > -1) {
            mSuccessView = layoutInflater.inflate(successViewId, this, false);
        } else {
            mSuccessView = mDefaultSuccessView;
        }

        int unknownViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_footUnknownView, -1);
        if (unknownViewId > -1) {
            mUnknownView = layoutInflater.inflate(unknownViewId, this, false);
        } else {
            mUnknownView = mDefaultUnknownView;
        }

        int requestErrorViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_requestErrorView, -1);
        if (requestErrorViewId > -1) {
            mRequestErrorView = layoutInflater.inflate(requestErrorViewId, this, false);
        } else {
            mRequestErrorView = mDefaultRequestErrorView;
        }

        typedArray.recycle();
    }


    /**
     * if you set recycler view,the list view will be invalid
     *
     * @param recyclerView
     */
    public void setRecyclerView(RecyclerView recyclerView) {

        this.mRecyclerView = recyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (canSwipeLayoutRefresh()) {
                        setEnabled(true);
                    } else {
                        setEnabled(false);
                    }

                    if (canRecyclerViewLoadMore()) {
                        loadData();
                    }
                }
            }
        });
    }

    private void removeListView() {
        if (mOuterContainer.getChildCount() > 0 && mListView != null) {
            mOuterContainer.removeView(mListView);
        }
    }

    private void addRecyclerView() {
        if (mRecyclerView != null) {
            mOuterContainer.addView(mRecyclerView, mRecyclerView.getLayoutParams());
        }
    }

    private boolean canRecyclerViewLoadMore() {

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();

        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (layoutManager.getChildCount() > 0) {
                int lastItemBottom = linearLayoutManager.getChildAt(linearLayoutManager.getItemCount() - 1).getBottom();
                if (!this.isRecyclerViewLoadingMore && lastItemBottom <= mRecyclerView.getBottom()) {
                    this.isRecyclerViewLoadingMore = true;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canSwipeLayoutRefresh() {

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();

        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

            if (layoutManager.getChildCount() > 0) {
                int firstChildViewTop = linearLayoutManager.getChildAt(0).getTop();

                if (firstChildViewTop >= mRecyclerView.getTop()) {
                    // it indicate that the recycler view has scrolled to the top,and the swipeLayout can refresh
                    return true;
                }
            }
        }
        return false;

    }


    public void setListView(final ListView mListView) {
        this.mListView = mListView;

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        //监听是都能够上拉刷新
                        if (canRefresh()) {
                            setEnabled(true);
                        } else {
                            setEnabled(false);
                        }
                        //监听能够上拉加载更多
                        if (canLoadMore(scrollState)) {
                            loadData();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private boolean canRefresh() {
        return isTop();
    }

    private boolean isTop() {
        if (mListView.getCount() > 0) {
            if (mListView.getFirstVisiblePosition() == 0
                    && mListView.getChildAt(0).getTop() >= mListView.getTop()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //Fix for support lib bug, happening when onDestroy is
            return true;
        }
    }

    private void loadData() {
        if (mOnLoadingListener != null) {
            setLoading(true);
        }
    }

    public void setLoading(boolean shouldLoad) {
        if (mListView == null) return;
        isLoading = shouldLoad;
        if (isLoading) {
            if (isRefreshing()) {
                setRefreshing(false);
            }
            showLoadingFoot();
            mOnLoadingListener.onLoadMore();
            isLoading = false;
        } else {
            mFirstTouchY = 0;
            mLastTouchY = 0;
        }
    }

    public void setOnLoadingListener(OnLoadingListener loadingListener) {
        this.mOnLoadingListener = loadingListener;
    }

    private boolean canLoadMore(int scrollState) {

        return isBottom() && !isLoading && isPullingUp() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        try {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mFirstTouchY = event.getRawY();
                    break;

                case MotionEvent.ACTION_UP:
                    mLastTouchY = event.getRawY();
                    break;

                default:
                    break;
            }
            return super.dispatchTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

    }

    /**
     * 表明正在上拉
     *
     * @return
     */
    private boolean isPullingUp() {
        return (mFirstTouchY - mLastTouchY) >= mTouchSlop;
    }

    /**
     * 判断该ListView已经滑到的bottom
     *
     * @return
     */
    private boolean isBottom() {

        if (mListView.getCount() > 0) {
            if (mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1 ||
                    mListView.getLastVisiblePosition() == mListView.getAdapter().getCount()
                    /*&& mListView.getChildAt(mListView.getChildCount() - 1).getBottom() <= mListView.getHeight() + CommonUtil.dip2px(mContext, 12)*/) {
                return true;
            }
        }
        return false;
    }

    /**
     * 展示已经查询到最后的foot
     */
    public void showEndLoadFootView() {
        this.mFootView.setViewStateAndValidate(MultiStateView.VIEW_STATE_UNKNOWN);
    }

    public void setSuccessFootView() {
        this.mFootView.setViewStateAndValidate(MultiStateView.VIEW_STATE_SUCCESS);
    }

    public void setLoadingFootView() {
        mFootView.setViewStateAndValidate(MultiStateView.VIEW_STATE_LOADING);
    }

    /**
     * 展示查询错误的foot
     */
    public void showErrorFootView() {
        this.mFootView.setViewStateAndValidate(MultiStateView.VIEW_STATE_ERROR);
    }

    public void showFootView(int viewState) {
        switch (viewState) {
            case MultiStateView.VIEW_STATE_UNKNOWN:
                showEndLoadFootView();
                break;
            case MultiStateView.VIEW_STATE_ERROR:
                showErrorFootView();
                break;
            case MultiStateView.VIEW_STATE_LOADING:
                showLoadingFoot();
                break;
            case MultiStateView.VIEW_STATE_SUCCESS:
                showSuccessFoot();
                break;
            default:
                break;
        }
    }

    public void showErrorMsg() {
        this.mListView.setVisibility(View.INVISIBLE);
        this.mScrollView.setVisibility(View.VISIBLE);
        this.mEmptyView.setVisibility(View.GONE);
        this.mRequestErrorView.setVisibility(View.VISIBLE);
    }

    public View getErrorView() {

        if (mErrorView != null) {
            return mErrorView;
        } else if (mDefaultErrorView != null) {
            return mDefaultErrorView;
        }
        return null;

    }

    public View getSuccessView() {

        if (mSuccessView != null) {
            return mSuccessView;
        } else if (mDefaultSuccessView != null) {
            return mDefaultSuccessView;
        }
        return null;

    }

    public interface OnLoadingListener {
        void onLoadMore();
    }

    /**
     * 展示查询为空的信息
     */
    public void showEmptyView() {
        this.mListView.setVisibility(View.INVISIBLE);
        this.mScrollView.setVisibility(View.VISIBLE);
        this.mEmptyView.setVisibility(View.VISIBLE);
        this.mRequestErrorView.setVisibility(View.GONE);
    }

    /**
     * 展示正常的view
     */
    public void showNormalView() {
        this.mListView.setVisibility(View.VISIBLE);
        this.mScrollView.setVisibility(View.GONE);
    }

    /**
     * 展示正在加载（load more）的foot
     */
    public void showLoadingFoot() {
        this.mEmptyView.setVisibility(View.GONE);
        this.mScrollView.setVisibility(View.GONE);
        this.mFootView.setViewStateAndValidate(MultiStateView.VIEW_STATE_LOADING);
    }

    /**
     * 展示加载（load more）成功的foot
     */
    public void showSuccessFoot() {
        this.mEmptyView.setVisibility(View.GONE);
        this.mScrollView.setVisibility(View.GONE);
        this.mFootView.setViewStateAndValidate(MultiStateView.VIEW_STATE_SUCCESS);
    }

    public ListView getListView() {
        return mListView;
    }

    public void setOnSucessFootClickListener(OnClickListener listener) {
        this.mFootView.getSuccessView().setOnClickListener(listener);
    }

    public void setOnErrorFootClickListener(OnClickListener listener) {
        this.mFootView.getErrorView().setOnClickListener(listener);
    }
}
