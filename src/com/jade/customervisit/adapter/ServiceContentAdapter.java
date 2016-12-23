package com.jade.customervisit.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.jade.customervisit.R;
import com.jade.customervisit.bean.ServiceContent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * <服务内容适配器> <功能详细描述>
 * 
 * @author cyf
 * @version [版本号, 2014-11-17]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ServiceContentAdapter extends BaseAdapter {

    private static final SimpleDateFormat DATE_SDF1 = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    private static final SimpleDateFormat DATE_SDF2 = new SimpleDateFormat(
            "yyyy.MM.dd");

    private List<ServiceContent> data;

    private Context context;

    public ServiceContentAdapter(List<ServiceContent> data, Context context) {
        super();
        this.data = data;
        this.context = context;
    }

    public List<ServiceContent> getData() {
        return data;
    }

    public void setData(List<ServiceContent> data) {
        this.data = data;
    }

    public void addData(List<ServiceContent> data) {
        this.data.addAll(data);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView,
            ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context)
                    .inflate(R.layout.item_service_content, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(data.get(position).getTitle());
        viewHolder.name.setText(data.get(position).getCustomerName());
        try {
            viewHolder.time.setText(DATE_SDF2.format(
                    DATE_SDF1.parse(data.get(position).getCreateTime())));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    class ViewHolder {
        private TextView title;
        private TextView name;
        private TextView time;
    }

}
