package com.jade.customervisit.ui.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jade.customervisit.CVApplication;
import com.jade.customervisit.R;
import com.jade.customervisit.bean.PicturePropertiesBean;
import com.jade.customervisit.bean.SubmitResult;
import com.jade.customervisit.bll.ServiceManager;
import com.jade.customervisit.network.RequestListener;
import com.jade.customervisit.scancode.MipcaActivityCapture;
import com.jade.customervisit.ui.BaseActivity;
import com.jade.customervisit.util.BitmapUtil;
import com.jade.customervisit.util.CommonUtils;
import com.jade.customervisit.util.Constants;
import com.jade.customervisit.util.FileSystemManager;
import com.jade.customervisit.util.ToastUtil;
import com.lidroid.xutils.http.HttpHandler;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * <服务结果痕迹> <功能详细描述>
 * 
 * @author cyf
 * @version [版本号, 2014-11-14]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TraceServiceActivity extends BaseActivity implements OnClickListener
{
    /**
     * 返回按钮
     */
    private Button back;
    
    /**
     * 标题
     */
    private TextView title;
    
    /**
     * 提交按钮
     */
    private Button submit;
    
    /**
     * 选择图片-第一行
     */
    private LinearLayout selectPhotoOne;
    
    /**
     * 选择图片-第二行
     */
    private LinearLayout selectPhotoTwo;
    
    /**
     * 拍摄图片的url地址
     */
    private String photoPath;
    
    /**
     * 缩放尺寸
     */
    protected static final float IMG_SCALE = 640f;
    
    private String serviceId;
    
    /**
     * url列表数据
     */
    private ArrayList<String> paths = new ArrayList<String>();
    
    HttpHandler<String> submitHttpHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trace_service);
        
        init();
        initData();
    }
    
    /**
     * 
     * <初始化布局组件> <功能详细描述>
     * 
     * @see [类、类#方法、类#成员]
     */
    private void init()
    {
        back = (Button)findViewById(R.id.title_btn_back);
        title = (TextView)findViewById(R.id.title_name);
        submit = (Button)findViewById(R.id.title_btn_confirm);
        selectPhotoOne = (LinearLayout)findViewById(R.id.select_photo_one);
        selectPhotoTwo = (LinearLayout)findViewById(R.id.select_photo_two);
        
        /**
         * 添加按钮点击事件
         */
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        title.setText("服务结果痕迹");
        submit.setText("提交");
        serviceId = getIntent().getStringExtra("serviceId");
        refreshImageView();
    }
    
    /**
     * 
     * <获取头像>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void showPhotoDiaLog()
    {
        photoPath = System.currentTimeMillis() + ".jpg";
        final Dialog imageSelectDialog = new Dialog(TraceServiceActivity.this, R.style.image_select_dialog);
        imageSelectDialog.setContentView(R.layout.image_select_dialog);
        imageSelectDialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
        imageSelectDialog.getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
        imageSelectDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        imageSelectDialog.getWindow().setWindowAnimations(R.style.dialog_style);
        Button camera = (Button)imageSelectDialog.findViewById(R.id.camera);
        Button gallery = (Button)imageSelectDialog.findViewById(R.id.gallery);
        Button cancel = (Button)imageSelectDialog.findViewById(R.id.cancel);
        imageSelectDialog.show();
        /**
         * 从相册选取照片
         */
        gallery.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                imageSelectDialog.dismiss();
                Intent intent =
                    new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Constants.NUM2);
            }
        });
        /**
         * 从相机拍摄照片
         */
        camera.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // 先验证手机是否有sdcard
                imageSelectDialog.dismiss();
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    try
                    {
                        photoPath = FileSystemManager.getTemporaryPath(TraceServiceActivity.this) + photoPath;
                        File picture = new File(photoPath);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageFileUri = Uri.fromFile(picture);
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                        startActivityForResult(intent, Constants.NUM1);
                    }
                    catch (ActivityNotFoundException e)
                    {
                        Toast.makeText(TraceServiceActivity.this, "没有找到储存目录", 1).show();
                    }
                }
                else
                {
                    Toast.makeText(TraceServiceActivity.this, "没有储存卡", 1).show();
                }
            }
        });
        cancel.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View arg0)
            {
                imageSelectDialog.dismiss();
                
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        String zoomPath =
            FileSystemManager.getPostPath(TraceServiceActivity.this) + System.currentTimeMillis() + ".jpg";
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case Constants.NUM1:
                    BitmapUtil.getImageScaleByPath(new PicturePropertiesBean(photoPath, zoomPath, IMG_SCALE, IMG_SCALE),
                        TraceServiceActivity.this);
                    paths.add(zoomPath);
                    refreshImageView();
                    break;
                // 直接从相册获取
                case Constants.NUM2:
                    if (data != null)
                    {
                        Uri originalUri = data.getData();
                        // 从媒体db中查询图片路径
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                        if (cursor != null)
                        {
                            int col = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            cursor.moveToFirst();
                            photoPath = cursor.getString(col);
                            cursor.close();
                            if (isTodayPhoto(photoPath))
                            {
                                BitmapUtil.getImageScaleByPath(new PicturePropertiesBean(photoPath, zoomPath,
                                    IMG_SCALE, IMG_SCALE), TraceServiceActivity.this);
                                paths.add(zoomPath);
                                refreshImageView();
                            }
                            else
                            {
                                Toast.makeText(TraceServiceActivity.this, "服务结果痕迹只能选择当天的照片,请重新选择.", 1).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(TraceServiceActivity.this, "获取图片失败", 1).show();
                        }
                        
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * 
     * <是否是今天的照片>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private boolean isTodayPhoto(String path)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        File file = new File(photoPath);
        Date date = new Date(file.lastModified());
        String photoTime = dateFormat.format(date);
        
        Date today = new Date();
        String todayTime = dateFormat.format(today);
        if (photoTime.substring(0, 4).equals(todayTime.substring(0, 4)))
        {
            if (photoTime.substring(4, 6).equals(todayTime.substring(4, 6)))
            {
                if (photoTime.substring(6, 8).equals(todayTime.substring(6, 8)))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 
     * <刷新界面>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void refreshImageView()
    {
        selectPhotoOne.removeAllViews();
        selectPhotoTwo.removeAllViews();
        if (paths.size() > 0)
        {
            for (int i = 0; i < paths.size(); i++)
            {
                ImageView pic = new ImageView(TraceServiceActivity.this);
                pic.setScaleType(ScaleType.CENTER_CROP);
                if (i < 3)
                {
                    selectPhotoOne.addView(pic);
                }
                else
                {
                    selectPhotoTwo.addView(pic);
                }
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)pic.getLayoutParams(); //取控件textView当前的布局参数  
                linearParams.width =
                    (TraceServiceActivity.this.getResources().getDisplayMetrics().widthPixels - CommonUtils.dip2px(TraceServiceActivity.this,
                        54)) / 3;
                linearParams.height = linearParams.width;
                linearParams.setMargins(0, 0, CommonUtils.dip2px(TraceServiceActivity.this, 7), 0);
                pic.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                pic.setImageBitmap(BitmapFactory.decodeFile(paths.get(i)));
            }
        }
        if (paths.size() < 3)
        {
            ImageView pic = new ImageView(TraceServiceActivity.this);
            pic.setScaleType(ScaleType.CENTER_CROP);
            selectPhotoOne.addView(pic);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)pic.getLayoutParams(); //取控件textView当前的布局参数  
            linearParams.width =
                (TraceServiceActivity.this.getResources().getDisplayMetrics().widthPixels - CommonUtils.dip2px(TraceServiceActivity.this,
                    54)) / 3;
            linearParams.height = linearParams.width;
            linearParams.setMargins(0, 0, CommonUtils.dip2px(TraceServiceActivity.this, 7), 0);
            pic.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            pic.setImageResource(R.drawable.home_circles_post_pick);
            pic.setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View arg0)
                {
                    showPhotoDiaLog();
                }
            });
        }
        else if (paths.size() < 6)
        {
            ImageView pic = new ImageView(TraceServiceActivity.this);
            pic.setScaleType(ScaleType.CENTER_CROP);
            selectPhotoTwo.addView(pic);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)pic.getLayoutParams(); //取控件textView当前的布局参数  
            linearParams.width =
                (TraceServiceActivity.this.getResources().getDisplayMetrics().widthPixels - CommonUtils.dip2px(TraceServiceActivity.this,
                    54)) / 3;
            linearParams.height = linearParams.width;
            linearParams.setMargins(0, 0, CommonUtils.dip2px(TraceServiceActivity.this, 7), 0);
            pic.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            pic.setImageResource(R.drawable.home_circles_post_pick);
            pic.setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View arg0)
                {
                    showPhotoDiaLog();
                }
            });
        }
        
    }
    
    /**
     * 
     * <登录接口>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void photoSign()
    {
        RequestListener<SubmitResult> callback = new RequestListener<SubmitResult>() {

            @Override
            public void onStart() {
                loadingDialogShow();
            }

            @Override
            public void onSuccess(int statusCode, SubmitResult result) {
                if (result != null) {
                    if (result.isSuccesses()) {
                        ToastUtil.showShort(context, "提交成功");
                        finish();
                    } else {
                        ToastUtil.showShort(context, result.getRetinfo());
                    }
                } else {
                    ToastUtil.showShort(context, R.string.connect_exception);
                }
            }

            @Override
            public void onFailure(Exception e, String error) {
                ToastUtil.showShort(context, R.string.connect_exception);
            }

            @Override
            public void onStopped() {
                loadingDialogDismiss();
            }
        };
        List<File> files = new ArrayList<File>();
        for (String path : paths)
        {
            files.add(new File(path));
        }
        submitHttpHandler = ServiceManager.photoSign(serviceId, files, callback);

//        loadingDialogShow();
//        Map<String, List<File>> fileParameters = new HashMap<String, List<File>>();
//        List<File> files = new ArrayList<File>();
//        for (String path : paths)
//        {
//            files.add(new File(path));
//        }
//        
//        fileParameters.put("file", files);
//        
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("userId", CVApplication.cvApplication.getUserid());
//        param.put("serviceId", serviceId);
//        ConnectService.instance().connectServiceUploadFile(TraceServiceActivity.this,
//            param,
//            fileParameters,
//            TraceServiceActivity.this,
//            BaseResponse.class,
//            /*URLUtil.PHOTO_SIGN*/URLUtil.SUBMIT_SERVICE_RESULT);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        /**
         * 响应返回按钮事件
         */
            case R.id.title_btn_back:
                if (paths.size() > 0)
                {
                    showDialog();
                }
                else
                {
                    finish();
                }
                break;
            /**
             * 响应提交按钮事件
             */
            case R.id.title_btn_confirm:
                if (paths.size() == 0)
                {
                    Toast.makeText(TraceServiceActivity.this, "请至少选择一张图片!", 1).show();
                }
                else
                {
                    photoSign();
                }
                break;
            
            default:
                break;
        }
    }
    
    /**
     * 
     * <退出框>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void showDialog()
    {
        AlertDialog.Builder builder = new Builder(this);
        builder.setTitle("提示").setMessage("图片未上传,是否离开？").setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                
            }
        }).create().show();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && paths.size() > 0)
        {
            showDialog();
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public void netBack(Object ob)
    {
//        super.netBack(ob);
//        dailog.dismissDialog();
//        if (ob instanceof BaseResponse)
//        {
//            BaseResponse response = (BaseResponse)ob;
//            if (GeneralUtils.isNotNullOrZeroLenght(response.getRetcode()))
//            {
//                if (Constants.SUCESS_CODE.equals(response.getRetcode()))
//                {
//                    Toast.makeText(TraceServiceActivity.this, "时间戳签到成功", 1).show();
//                    finish();
//                }
//                else
//                {
//                    Toast.makeText(TraceServiceActivity.this, response.getRetinfo(), 1).show();
//                }
//            }
//            else
//            {
//                Toast.makeText(TraceServiceActivity.this, "请求失败，请稍后再试", 1).show();
//            }
//        }
    }

    /**
     * 释放登录等待框
     */
    private void loadingDialogDismiss() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 显示登录等待框
     */
    private void loadingDialogShow() {
        if (!loadingDialog.isShowing()) {
            loadingDialog.show("正在加载…");
        }
    }
}
