package com.example.custom.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.example.custom.span.ISpanConfig;
import com.example.custom.span.SpanColor;
import com.example.custom.span.SpanManager;

import com.example.custom.R;

import java.util.List;

public class EditTextActivity extends AppCompatActivity {
    private static final String TAG = "wtx_EditTextActivity";
    private EditText editText;
    private EditText et_color;
    private Button btn_italic;
    private Button btn_bold;
    private Button btn_line;
    private Button btn_color;
    private Button btn_size;
    private Button btn_test;
    private boolean alreadyBold = false;
    private boolean alreadyItalic = false;
    private boolean alreadyLine = false;
    private SpanManager mSpanManager;
    private int testColor = 0;
    private String text_test = "来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看看我用途懂得记录特咳咳咳咳咯了解了解透来看";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edittextactivity);

        initData();

        editText = findViewById(R.id.edittext_content);
        btn_bold = findViewById(R.id.btn_bold);
        btn_italic = findViewById(R.id.btn_italic);
        btn_line = findViewById(R.id.btn_line);
        btn_color = findViewById(R.id.btn_color);
        btn_size = findViewById(R.id.btn_size);
        btn_test = findViewById(R.id.btn_test);
        et_color = findViewById(R.id.et_color);

        btn_bold.setOnClickListener(onClickListener);
        btn_italic.setOnClickListener(onClickListener);
        btn_line.setOnClickListener(onClickListener);
        btn_color.setOnClickListener(onClickListener);
        btn_size.setOnClickListener(onClickListener);
        btn_test.setOnClickListener(onClickListener);

        editText.setGravity(Gravity.TOP | Gravity.LEFT);

        editText.setText(text_test);
        for (int i = 0; i < 3; i++) {
            SpanColor iSpanConfig = new SpanColor(Color.RED);
            iSpanConfig.setSpanConfigType(ISpanConfig.COLOR);
            ((SpannableStringBuilder) editText.getText()).setSpan(iSpanConfig, i * 10 + 0, i * 10 + 8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        et_color.setText("2");

        mSpanManager = SpanManager.getInstance();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e(TAG, "beforeTextChanged, start:" + start + ", count:" + count + ", after:" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "onTextChanged, start:" + start + ", count:" + count + ", before:" + before);
//                mSpanManager.fixError(start, count);
                // TODO 新输入的数据进行span设置，鼠标处直接设置span不再直接添加，改为输入数据后对数据的span进行设置
                mSpanManager.addCursorTextSpan(s, start, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "afterTextChanged");
            }
        });

        EditText pwd_et = findViewById(R.id.pwd_et);
        CheckBox pwd_eye = findViewById(R.id.pwd_eye);

        pwd_eye.setBackgroundResource(R.drawable.selector_pwd_eye);
        pwd_eye.setChecked(false);

        pwd_eye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //设置密码为明文，并更改眼睛图标
                    pwd_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置密码为暗文，并更改眼睛图标
                    pwd_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                //设置光标位置的代码需放在设置明暗文的代码后面
                pwd_et.setSelection(pwd_et.getText().toString().length());
            }
        });

        String[] data = new String[]{
                "aaaaa", "bbbbbb", "cccccccc", "dddddd", "eeeeeeee"
        };

        AppCompatAutoCompleteTextView appCompatAutoCompleteTextView = findViewById(R.id.autocompletetextview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, data);
        appCompatAutoCompleteTextView.setAdapter(adapter);

        MultiAutoCompleteTextView matv_content = findViewById(R.id.multiautocompletetextview);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, data);
        matv_content.setAdapter(adapter);
        matv_content.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private void initData() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (null != uri) {
            Log.i(TAG, "initData, uri:" + uri.toString());
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            int end = editText.getSelectionEnd();
            Log.i(TAG, "click, [" + start + ", " + end + "]");
            switch (v.getId()) {
                case R.id.btn_bold:
                    if (!alreadyBold) {
                        alreadyBold = true;
                        mSpanManager.setSpanConfig(editText, ISpanConfig.BOLD, start, end, 1);
                        btn_bold.setBackgroundColor(getResources().getColor(R.color.def_100, null));
                    } else {
                        alreadyBold = false;
                        mSpanManager.setSpanConfig(editText, ISpanConfig.BOLD, start, end, 0);
                        btn_bold.setBackgroundColor(getResources().getColor(R.color.white, null));
                    }
                    break;
                case R.id.btn_italic:
                    if (!alreadyItalic) {
                        alreadyItalic = true;
                        mSpanManager.setSpanConfig(editText, ISpanConfig.ITALIC, start, end, 1);
                        btn_italic.setBackgroundColor(getResources().getColor(R.color.def_100, null));
                    } else {
                        alreadyItalic = false;
                        mSpanManager.setSpanConfig(editText, ISpanConfig.ITALIC, start, end, 0);
                        btn_italic.setBackgroundColor(getResources().getColor(R.color.white, null));
                    }
                    break;
                case R.id.btn_line:
//                    if (!alreadyLine) {
//                        alreadyLine = true;
//                        mSpanManager.setSpanConfig(editText, ISpanConfig.LINE, start, end, 1);
//                        btn_line.setBackgroundColor(getResources().getColor(R.color.def_100, null));
//                    } else {
//                        alreadyLine = false;
//                        mSpanManager.setSpanConfig(editText, ISpanConfig.LINE, start, end, 0);
//                        btn_line.setBackgroundColor(getResources().getColor(R.color.white, null));
//                    }
                    mSpanManager.setSpanConfig(editText, ISpanConfig.LINE, start, end, 1);
                    break;
                case R.id.btn_color:
                    if (et_color.getText().toString().equals("0")) {
                        mSpanManager.setSpanConfig(editText, ISpanConfig.COLOR, start, end, Color.RED);
                    } else if (et_color.getText().toString().equals("1")) {
                        mSpanManager.setSpanConfig(editText, ISpanConfig.COLOR, start, end, Color.GREEN);
                    } else if (et_color.getText().toString().equals("2")) {
                        mSpanManager.setSpanConfig(editText, ISpanConfig.COLOR, start, end, Color.BLUE);
                    } else if (et_color.getText().toString().equals("3")) {
                        mSpanManager.setSpanConfig(editText, ISpanConfig.COLOR, start, end, Color.YELLOW);
                    }
                    break;
                case R.id.btn_size:
                    mSpanManager.setSpanConfig(editText, ISpanConfig.SIZE, start, end,1);

                    break;
                case R.id.btn_test:
                    mSpanManager.test(editText);
                    break;
            }
        }
    };

}
