/**
 * CustomerVisit
 * WorkflowTodoAdapter
 * zhoushujie
 * 2016年12月8日 下午9:58:27
 */
package com.jade.customervisit.adapter;

import java.util.List;

import com.jade.customervisit.R;
import com.jade.customervisit.bean.Workflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author zhoushujie
 *
 */
public class WorkflowTodoAdapter extends BaseAdapter {

    private List<Workflow> data;

    private Context context;

    /**
     * @param data
     * @param context
     */
    public WorkflowTodoAdapter(List<Workflow> data, Context context) {
        super();
        this.setData(data);
        this.context = context;
    }

    public List<Workflow> getData() {
        return data;
    }

    public void setData(List<Workflow> data) {
        this.data = data;
    }

    public void addData(List<Workflow> data) {
        this.data.addAll(data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context)
                    .inflate(R.layout.item_workflow_todo, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Workflow workflow = data.get(position);
        viewHolder.title.setText(workflow.getDescription());
        viewHolder.name.setText(workflow.getCustomer().getName());
        viewHolder.time.setText(workflow.getHandleTime());
        return convertView;
    }

    class ViewHolder {
        private TextView title;
        private TextView name;
        private TextView time;
    }

}
