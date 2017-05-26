package net.faceiraq.and_faceiraq.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by user on 19.04.2017.
 */

public class FaceIraqEditText extends AppCompatEditText {

    private Context mContext;

    public FaceIraqEditText(Context context)
    {
        super(context);
        this.mContext = context;
        init();
    }

    public FaceIraqEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public FaceIraqEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    private void init() {}

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
            this.setVisibility(GONE);
            this.clearFocus();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }



}
