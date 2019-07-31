package com.cs.architecture.jetpack.coroutines

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.cs.architecture.R
import com.cs.architecture.jetpack.coroutines.data.Product
import com.cs.architecture.jetpack.coroutines.data.ProductDataBase
import com.cs.architecture.jetpack.coroutines.data.ProductsRepository
import com.cs.common.base.BaseActivity
import com.cs.common.utils.toast
import kotlinx.android.synthetic.main.activity_coroutines.*
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 *
 * author : ChenSen
 * data : 2019/7/17
 * desc:
 *
 * 我们将协程添加到 ViewModel 、Repository 和 Room 中，每一层都有不同的责任
 *
 * ViewModel 在主线程启动协程，一旦有了结果就结束。
 * Repository 提供挂起函数并保证它们主线程安全。
 * 数据库和网络层提供挂起函数并保证它们主线程安全。
 */
class CoroutinesActivity : BaseActivity() {

    lateinit var mAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines)


        val dao = Room.databaseBuilder(this,
                ProductDataBase::class.java, "product_database")
                .build().getProductDao()

        val factory = ProductsViewModel.Factory(ProductsRepository(dao))

        val productsViewModel = ViewModelProviders.of(this, factory)
                .get(ProductsViewModel::class.java)




        productsViewModel.sortedProducts.observe(this, Observer {
            mAdapter.setData(it)
            toast("查询数据完毕！${it.size}")
            rvProduct.scrollToPosition(it.size)
        })

        productsViewModel.sortedButtonEnable.observe(this, Observer {
            btnQueryAes.isEnabled = it
        })

        btnQueryAes.setOnClickListener {
            productsViewModel.onSortAscending()
        }

        btnQueryDesc.setOnClickListener {
            productsViewModel.onSortDescending()
        }

        btnAdd.setOnClickListener {
            val product = Product(0, "娃哈哈~${System.currentTimeMillis()}")
            productsViewModel.addProduct(product)
        }

        btnClear.setOnClickListener {
            productsViewModel.deleteAll()
        }

        mAdapter = ProductAdapter(this)
        rvProduct.adapter = mAdapter
        rvProduct.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        productsViewModel.onSortAscending()
    }


    class ProductAdapter(private val context: Context) : RecyclerView.Adapter<ProductHolder>() {

        var mData = emptyList<Product>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
            return ProductHolder(view)
        }

        override fun getItemCount() = mData.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ProductHolder, position: Int) {

            holder.tvId.text = "产品ID : ${mData[position].id}"
            holder.tvName.text = "产品名称 : ${mData[position].name}"
        }

        fun setData(data: List<Product>) {
            mData = data
            notifyDataSetChanged()
        }
    }


    class ProductHolder(root: View) : RecyclerView.ViewHolder(root) {
        val tvId: TextView = root.tvId
        val tvName: TextView = root.tvName
    }


    suspend fun getDoc() {

        val result = get("as")

    }

    suspend fun get(url: String) {
        withContext(Dispatchers.IO) {

        }

        coroutineScope {

        }
    }


}

