package com.delightroom.delightplayer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delightroom.core_ui.ErrorPlaceholder
import com.delightroom.delightplayer.ui.theme.DelightPlayerTheme
import com.delightroom.feature_list.ui.ListDestination
import com.delightroom.feature_list.ui.ListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var scaffoldPadding: Modifier = Modifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            DelightPlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    scaffoldPadding = Modifier.padding(innerPadding)

                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_AUDIO
                            else Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        AppNavHost(
                            modifier = scaffoldPadding
                        )
                    } else {
                        requestAudioPermission()
                    }
                }
            }
        }
    }

    fun requestAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
                100
            )
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == 100) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                setContent { AppNavHost() }
            } else {
                setContent {
                    ErrorPlaceholder(
                        modifier = scaffoldPadding,
                        errorMessage = "앱을 사용하기 위해서는 권한이 필요합니다.") {
                        requestAudioPermission()
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ListDestination,
        modifier = modifier
    ) {
        composable<ListDestination> {
            ListScreen()
        }
    }
}

