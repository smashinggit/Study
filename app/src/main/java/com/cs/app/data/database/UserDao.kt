package com.cs.app.data.database

import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import java.nio.file.Files.delete


/**
 *
 * author : ChenSen
 * data : 2019/7/16
 * desc:
 *
 * 数据库操作类  dao后缀的都是数据库操作类
 *
 *
 */
class UserDao(private val context: Context) {

    private var dbHelper = MyDBHelper(context)


    fun insert(user: User): Boolean {

        val database = dbHelper.readableDatabase

        val values = ContentValues()
        values.put("name", user.name)
        values.put("sex", user.sex)
        values.put("age", user.age)

        val rowId = database.insert(MyDBHelper.SQL_USER_TABLE_NAME, null, values)

        //记得释放数据库资源
        database.close()
        if (rowId == -1L) {
            Toast.makeText(context, "插入数据失败！", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "插入数据成功！", Toast.LENGTH_SHORT).show()
        }
        return rowId != -1L
    }

    fun deleteAll() {

        val database = dbHelper.writableDatabase

        //两种方式
        //db.execSQL("delete from user_table where name=?", new Object[]{name});
        //val rowcount = db.delete("user_table", "name=?", arrayOf<String>(name))

        database.delete(MyDBHelper.SQL_USER_TABLE_NAME, null, null)
        database.close()

        Toast.makeText(context, "删除所有数据", Toast.LENGTH_SHORT).show()
    }

    fun queryAll(): ArrayList<User> {

        val database = dbHelper.readableDatabase
        val cursor = database.rawQuery(MyDBHelper.SQL_QUERY_ALL, arrayOf())

        val result = ArrayList<User>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val sex = cursor.getInt(cursor.getColumnIndex("sex"))
            val age = cursor.getInt(cursor.getColumnIndex("age"))
            result.add(User(name, sex, age))
        }

        cursor.close()
        database.close()

        return result
    }

    fun queryByName(name: String): ArrayList<User> {
        val database = dbHelper.readableDatabase
        val cursor = database.rawQuery("select * from ${MyDBHelper.SQL_USER_TABLE_NAME} where name=?", arrayOf(name))

        val result = ArrayList<User>()

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val sex = cursor.getInt(cursor.getColumnIndex("sex"))
            val age = cursor.getInt(cursor.getColumnIndex("age"))

            val user = User(name, sex, age)
            result.add(user)
        }

        cursor.close()
        database.close()
        return result
    }


}