package com.example.customutil.designpattern.factory.simplefactory;

import com.example.customutil.designpattern.factory.factoryinterface.IDisplay;

/**
 *      静态方法
 *      简单工厂负责设置所有的style属性：background, textsize, textcolor，可以完成所有的属性设置
 *      现在如果要添加一个属性：font，那么就需要对简单工厂的代码进行修改。
 * @author wtx
 *
 */
public class SimpleStyleFactory {
        
        public static IDisplay setStyle(String styleName) {
                IDisplay display = null;

                if ("textsize".equals(styleName)) {
                        display = new Textsize();
                } else if ("background".equals(styleName)) {
                        display = new Background();
                } else if ("textcolor".equals(styleName)) {
                        display = new Textcolor();
                } else {
                        throw new IllegalArgumentException("no this style");
                }
                
                return display;
        }
}
