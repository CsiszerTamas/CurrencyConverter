package com.cst.currencyconverter.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rate: Rate): Long

    @Query("SELECT * from latest_rate_table WHERE currency_name LIKE :code")
    suspend fun get(code: String): Rate?

    @Query("DELETE FROM latest_rate_table")
    suspend fun clearAllRates()

    @Query("SELECT * FROM latest_rate_table ORDER BY CASE WHEN currency_rate = 1.00 THEN 0 ELSE 1 END")
    suspend fun getAllRates(): List<Rate>
}
