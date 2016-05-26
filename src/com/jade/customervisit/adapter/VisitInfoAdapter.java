package com.jade.customervisit.adapter;

import java.util.List;

import com.jade.customervisit.CVApplication;
import com.jade.customervisit.R;
import com.jade.customervisit.bean.VisitInfo;
import com.jade.customervisit.ui.view.CustomGridView;
import com.jade.customervisit.util.CommonUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * <所有拜访信息适配器>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class VisitInfoAdapter extends BaseAdapter
{
    private List<VisitInfo> data;
    
    private Context context;
    
    public VisitInfoAdapter(List<VisitInfo> data, Context context)
    {
        super();
        this.data = data;
        this.context = context;
    }

    public List<VisitInfo> getData() {
        return data;
    }

    public void setData(List<VisitInfo> data) {
        this.data = data;
    }
    
    public void addData(List<VisitInfo> data) {
        this.data.addAll(data);
    }

    @Override
    public int getCount()
    {
        return data == null ? 0 : data.size();
    }
    
    @Override
    public Object getItem(int arg0)
    {
        return null;
    }
    
    @Override
    public long getItemId(int arg0)
    {
        return 0;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.item_visit_info, null);
            viewHolder.nickName = (TextView)convertView.findViewById(R.id.nick_name);
            viewHolder.praise = (TextView)convertView.findViewById(R.id.praise);
            viewHolder.arriveTime = (TextView)convertView.findViewById(R.id.arrive_time);
            viewHolder.leaveTime = (TextView)convertView.findViewById(R.id.leave_time);
            viewHolder.city = (TextView)convertView.findViewById(R.id.city);
            viewHolder.line = (ImageView)convertView.findViewById(R.id.line);
            viewHolder.photoGridview = (CustomGridView)convertView.findViewById(R.id.photo_gridview);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.nickName.setText(data.get(position).getUsername());
        viewHolder.praise.setText("0".equals(data.get(position).getPraise()) ? "未评价" : "满意");
        viewHolder.arriveTime.setText(data.get(position).getArriveTime());
        viewHolder.leaveTime.setText(data.get(position).getLeaveTime());
        viewHolder.city.setText(data.get(position).getCity());
        if (data.get(position).getImageList() != null && data.get(position).getImageList().size() > 0)
        {
            viewHolder.photoGridview.setVisibility(View.VISIBLE);
            viewHolder.line.setVisibility(View.VISIBLE);
            viewHolder.photoGridview.setAdapter(new PhotoAdapter(data.get(position).getImageList()));
        }
        else
        {
            viewHolder.photoGridview.setVisibility(View.GONE);
            viewHolder.line.setVisibility(View.GONE);
        }
        return convertView;
    }
    
    class ViewHolder
    {
        private TextView nickName;
        
        private TextView praise;
        
        private TextView arriveTime;
        
        private TextView leaveTime;
        
        private TextView city;
        
        private CustomGridView photoGridview;
        
        private ImageView line;
    }
    
    /**
     * 图片gridview的adapter
     */
    
    class PhotoAdapter extends BaseAdapter
    {
        
        protected List<String> photos;
        
        public PhotoAdapter(List<String> photos)
        {
            this.photos = photos;
        }
        
        @Override
        public int getCount()
        {
            return photos == null ? 0 : photos.size();
        }
        
        @Override
        public Object getItem(int position)
        {
            return photos.get(position);
        }
        
        @Override
        public long getItemId(int position)
        {
            return position;
        }
        
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            PhotoViewHolder photoViewHolder;
            if (null == convertView)
            {
                photoViewHolder = new PhotoViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_visit_info_photo, null);
                photoViewHolder.pic = (ImageView)convertView.findViewById(R.id.group_dynamic_photo);
                LinearLayout.LayoutParams linearParams =
                    (LinearLayout.LayoutParams)photoViewHolder.pic.getLayoutParams(); //取控件textView当前的布局参数  
                linearParams.width =
                    (context.getResources().getDisplayMetrics().widthPixels - CommonUtils.dip2px(context, 44)) / 3;// 控件的高强制设成20
                linearParams.height = linearParams.width;
                photoViewHolder.pic.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                convertView.setTag(photoViewHolder);
            }
            else
            {
                photoViewHolder = (PhotoViewHolder)convertView.getTag();
            }
            ImageLoader.getInstance().displayImage(photos.get(position),
                photoViewHolder.pic,
                CVApplication.setAllDisplayImageOptions(context, "default_load", "default_load", "default_load"));
            return convertView;
        }
    }
    
    class PhotoViewHolder
    {
        private ImageView pic;
    }
    
}
