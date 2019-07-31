package com.cs.app.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

/**
 *
 * author : ChenSen
 * data : 2019/7/16
 * desc:
 *
 *
 * SQLiteOpenHelper:
 * 第一个参数： 上下文
 * 第二个参数：数据库的名称
 * 第三个参数：null代表的是默认的游标工厂
 * 第四个参数：是数据库的版本号  数据库只能升级,不能降级,版本号只能变大不能变小
 */
class MyDBHelper(private val context: Context,
                 name: String = "test.db") : SQLiteOpenHelper(context, name, null, 1) {


    companion object {
        const val SQL_USER_TABLE_NAME = "user_table"

        const val SQL_CREATE_TABLE = "create table $SQL_USER_TABLE_NAME(id integer primary key autoincrement," +
                "name varchar(20),sex varchar(5),age varchar(20))"

        const val SQL_QUERY_ALL = "select * from $SQL_USER_TABLE_NAME"
    }


    /**
     * onCreate是在数据库创建的时候调用的，主要用来初始化数据表结构和插入数据初始化的记录
     *
     * 当数据库第一次被创建的时候调用的方法,适合在这个方法里面把数据库的表结构定义出来.
     * 所以只有程序第一次运行的时候才会执行
     * 如果想再看到这个函数执行，必须写在程序然后重新安装这个app
     */
    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(SQL_CREATE_TABLE)  //创建一张用户表

        //默认添加一个用户
        val values = ContentValues()
        values.put("name", "陈森")
        values.put("sex", 1)
        values.put("age", 18)
        db?.insert(SQL_USER_TABLE_NAME, null, values)
        Toast.makeText(context, "创建用户表", Toast.LENGTH_SHORT).show()
    }


    /**
     * 当数据库更新的时候调用的方法
     * 这个要显示出来得在上面的super语句里面版本号发生改变时才会 打印  （super(context, "itheima.db", null, 2); ）
     * 注意，数据库的版本号只可以变大，不能变小，假设我们当前写的版本号是3，运行，然后又改成1，运行则报错。不能变小
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        db?.execSQL("alter table user_table add account varchar(20)")
    }
}