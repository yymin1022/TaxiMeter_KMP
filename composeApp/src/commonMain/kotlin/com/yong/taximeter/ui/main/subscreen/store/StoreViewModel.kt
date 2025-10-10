package com.yong.taximeter.ui.main.subscreen.store

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOff
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.ktx.awaitOfferings
import com.yong.taximeter.ui.main.subscreen.store.model.StoreProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StoreUiState(
    val isLoading: Boolean = true,
    val products: List<StoreProduct> = emptyList(),
)

class StoreViewModel: ScreenModel {
    companion object {
        private const val SKU_ID_AD_REMOVE = "ad_remove"
        private const val SKU_ID_DONATION_1000 = "donation_1000"
        private const val SKU_ID_DONATION_5000 = "donation_5000"
        private const val SKU_ID_DONATION_10000 = "donation_10000"
        private const val SKU_ID_DONATION_50000 = "donation_50000"

        private val SKU_ID_ICON_MAPPING = mapOf(
            SKU_ID_AD_REMOVE to Icons.Default.MoneyOff,
            SKU_ID_DONATION_1000 to Icons.Default.LocalDrink,
            SKU_ID_DONATION_5000 to Icons.Default.Coffee,
            SKU_ID_DONATION_10000 to Icons.Default.Fastfood,
            SKU_ID_DONATION_50000 to Icons.Default.DinnerDining,
        )
    }

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDefaultOffers()
    }

    private fun loadDefaultOffers() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val offerings = Purchases.sharedInstance.awaitOfferings()
                val packages = offerings.current?.availablePackages ?: emptyList()

                val storeProducts = packages.map {
                    StoreProduct(
                        id = it.storeProduct.id,
                        title = it.storeProduct.title,
                        icon = SKU_ID_ICON_MAPPING[it.storeProduct.id] ?: Icons.Default.Money,
                        rcPackage = it
                    )
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        products = storeProducts,
                    )
                }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }
}