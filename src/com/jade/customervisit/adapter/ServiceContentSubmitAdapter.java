package com.jade.customervisit.adapter;

import java.util.List;

import com.jade.customervisit.R;
import com.jade.customervisit.bean.ContentItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
public class ServiceContentSubmitAdapter extends BaseAdapter {
    private List<ContentItem> data;

    private Context context;

    public ServiceContentSubmitAdapter(List<ContentItem> data,
            Context context) {
        super();
        this.data = data;
        this.context = context;
    }

    public List<ContentItem> getData() {
        return data;
    }

    public void setData(List<ContentItem> data) {
        this.data = data;
    }

    public void addData(List<ContentItem> data) {
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
                    .inflate(R.layout.item_content, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.select = (CheckBox) convertView
                    .findViewById(R.id.select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(data.get(position).getName());
        if ("0".equals(data.get(position).getIsClick())) {
            viewHolder.select.setChecked(false);
        } else {
            viewHolder.select.setChecked(true);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView title;

        private CheckBox select;
    }

}
