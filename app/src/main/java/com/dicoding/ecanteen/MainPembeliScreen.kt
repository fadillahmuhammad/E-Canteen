package com.dicoding.ecanteen

import EwalletPembeliViewModel
import SuccessOrderScreen
import SuccessTopUpScreen
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
import com.dicoding.ecanteen.ui.screen.detail.DetailMenuPembeli
import com.dicoding.ecanteen.ui.screen.detailpesanan.DetailPesananPembeliScreen
import com.dicoding.ecanteen.ui.screen.detailpesanan.DetailPesananPembeliViewModel
import com.dicoding.ecanteen.ui.screen.detailpesanan.TransactionScreen
import com.dicoding.ecanteen.ui.screen.ewallet.EwalletPembeliScreen
import com.dicoding.ecanteen.ui.screen.history.HistoryPembeliScreen
import com.dicoding.ecanteen.ui.screen.history.HistoryPembeliViewModel
import com.dicoding.ecanteen.ui.screen.home.EditMenuViewModel
import com.dicoding.ecanteen.ui.screen.home.HomePembeliScreen
import com.dicoding.ecanteen.ui.screen.home.HomeViewModel
import com.dicoding.ecanteen.ui.screen.orders.OrdersPembeliScreen
import com.dicoding.ecanteen.ui.screen.pesanmenu.menuorder.MenuOrderScreen
import com.dicoding.ecanteen.ui.screen.pesanmenu.menuorder.MenuOrderViewModel
import com.dicoding.ecanteen.ui.screen.pesanmenu.transaction.TransactionViewModel
import com.dicoding.ecanteen.ui.screen.profile.EditProfilePembeliScreen
import com.dicoding.ecanteen.ui.screen.profile.EditProfilePembeliViewModel
import com.dicoding.ecanteen.ui.screen.profile.ProfilePembeliScreen
import com.dicoding.ecanteen.ui.screen.profile.ProfileViewModel
import com.dicoding.ecanteen.ui.screen.topup.TopUpScreen
import com.dicoding.ecanteen.ui.screen.topup.TopUpViewModel
import com.dicoding.ecanteen.ui.screen.transaction.OrdersPembeliViewModel

