package com.dicoding.ecanteen

import EwalletPenjualViewModel
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dicoding.ecanteen.data.di.Injection
import com.dicoding.ecanteen.ui.navigaion.NavigationItem
import com.dicoding.ecanteen.ui.navigaion.Screen
import com.dicoding.ecanteen.ui.screen.about.AboutScreen
import com.dicoding.ecanteen.ui.screen.crudmenu.AddMenuScreen
import com.dicoding.ecanteen.ui.screen.crudmenu.AddMenuViewModel
import com.dicoding.ecanteen.ui.screen.crudmenu.EditMenuScreen
import com.dicoding.ecanteen.ui.screen.detail.DetailMenuPenjual
import com.dicoding.ecanteen.ui.screen.detailpesanan.DetailPesananPenjualScreen
import com.dicoding.ecanteen.ui.screen.detailpesanan.DetailPesananPenjualViewModel
import com.dicoding.ecanteen.ui.screen.ewallet.EwalletPenjualScreen
import com.dicoding.ecanteen.ui.screen.history.HistoryPenjualScreen
import com.dicoding.ecanteen.ui.screen.history.HistoryPenjualViewModel
import com.dicoding.ecanteen.ui.screen.home.EditMenuViewModel
import com.dicoding.ecanteen.ui.screen.home.HomePenjualScreen
import com.dicoding.ecanteen.ui.screen.home.HomeViewModel
import com.dicoding.ecanteen.ui.screen.orders.OrdersPenjualScreen
import com.dicoding.ecanteen.ui.screen.profile.EditProfilePenjualScreen
import com.dicoding.ecanteen.ui.screen.profile.EditProfilePenjualViewModel
import com.dicoding.ecanteen.ui.screen.profile.ProfilePenjualScreen
import com.dicoding.ecanteen.ui.screen.profile.ProfileViewModel
import com.dicoding.ecanteen.ui.screen.transaction.OrdersPenjualViewModel

