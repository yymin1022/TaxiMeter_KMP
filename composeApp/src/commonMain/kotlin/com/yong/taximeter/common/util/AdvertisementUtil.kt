package com.yong.taximeter.common.util

import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.ktx.awaitCustomerInfo
import com.yong.taximeter.common.util.PreferenceUtil.KEY_AD_REMOVAL
import com.yong.taximeter.ui.main.subscreen.store.StoreViewModel.Companion.SKU_ID_AD_REMOVE

object AdvertisementUtil {
    suspend fun isAdRemovalPurchased(): Boolean {
        try {
            val customerInfo = Purchases.sharedInstance.awaitCustomerInfo()
            val isAdRemove = customerInfo.entitlements[SKU_ID_AD_REMOVE]?.isActive ?: false
            return isAdRemove
        } catch(e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    suspend fun updateAdRemovalPref(isEnabled: Boolean) {
        PreferenceUtil.putBoolean(KEY_AD_REMOVAL, isEnabled)
    }
}