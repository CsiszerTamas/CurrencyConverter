package com.cst.currencyconverter.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "latest_rate_table")
data class Rate(

    @PrimaryKey
    @ColumnInfo(name = "currency_name")
    val code: String,

    @ColumnInfo(name = "currency_rate")
    var rate: Double,
)
