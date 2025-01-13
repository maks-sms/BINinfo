package com.example.bininfo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bininfo.ui.MainScreen
import com.example.bininfo.adapters.CardInfo
import com.example.bininfo.data.HistoryItem
import com.example.bininfo.data.AppDatabase
import com.example.bininfo.ui.HistoryScreen
import com.example.bininfo.viewmodel.HistoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : ComponentActivity() {

    private var cardInfo by mutableStateOf<CardInfo?>(null)
    private var errorMessage by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            NavHost(navController, startDestination = "main") {
                composable("main") {
                    MainScreen(
                        navController,
                        onBinSubmitted = { bin -> requestCardData(bin) },
                        errorMessage = errorMessage,
                        cardInfo = cardInfo
                    )
                }
                composable("history") {
                    val viewModel: HistoryViewModel = viewModel()
                    HistoryScreen(viewModel = viewModel, navController)
                }
            }
        }
    }

    private fun requestCardData(bin: String) {
        val url = "$URL$bin"
        val queue = Volley.newRequestQueue(this)

        val request = StringRequest(Request.Method.GET, url, { result ->
            Log.d("MainActivity", "Response: $result")
            parseCardData(result, bin)
        }, { error ->
            Log.e("MainActivity", "Error: ${error.message}")
            errorMessage = getString(R.string.error)
        })

        queue.add(request)
    }

    private fun parseCardData(result: String, bin: String) {
        try {
            val mainObject = JSONObject(result)
            cardInfo = CardInfo(
                getCardData(mainObject, JSON_KEY_NUMBER, "length", true),
                getCardData(mainObject, JSON_KEY_NUMBER, "luhn", true),
                getCardData(mainObject, EMPTY_STRING_VALUE, "scheme", false),
                getCardData(mainObject, EMPTY_STRING_VALUE, "type", false),
                getCardData(mainObject, EMPTY_STRING_VALUE, "brand", false),
                getCardData(mainObject, EMPTY_STRING_VALUE, "prepaid", false),
                getCardData(mainObject, JSON_KEY_COUNTRY, "numeric", true),
                getCardData(mainObject, JSON_KEY_COUNTRY, "alpha2", true),
                getCardData(mainObject, JSON_KEY_COUNTRY, "name", true),
                getCardData(mainObject, JSON_KEY_COUNTRY, "emoji", true),
                getCardData(mainObject, JSON_KEY_COUNTRY, "currency", true),
                getCardData(mainObject, JSON_KEY_COUNTRY, "latitude", true),
                getCardData(mainObject, JSON_KEY_COUNTRY, "longitude", true),
                getCardData(mainObject, JSON_KEY_BANK, "name", true),
                getCardData(mainObject, JSON_KEY_BANK, "url", true),
                getCardData(mainObject, JSON_KEY_BANK, "phone", true),
                getCardData(mainObject, JSON_KEY_BANK, "city", true)
            )

            // Создание истории запроса
            val historyItem = HistoryItem(
                bin = bin,
                brand = getCardData(mainObject, EMPTY_STRING_VALUE, "brand", false),
                country = getCardData(mainObject, JSON_KEY_COUNTRY, "name", true)
            )

            // Вставка записи в базу данных в корутине
            val historyDao = AppDatabase.getDb(applicationContext).getHistoryDao()
            CoroutineScope(Dispatchers.IO).launch {
                historyDao.insert(historyItem)
            }

            errorMessage = ""
        } catch (ex: Exception) {
            Log.e("MainActivity", "Error parsing JSON: ${ex.message}")
            errorMessage = getString(R.string.error)
        }
    }

    private fun getCardData(
        mainObject: JSONObject,
        jsonObject: String,
        jsonString: String,
        getJSONobj: Boolean
    ): String {
        return if (getJSONobj) {
            mainObject.optJSONObject(jsonObject)?.optString(jsonString, EMPTY_STRING_VALUE)
                ?: EMPTY_STRING_VALUE
        } else {
            mainObject.optString(jsonString, EMPTY_STRING_VALUE)
        }
    }

    companion object {
        const val EMPTY_STRING_VALUE = ""
        const val URL = "https://lookup.binlist.net/"
        const val JSON_KEY_NUMBER = "number"
        const val JSON_KEY_COUNTRY = "country"
        const val JSON_KEY_BANK = "bank"
    }
}