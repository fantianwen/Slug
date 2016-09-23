package van.tian.wen.multistaterefreshlayoutlib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * PackageName:com.dooioo.mobile.common.utils
 * Author: Jerry.hu
 * Create: Jerry.hu (2015-06-05 下午2:12)
 * Description:
 * To change this template use File | Settings | File Templates.
 */
public class NetWorkUtil {

    /**
     * <code>
     * 判断当前是否有可用网络.
     *
     * @return true有可用网络, 否则返回false.</code>
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();//isAvailable()  Indicates whether network connectivity is possible.
    }

    /**
     * 验证wifi 是否连接上
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo != null && wifiNetworkInfo.isConnected();
    }


    /**
     * 验证手机网络 是否连接上
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return mobileNetworkInfo != null && mobileNetworkInfo.isConnected();
    }

}
