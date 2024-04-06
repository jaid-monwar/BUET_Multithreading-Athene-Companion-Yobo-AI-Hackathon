package com.google.mediapipe.examples.llminference

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.mediapipe.examples.llminference.ui.theme.LLMInferenceTheme
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.google.mediapipe.examples.llminference.ui.theme.Dropdown_demoTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.mediapipe.examples.llminference.ui.theme.LLMInferenceTheme



const val START_SCREEN = "start_screen"
const val CHAT_SCREEN = "chat_screen/{modelName}" // Route with argument placeholder
const val MODEL_NAME_ARG = "modelName" // Argument name

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LLMInferenceTheme {
                Scaffold(topBar = { AppBar() }) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        val navController = rememberNavController()

                        NavHost(navController = navController, startDestination = START_SCREEN) {
                            composable(START_SCREEN) {
                                // Pass a lambda that navigates to CHAT_SCREEN with the selected modelName
                                LoadingRoute { modelName ->
                                    navController.navigate("$CHAT_SCREEN/$modelName")
                                }
                            }

                            // Define the composable for CHAT_SCREEN, including the argument
                            composable(
                                "$CHAT_SCREEN/{$MODEL_NAME_ARG}",
                                arguments = listOf(navArgument(MODEL_NAME_ARG) { type = NavType.StringType })
                            ) { backStackEntry ->
                                // Retrieve the modelName argument to pass to ChatRoute
                                val modelName = backStackEntry.arguments?.getString(MODEL_NAME_ARG) ?: ""
                                ChatRoute(modelName)
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppBar() {
        Column(modifier = Modifier.fillMaxWidth()) {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
                Text(
                      text = stringResource(R.string.disclaimer),
//                   text = "Model in-use: $modelName",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
            }
        }
    }
}



