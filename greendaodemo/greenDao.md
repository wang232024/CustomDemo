### 引入GreenDao
1. 工程build.gradle中添加依赖
buildscript {
    dependencies {
        // GreenDao插件
        classpath 'org.greenrobot:greendao-gradle-plugin:3.3.0'
    }
}

2. app模块build.gradle中添加依赖
plugins {
    //GreenDao插件
    id 'org.greenrobot.greendao'
}
或
//GreenDao插件
apply plugin: 'org.greenrobot.greendao'

android {
    greendao {
        schemaVersion 1 //数据库版本号 每次升级数据库都需要改变版本，只能增加
        daoPackage 'com.custom.savedb.greendaobean'  //设置DaoMaster、DaoSession、Dao包名，推荐工程包名+greendaobean
        targetGenDir 'src/main/java' //设置DaoMaster、DaoSession、Dao目录
    }
}

dependencies {
    implementation 'org.greenrobot:greendao:3.3.0'
}

3. 添加实体类Bean
@Entity
public class Bean {
    @Id
    private Long id;    // 对应数据表中的主键，是一条数据的唯一标识
    private String userId;
    private String name;
    private String  url;
}

4. 使用AndroidStudio的工具Make Project运行，生成BeanDao, BeanMaster, BeanSession

5. 对于修改了bean的情况，可以直接删除掉app以清除已存在的数据库。

### 基本属性
greenDAO中，schemaVersion表示数据库版本号，每次数据库升级的时候我们修改这里的版本号即可（修改这里的版本号，greenDAO会自动修改生成到DAOMaster中的版本号），targetGenDir表示greenDAO生成的DAOMaster和DaoSession的位置。
schemaVersion： 数据库schema版本，也可以理解为数据库版本号
daoPackage：设置DaoMaster、DaoSession、Dao包名
targetGenDir：设置DaoMaster、DaoSession、Dao目录
targetGenDirTest：设置生成单元测试目录
generateTests：设置自动生成单元测试用例

1.）实体@Entity注解

schema：告知GreenDao当前实体属于哪个schema
active：标记一个实体处于活动状态，活动实体有更新、删除和刷新方法
nameInDb：在数据中使用的别名，默认使用的是实体的类名
indexes：定义索引，可以跨越多个列
createInDb：标记创建数据库表**

2.）基础属性注解

@Id :主键 Long型，可以通过@Id(autoincrement = true)设置自增长
@Property：设置一个非默认关系映射所对应的列名，默认是的使用字段名举例：@Property (nameInDb="name")
@NotNul：设置数据库表当前列不能为空
@Transient：添加次标记之后不会生成数据库表的列

3.)索引注解

@Index：使用@Index作为一个属性来创建一个索引，通过name设置索引别名，也可以通过unique给索引添加约束
@Unique：向数据库列添加了一个唯一的约束

4.）关系注解

@ToOne：定义与另一个实体（一个实体对象）的关系
@ToMany：定义与多个实体对象的关系

(一) @Entity 定义实体
@nameInDb 在数据库中的名字，如不写则为实体中类名
@indexes 索引
@createInDb 是否创建表，默认为true,false时不创建
@schema 指定架构名称为实体
@active 无论是更新生成都刷新
(二) @Id
(三) @NotNull 不为null
(四) @Unique 唯一约束
(五) @ToMany 一对多
(六) @OrderBy 排序
(七) @ToOne 一对一
(八) @Transient 不存储在数据库中
(九) @generated 由greendao产生的构造函数或方法

二、数据库初始化
DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "lenve.db", null);
DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
DaoSession daoSession = daoMaster.newSession();
testBeanDao = daoSession.getTestBeanDao();

三、注意事项。
默认生成文件的位置/app/build/generated/source/greendao

id默认从1开始自增，即1,2,3,4,5...

使用unique()方法时，如果查询到的条目大于1，那么将抛出运行时异常。

