package com.challenge.marleyspoon.utils

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
 * @author moustafasamhoury
 * created on Sunday, 15 Sep, 2019
 */
/**
 * Creates a Chip View with some default properties that we would like to present the recipe tag
 * with, and then adds that chip into the ChipGroup instance
 *
 * @param tagName The text that will appear on the chip – i.e. Vegan
 */
fun ChipGroup.addRecipeTagChip(tagName: String) {
    addView(Chip(context).apply {
        text = tagName
        isClickable = false
        isCheckable = false
        chipEndPadding = 0f
        chipStartPadding = 0f
    })
}