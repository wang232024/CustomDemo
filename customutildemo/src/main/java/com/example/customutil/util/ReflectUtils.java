package com.example.customutil.util;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 为了使用类而做的准备工作一般有以下3个步骤:
 * 1.加载：由类加载器完成，找到对应的字节码，创建一个Class对象
 * 2.链接：验证类中的字节码，为静态域分配空间
 * 3.初始化：如果该类有超类，则对其初始化，执行静态初始化器和静态初始化块
 *
 *  如果不知道某个对象的确切类型，RTTI可以告诉你，但是有一个前提：这个类型在编译时必须已知，这样才能使用RTTI来识别它。
 *  Class类与java.lang.reflect类库一起对反射进行了支持，该类库包含Field、Method和Constructor类，这些类的对象由JVM在启动时创建，用以表示未知类里对应的成员。
 *  这样的话就可以使用Contructor创建新的对象，用get()和set()方法获取和修改类中与Field对象关联的字段，用invoke()方法调用与Method对象关联的方法。
 *  另外，还可以调用getFields()、getMethods()和getConstructors()等许多便利的方法，以返回表示字段、方法、以及构造器对象的数组，这样，对象信息可以在运行时被完全确定下来，而在编译时不需要知道关于类的任何事情。
 *  反射机制并没有什么神奇之处，当通过反射与一个未知类型的对象打交道时，JVM只是简单地检查这个对象，看它属于哪个特定的类。
 *  因此，那个类的.class对于JVM来说必须是可获取的，要么在本地机器上，要么从网络获取。所以对于RTTI和反射之间的真正区别只在于：
 *  RTTI，编译器在编译时打开和检查.class文件
 *  反射，运行时打开和检查.class文件
 */
public class ReflectUtils {
    private final static String TAG = "wtx_ReflectUtils";

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ReflectUtils() {
    }

