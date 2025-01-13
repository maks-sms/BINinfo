package com.example.bininfo.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bininfo.R
import com.example.bininfo.adapters.CardInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController, onBinSubmitted: (String) -> Unit,
    errorMessage: String,
    cardInfo: CardInfo?
) {
    var cardBinState by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Отображаем сообщение об ошибке
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Text(
            text = stringResource(id = R.string.enter_bin),
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = cardBinState,
            onValueChange = { newValue -> cardBinState = newValue },
            label = { Text(stringResource(id = R.string.bin)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                onBinSubmitted(cardBinState)
                Log.d("MainScreen", "onBinSubmitted: $cardBinState")
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.submit))
        }

        // Отображения информации по карте
        Spacer(modifier = Modifier.height(16.dp))
        if (cardInfo != null) {
            Text(stringResource(id = R.string.scheme, cardInfo.scheme), fontSize = 16.sp)
            Text(stringResource(id = R.string.type, cardInfo.type), fontSize = 16.sp)
            Text(stringResource(id = R.string.brand, cardInfo.brand), fontSize = 16.sp)
            Text(
                stringResource(id = R.string.country_main, cardInfo.countryName, cardInfo.countryEmoji),
                fontSize = 16.sp
            )
            Text(stringResource(id = R.string.currency, cardInfo.countryCurrency), fontSize = 16.sp)
            Text(stringResource(id = R.string.bank, cardInfo.bankName), fontSize = 16.sp)

        } else {
            Text(stringResource(id = R.string.card_info_display), fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("history") }) {
            Text(text = stringResource(id = R.string.go_to_history))
        }

    }
}