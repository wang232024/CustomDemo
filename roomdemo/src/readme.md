Room主要包含三个组件：
数据库：包含数据库持有者，作为应用已保留的持久关系型数据的底层连接的主要接入点。
使用@Database注解的类应满足以下条件：
是扩展RoomDatabase的抽象类。
在注释中添加与数据库关联的实体列表。
包含具有0个参数且返回使用@Dao注释的类的抽象方法。
Entity：表示数据库中的表。
DAO：包含用于访问数据库的方法。

1. Bean类(MyBean)
须添加注释
@Entity(tableName = "表名")
否则报错Entity class must be annotated with @Entity

2. Dao接口(MyBeanDao)
须添加注释
@Dao
否则报错Dao class must be annotated with @Dao
Dao接口相当一个用于操作数据库的接口，一般命名为class+Dao

3. XXXDatabase
须添加注释
@Database(entities = {MyBeanDao.class}, version = 1)
该类是一个抽象类，继承自RoomDatabase

