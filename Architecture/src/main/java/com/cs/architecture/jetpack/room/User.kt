package com.cs.architecture.jetpack.room

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 *
 * author : ChenSen
 * data : 2019/7/24
 * desc:
 */
@Entity
data class User(@PrimaryKey(autoGenerate = true) val id: Int,
                val name: String?,
                val age: Int,
                @Ignore val pic: Bitmap?) {
}