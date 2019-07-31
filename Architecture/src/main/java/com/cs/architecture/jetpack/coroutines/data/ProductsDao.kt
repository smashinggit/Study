package com.cs.architecture.jetpack.coroutines.data

import androidx.room.*

/**
 *
 * author : ChenSen
 * data : 2019/7/22
 * desc:
 *
 * 数据层（网络或者数据库）总是提供挂起函数。使用 Kotlin 协程的时候需要保证这些挂起函数是主线程安全的，
 * Room 和 Retrofit 都遵循了这一原则
 *
 *
 * ProductsDao 是一个 Room Dao，它对外提供了两个挂起函数。由于函数由 suspend 修饰，
 * Room 会确保它们主线程安全。
 * 这就意味着你可以直接在 Dispatchers.Main 中调用它们
 *
 *
 * 注意：Room 使用自己的调度器在后台线程进行查询操作。你不应该再使用 withContext(Dispatchers.IO)
 * 来调用 Room 的 suspend 查询，这只会让你的代码运行的更慢
 */
@Dao
interface ProductsDao {

    // Because this is marked suspend, Room will use it's own dispatcher
    // to run this query in a main-safe way,
    @Query("select * from product ORDER BY id ASC")
    suspend fun loadProductsByDateStockedAscending(): List<Product>


    // Because this is marked suspend, Room will use it's own dispatcher
    // to run this query in a main-safe way,
    @Query("select * from product ORDER BY id DESC")
    suspend fun loadProductsByDateStockedDescending(): List<Product>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(vararg product: Product)

    @Delete
    suspend fun delete(product: List<Product>)
}