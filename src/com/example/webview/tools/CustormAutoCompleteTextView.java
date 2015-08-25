package com.example.webview.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class CustormAutoCompleteTextView extends AutoCompleteTextView {
	 
    private Context mContext;

    public CustormAutoCompleteTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
            this.mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);

            this.setFocusable(true);
            this.setBackgroundColor( Color.WHITE );
            Paint mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);        //        ����style
            mPaint.setAntiAlias(true); // �����
            mPaint.setStrokeWidth( 10 ); // ���ñ߿���
            mPaint.setColor(Color.parseColor("#02b9f5")); // ���ñ߿���ɫ
             
             
            canvas.drawRect( new Rect(0, 0, getWidth(), getHeight()), mPaint);

    }

}
