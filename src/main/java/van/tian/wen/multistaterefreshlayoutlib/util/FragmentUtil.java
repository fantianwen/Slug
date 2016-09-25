package van.tian.wen.multistaterefreshlayoutlib.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * utils about add/remove/replace fragment
 */
public class FragmentUtil {

    private final Context mContext;
    private int mFragmentContainerResId;
    private FragmentManager mManager;

    public FragmentUtil(Context context, FragmentManager manager, int fragmentContainerResId) {
        this.mContext = context;
        this.mManager = manager;
        this.mFragmentContainerResId = fragmentContainerResId;
    }

    public void addFragment(Class<? extends Fragment> fragmentClass) {

        String fragmentTag = fragmentClass.getCanonicalName();

        Fragment fragment = this.mManager.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            // 初始化
            fragment = Fragment.instantiate(mContext, fragmentClass.getName());
        }

        FragmentTransaction fragmentTransaction = mManager.beginTransaction();

        // FragmentTransaction.detach(Fragment)} has been used on it.
        // 表明曾经添加过
        if (fragment.isDetached()) {
            fragmentTransaction.attach(fragment);
        } else if (!fragment.isAdded()) {
            fragmentTransaction.replace(mFragmentContainerResId, fragment, fragmentTag);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();

    }




}
