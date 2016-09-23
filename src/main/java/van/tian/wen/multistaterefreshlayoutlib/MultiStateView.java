package van.tian.wen.multistaterefreshlayoutlib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 四种状态的ViewGroup
 */
public class MultiStateView extends FrameLayout {
    /**
     * loading状态
     */
    public static final int VIEW_STATE_LOADING = 0;

    /**
     * error状态
     */
    public static final int VIEW_STATE_ERROR = 1;

    /**
     * success状态
     */
    public static final int VIEW_STATE_SUCCESS = 2;

    /**
     * unknown状态
     */
    public static final int VIEW_STATE_UNKNOWN = 3;

    private View loadingView;
    private View errorView;
    private View successView;
    private View unknownView;

    private int viewState;

    public MultiStateView(Context context) {
        this(context, null);
    }

    public MultiStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
    }


    public MultiStateView setLoadingView(View _loadingView) {
        this.loadingView = _loadingView;
        addView(loadingView, loadingView.getLayoutParams());
        return this;
    }


    public MultiStateView setErrorView(View _errorView) {
        this.errorView = _errorView;
        addView(errorView, errorView.getLayoutParams());
        return this;
    }

    public MultiStateView setSuccessView(View _successView) {
        this.successView = _successView;
        addView(successView, successView.getLayoutParams());
        return this;
    }

    public MultiStateView setUnknownView(View _unknownView) {
        this.unknownView = _unknownView;
        addView(unknownView, unknownView.getLayoutParams());
        return this;
    }

    /**
     * 设置并刷新
     *
     * @param viewState
     */
    public void setViewStateAndValidate(int viewState) {
        this.viewState = viewState;
        setView();
    }

    /**
     * 该方法会在onResume之后调用
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (successView == null) {
            throw new IllegalArgumentException("SuccessView is not defined!");
        }
        setView();
    }

    public void setView() {
        switch (viewState) {
            case VIEW_STATE_ERROR:
                if (errorView == null) {
                    throw new NullPointerException("Error View");
                }
                errorView.setVisibility(View.VISIBLE);
                if (successView != null) {
                    successView.setVisibility(View.GONE);
                }
                if (loadingView != null) {
                    loadingView.setVisibility(View.GONE);
                }
                if (unknownView != null) {
                    unknownView.setVisibility(View.GONE);
                }
                break;
            case VIEW_STATE_LOADING:
                if (loadingView == null) {
                    throw new NullPointerException("Loading View");
                }
                loadingView.setVisibility(View.VISIBLE);
                if (successView != null) {
                    successView.setVisibility(View.GONE);
                }
                if (errorView != null) {
                    errorView.setVisibility(View.GONE);
                }
                if (unknownView != null) {
                    unknownView.setVisibility(View.GONE);
                }
                break;
            case VIEW_STATE_SUCCESS:
                if (successView == null) {
                    throw new NullPointerException("Success View");
                }
                successView.setVisibility(View.VISIBLE);
                if (errorView != null) {
                    errorView.setVisibility(View.GONE);
                }
                if (loadingView != null) {
                    loadingView.setVisibility(View.GONE);
                }
                if (unknownView != null) {
                    unknownView.setVisibility(View.GONE);
                }
                break;
            case VIEW_STATE_UNKNOWN:
            default:
                if (unknownView == null) {
                    throw new NullPointerException("unknown View");
                }
                unknownView.setVisibility(View.VISIBLE);
                if (errorView != null) {
                    errorView.setVisibility(View.GONE);
                }
                if (loadingView != null) {
                    loadingView.setVisibility(View.GONE);

                }
                if (successView != null) {
                    successView.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void setMultiViewState(int state) {
        this.viewState = state;
        setView();
    }

    public View getSuccessView() {
        return successView;
    }

    public View getErrorView() {
        return errorView;
    }
}