    public static void testReflectUtils() {
        try {
            // 1.获取类型
            // 该方法会自动初始化该Class对象
            Class c1 = Class.forName("com.example.wtx.ReflectUtils");
            // 该方法不会自动初始化该Class对象
            Class c2 = ReflectUtils.class;

            ReflectUtils h = new ReflectUtils();
            Class c3 = h.getClass();

            // 2.创建此Class对象所表示的一个新实例
            Object o = c3.newInstance(); // 调用了HashMapUtils的无参构造方法。

            // 特定属性。
            Field[] fs = c3.getDeclaredFields();
            Field idF = c3.getDeclaredField("id");
            // 打破封装
            idF.setAccessible(true);
            idF.set(o, 110);
            Log.e(TAG, "id: " + idF.get(o));

            // 3.获取属性：分为所有的属性和指定的属性。
            // 所有的属性。
            // 定义可变长的字符串，用来存储属性。
            StringBuffer sb = new StringBuffer();
            sb.append(Modifier.toString(c3.getModifiers()) + " class "
                    + c3.getSimpleName() + "{\n");
            for (Field field : fs) {
                sb.append("\t");
                sb.append(Modifier.toString(field.getModifiers()) + " "); //获取属性的修饰符，例如public，static等。
                sb.append(field.getType().getSimpleName() + " "); //属性的类型的名字
                sb.append(field.getName() + ";\n"); //属性的名字+回车
            }
            sb.append("}");
            Log.e(TAG, "sb: " + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class PrintT {
        public PrintT() {
        }

        public PrintT(int x) {
            Log.e(TAG, x + " : ");
        }
    }

    public void testObject() {
        try {
            Object obj = getObject("com.custom.util.ReflectUtils");
            Object obj1 = getObjectFromInnerClass("com.custom.util.ReflectUtils$PrintT", ReflectUtils.class); // 内部类
            getField("com.custom.bean.Device");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过类名调用空参构造函数获取实例对象。
     * @param className
     * @return
     */
    public static Object getObject(String className) throws Exception {
        Object obj = null;

        Class clazz = Class.forName(className);
        if (clazz != null) {
            // 空参构造函数
            Constructor constructor = clazz.getDeclaredConstructor(
                    new Class[] {int.class});
            if (constructor != null) {
                obj = constructor.newInstance(10);
            }
        }

        return obj;
    }

    /**
     * 1.通过 内部类的类名 调用空参构造函数获取实例对象,用$进行连接。
     *  如com.custom.util.ReflectUtils.PrintT
     *  Class clazz = Class.forName(com.custom.util.ReflectUtils$PrintT);
     *
     *  获取内部类构造方法时，需要将其外部类的类对象作为参数传进去
     *   Constructor constructor = c.getDeclaredConstructor(new Class[] {A.class, String.class});
     *  同样实例化内部类时，也需要将外部类对象作为参数传进去
     *  obj = constructor.newInstance(new A(), "aaa");
     *
     * @param className 完整路径            ：     com.custom.util.ReflectUtils$PrintT
     * @param cl        外部类的class对象   ：     ReflectUtils.class
     * @return
     */
    public static Object getObjectFromInnerClass(String className, Class cl) throws Exception {
        String[] str = className.split("\\$");
        Object obj = null;

        Class clazz = Class.forName(className);
        if (clazz != null) {
            // 空参构造函数
            Constructor constructor = clazz.getDeclaredConstructor(
                    new Class[] {cl, int.class});
            if (constructor != null) {
                obj = constructor.newInstance(getObject(str[0]), 20);
            }
        }

        return obj;
    }

    public static Field[] getField(String className) throws Exception {
        Class clazz = Class.forName(className);

        Field[] fields = clazz.getDeclaredFields();
        Log.e(TAG, "fields: " + fields);
        for (Field field : fields) {
            Log.e(TAG, "field: " + field);
            //取消语言访问检查
        }

        return fields;
    }

//    public static List<Field> getOrderedField(Field[] fields) {
//        // 用来存放所有的属性域
//        List<Field> fieldList = new ArrayList<>();
//        // 过滤带有标签的Field
//        for (Field field : fields) {
//            if (field.getAnnotation(BeanFieldAnnotation.class) != null) {
//                Log.e(TAG, "order: " + field.getAnnotation(BeanFieldAnnotation.class).order());
//                fieldList.add(field);
//            }
//        }
//
////        // 这个比较排序的语法依赖于java1.8
////        fieldList.sort(Comparator.comparingInt(
////                m -> m.getAnnotation(BeanFieldAnnotation.class).order()
////        ));
//
//        return fieldList;
//    }

    /**
     * getFields            :   获取指定类及其父类(以及父类的父类)中的公有权限的字段
     * getDeclaredFields    :   获取指定类中的字段
     */
    public void testAccess() {
        try {
            Class cls = Class.forName("com.example.customutil.util.ReflectSon");
            Field[] fields1 = cls.getFields();
            for (int i = 0; i < fields1.length; i++) {
                Log.i(TAG, i + "--1-->" + fields1[i].getName());
            }
            Log.w(TAG, "=========================");
            Field[] fields2 = cls.getDeclaredFields();
            for (int i = 0; i < fields2.length; i++) {
                Log.i(TAG, i + "--2-->" + fields2[i].getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射访问普通字段
     * @param reflectBean
     */
    public void testNormalMember(ReflectBean reflectBean) {
        try {
            Class cls = Class.forName("com.example.customutil.util.ReflectBean");
            Field field = cls.getDeclaredField("name");

            field.setAccessible(true);
            Log.i(TAG, "testNormalMember, name:" + field.get(reflectBean));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射访问静态字段
     */
    public void testStaticMember() {
        try {
            Class cls = Class.forName("com.example.customutil.util.ReflectBean");
            Field field = cls.getDeclaredField("num");

            field.setAccessible(true);
            Log.i(TAG, "testNormalMember, num:" + field.get(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射调用普通无参方法
     * @param reflectBean
     */
    public void testNormalVoid(ReflectBean reflectBean) {
        try {
//            Class cls = ReflectBean.class;
            Class cls = Class.forName("com.example.customutil.util.ReflectBean");
            Method method = cls.getMethod("getName");

//            ReflectBean reflectBean = new ReflectBean();
            String name = (String) method.invoke(reflectBean);

            Log.i(TAG, "testNormalVoid, name:" + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射调用普通有参方法
     * @param reflectBean
     */
    public void testNormalString(ReflectBean reflectBean) {
        try {
            Class cls = Class.forName("com.example.customutil.util.ReflectBean");
            Method method = cls.getMethod("setName", String.class);

            Log.i(TAG, "testNormalString, before name:" + reflectBean.getName());
            method.invoke(reflectBean, "456");
            Log.i(TAG, "testNormalString, after name:" + reflectBean.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射调用静态无参方法
     */
    public void testStaticVoid() {
        try {
            Class cls = Class.forName("com.example.customutil.util.ReflectBean");
            Method method = cls.getDeclaredMethod("getNum");

            int num = (int) method.invoke(null);
            Log.i(TAG, "testStaticVoid, num:" + num);
        } catch (Exception e) {

        }
    }

    /**
     * 反射调用静态有参方法
     */
    public void testStaticInt() {
        try {
            Class cls = Class.forName("com.example.customutil.util.ReflectBean");
            Method method = cls.getDeclaredMethod("setNum", int.class);

            Log.i(TAG, "testStaticInt, before num:" + ReflectBean.getNum());
            method.invoke(null, 100);
            Log.i(TAG, "testStaticInt, after num:" + ReflectBean.getNum());
        } catch (Exception e) {

        }
    }

    /**
     * 反射访问静态内部类的父类中的成员
     */
    public void testInnerFather(ReflectBean.ReflectInner reflectInner) {
        try {
            /**
             * 1. 获取静态内部类
             * 通过打印，可知能访问到其父类中共有权限的字段
             */
            Class cls = Class.forName("com.example.customutil.util.ReflectBean$ReflectInner");

            Field[] fields1 = cls.getFields();
            for (int i = 0; i < fields1.length; i++) {
                Log.i(TAG, "testInnerFather, " + i + "--1-->" + fields1[i].getName());
            }
            Log.w(TAG, "=========================");
            Field[] fields2 = cls.getDeclaredFields();
            for (int i = 0; i < fields2.length; i++) {
                Log.i(TAG, "testInnerFather, " + i + "--2-->" + fields2[i].getName());
            }

            /**
             * 2. 获取其父类中的字段
             */
            Field field = cls.getField("fatherPublicField");

            String str = (String) field.get(reflectInner);
            Log.i(TAG, "--->" + str + ", str.len:" + str.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() {
        ReflectBean reflectBean = new ReflectBean();
        reflectBean.setName("123");
        ReflectBean.setNum(50);

        testNormalVoid(reflectBean);
        testNormalString(reflectBean);
        testStaticVoid();
        testStaticInt();

        testAccess();
        testNormalMember(reflectBean);
        testStaticMember();

        ReflectBean.ReflectInner reflectInner = new ReflectBean.ReflectInner();
        reflectInner.fatherPublicField = "789";
        testInnerFather(reflectInner);
        Log.i(TAG, "reflectInner.objectPublicField:" + reflectInner.fatherPublicField);
    }

}