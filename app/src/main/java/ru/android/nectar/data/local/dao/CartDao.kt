package ru.android.nectar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.android.nectar.data.local.entity.CartEntity

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCart(cart: CartEntity)

    @Query("DELETE FROM cart_products WHERE userId = :userId AND productId = :productId")
    suspend fun removeCart(userId: Int, productId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM cart_products WHERE userId = :userId AND productId = :productId)")
    fun isCart(userId: Int, productId: Int): Flow<Boolean>

    @Query("SELECT productId FROM cart_products WHERE userId = :userId")
    fun getCartProducts(userId: Int): Flow<List<Int>>

    @Query("SELECT * FROM cart_products WHERE userId=:userId AND productId=:productId")
    fun getCartProduct(userId: Int, productId: Int) : Flow<CartEntity>

    @Query("UPDATE cart_products SET count = count + 1 WHERE userId = :userId AND productId = :productId")
    suspend fun incrementCount(userId: Int, productId: Int)

    @Query("UPDATE cart_products SET count = CASE WHEN count > 1 THEN count - 1 ELSE 1 END WHERE userId = :userId AND productId = :productId")
    suspend fun decrementCount(userId: Int, productId: Int)

}