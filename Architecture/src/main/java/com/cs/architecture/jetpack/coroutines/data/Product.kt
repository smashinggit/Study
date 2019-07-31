package com.cs.architecture.jetpack.coroutines.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 * author : ChenSen
 * data : 2019/7/22
 * desc:
 */

@Entity
data class Product(@PrimaryKey(autoGenerate = true) val id: Int, val name: String) {

}