package com.jade.customervisit.ui.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jade.customervisit.R;
import com.jade.customervisit.ui.BaseActivity;
import com.jade.customervisit.ui.view.TitleBarView;
import com.jade.customervisit.util.Constants;

public class ServiceSearchActivity extends BaseActivity implements OnClickListener {

    /** 标题栏 */
    private TitleBarView mTitleBar;

    /** 关键字 */
    private EditText mKeywordEt;

    /** 滚动条 */
    private ScrollView mSv;

    /** 搜索按钮 */
    private Button mSearchBtn;
    
    private String keyword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_search);
		keyword = getIntent().getStringExtra("keyword");
		if (keyword == null) {
			keyword = "";
		}
		initView();
		initData();
	}

	private void initView() {
	    mTitleBar = (TitleBarView)findViewById(R.id.service_search_titlebar);
	    mTitleBar.hideSearchBtn();
		mKeywordEt = (EditText)findViewById(R.id.service_search_keyword_edit);
		mSearchBtn = (Button)findViewById(R.id.search);
		mSearchBtn.setOnClickListener(this);
	}

	private void initData() {
		mKeywordEt.setText(keyword);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.putExtra(Constants.KEYWORD, mKeywordEt.getText().toString());
		setResult(RESULT_OK, intent);
		finish();
	}

}
