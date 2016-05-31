package com.jade.customervisit.scancode;

import java.io.IOException;
import java.util.Vector;

import org.xutils.common.Callback.Cancelable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.jade.customervisit.R;
import com.jade.customervisit.bean.SubmitResult;
import com.jade.customervisit.bll.ServiceManager;
import com.jade.customervisit.network.RequestListener;
import com.jade.customervisit.scancode.camera.CameraManager;
import com.jade.customervisit.scancode.decoding.CaptureActivityHandler;
import com.jade.customervisit.scancode.decoding.InactivityTimer;
import com.jade.customervisit.scancode.view.ViewfinderView;
import com.jade.customervisit.ui.BaseActivity;
import com.jade.customervisit.util.Constants;
import com.jade.customervisit.util.ToastUtil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 扫码界面
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-4-18]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MipcaActivityCapture extends BaseActivity implements Callback, AMapLocationListener
{
    
    private CaptureActivityHandler handler;
    
    private ViewfinderView viewfinderView;
    
    private SurfaceView surfaceView;
    
    private TextView gpsTitle;
    
    private boolean hasSurface;
    
    private Vector<BarcodeFormat> decodeFormats;
    
    private String characterSet;
    
    private InactivityTimer inactivityTimer;
    
    private MediaPlayer mediaPlayer;
    
    private boolean playBeep;
    
    private static final float BEEP_VOLUME = 0.10f;
    
    private boolean vibrate;
    
    // 打开闪光灯
    boolean islight = false;
    
    //判断是否有闪光灯
    boolean hasFlashLight = false;
    
    private LinearLayout llOpenLight;
    
    /**
     * 离开/到达   0到达  1  离开
     */
    private String flag = "0";
    
    /**
     * 提示框
     */
    private ProgressDialog mDialog;
    
    /**
     * 扫描返回结果
     */
    private String code;
    
    /**
     * 手机纬度
     */
    private String lat;
    
    /**
     * 手机纬度
     */
    private String lon;
    
    /**
     * 具体地址
     */
    private String city;
    
    /**
     * 扫描类型
     * 0: 签到
     * 1: 评价
     */
    private String type;
    
    private String serviceId;
    
    Cancelable submitHttpHandler;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_capture);
        type = getIntent().getStringExtra("type");
        serviceId = getIntent().getStringExtra("serviceId");
        flag = getIntent().getStringExtra("flag");
        
        initView();
        initData();
    }
    
    /**
     * 
     */
    private void initView() {
        surfaceView = (SurfaceView)findViewById(R.id.preview_view);
        viewfinderView = (ViewfinderView)findViewById(R.id.viewfinder_view);
        gpsTitle = (TextView)findViewById(R.id.title_gps_tv);
        final Button mButtonOpen = (Button)findViewById(R.id.integral_help_btn);
        LinearLayout llback = (LinearLayout)findViewById(R.id.title_back_layout);
        llback.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MipcaActivityCapture.this.finish();
            }
        });
        llOpenLight = (LinearLayout)findViewById(R.id.title_integral_help_layout);
        llOpenLight.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //以下代码判断手机是否带闪光灯
                FeatureInfo[] feature = MipcaActivityCapture.this.getPackageManager().getSystemAvailableFeatures();
                for (FeatureInfo featureInfo : feature)
                {
                    if (PackageManager.FEATURE_CAMERA_FLASH.equals(featureInfo.name))
                    {
                        hasFlashLight = true;
                        break;
                    }
                }
                //带闪光灯
                if (hasFlashLight)
                {
                    CameraManager.get().openLight(islight);
                    
                    if (!islight)
                    {
                        islight = true;
                        mButtonOpen.setBackgroundResource(R.drawable.scan_btn_light_press);
                    }
                    else
                    {
                        islight = false;
                        mButtonOpen.setBackgroundResource(R.drawable.app_scan_right_selector);
                    }
                }
                else
                {
                    Toast.makeText(MipcaActivityCapture.this, "抱歉,你的设备没有闪光灯", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
    }

    /**
     * 
     */
    private void initData() {
        CameraManager.init(getApplication());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        if (Constants.SIGN.equals(type) && (city == null || city.equals("")))
        {
            getGPS();
        }
    }
    
    private void resume() {
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface)
        {
            initCamera(surfaceHolder);
        }
        else
        {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        
        playBeep = true;
        AudioManager audioService = (AudioManager)getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
        {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    /**
     * 
     * <打开定位功能>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void getGPS()
    {
        gpsTitle.setText("正在定位…");
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);
        // 设置定位一次
        locationOption.setOnceLocation(true);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        // 设置是否优先使用GPS定位
        locationOption.setGpsFirst(false);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }
    
    /**
     * 获取经纬度
     */
    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
            // 定位完成
            case Constants.MSG_LOCATION_FINISH:
                AMapLocation loc = (AMapLocation) msg.obj;
                lat = String.valueOf(loc.getLatitude());
                lon = String.valueOf(loc.getLongitude());
                city = loc.getAddress();
                gpsTitle.setText("定位成功，请扫码");
                break;
            }
        }
    };
    
    @Override
    protected void onResume()
    {
        super.onResume();
        resume();
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        if (handler != null)
        {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        inactivityTimer.shutdown();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
    
    /**
     * 处理扫描结果
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode)
    {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        
        if (resultString.equals(""))
        {
            Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            code = resultString;
            if (Constants.SIGN.equals(type))
            {
                codeSign();
            }
            else
            {
                praise();
            }
        }
    }
    
    /**
     * 
     * <调用二维码签到接口>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void codeSign()
    {
    	RequestListener<SubmitResult> callback = new RequestListener<SubmitResult>() {

            @Override
            public void onStart() {
            	mDialog = ProgressDialog.show(MipcaActivityCapture.this, "请稍后...", "正在提交签到...", true, false);
            	mDialog.setCanceledOnTouchOutside(false);
            }

            @Override
            public void onSuccess(int statusCode, SubmitResult result) {
            	if (result != null) {
                    if (result.isSuccesses()) {
                    	if (Constants.SIGN.equals(flag)) {
                    		ToastUtil.showShort(context, "二维码签到成功");
                    	} else {
                    		ToastUtil.showShort(context, "二维码签退成功");
                    		sendRefresh();
                    	}
                    	finish();
                    } else if (Constants.CANCEL_FUNCTION_CODE.equals(result.getRetcode())) {
                        ToastUtil.showShort(context, result.getRetinfo());
                    } else {
                    	showDialog(result.getRetinfo());
                    }
                } else {
                	showDialog("");
                }
            }

            @Override
            public void onFailure(Exception e, String error) {
            	showDialog("");
            }

            @Override
            public void onStopped() {
            	mDialog.dismiss();
            }
        };
        submitHttpHandler = ServiceManager.codeSign(serviceId, code, flag, lat, lon, city, callback);
    }
    
    /**
     * 
     * <调用二维码签到接口>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void praise()
    {
    	RequestListener<SubmitResult> callback = new RequestListener<SubmitResult>() {

            @Override
            public void onStart() {
            	mDialog = ProgressDialog.show(MipcaActivityCapture.this, "请稍后...", "正在提交评价...", true, false);
            	mDialog.setCanceledOnTouchOutside(false);
            }

            @Override
            public void onSuccess(int statusCode, SubmitResult result) {
            	if (result != null) {
                    if (result.isSuccesses()) {
                    	ToastUtil.showShort(context, "二维码评价成功");
                    	finish();
                    } else if (Constants.CANCEL_FUNCTION_CODE.equals(result.getRetcode())) {
                        ToastUtil.showShort(context, result.getRetinfo());
                    } else {
                    	showDialog(result.getRetinfo());
                    }
                } else {
                    showDialog("");
                }
            }

            @Override
            public void onFailure(Exception e, String error) {
                showDialog("");
            }

            @Override
            public void onStopped() {
            	mDialog.dismiss();
            }
        };
        submitHttpHandler = ServiceManager.praise(serviceId, code, callback);
//        mDialog = ProgressDialog.show(MipcaActivityCapture.this, "请稍后...", "正在提交评价...", true, false);
//        mDialog.setCanceledOnTouchOutside(false);
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("code", code);
//        param.put("userId", Global.getUserId(MipcaActivityCapture.this));
//        param.put("serviceId", serviceId);
//        ConnectService.instance().connectServiceReturnResponse(MipcaActivityCapture.this,
//            param,
//            MipcaActivityCapture.this,
//            BaseResponse.class,
//            URLUtil.PRAISE);
    }
    
    private void initCamera(SurfaceHolder surfaceHolder)
    {
        try
        {
            CameraManager.get().openDriver(surfaceHolder);
        }
        catch (IOException ioe)
        {
            return;
        }
        catch (RuntimeException e)
        {
            return;
        }
        if (handler == null)
        {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (!hasSurface)
        {
            hasSurface = true;
            initCamera(holder);
        }
        
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        hasSurface = false;
        
    }
    
    public ViewfinderView getViewfinderView()
    {
        return viewfinderView;
    }
    
    public Handler getHandler()
    {
        return handler;
    }
    
    public void drawViewfinder()
    {
        viewfinderView.drawViewfinder();
        
    }

    /**
     * 发送刷新广播
     */
    public void sendRefresh() {
        Intent intent = new Intent(Constants.ACTION_REFRESH_LIST);
        sendBroadcast(intent);
    }
    
    private void initBeepSound()
    {
        if (playBeep && mediaPlayer == null)
        {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try
            {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            }
            catch (IOException e)
            {
                mediaPlayer = null;
            }
        }
    }
    
    private static final long VIBRATE_DURATION = 200L;
    
    private void playBeepSoundAndVibrate()
    {
        if (playBeep && mediaPlayer != null)
        {
            mediaPlayer.start();
        }
        if (vibrate)
        {
            Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
    
    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener()
    {
        public void onCompletion(MediaPlayer mediaPlayer)
        {
            mediaPlayer.seekTo(0);
        }
    };
    
    /**
     * 
     * 询问是否愿意重新扫描
     * <功能详细描述>
     * @param str
     * @param isFinish
     * @see [类、类#方法、类#成员]
     */
    private void showDialog(String retinfo)
    {
        OnKeyListener keylistener = new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        };
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dapeng_dialog, null);
        Button agree = (Button)view.findViewById(R.id.dapeng_agree);
        Button disagree = (Button)view.findViewById(R.id.dapeng_disagree);
        TextView content = (TextView)view.findViewById(R.id.content);
        final Dialog dialog = new Dialog(MipcaActivityCapture.this, R.style.main_dialog);
        dialog.setContentView(view);
        dialog.setOnKeyListener(keylistener);
        dialog.setCancelable(false);
        if (Constants.SIGN.equals(type))
        {
            if (retinfo == null || retinfo.equals("")) {
                content.setText("签到提交请求失败");
            } else {
                content.setText("签到失败：" + retinfo);
            }
        }
        else
        {
            if (retinfo == null || retinfo.equals("")) {
                content.setText("评价提交请求失败");
            } else {
                content.setText("评价失败：" + retinfo);
            }
        }
        
        agree.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                codeSign();
                dialog.dismiss();
            }
        });
        disagree.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                MipcaActivityCapture.this.finish();
                startActivity(new Intent(MipcaActivityCapture.this, MipcaActivityCapture.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /* (non-Javadoc)
     * @see com.amap.api.location.AMapLocationListener#onLocationChanged(com.amap.api.location.AMapLocation)
     */
    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Constants.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }
    }
}