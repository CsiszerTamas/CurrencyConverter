package com.cst.currencyconverter.ui.rates.adapter

import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cst.currencyconverter.R
import com.cst.currencyconverter.constants.Constants
import com.cst.currencyconverter.data.CurrencyRate
import com.cst.currencyconverter.databinding.RateItemBinding
import com.cst.currencyconverter.ui.rates.diffutil.RatesDiffUtilCallback
import com.cst.currencyconverter.utils.addDecimalLimiter
import java.math.BigDecimal

class RatesAdapter(
    private val onAmountChanged: (value: BigDecimal, ticker: String) -> Unit,
    private val rateAdapterContract: RateAdapterContract
) :
    RecyclerView.Adapter<RatesAdapter.RatesViewHolder>() {
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    var rates: MutableList<CurrencyRate> = mutableListOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(RatesDiffUtilCallback(value, field))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val ratesView = RateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatesViewHolder(ratesView, mainThreadHandler) { value, ticker ->
            val changeIndex =
                rates.indexOfFirst { rate -> rate.basicCurrencyRate.currencyShortName == ticker }
            val oldValue = rates[changeIndex]
            rates[changeIndex] =
                oldValue.copy(basicCurrencyRate = oldValue.basicCurrencyRate.copy(rate = value))
            onAmountChanged.invoke(value, ticker)
        }
    }

    override fun getItemCount() = rates.size

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            holder.bindCurrencyRate(rates[position], null)
        }
    }

    override fun onBindViewHolder(
        holder: RatesViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (position != RecyclerView.NO_POSITION) {
            holder.bindCurrencyRate(
                rates[position],
                if (payloads.size > 0) payloads[0] as Bundle else null
            )
        }
    }

    override fun getItemId(position: Int): Long {
        return rates[position].basicCurrencyRate.currencyShortName.hashCode().toLong()
    }

    inner class RatesViewHolder(
        private val binding: RateItemBinding,
        private val mainThreadHandler: Handler,
        private val onAmountChanged: (value: BigDecimal, ticker: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val textWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                sendOnAmountChanged()
            }
        }

        fun bindCurrencyRate(currencyRate: CurrencyRate, diff: Bundle?) {
            if (diff == null || diff.containsKey(RatesDiffUtilCallback.KEY_TICKER)) {
                binding.ticker.text = currencyRate.basicCurrencyRate.currencyShortName
            }
            if (diff == null || diff.containsKey(RatesDiffUtilCallback.KEY_DESCRIPTION)) {
                binding.description.text = currencyRate.currencyDescription
            }

            if (diff == null || diff.containsKey(RatesDiffUtilCallback.KEY_AMOUNT)) {
                binding.amount.onFocusChangeListener = null
                binding.amount.removeTextChangedListener(textWatcher)

                binding.amount.setText(currencyRate.basicCurrencyRate.rate.toPlainString())
                binding.amount.addTextChangedListener(textWatcher)

                binding.amount.onFocusChangeListener =
                    View.OnFocusChangeListener { _, hasFocus ->
                        // We want to wait for recycler scroll to avoid the
                        // "RecyclerView is computing a layout" exception
                        mainThreadHandler.post {
                            if (hasFocus) {
                                sendOnAmountChanged()
                            }
                        }
                    }

                binding.itemLayout.setOnClickListener {
                    mainThreadHandler.post {
                        sendOnAmountChanged()
                        rateAdapterContract.onBaseRateChanged()
                        // request focus for the selected first item and put cursor to the end of the EditText
                        binding.amount.requestFocus()
                        binding.amount.text?.length?.let { it1 -> binding.amount.setSelection(it1) }
                    }
                }

                binding.amount.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        sendOnAmountChanged()
                    }
                    false
                }
            }

            if (diff == null || diff.containsKey(RatesDiffUtilCallback.KEY_FLAG)) {
                try {
                    binding.flag.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            binding.root.resources,
                            currencyRate.iconId,
                            null
                        )
                    )
                } catch (ex: Resources.NotFoundException) {
                    binding.flag.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            binding.root.resources,
                            R.drawable.ic_flag_placeholder,
                            null
                        )
                    )
                }
            }

            // add decimal limiter, we enable only 2 decimals
            binding.amount.addDecimalLimiter(Constants.RATE_ENABLED_DECIMALS)
        }

        private fun sendOnAmountChanged() {
            if (isNumber(binding.amount.text.toString())) {
                onAmountChanged(
                    BigDecimal(binding.amount.text.toString()),
                    binding.ticker.text.toString()
                )
            }
        }

        private fun isNumber(value: String): Boolean {
            return try {
                value.toDouble()
                true
            } catch (ex: NumberFormatException) {
                false
            }
        }
    }
}
