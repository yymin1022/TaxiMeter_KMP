package com.yong.taximeter.ui.main.subscreen.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.yong.taximeter.ui.main.subscreen.store.model.StoreProduct
import org.jetbrains.compose.resources.stringResource
import taximeter.composeapp.generated.resources.Res
import taximeter.composeapp.generated.resources.store_desc_text
import taximeter.composeapp.generated.resources.store_item_restore

object StoreScreen: Screen {
    @Composable
    override fun Content() {
        val viewModel: StoreViewModel = getScreenModel()
        val uiState = viewModel.uiState.collectAsState()

        StoreScreenInternal(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            uiState = uiState.value,
            processPurchase = viewModel::processPurchase,
            restorePurchase = viewModel::restorePurchases,
        )
    }

    @Composable
    private fun StoreScreenInternal(
        modifier: Modifier = Modifier,
        uiState: StoreUiState,
        processPurchase: (StoreProduct) -> Unit,
        restorePurchase: () -> Unit,
    ) {
        val isLoading = uiState.isLoading
        val products = uiState.products

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if(isLoading) {
                CircularProgressIndicator()
            } else {
                ProductList(
                    modifier = Modifier,
                    products = products,
                    processPurchase = processPurchase,
                )

                ProductItem(
                    modifier = Modifier,
                    icon = Icons.Default.History,
                    title = stringResource(Res.string.store_item_restore),
                    onClick = restorePurchase,
                )

                StoreDescriptionText(
                    modifier = Modifier,
                )
            }
        }
    }

    @Composable
    private fun ProductList(
        modifier: Modifier = Modifier,
        products: List<StoreProduct>,
        processPurchase: (StoreProduct) -> Unit,
    ) {
        LazyColumn(
            modifier = modifier,
        ) {
            items(products.size) {
                val product = products[it]
                val productIcon = product.icon
                val productTitle = product.title

                ProductItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = productIcon,
                    title = productTitle,
                    onClick = { processPurchase(product) }
                )

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.Gray,
                    thickness = 1.dp,
                )
            }
        }
    }

    @Composable
    private fun ProductItem(
        modifier: Modifier = Modifier,
        icon: ImageVector,
        title: String,
        onClick: () -> Unit,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 5.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp),
                imageVector = icon,
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(start = 10.dp),
                text = title,
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
private fun StoreDescriptionText(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(Res.string.store_desc_text),
            color = Color.Gray,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Center,
        )
    }
}