@Composable
fun MainPenjualScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("CurrentRoute", currentRoute ?: "No route")
    val userRepository = Injection.provideRepository()
    val profileViewModel = ProfileViewModel(userRepository)
    val ewalletViewModel = EwalletPenjualViewModel(userRepository)
    val editMenuViewModel = EditMenuViewModel(userRepository)
    val homeViewModel = HomeViewModel(userRepository)
    val addMenuViewModel = AddMenuViewModel(userRepository)
    val ordersPenjualViewModel = OrdersPenjualViewModel(userRepository)
    val detailPesananPenjualViewModel = DetailPesananPenjualViewModel(userRepository)
    val editProfilePenjualViewModel = EditProfilePenjualViewModel(userRepository)
    val historyPenjualViewModel = HistoryPenjualViewModel(userRepository)

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Initial.route
                && currentRoute != Screen.About.route
                && currentRoute != Screen.HistoryPenjual.route
                && currentRoute != Screen.AddMenu.route
                && currentRoute != Screen.EditMenu.route
                && currentRoute != Screen.DetailMenuPenjual.route
                && currentRoute != Screen.DetailPesananPenjual.route
                && currentRoute != Screen.EditProfilePenjual.route
            ) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.HomePenjual.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.HomePenjual.route) {
                HomePenjualScreen(
                    viewModel = homeViewModel,
                    onAddClick = {
                        navController.navigate(Screen.AddMenu.route)
                    },
                    onEditClick = { menuId ->
                        val route = Screen.EditMenu.createRoute(menuId)
                        Log.d("NavigateTo", route)
                        navController.navigate(route)
                    },
                    onMenuClick = { menuId ->
                        val route = Screen.DetailMenuPenjual.createRoute(menuId)
                        Log.d("NavigateTo", route)
                        navController.navigate(route)
                    }
                )
            }
            composable(
                route = Screen.EditMenu.route,
                arguments = listOf(navArgument("menuId") { type = NavType.IntType }),
            ) {
                val id = it.arguments?.getInt("menuId") ?: -1
                EditMenuScreen(
                    menuId = id,
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onButtonClick = {
                        navController.navigate(Screen.HomePenjual.route)
                    },
                    profileViewModel = profileViewModel,
                    editMenuViewModel = editMenuViewModel
                )
            }
            composable(
                route = Screen.DetailMenuPenjual.route,
                arguments = listOf(navArgument("menuId") { type = NavType.IntType }),
            ) {
                val id = it.arguments?.getInt("menuId") ?: -1
                DetailMenuPenjual(
                    menuId = id,
                    onBackClick = {
                        navController.navigateUp()
                    },

                    editMenuViewModel = editMenuViewModel
                )
            }
            composable(Screen.OrdersPenjual.route) {
                OrdersPenjualScreen(
                    ordersPenjualViewModel = ordersPenjualViewModel,
                    profileViewModel = profileViewModel,
                    onDetailPesananPenjual = { pesananId ->
                        val route = Screen.DetailPesananPenjual.createRoute(pesananId)
                        Log.d("NavigateTo", route)
                        navController.navigate(route)
                    },
                )
            }
            composable(Screen.EwalletPenjual.route) {
                EwalletPenjualScreen(
                    profileViewModel = profileViewModel,
                    ewalletViewModel = ewalletViewModel,
                )
            }
            composable(Screen.ProfilePenjual.route) {
                ProfilePenjualScreen(
                    onLogoutClick = {
                        navController.navigate(Screen.Initial.route)
                    },
                    onAboutClick = {
                        navController.navigate(Screen.About.route)
                    },
                    onHistoryClick = {
                        navController.navigate(Screen.HistoryPenjual.route)
                    },
                    viewModel = profileViewModel,
                    onEditProfile = { profilePenjualModel ->
                        val route = Screen.EditProfilePenjual.createRoute(
                            id = profilePenjualModel.id,
                            fullName = profilePenjualModel.fullName,
                            email = profilePenjualModel.email,
                            telp = profilePenjualModel.telp,
                            namaToko = profilePenjualModel.namaToko,
                            deskripsi = profilePenjualModel.deskripsi,
                        )
                        navController.navigate(route)
                    }
                )
            }
            composable(
                route = Screen.EditProfilePenjual.route,
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType },
                    navArgument("fullName") { type = NavType.StringType },
                    navArgument("email") { type = NavType.StringType },
                    navArgument("telp") { type = NavType.StringType },
                    navArgument("namaToko") { type = NavType.StringType },
                    navArgument("deskripsi") { type = NavType.StringType },
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: -1
                val fullName = backStackEntry.arguments?.getString("fullName") ?: ""
                val email = backStackEntry.arguments?.getString("email") ?: ""
                val telp = backStackEntry.arguments?.getString("telp") ?: ""
                val namaToko = backStackEntry.arguments?.getString("namaToko") ?: ""
                val deskripsi = backStackEntry.arguments?.getString("deskripsi") ?: ""

                EditProfilePenjualScreen(
                    id = id,
                    fullName = fullName,
                    email = email,
                    telp = telp,
                    namaToko = namaToko,
                    deskripsi = deskripsi,
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onButtonClick = {
                        navController.navigate(Screen.ProfilePenjual.route){
                            popUpTo(Screen.HomePenjual.route)
                        }
                    },
                    editProfilePenjualViewModel = editProfilePenjualViewModel
                )
            }
            composable(Screen.Initial.route) {
                EcanteenApp()
            }
            composable(Screen.About.route) {
                AboutScreen(
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
            composable(Screen.HistoryPenjual.route) {
                HistoryPenjualScreen(
                    profileViewModel = profileViewModel,
                    historyPenjualViewModel = historyPenjualViewModel,
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
            composable(Screen.AddMenu.route) {
                AddMenuScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onButtonClick = {
                        navController.navigate(Screen.HomePenjual.route)
                    },
                    viewModel = addMenuViewModel,
                    profileViewModel = profileViewModel
                )
            }
            composable(
                route = Screen.DetailPesananPenjual.route,
                arguments = listOf(navArgument("pesananId") { type = NavType.IntType }),
            ) {
                val id = it.arguments?.getInt("pesananId") ?: -1
                DetailPesananPenjualScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    pesananId = id,
                    detailPesananPenjualViewModel = detailPesananPenjualViewModel
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.HomePenjual
            ),
            NavigationItem(
                title = stringResource(R.string.menu_orders),
                icon = Icons.Default.ShoppingCart,
                screen = Screen.OrdersPenjual
            ),
            NavigationItem(
                title = stringResource(R.string.ewallet),
                icon = Icons.Default.CheckCircle,
                screen = Screen.EwalletPenjual
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Screen.ProfilePenjual
            ),
        )
        navigationItems.map { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.outline,
                    unselectedTextColor = MaterialTheme.colorScheme.outline,
                )
            )
        }
    }
}