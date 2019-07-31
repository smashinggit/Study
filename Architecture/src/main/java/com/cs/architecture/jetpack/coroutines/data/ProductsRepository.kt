package com.cs.architecture.jetpack.coroutines.data

/**
 *
 * author : ChenSen
 * data : 2019/7/22
 * desc:
 *
 * Repository 提供挂起函数来访问数据。它通常不会启动长生命周期的协程，因为它没有办法取消它们。
 * 无论何时 Repository 需要做昂贵的操作（集合转换等），它都需要使用 withContext 来提供主线程安全的接口
 *
 *
 * 注意：
 * 当用户离开界面时，一些后台执行的保存操作可能想继续运行，这种情况下，脱离生命周期运行是有意义的。
 * 在大多数情况下，viewModelScope 都是一个好选择
 */
class ProductsRepository(private val productDao: ProductsDao) {


    /**
     * This is a "regular" suspending function, which means the caller must
     * be in a coroutine. The repository is not responsible for starting or
     * stoppong coroutines since it doesn't have a natural lifecycle to cancel
     * unnecssary work.
     *
     * This *may* be called from Dispatchers.Main abd is main-safe because
     * Room will take care of main-safety for us.
     */
    suspend fun loadSortedProducts(ascending: Boolean): List<Product> {

        return if (ascending)
            productDao.loadProductsByDateStockedAscending()
        else
            productDao.loadProductsByDateStockedDescending()
    }


    suspend fun addProduct(product: Product) {
        productDao.addProduct(product)
    }

    suspend fun deleteAll(value: List<Product>) {
        productDao.delete(value)
    }
}