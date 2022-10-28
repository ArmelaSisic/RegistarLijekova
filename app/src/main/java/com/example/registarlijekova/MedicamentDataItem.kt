package com.example.registarlijekova


data class MedicamentDataItem(
    val id: Int,
    val activeSubstanceMeasurementUnit: String,
    val activeSubstanceQuantityMeasurementUnit: String,
    val activeSubstanceSelectedQuantity: Int,
    val activeSubstanceValue: Int,
    val atc: String,
    val categoryId: Int,
    val description: String,
    val forbiddenInPregnancy: Boolean,
    val maximumDailyDose: Int,
    val minimumDailyDose: Int,
    var name: String,
    val shortDescription: String,
    val showOnCalculator: Boolean,
)