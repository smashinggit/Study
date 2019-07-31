package com.cs.architecture.jetpack.room

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 *
 * author : ChenSen
 * data : 2019/7/24
 * desc:
 */
@Dao
interface UserDao {

    //协程
    //You can add the suspend Kotlin keyword to your DAO methods to make them asynchronous
    // using Kotlin coroutines functionality. This ensures that they cannot be executed on
    // the main thread

    @Transaction
    @Query("SELECT * FROM user")
    suspend fun loadAllUsersCoroutine(): Array<User>


    @Query("SELECT * FROM user")
    fun loadAllUsers(): Array<User>


    @Query("SELECT * FROM user WHERE age > :minAge")
    fun loadAllUsersOlderThan(minAge: Int): Array<User>


    @Query("SELECT * FROM user WHERE age BETWEEN :minAge AND :maxAge")
    fun loadAllUsersBetweenAges(minAge: Int, maxAge: Int): Array<User>


    @Query("SELECT * FROM user WHERE name LIKE :search")
    fun findUserWithName(search: String): Array<User>


    @Query("SELECT * FROM user WHERE name IN (:names)")
    fun loadUsersFromRegions(names: List<String>): Array<User>


    //Observable queries
    @Query("SELECT * FROM user WHERE name IN (:names)")
    fun loadUsersFromRegionsSync(names: List<String>): LiveData<List<User>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg users: User)


    @Insert
    fun insertBothUsers(user1: User, user2: User)

    @Insert
    fun insertUsersAndFriends(user2: User, friends: List<User>)


    @Update
    fun updateUsers(vararg user: User)

    @Delete
    fun deleteUsers(vararg user: User)
}