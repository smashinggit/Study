package com.cs.architecture.jetpack.coroutines

import androidx.lifecycle.*
import com.cs.architecture.jetpack.coroutines.data.Product
import com.cs.architecture.jetpack.coroutines.data.ProductsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 *
 * author : ChenSen
 * data : 2019/7/22
 * desc:
 *
 * ViewModel 负责启动协程，保证用户离开界面时取消协程。它本身不做昂贵的操作，而是依赖其他层来做。
 * 一旦有了结果，就使用 LiveData 发送给 UI 界面。也正因为 ViewModel 不做昂贵的操作，
 * 所以它在主线程启动协程。通过在主线程启动，当结果可用它可以更快的响应用户事件（例如内存缓存
 *
 *
 * 由于 ViewModel 可以在 onCleared 回调中取消协程，所以它是这个架构中启动协程的好位置
 * 在 ViewModel 中启动协程是一个通用的设计模式
 */
class ProductsViewModel(private val repository: ProductsRepository) : ViewModel() {

    private val _sortedProducts = MutableLiveData<List<Product>>()
    val sortedProducts: LiveData<List<Product>> = _sortedProducts

    private val _sortedButtonEnable = MutableLiveData<Boolean>()
    val sortedButtonEnable: LiveData<Boolean> = _sortedButtonEnable

    init {
        _sortedButtonEnable.value = true
    }


    fun addProduct(product: Product) {
        viewModelScope.launch {

            val deferred = async { repository.addProduct(product) }
            deferred.await()
            onSortAscending()
        }
    }

    fun deleteAll() {
        viewModelScope.launch {

            val deferred = async { repository.deleteAll(sortedProducts.value!!) }
            deferred.await()
            onSortAscending()
        }
    }

    fun onSortAscending() = sortPricesBy(true)

    fun onSortDescending() = sortPricesBy(false)


    private fun sortPricesBy(ascending: Boolean) {

        viewModelScope.launch {

            // disable the sort buttons whenever a sort is running
            _sortedButtonEnable.value = false

            // suspend and resume make this database request main-safe
            // so our ViewModel doesn't need to worry about threading
            try {
                _sortedProducts.value = repository.loadSortedProducts(ascending)
            } finally {
                _sortedButtonEnable.value = true
            }
        }
    }

    class Factory(private val repository: ProductsRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ProductsViewModel(repository) as T
        }
    }

}