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
import com.example.gateguardianapp.presentation.OopsieScreen
import com.example.gateguardianapp.presentation.SplashScreen
import com.example.gateguardianapp.presentation.auth.SignInScreen
import com.example.gateguardianapp.presentation.auth.SignInViewModel
import com.example.gateguardianapp.presentation.auth.googleclient.GoogleAuthClient
import com.example.gateguardianapp.presentation.navigation.AppSections
import com.example.gateguardianapp.presentation.resident.ResidentDrawer
import com.example.gateguardianapp.presentation.security.SecurityBottomBar
import com.example.gateguardianapp.ui.theme.GateGuardianAppTheme
import com.example.gateguardianapp.util.Delays
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
                var user: User? = User()

                LaunchedEffect(key1 = Unit) {
                    val authUser = googleAuthClient.getSignedInUser()
                    if(authUser != null) {
                        user = viewModel.getUserByEmail(authUser.email)
                        delay(Delays.SPLASH_DELAY)
                            if(user == null) {
                                navController.navigate(AppSections.Error.name) {
                                    popUpTo(0)
                                }
                            }

                        user?.let {
                            when(it.category.lowercase()) {
                                "resident", "admin" -> {
                                    navController.navigate(AppSections.Resident.name) {
                                        popUpTo(0)
                                    }
                                }
                                "security" -> {
                                    navController.navigate(AppSections.Security.name) {
                                        popUpTo(0)
                                    }
                                }
                                else -> {
                                    navController.navigate(AppSections.Error.name) {
                                        popUpTo(0)
                                    }
                                }
                            }
                        }

                    } else {
                        delay(Delays.SPLASH_DELAY)
                        navController.navigate(AppSections.SignIn.name)
                    }
                }

                NavHost(navController = navController, startDestination = AppSections.Splash.name) {

                    composable(AppSections.Splash.name) {
                        SplashScreen()
                    }

                    composable(AppSections.SignIn.name) {
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
                                user = viewModel.repository.getUserByEmail(email = googleAuthClient.getSignedInUser()?.email.toString())
                                if(user == null) {
                                    navController.navigate(AppSections.Error.name) {
                                        popUpTo(0)
                                    }
                                }
                                when(user?.category?.lowercase()) {
                                    "resident", "admin" -> {
                                        navController.navigate(AppSections.Resident.name) {
                                            popUpTo(0)
                                        }
                                    }
                                    "security" -> {
                                        navController.navigate(AppSections.Security.name) {
                                            popUpTo(0)
                                        }
                                    }
                                    else -> {
                                        navController.navigate(AppSections.Error.name) {
                                            popUpTo(0)
                                        }
                                    }
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

                    composable(AppSections.Resident.name) {
                        ResidentDrawer(
                            user = user!!,
                            onSignOut = {
                                lifecycleScope.launch {
                                    googleAuthClient.signOut()
                                    Toast.makeText(
                                        applicationContext,
                                        "Signed out",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate(AppSections.SignIn.name) {
                                        popUpTo(0)
                                    }
                                }
                            }
                        )
                    }

                    composable(AppSections.Security.name) {
                        SecurityBottomBar(
                            user = user!!,
                            onSignOut = {
                                lifecycleScope.launch {
                                    googleAuthClient.signOut()
                                    Toast.makeText(
                                        applicationContext,
                                        "Signed out",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate(AppSections.SignIn.name) {
                                        popUpTo(0)
                                    }
                                }
                            }
                        )
                    }

                    composable(AppSections.Error.name) {
                        OopsieScreen(
                            backToSignIn = {
                                navController.navigate(AppSections.SignIn.name) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}