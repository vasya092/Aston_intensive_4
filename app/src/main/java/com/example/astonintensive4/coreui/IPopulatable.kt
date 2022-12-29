package com.example.astonintensive4.coreui

interface IPopulatable<T: BaseItem> {
    fun populate(model: T)
}