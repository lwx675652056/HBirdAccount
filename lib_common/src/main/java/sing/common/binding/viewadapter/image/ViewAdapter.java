package sing.common.binding.viewadapter.image;


import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


/**
 * Created by goldze on 2017/6/18.
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"imageUrl", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(placeholderRes))
                    .into(imageView);
        }
    }

    @BindingAdapter(value = {"imageUrlNoCache", "placeholderRes"}, requireAll = false)
    public static void setImageUriNoCache(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(placeholderRes))
                    .apply(new RequestOptions().skipMemoryCache(true))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(imageView);
        }
    }

    @BindingAdapter(value = {"resId", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, int resId, int placeholderRes) {
        imageView.setImageResource(resId);
//        //使用Glide框架加载图片
//        Glide.with(imageView.getContext())
//                .load(resId)
//                .apply(new RequestOptions().placeholder(placeholderRes))
//                .into(imageView);
    }
}

