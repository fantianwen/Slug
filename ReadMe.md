## <div align="center">Slug</div>

   <div align="center">![Slug](http://o7zh7nhn0.bkt.clouddn.com/slug.jpg)</div>

### Feature

- [MultiStateRefreshLayout](https://github.com/fantianwen/MultiStateRefreshLayout)
	
	
- 以`MultiStateRefreshLayout`为基础封装的[BaseRefreshLoadingFragment](https://github.com/fantianwen/Slug/blob/master/src/main/java/van/tian/wen/multistaterefreshlayoutlib/BaseRefreshLoadingFragment.java)
	 
	 支持上拉刷新和滑到底部刷新
	 
	 栗子：
	 
	 继承`BaseRefreshLoadingFragment`,并覆写三个方法即可，在`onRequestData`成功的时候，调用`onRequestResult`，在调用失败的时候调用`onRequestFail`即可，将自动显示各种不同状态的view。
	 
```
public class RefreshingLoadingDemoFragment extends BaseRefreshLoadingFragment<MockedData> {

    @Override
    protected void onRequestData() {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Pagination<MockedData> mockedData = MockedData.getMockedData();
                onRequestResult(mockedData);
            }
        }, 3000);
    }

    @Override
    protected int provideListViewResId() {
        return 0;
    }

    @Override
    protected View getAdapterView(int position, View convertView, ViewGroup parent) {

        MockedData mockedData = mList.get(position);

        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_mocked);

        viewHolder.setTextForTextView(R.id.mockedTextView, String.valueOf(mockedData.getNumber()));

        return viewHolder.getConvertView();

    }
}
```

- MVP

继承`BasePresenter`,默认构造实现是：

```
public BasePresenter(V view) {
     ...
}
```

或者使用(效率略高)：

```
public BasePresenter(V view, Class<M> modelClass) {
		...
}
```

Demo 参见 [MVP_Demo](https://github.com/fantianwen/MultiStateRefreshLayout/blob/master/app/src/main/java/van/tian/wen/multirefreshlayout/MainActivity.java#L27)



