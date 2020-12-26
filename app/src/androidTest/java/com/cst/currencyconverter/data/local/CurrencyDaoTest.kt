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

    @Test
    fun getRate() = runBlockingTest {
        val rate = Rate("BGN", 1.956)
        dao.insert(rate)

        val rateFromDb = dao.get("BGN")

        assertThat(rate == rateFromDb)
    }

    @Test
    fun getAllRates() = runBlockingTest {

        val ratesList: MutableList<Rate> = createCurrencyList()

        for (rate in ratesList) {
            dao.insert(rate)
        }

        val allLocalRates = dao.getAllRates()

        assertThat(ratesList == allLocalRates)
    }

    @Test
    fun cleanAllRates() = runBlockingTest {

        val ratesList: MutableList<Rate> = createCurrencyList()

        for (rate in ratesList) {
            dao.insert(rate)
        }

        dao.clearAllRates()

        val allLocalRates = dao.getAllRates()

        assertThat(allLocalRates.isEmpty())
    }

    // util method to create currency test data list
    private fun createCurrencyList(): MutableList<Rate> {
        val rateEUR = Rate("EUR", 1.0)
        val rateAUD = Rate("AUD", 1.611)
        val rateBGN = Rate("BGN", 1.956)
        val rateBRL = Rate("BRL", 4.263)
        val rateCAD = Rate("CAD", 1.523)
        val rateCHF = Rate("CHF", 1.151)
        val rateCNY = Rate("CNY", 7.332)
        val rateCZK = Rate("CZK", 25.721)
        val rateDKK = Rate("DKK", 7.588)
        val rateGBP = Rate("GBP", 0.886)
        val rateHKD = Rate("HKD", 9.035)
        val rateHRK = Rate("HRK", 7.435)
        val rateHUF = Rate("HUF", 322.701)
        val rateIDR = Rate("IDR", 16205.701)
        val rateILS = Rate("ILS", 4.131)
        val rateINR = Rate("INR", 82.277)
        val rateISK = Rate("ISK", 136.542)
        val rateJPY = Rate("JPY", 126.756)
        val rateKRW = Rate("KRW", 1287.688)
        val rateMXN = Rate("MXN", 21.076)
        val rateMYR = Rate("MYR", 4.619)
        val rateNOK = Rate("NOK", 9.893)
        val rateNZD = Rate("NZD", 1.678)
        val ratePHP = Rate("PHP", 60.054)
        val ratePLN = Rate("PLN", 4.333)
        val rateRON = Rate("RON", 4.809)
        val rateRUB = Rate("RUB", 75.319)
        val rateSEK = Rate("SEK", 10.589)
        val rateSGD = Rate("SGD", 1.547)
        val rateTHB = Rate("THB", 35.952)
        val rateUSD = Rate("USD", 1.136)
        val rateZAR = Rate("ZAR", 16.068)

        val ratesList: MutableList<Rate> = mutableListOf()

        ratesList.add(rateEUR)
        ratesList.add(rateAUD)
        ratesList.add(rateBGN)
        ratesList.add(rateEUR)
        ratesList.add(rateBRL)
        ratesList.add(rateCAD)
        ratesList.add(rateCHF)
        ratesList.add(rateCNY)
        ratesList.add(rateCZK)
        ratesList.add(rateDKK)
        ratesList.add(rateGBP)
        ratesList.add(rateHKD)
        ratesList.add(rateHRK)
        ratesList.add(rateHUF)
        ratesList.add(rateIDR)
        ratesList.add(rateILS)
        ratesList.add(rateINR)
        ratesList.add(rateISK)
        ratesList.add(rateJPY)
        ratesList.add(rateKRW)
        ratesList.add(rateMXN)
        ratesList.add(rateMYR)
        ratesList.add(rateNOK)
        ratesList.add(rateNZD)
        ratesList.add(ratePHP)
        ratesList.add(ratePLN)
        ratesList.add(rateRON)
        ratesList.add(rateRUB)
        ratesList.add(rateSEK)
        ratesList.add(rateSGD)
        ratesList.add(rateTHB)
        ratesList.add(rateUSD)
        ratesList.add(rateZAR)
        return ratesList
    }
}
