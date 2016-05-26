package com.jade.customervisit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jade.customervisit.util.FileSystemManager;
import com.jade.customervisit.util.L;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * 
 * <应用初始化> <功能详细描述>
 * 
 * @author cyf
 * @version [版本号, 2014-3-24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CVApplication extends Application {

    /**
     * 打包发布要改为false
     */
    public static final boolean DEBUG = true;

    /**
     * 服务器地址
     */
    public static final String DEFAULT_URL = "http://117.34.71.28/web/mobile";
//    public static final String DEFAULT_URL = "http://192.168.0.106/cv/mobile";

    /**
	 * app实例
	 */
	public static CVApplication cvApplication = null;

	/**
	 * 本地activity栈
	 */
	public static List<Activity> activitys = new ArrayList<Activity>();

    /** 字号大小 */
    private int titleTextStyle;
    private int moduleTextStyle;
    private int moduleNumTextStyle;
    private int settingsTextStyle;

    private String userid;
    private String username;

    @Override
	public void onCreate() {
		super.onCreate();
		cvApplication = this;
        L.allowD = DEBUG;
        L.allowE = DEBUG;
        L.allowI = DEBUG;
        L.allowV = DEBUG;
        L.allowW = DEBUG;
        L.allowWtf = DEBUG;
        
        loadData(getApplicationContext());
	}

    public static void loadData(Context context)
    {
        ImageLoaderConfiguration config =
            new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(4)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(4 * 1024 * 1024))
                .discCacheSize(50 * 1024 * 1024)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiscCache(new File(FileSystemManager.getCacheImgFilePath(context))))
                .build();
        ImageLoader.getInstance().init(config);
    }

	@Override
	public void onTerminate() {
		super.onTerminate();
		for (Activity activity : activitys) {
			activity.finish();
			activity = null;
		}
		activitys.clear();
	}

	/**
	 * 
	 * <添加> <功能详细描述>
	 * 
	 * @param activity
	 * @see [类、类#方法、类#成员]
	 */
	public void addActivity(Activity activity) {
		activitys.add(activity);
	}

	/**
	 * 
	 * <删除> <功能详细描述>
	 * 
	 * @param activity
	 * @see [类、类#方法、类#成员]
	 */
	public void deleteActivity(Activity activity) {
		if (activity != null) {
			activitys.remove(activity);
			activity.finish();
			activity = null;
		}
	}
    
    /**
     * 初始化option
     * picFail--加载失败时显示
     * picLoading--正在加载图片时显示
     * picEmpty--uri为空的时候显示
     */
    public static DisplayImageOptions setAllDisplayImageOptions(Context context, String picFail, String picLoading,
        String picEmpty)
    {
        DisplayImageOptions options;
        //通过图片名调用图片ID
        int fail = context.getResources().getIdentifier(picFail, "drawable", "com.jade.customervisit");
        int loading = context.getResources().getIdentifier(picLoading, "drawable", "com.jade.customervisit");
        int empty = context.getResources().getIdentifier(picEmpty, "drawable", "com.jade.customervisit");
        options =
            new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageOnFail(fail)
                .showImageForEmptyUri(empty)
                .showImageOnLoading(loading)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(0))
                .build();
        
        return options;
    }

    /**
     * 初始化字号大小
     */
    public void initTextSize() {
            titleTextStyle = R.style.titleTextNormal;
            moduleTextStyle = R.style.moduleTextNormal;
            moduleNumTextStyle = R.style.moduleNumTextNormal;
            settingsTextStyle = R.style.settingsTextNormal;
    }

    public int getTitleTextStyle() {
        if (titleTextStyle == 0) {
            initTextSize();
        }
        return titleTextStyle;
    }

    public int getModuleTextStyle() {
        if (moduleTextStyle == 0) {
            initTextSize();
        }
        return moduleTextStyle;
    }

    public int getModuleNumTextStyle() {
        if (moduleNumTextStyle == 0) {
            initTextSize();
        }
        return moduleNumTextStyle;
    }

    public int getSettingsTextStyle() {
        if (settingsTextStyle == 0) {
            initTextSize();
        }
        return settingsTextStyle;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return this.userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
