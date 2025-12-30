package com.yong.taximeter.common.util

import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.ktx.awaitCustomerInfo
import com.yong.taximeter.common.def.PreferenceDef.KEY_AD_REMOVAL
import com.yong.taximeter.ui.main.subscreen.store.StoreViewModel.Companion.SKU_ID_AD_REMOVE

/**
 * Advertisement Util
 * - Logics about advertisement
 */
object AdvertisementUtil {
    /**
     * Checks if ad removal is purchased.
     *
     * @return Flag value about ad removal is purchased
     */
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

    /**
     * Update preference value about ad removal
     *
     * @param isEnabled Flag value about ad removal
     */
    suspend fun updateAdRemovalPref(isEnabled: Boolean) {
        PreferenceUtil.putBoolean(KEY_AD_REMOVAL, isEnabled)
    }
}