package com.cst.currencyconverter.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CurrencyDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: CurrencyDatabase
    private lateinit var dao: CurrencyDao

    @Before
    fun setup() {
        // we don't want to use multiple threads (main, background) when executing tests so
        // we use allowMainThreadQueries() method
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CurrencyDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = database.currencyDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertRate() = runBlockingTest {
        val rate = Rate("BGN", 1.956)
        dao.insert(rate)

        val allRates = dao.getAllRates()

        assertThat(allRates.contains(rate))
    }
}