@Composable
fun MainPembeliScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("CurrentRoute", currentRoute ?: "No route")
    val userRepository = Injection.provideRepository()

    val homeViewModel = HomeViewModel(userRepository)
    val profileViewModel = ProfileViewModel(userRepository)
    val ewalletViewModel = EwalletPembeliViewModel(userRepository)
    val editMenuViewModel = EditMenuViewModel(userRepository)
    val menuOrderViewModel = MenuOrderViewModel(userRepository)
    val topUpViewModel = TopUpViewModel(userRepository)
    val transactionViewModel = TransactionViewModel(userRepository)
    val ordersPembeliViewModel = OrdersPembeliViewModel(userRepository)
    val detailPesananPembeliViewModel = DetailPesananPembeliViewModel(userRepository)
    val editProfilePembeliViewModel = EditProfilePembeliViewModel(userRepository)
    val historyPembeliViewModel = HistoryPembeliViewModel(userRepository)

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Initial.route
                && currentRoute != Screen.About.route
                && currentRoute != Screen.HistoryPembeli.route
                && currentRoute != Screen.DetailMenuPembeli.route
                && currentRoute != Screen.MenuOrder.route
                && currentRoute != Screen.Transaction.route
                && currentRoute != Screen.EditProfilePembeli.route
                && currentRoute != Screen.TopUp.route
                && currentRoute != Screen.SuccessTopUp.route
                && currentRoute != Screen.DetailPesananPembeli.route
                && currentRoute != Screen.SuccessOrder.route
            ) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.HomePembeli.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.HomePembeli.route) {
                HomePembeliScreen(
                    viewModel = homeViewModel,
                    onMenuClick = { menuId ->
                        val route = Screen.DetailMenuPembeli.createRoute(menuId)
                        Log.d("NavigateTo", route)
                        navController.navigate(route)
                    },
                    onPesanClick = { menuId ->
                        val route = Screen.MenuOrder.createRoute(menuId)
                        Log.d("NavigateTo", route)
                        navController.navigate(route)
                    },
                )
            }
            composable(Screen.OrdersPembeli.route) {
                OrdersPembeliScreen(
                    ordersPembeliViewModel = ordersPembeliViewModel,
                    profileViewModel = profileViewModel,
                    onDetailPesananPembeli = { pesananId ->
                        val route = Screen.DetailPesananPembeli.createRoute(pesananId)
                        Log.d("NavigateTo", route)
                        navController.navigate(route)
                    },
                    onBackClick = {
                        navController.navigate(Screen.HomePembeli.route) {
                            popUpTo(Screen.HomePembeli.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.ProfilePembeli.route) {
                ProfilePembeliScreen(
                    onLogoutClick = {
                        navController.navigate(Screen.Initial.route)
                    },
                    onAboutClick = {
                        navController.navigate(Screen.About.route)
                    },
                    onHistoryClick = {
                        navController.navigate(Screen.HistoryPembeli.route)
                    },
                    viewModel = profileViewModel,
                    onEditProfile = { profilePembeliModel ->
                        val route = Screen.EditProfilePembeli.createRoute(
                            id = profilePembeliModel.id,
                            fullName = profilePembeliModel.fullName,
                            email = profilePembeliModel.email,
                            telp = profilePembeliModel.telp,
                        )
                        navController.navigate(route)
                    }
                )
            }
            composable(Screen.Initial.route) {
                EcanteenApp()
            }
            composable(Screen.HistoryPembeli.route) {
                HistoryPembeliScreen(
                    profileViewModel = profileViewModel,
                    historyPembeliViewModel = historyPembeliViewModel,
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
            composable(Screen.EwalletPembeli.route) {
                EwalletPembeliScreen(
                    profileViewModel = profileViewModel,
                    ewalletViewModel = ewalletViewModel,
                    onTopUpScreen = { saldoId ->
                        val route = Screen.TopUp.createRoute(saldoId)
                        Log.d("NavigateTo", route)
                        navController.navigate(route)
                    },
                )
            }
            composable(Screen.About.route) {
                AboutScreen(
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
            composable(
                route = Screen.DetailMenuPembeli.route,
                arguments = listOf(navArgument("menuId") { type = NavType.IntType }),
            ) {
                val id = it.arguments?.getInt("menuId") ?: -1
                DetailMenuPembeli(
                    menuId = id,
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onMenuOrder = { menuId ->
                        val route = Screen.MenuOrder.createRoute(menuId)
                        Log.d("NavigateTo", route)
                        navController.navigate(route)
                    },
                    editMenuViewModel = editMenuViewModel,
                )
            }
            composable(
                route = Screen.MenuOrder.route,
                arguments = listOf(navArgument("menuId") { type = NavType.IntType }),
            ) {
                val id = it.arguments?.getInt("menuId") ?: -1
                MenuOrderScreen(
                    menuId = id,
                    onBackClick = {
                        navController.navigateUp()
                    },
                    editMenuViewModel = editMenuViewModel,
                    menuOrderViewModel = menuOrderViewModel,
                    profileViewModel = profileViewModel,
                    onTransaction = { transactionModel ->
                        val route = Screen.Transaction.createRoute(
                            orderId = transactionModel.orderId,
                            menuId = transactionModel.menuId,
                            nama = transactionModel.nama,
                            gambar = transactionModel.gambar,
                            pembeliId = transactionModel.pembeliId,
                            penjualId = transactionModel.penjualId,
                            quantity = transactionModel.quantity,
                            catatan = transactionModel.catatan,
                            jam_pesan = transactionModel.jam_pesan,
                            harga = transactionModel.harga,
                            jml_harga = transactionModel.jml_harga,
                            status = transactionModel.status
                        )
                        navController.navigate(route)
                    }
                )
            }

            composable(
                route = Screen.Transaction.route,
                arguments = listOf(
                    navArgument("orderId") { type = NavType.StringType },
                    navArgument("menuId") { type = NavType.IntType },
                    navArgument("nama") { type = NavType.StringType },
                    navArgument("gambar") { type = NavType.StringType },
                    navArgument("pembeliId") { type = NavType.IntType },
                    navArgument("penjualId") { type = NavType.IntType },
                    navArgument("quantity") { type = NavType.IntType },
                    navArgument("catatan") { type = NavType.StringType },
                    navArgument("jam_pesan") { type = NavType.StringType },
                    navArgument("harga") { type = NavType.IntType },
                    navArgument("jml_harga") { type = NavType.IntType },
                    navArgument("status") { type = NavType.BoolType }
                )
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                val menuId = backStackEntry.arguments?.getInt("menuId") ?: -1
                val nama = backStackEntry.arguments?.getString("nama") ?: ""
                val gambar = backStackEntry.arguments?.getString("gambar") ?: ""
                val pembeliId = backStackEntry.arguments?.getInt("pembeliId") ?: -1
                val penjualId = backStackEntry.arguments?.getInt("penjualId") ?: -1
                val quantity = backStackEntry.arguments?.getInt("quantity") ?: 0
                val catatan = backStackEntry.arguments?.getString("catatan") ?: ""
                val jam_pesan = backStackEntry.arguments?.getString("jam_pesan") ?: ""
                val harga = backStackEntry.arguments?.getInt("harga") ?: 0
                val jml_harga = backStackEntry.arguments?.getInt("jml_harga") ?: 0
                val status = backStackEntry.arguments?.getBoolean("status") ?: false

                TransactionScreen(
                    ewalletViewModel = ewalletViewModel,
                    transactionViewModel = transactionViewModel,
                    orderId = orderId,
                    menuId = menuId,
                    nama = nama,
                    gambar = gambar,
                    pembeliId = pembeliId,
                    penjualId = penjualId,
                    quantity = quantity,
                    catatan = catatan,
                    jam_pesan = jam_pesan,
                    harga = harga,
                    jml_harga = jml_harga,
                    status = status,
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onSuccessOrderScreen = {
                        navController.navigate(Screen.SuccessOrder.route)
                    }
                )
            }
            composable(
                route = Screen.EditProfilePembeli.route,
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType },
                    navArgument("fullName") { type = NavType.StringType },
                    navArgument("email") { type = NavType.StringType },
                    navArgument("telp") { type = NavType.StringType },
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: -1
                val fullName = backStackEntry.arguments?.getString("fullName") ?: ""
                val email = backStackEntry.arguments?.getString("email") ?: ""
                val telp = backStackEntry.arguments?.getString("telp") ?: ""

                EditProfilePembeliScreen(
                    id = id,
                    fullName = fullName,
                    email = email,
                    telp = telp,
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onButtonClick = {
                        navController.navigate(Screen.ProfilePembeli.route){
                            popUpTo(Screen.HomePembeli.route)
                        }
                    },
                    editProfilePembeliViewModel = editProfilePembeliViewModel
                )
            }
            composable(Screen.SuccessTopUp.route) {
                SuccessTopUpScreen(
                    onDompetScreen = {
                        navController.navigate(Screen.EwalletPembeli.route){
                            popUpTo(Screen.HomePembeli.route)
                        }
                    }
                )
            }
            composable(Screen.SuccessOrder.route) {
                SuccessOrderScreen(
                    onOrdersScreen = {
                        navController.navigate(Screen.OrdersPembeli.route){
                            popUpTo(Screen.HomePembeli.route)  { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = Screen.TopUp.route,
                arguments = listOf(navArgument("saldoId") { type = NavType.IntType }),
            ) {
                val id = it.arguments?.getInt("saldoId") ?: -1
                TopUpScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    saldoId = id,
                    topUpViewModel = topUpViewModel,
                    profileViewModel = profileViewModel,
                    onSuccessScreen = {
                        navController.navigate(Screen.SuccessTopUp.route)
                    }
                )
            }
            composable(
                route = Screen.DetailPesananPembeli.route,
                arguments = listOf(navArgument("pesananId") { type = NavType.IntType }),
            ) {
                val id = it.arguments?.getInt("pesananId") ?: -1
                DetailPesananPembeliScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    pesananId = id,
                    detailPesananPembeliViewModel = detailPesananPembeliViewModel
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
                screen = Screen.HomePembeli
            ),
            NavigationItem(
                title = stringResource(R.string.menu_orders),
                icon = Icons.Default.ShoppingCart,
                screen = Screen.OrdersPembeli
            ),
            NavigationItem(
                title = stringResource(R.string.ewallet),
                icon = Icons.Default.CheckCircle,
                screen = Screen.EwalletPembeli
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Screen.ProfilePembeli
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