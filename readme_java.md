### Java中怎么继承一个非静态的内部类？
```java
package com;

public class Outer {
    public class Inner {
        public void test() {
            ;
        }
    }
}

class Other extends Outer.Inner {
    /**
     * 一个内部类必须通过外部类的实例才能访问，所有得有一个带有外部类的参数的构造方法，并且在构造方法中需要调用外部类的super方法。
     */
    public Other(Outer outer) {
        outer.super();
    }

}

//重写的内部类不会被调用，在不同的外围类中的内部类是相互独立的实体，他们存在于自己的命名空间中，如果想要实现覆盖的话，可以直接使用继承语法，将子类的内部类继承自父类的内部类。
```