package van.tian.wen.multistaterefreshlayoutlib;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import van.tian.wen.multistaterefreshlayoutlib.adapter.BasicAdapter;
import van.tian.wen.multistaterefreshlayoutlib.factory.ModelFactory;
import van.tian.wen.multistaterefreshlayoutlib.model.IModel;
import van.tian.wen.multistaterefreshlayoutlib.model.Pagination;
import van.tian.wen.multistaterefreshlayoutlib.util.NetWorkUtil;


/**
 * 一个刷新，加载更多的基本界面
 * <p>
 * 单例
 * <p/>
 * Notice：只支持获取数据是分页格式的，且按照{@link van.tian.wen.multistaterefreshlayoutlib.model.Pagination}作为返回的数据格式
 *
 * @param <T> 获取的数据
 */
public abstract class BaseRefreshLoadingFragment<T> extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MultiStateRefreshLayout.OnLoadingListener, View.OnClickListener {

    protected Context mContext;

    private MultiStateRefreshLayout mRefreshLayout;
    protected ListView mListView;

    private BasicAdapter<T> mAdapter;
    protected ArrayList<T> mList = new ArrayList<>();

    protected int pageNo = 1;
    protected boolean isLastPage;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (activity != null) {
            this.mContext = activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.base_refresh_loading_view, container, false);
        initViews(view);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadingListener(this);
        mRefreshLayout.setOnErrorFootClickListener(this);
        mRefreshLayout.setOnSucessFootClickListener(this);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new BasicAdapter<T>(mContext) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return getAdapterView(position, convertView, parent);
            }
        };

        mAdapter.setList(mList, true);
        mListView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        request();

    }

    private void request() {
        setRefreshing(true);
        checkNetWork();

        onRequestData();
    }

    protected abstract void onRequestData();

    protected void showList(List<T> pageList) {

        if (pageList != null && pageList.size() > 0) {
            mRefreshLayout.showNormalView();
            mAdapter.clear();
            mAdapter.addExtraList(pageList);
            mAdapter.notifyDataSetChanged();
        } else {
            mRefreshLayout.showEmptyView();
        }

    }

    protected void addExtra(List<T> extra) {

        if (extra.size() > 0) {
            mAdapter.addExtraList(extra);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void checkNetWork() {
        if (!NetWorkUtil.isNetworkAvailable(mContext)) {
            // 网络不通
            Toast.makeText(mContext, "网络不通", Toast.LENGTH_SHORT).show();
            setRefreshing(false);
            mRefreshLayout.showErrorFootView();
            return;
        }
    }

    private void setRefreshing(final boolean shouldRefresh) {

        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(shouldRefresh);
            }
        });

    }

    protected void initViews(View view) {

        mRefreshLayout = (MultiStateRefreshLayout) view.findViewById(R.id.validSaleRefreshLayout);

        int listViewId = provideListViewResId();
        if (listViewId <= 0) {
            mListView = mRefreshLayout.getListView();
        } else {
            mListView = buildViewFromRes(listViewId);
            mRefreshLayout.setListView(mListView);
        }

    }

    private ListView buildViewFromRes(int listViewId) {
        View inflatedView = LayoutInflater.from(mContext).inflate(listViewId, null);
        if (inflatedView instanceof ListView) {
            return (ListView) inflatedView;
        } else {
            throw new InflateException("you should put the root view with 'ListView' tag");
        }
    }

    /**
     * provide a listView resId if you want,absolutely,we have a default one
     *
     * @return
     */
    protected abstract int provideListViewResId();


    /**
     * onRefresh
     */
    @Override
    public void onRefresh() {
        resetPageNo();
        request();
    }

    /**
     * onLoadingMore
     */
    @Override
    public void onLoadMore() {
        checkNetWork();
        onRequestMore();
    }

    protected void onRequestMore() {
        if (!isLastPage) {
            pageNo++;
            mRefreshLayout.showLoadingFoot();
            request();
        } else {
            mRefreshLayout.showEndLoadFootView();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mRefreshLayout.getErrorView()) {
            // onClick Error Foot View
            request();
        } else if (v == mRefreshLayout.getSuccessView()) {
            // onClick Success Foot View
            request();
        } else {
            // TODO for more conditions
        }
    }

    protected abstract View getAdapterView(int position, View convertView, ViewGroup parent);

    /**
     * 请求数据成功之后调用
     *
     * @param body
     */
    protected void onRequestResult(Pagination<T> body) {
        setRefreshing(false);

        if (body != null) {
            syncState(body);
            if (isLastPage) {
                mRefreshLayout.showEndLoadFootView();
            } else {
                mRefreshLayout.showSuccessFoot();
            }

            if (pageNo == 1) {
                showList(body.getPageList());
            } else {
                addExtra(body.getPageList());
            }

        } else {
            mRefreshLayout.showEmptyView();
        }
    }

    private void syncState(Pagination<T> body) {
        if (body.getTotalPage() == pageNo) {
            isLastPage = true;
        } else {
            isLastPage = false;
        }
    }

    /**
     * 请求失败调用
     */
    protected void onRequestResultFail() {
        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.showErrorMsg();
    }

    private void resetPageNo() {
        pageNo = 1;
    }
}

