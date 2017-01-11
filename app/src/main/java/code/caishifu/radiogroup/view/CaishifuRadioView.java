package code.caishifu.radiogroup.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import code.caishifu.radiogroup.R;


/**
 * Created by wp on 2017/1/3.
 * 默认子元素没有margin值,且默认子元素大小规格一样
 * @description
 */

public class CaishifuRadioView extends ViewGroup {
    private final int COLUMN = 3;

    private final int PADDING = 10;
    /**
     * 每行个数
     */
    private int mColumn = COLUMN;
    /**
     * 左右方向的间距
     */
    private int mHorizontalPadding = PADDING;
    /**
     * 上下方向的间距
     */
    private int mVerticalPadding = PADDING;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 获取模式
     */
    private int mMode;
    /**
     * 行数
     */
    private int mLine;

    private OnItemClickListener mListener;

    public CaishifuRadioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.my_radio_style);
        //获取每行个数，默认为3
        mColumn = a.getInteger(R.styleable.my_radio_style_column, COLUMN);
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        mMode = widthMode;

        int count = getChildCount();

        if (count == 0) {
            throw new RuntimeException("child must not bu null");
        }

        //取得余数
        int c = count % mColumn;
        //获取行数
        mLine = c == 0 ? (count / mColumn) : ((count / mColumn) + 1);

        View child = getChildAt(0);
        //设置padding值
//        mHorizontalPadding = getPaddingLeft() == 0 ? PADDING : getPaddingLeft();
//        mVerticalPadding = getPaddingTop() == 0 ? PADDING : getPaddingTop();
        mHorizontalPadding = getPaddingLeft();
        mVerticalPadding = getPaddingTop();
        //设置宽
        int w = child.getMeasuredWidth() * mColumn + mHorizontalPadding * (mColumn + 1);
        if (w > widthSize) {
            w = widthSize;
        }

        //设置高
        int h = child.getMeasuredHeight() * mLine + mVerticalPadding * (mLine + 1);
        if (h > heightSize) {
            h = heightSize;
        }

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : w,
                heightMode == MeasureSpec.EXACTLY ? heightSize : h
        );
    }


    @Override
    protected void onLayout(boolean change, int l, int r, int t, int b) {
        //子元素的宽
        int width = getChildAt(0).getMeasuredWidth();
        //子元素的高
        int height = getChildAt(0).getMeasuredHeight();
        //EXACTLY
        if (mMode == MeasureSpec.EXACTLY) {
            if (getWidth() >= width * mColumn) {
                //空间足够则平分
                //计算间距
                mHorizontalPadding = (getWidth() - width * mColumn) / (mColumn + 1);
            } else {
                //空间不足够
                //重置间距为默认间距
                mHorizontalPadding = PADDING;
            }
        }
        //放置
        layoutMyChildren(width, height);

    }


    /**
     * 放置子元素位置
     * @param width
     * @param height
     */
    private void layoutMyChildren(int width, int height) {
        for (int i = 0; i < mLine; i++) {
            for (int j = 0; j < mColumn; j++) {
                int position = (i + 1) * mColumn - (mColumn - j);
                if (position == getChildCount()) {
                    return;
                }
                View v = getChildAt(((i + 1) * mColumn - (mColumn - j)));
                v.layout((mHorizontalPadding + width) * j + mHorizontalPadding,
                        mVerticalPadding * (i + 1) + height * i,
                        (mHorizontalPadding + width) * j + mHorizontalPadding + width,
                        (mVerticalPadding + height) * (i + 1));
            }

        }
        /**
         * 点击事件
         */
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            final int j = i + 1;
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    reset();
                    view.setSelected(true);
                    if (mListener != null) {
                        mListener.clickOnPosition(j);
                    }
                }
            });
        }
    }


    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }


    private void reset() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setSelected(false);
        }
    }

    /**
     * 点击回调
     */
    public interface OnItemClickListener {

        void clickOnPosition(int position);
    }
}
