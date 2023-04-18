package com.example.customutil.designpattern.factory.abstractfactory;

import com.example.customutil.designpattern.factory.factoryinterface.IDisplayCold;
import com.example.customutil.designpattern.factory.factoryinterface.IDisplayHot;

/**
 *      依然是将setStyle()方法设置为抽象方法，针对冷色调和暖色调。
 *      总结：
 *              1.针对不同的属性特点设置接口，如冷色调接口，暖色调接口。
 *              2.抽象工厂提供抽象方法供具体工厂实现，抽象方法返回类型为相应的接口。
 *              3.具体的工厂需要实现实现所有的接口，提供产品方法，返回类型为相应的接口。
 *              4.具体的产品针对接口进行实现，如字体暖色调。
 *          
 *       比较：
 *      1.工厂方法只实现一个接口，易于扩展。工厂方法能给所有属性都提供一种属性值。
 *      
 *      2.抽象工厂方法其实就是工厂方法实现多个接口。抽象工厂方法能给所有属性都提供多套属性值。
 * @author wtx
 *
 */
public abstract class AbstractFactory {
        protected abstract IDisplayCold setStyleCold();
        protected abstract IDisplayHot setStyleHot();
}
