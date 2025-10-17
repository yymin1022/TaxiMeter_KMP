package com.yong.taximeter.ui.main.subscreen.store.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.revenuecat.purchases.kmp.models.Package

data class StoreProduct(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val rcPackage: Package,
)