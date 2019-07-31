package com.cs.architecture.jetpack.coroutines.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 *
 * author : ChenSen
 * data : 2019/7/24
 * desc:
 */
@Database(version = 1, entities = [Product::class])
abstract class ProductDataBase : RoomDatabase() {

    abstract fun getProductDao(): ProductsDao

}