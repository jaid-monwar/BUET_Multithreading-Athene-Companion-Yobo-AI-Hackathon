package com.google.mediapipe.examples.llminference

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.shape.RoundedCornerShape
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.foundation.ScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import com.google.mediapipe.examples.llminference.InferenceModel


@Composable
fun LoadingRoute(
    onModelLoaded: (String) -> Unit
) {
    val modelsList = InferenceModel.listAvailableModels()
    var selectedModelIndex by rememberSaveable { mutableStateOf(-1) }
    var showDropdown by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = "Select a model to load:", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier
                .border(1.dp, Color.Gray)
                .clickable { showDropdown = true }
                .padding(12.dp)) {
                Text(text = if (selectedModelIndex >= 0) modelsList[selectedModelIndex] else "Tap here to select a model")
            }

            DropdownMenu(
                showDropdown = showDropdown,
                itemList = modelsList,
                onDismissRequest = { showDropdown = false },
                onItemSelect = { index ->
                    selectedModelIndex = index
                    showDropdown = false
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (selectedModelIndex >= 0) {
                        onModelLoaded(modelsList[selectedModelIndex])
                    }
                },
                enabled = selectedModelIndex >= 0
            ) {
                Text("Load Selected Model")
            }
        }
    }
}

@Composable
fun DropdownMenu(
    showDropdown: Boolean,
    itemList: List<String>,
    onDismissRequest: () -> Unit,
    onItemSelect: (Int) -> Unit
) {

    val bubbleShape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
    val backgroundColor = MaterialTheme.colorScheme.primaryContainer

    if (showDropdown) {
        Popup(
            alignment = Alignment.TopStart,
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(focusable = true)
        ) {
            Card(
                shape = bubbleShape,
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                modifier = Modifier.padding(10.dp)
            ) {
                itemList.forEachIndexed { index, modelName ->
                    Text(
                        text = modelName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onItemSelect(index)
                            }
                            .padding(12.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
