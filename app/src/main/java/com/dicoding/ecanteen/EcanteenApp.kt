package com.dicoding.ecanteen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dicoding.ecanteen.data.di.Injection
import com.dicoding.ecanteen.data.local.pref.UserModel
import com.dicoding.ecanteen.data.local.pref.UserPreference
import com.dicoding.ecanteen.data.local.pref.dataStore
import com.dicoding.ecanteen.ui.navigaion.Screen
import com.dicoding.ecanteen.ui.screen.login.LoginPembeliScreen
import com.dicoding.ecanteen.ui.screen.login.LoginPenjualScreen
import com.dicoding.ecanteen.ui.screen.login.LoginViewModel
import com.dicoding.ecanteen.ui.screen.register.RegisterPembeliScreen
import com.dicoding.ecanteen.ui.screen.register.RegisterPenjualScreen
import com.dicoding.ecanteen.ui.screen.register.RegisterViewModel
import com.dicoding.ecanteen.ui.screen.splash.SplashScreen
import com.dicoding.ecanteen.ui.screen.welcome.WelcomeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@Composable
fun EcanteenApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val userRepository = Injection.provideRepository()
    val context = LocalContext.current
    val userPreference = remember { UserPreference.getInstance(context.dataStore) }
    var user by remember { mutableStateOf<UserModel?>(null) }

    LaunchedEffect(userPreference) {
        val userFlow = userPreference.getSession().first()
        user = userFlow
        delay(2000)
        navigateToNextScreen(navController, user)
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navigateToNextScreen(navController, user)
                }
            )
        }
        composable(Screen.MainPembeli.route) {
            MainPembeliScreen()
        }
        composable(Screen.MainPenjual.route) {
            MainPenjualScreen()
        }
        composable(Screen.RegisterPenjual.route) {
            val registerViewModel = RegisterViewModel(userRepository)
            RegisterPenjualScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onButtonClick = {
                    navController.navigate(Screen.LoginPenjual.route) {
                        popUpTo(Screen.Welcome.route) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                viewModel = registerViewModel
            )
        }
        composable(Screen.RegisterPembeli.route) {
            val registerViewModel = RegisterViewModel(userRepository)
            RegisterPembeliScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onButtonClick = {
                    navController.navigate(Screen.LoginPembeli.route) {
                        popUpTo(Screen.Welcome.route) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                viewModel = registerViewModel
            )
        }
        composable(Screen.LoginPenjual.route) {
            val loginViewModel = LoginViewModel(userRepository)
            LoginPenjualScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onRegisterClick = {
                    navController.navigate(Screen.RegisterPenjual.route) {
                        popUpTo(Screen.Welcome.route) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onButtonLoginClick = {
                    navController.navigate(Screen.MainPenjual.route)
                },
                viewModel = loginViewModel
            )
        }
        composable(Screen.LoginPembeli.route) {
            val loginViewModel = LoginViewModel(userRepository)
            LoginPembeliScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onRegisterClick = {
                    navController.navigate(Screen.RegisterPembeli.route) {
                        popUpTo(Screen.Welcome.route) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onButtonLoginClick = {
                    navController.navigate(Screen.MainPembeli.route)
                },
                viewModel = loginViewModel
            )
        }
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                navigateToRegisterPenjual = {
                    navController.navigate(Screen.RegisterPenjual.route) {

                    }
                },
                navigateToRegisterPembeli = {
                    navController.navigate(Screen.RegisterPembeli.route) {

                    }
                },
                navigateToLoginPenjual = {
                    navController.navigate(Screen.LoginPenjual.route) {

                    }
                },
                navigateToLoginPembeli = {
                    navController.navigate(Screen.LoginPembeli.route) {

                    }
                }
            )
        }
    }
}

private fun navigateToNextScreen(navController: NavHostController, user: UserModel?) {
    val destination = when {
        user == null -> Screen.Welcome.route
        user.isLogin && user.userType == "pembeli" -> Screen.MainPembeli.route
        user.isLogin && user.userType == "penjual" -> Screen.MainPenjual.route
        else -> Screen.Welcome.route
    }

    navController.navigate(destination) {
        popUpTo(Screen.Splash.route) { inclusive = true }
    }
}
