package com.skedgo.tripkit.booking.viewmodel

import com.skedgo.tripkit.booking.OptionFormField
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject

class OptionFieldViewModel(private val optionField: OptionFormField) : TwoLineViewModel {

    private val onValueSelected: BehaviorSubject<OptionFieldViewModel> = BehaviorSubject.create()
    private var selectedIndex: Int = -1

    init {
        onValueSelected.onNext(this)
    }

    companion object {
        fun create(optionField: OptionFormField): OptionFieldViewModel {
            return OptionFieldViewModel(optionField)
        }
    }

    fun onValueSelected(): Flowable<OptionFieldViewModel> {
        return onValueSelected.hide().toFlowable(BackpressureStrategy.BUFFER)
    }

    fun getAllValues(): List<OptionFormField.OptionValue> {
        return optionField.allValues
    }

    fun getSelectedIndex(): Int {
        if (selectedIndex == -1) {
            selectedIndex = findSelectedIndex()
        }
        return selectedIndex
    }

    fun select(valueIndex: Int) {
        val allValues = optionField.allValues
        if (allValues.isNullOrEmpty() || valueIndex < 0 || valueIndex >= allValues.size) {
            return
        }

        selectedIndex = valueIndex
        val newSelectedValue = allValues[valueIndex]
        optionField.setValue(newSelectedValue)
        onValueSelected.onNext(this)
    }

    override fun getPrimaryText(): String? {
        val selectedValue = optionField.getValue()
        return selectedValue?.title
    }

    override fun getSecondaryText(): String? {
        val selectedValue = optionField.getValue()
        return selectedValue?.value
    }

    private fun findSelectedIndex(): Int {
        val selectedValue = optionField.getValue()
        val allValues = optionField.allValues
        if (selectedValue != null && allValues.isNotEmpty()) {
            for (i in allValues.indices) {
                if (allValues[i].value == selectedValue.value) {
                    return i
                }
            }
        }
        return 0
    }
}