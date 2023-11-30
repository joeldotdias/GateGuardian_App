package com.example.gateguardianapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gateguardianapp.domain.model.User
import com.example.gateguardianapp.presentation.SplashScreen
import com.example.gateguardianapp.presentation.auth.SecScreen
import com.example.gateguardianapp.presentation.auth.SignInScreen
import com.example.gateguardianapp.presentation.auth.SignInViewModel
import com.example.gateguardianapp.presentation.auth.googleclient.GoogleAuthClient
import com.example.gateguardianapp.presentation.resident.ResidentDrawer
import com.example.gateguardianapp.ui.theme.GateGuardianAppTheme
import com.example.gateguardianapp.util.Constants
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GateGuardianAppTheme {

                val viewModel = hiltViewModel<SignInViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                val navController = rememberNavController()
                var user = User()

                LaunchedEffect(key1 = Unit) {
                    val authUser = googleAuthClient.getSignedInUser()
                    if(authUser != null) {
                        user = viewModel.getUserByEmail(authUser.email)
                        delay(Constants.SPLASH_DELAY)
                        when(user.category.lowercase()) {
                            "resident" -> { navController.navigate("res") }
                            "admin" -> { navController.navigate("res") }
                            "security" -> { navController.navigate("sec") }
                        }
                    } else {
                        delay(Constants.SPLASH_DELAY)
                        navController.navigate("sign_in")
                    }
                }

                NavHost(navController = navController, startDestination = "splash") {

                    composable("splash") {
                        SplashScreen()
                    }

                    composable("sign_in") {
                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = { result ->
                                if(result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        viewModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )

                        LaunchedEffect(key1 = state.isSignInSuccessful) {
                            if(state.isSignInSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Sign In Successful",
                                    Toast.LENGTH_LONG
                                ).show()
                                user = viewModel.repository.getUserByEmail(email = googleAuthClient.getSignedInUser()?.email.toString())!!
                                when(user.category.lowercase()) {
                                    "resident" -> { navController.navigate("res") }
                                    "admin" -> { navController.navigate("res") }
                                    "security" -> { navController.navigate("sec") }
                                }
                                viewModel.resetState()
                            }
                        }

                        SignInScreen(
                            state = state,
                            onSignInClick = {
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthClient.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }
                        )
                    }

                    composable("res") {
                        ResidentDrawer(user = user,
                            onSignOut = {lifecycleScope.launch {
                                googleAuthClient.signOut()
                                Toast.makeText(applicationContext, "Signed out", Toast.LENGTH_LONG).show()
                                navController.navigate("sign_in")
                            }}
                        )
                    }

                    composable("sec") {
                        SecScreen(user = user, onSignOut = { lifecycleScope.launch {
                            googleAuthClient.signOut()
                            Toast.makeText(applicationContext, "Signed out", Toast.LENGTH_LONG).show()
                            navController.navigate("sign_in")
                        } })
                    }
                }
             }
        }
    }
}