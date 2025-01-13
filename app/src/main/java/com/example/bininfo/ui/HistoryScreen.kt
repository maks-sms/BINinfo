package com.example.bininfo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bininfo.R
import com.example.bininfo.viewmodel.HistoryViewModel
import com.example.bininfo.data.HistoryItem

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = viewModel(), navController: NavHostController) {

    val historyItems by viewModel.historyItems.observeAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.history_of_bin_requests),
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(historyItems) { item: HistoryItem ->
                HistoryCard(historyItem = item)
            }
        }

        Button(onClick = { navController.popBackStack() }) {
            Text(text = stringResource(id = R.string.back_to_main_screen))
        }

// Если история пуста, показываем сообщение
        if (historyItems.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_history_found),
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun HistoryCard(historyItem: HistoryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(id = R.string.bin_history, historyItem.bin), fontSize = 18.sp)
            Text(text = stringResource(id = R.string.brand, historyItem.brand), fontSize = 16.sp)
            Text(text = stringResource(id = R.string.country_history, historyItem.country), fontSize = 16.sp)

        }

    }
}

