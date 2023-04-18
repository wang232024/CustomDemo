package com.example.customutil.designpattern.observer.buttonlistener;

/**
 *      按键类实现点击和长按的监听接口
 * @author wtx
 *
 */
public class Button {
        private OnClickListener mListener;
        
        public interface OnClickListener {
                public void onCLick();
                public void onLongClick();
        }
        
        public void setOnClickListener(OnClickListener listener) {
                mListener = listener;
        }
        
        public void clickonce(int i) {
                if (1 == i)
                        mListener.onCLick();
                else
                        mListener.onLongClick();
        }
        
}
