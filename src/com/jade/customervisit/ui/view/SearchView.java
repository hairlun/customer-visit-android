package com.jade.customervisit.ui.view;

import com.jade.customervisit.CVApplication;
import com.jade.customervisit.R;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 搜索界面
 * 
 * @author zhoushujie
 * 
 */
public class SearchView extends LinearLayout {

    public static final String TAG = SearchView.class.getName();
    /** 搜索关键字文本框 */
    private EditText keywordEt;
    /** 搜索按钮 */
    private Button searchBtn;
    /** 取消按钮 */
    private Button cancelBtn;
    /** 清除关键字按钮 */
    private Button clearBtn;
    /** 进度条 */
    private ProgressBar searchPb;

    private OnEnterListener enterListener = new OnEnterListener() {

        @Override
        public void onEnter(String keyword) {
            Toast.makeText(getContext(), "搜索", Toast.LENGTH_SHORT).show();
        }
    };

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initListener();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search, this);
        keywordEt = (EditText) findViewById(R.id.keyword_et);
        searchBtn = (Button) findViewById(R.id.search_search_btn);
        cancelBtn = (Button) findViewById(R.id.search_cancel_btn);
        clearBtn = (Button) findViewById(R.id.clear_btn);
        searchPb = (ProgressBar) findViewById(R.id.search_pb);

        keywordEt.setTextAppearance(getContext(),
                CVApplication.cvApplication.getSettingsTextStyle());
        searchBtn.setTextAppearance(getContext(),
                CVApplication.cvApplication.getSettingsTextStyle());
        cancelBtn.setTextAppearance(getContext(),
                CVApplication.cvApplication.getSettingsTextStyle());

        hideClearBtn();
        hideProgressBar();
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        keywordEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    // 文本框中文本长度大于0，则显示清除按钮
                    showClearBtn();
                } else {
                    // 文本框中文本长度不大于0，则隐藏清除按钮
                    hideClearBtn();
                }
            }
        });

        keywordEt.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    if (enterListener != null) {
                        enterListener.onEnter(getKeyword());
                    }
                    return true;
                }
                return false;
            }
        });

        clearBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                keywordEt.setText("");
            }
        });

        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }

    /**
     * 获取搜索关键字
     * 
     * @return 搜索关键字
     */
    public String getKeyword() {
        return keywordEt.getText().toString().trim();
    }

    /**
     * 设置关键字
     * 
     * @param keyword
     */
    public void setKeyword(String keyword) {
        keywordEt.setText(keyword);
    }

    /**
     * 获取搜索文本框
     * 
     * @return 文本框对象
     */
    public EditText getEditText() {
        return keywordEt;
    }

    /**
     * 获取搜索按钮
     * 
     * @return 按钮对象
     */
    public Button getSearchBtn() {
        return searchBtn;
    }

    /**
     * 获取取消按钮
     * 
     * @return 按钮对象
     */
    public Button getCancelBtn() {
        return cancelBtn;
    }

    /**
     * 获取进度条对象
     * 
     * @return
     */
    public ProgressBar getSearchPb() {
        return searchPb;
    }

    /**
     * 获取清除按钮对象
     * 
     * @return
     */
    public Button getClearBtn() {
        return clearBtn;
    }

    /**
     * 显示清除按钮
     * 
     * @return
     */
    public SearchView showClearBtn() {
        clearBtn.setVisibility(VISIBLE);
        return this;
    }

    /**
     * 隐藏清除按钮
     * 
     * @return
     */
    public SearchView hideClearBtn() {
        clearBtn.setVisibility(GONE);
        return this;
    }

    /**
     * 显示进度条
     * 
     * @return
     */
    public SearchView showProgressBar() {
        searchPb.setVisibility(VISIBLE);
        return this;
    }

    /**
     * 隐藏进度条
     * 
     * @return
     */
    public SearchView hideProgressBar() {
        searchPb.setVisibility(GONE);
        return this;
    }

    /**
     * 搜索按钮点击监听
     * 
     * @param listener
     * @return
     */
    public SearchView setOnSearchClickListener(OnClickListener listener) {
        searchBtn.setOnClickListener(listener);
        return this;
    }

    /**
     * 取消按钮点击监听
     * 
     * @param listener
     * @return
     */
    public SearchView setOnCancelClickListener(OnClickListener listener) {
        cancelBtn.setOnClickListener(listener);
        return this;
    }

    /**
     * 清除按钮点击监听
     * 
     * @param listener
     * @return
     */
    public SearchView setOnClearClickListener(OnClickListener listener) {
        clearBtn.setOnClickListener(listener);
        return this;
    }

    /**
     * 回车键监听
     * 
     * @param enterListener
     * @return
     */
    public SearchView setOnEnterListener(OnEnterListener enterListener) {
        this.enterListener = enterListener;
        return this;
    }

    /**
     * 按下回车键时执行的
     * 
     * @author zhoushujie
     * 
     */
    public interface OnEnterListener {
        void onEnter(String keyword);
    }
}
