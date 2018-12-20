package sing.common.util;

import android.app.Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: LiangYX
 * @ClassName: DownLoadUtil
 * @date: 2018/12/20 10:37
 * @Description: 网络文件下载工具类
 */
public class DownLoadUtil {

    //下载路径
    private String localPath;
    private String url;
    private Activity activity;
    private long total = 0;
    private long sum = 0;

    public DownLoadUtil(Activity activity, String url, String path) {
        this.localPath = path;
        this.url = url;
        this.activity = activity;
    }

    //当前进度
    public int progress;
    static OkHttpClient mOkHttpClient = new OkHttpClient();

    public boolean isExit = false;

    public void exit() {
        isExit = true;
    }

    public void downLoadFile(final Callback callback) {
        progress = 0;
        isExit = false;
        File file = new File(localPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        localPath = localPath + url.substring(url.lastIndexOf("/") + 1);
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.failure(url, e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    total = response.body().contentLength();
                    File file = new File(localPath);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1 && !isExit) {
                        fos.write(buf, 0, len);
                        sum += len;
                        progress = (int) (sum * 100 / total);
                        if (!isExit) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.process(sum, total);
                                }
                            });
                        } else {
                            break;
                        }
                    }
                    fos.flush();
                    if (!isExit) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.success(url, localPath);
                            }
                        });
                    }
                } catch (final Exception e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.failure(url, e.toString());
                        }
                    });
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException ignored) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }

    public interface Callback {
        //成功回调
        void success(String url, String localPath);

        void failure(String url, String errorMsg);

        void process(long current, long total);
    }
}
