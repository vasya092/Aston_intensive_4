package com.example.astonintensive4.utils

import android.app.Activity
import android.view.View

fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun <T : View> Activity.find(idRes: Int) = unsafeLazy<T> { findViewById(idRes) }