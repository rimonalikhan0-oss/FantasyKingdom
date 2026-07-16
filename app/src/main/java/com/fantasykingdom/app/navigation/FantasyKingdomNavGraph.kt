package com.fantasykingdom.app.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.fantasykingdom.app.ui.auth.LoginScreen
import com.fantasykingdom.app.ui.auth.SignUpScreen
import com.fantasykingdom.app.ui.booking.TicketBookingScreen
import com.fantasykingdom.app.ui.events.EventsOffersScreen
import com.fantasykingdom.app.ui.food.FoodListScreen
import com.fantasykingdom.app.ui.gallery.ImageGalleryScreen
import com.fantasykingdom.app.ui.home.HomeScreen
import com.fantasykingdom.app.ui.map.ParkMapScreen
import com.fantasykingdom.app.ui.park.ParkInfoScreen
import com.fantasykingdom.app.ui.profile.ProfileScreen
import com.fantasykingdom.app.ui.rides.RideCategoriesScreen
import com.fantasykingdom.app.ui.rides.RideDetailScreen
import com.fantasykingdom.app.ui.settings.SettingsScreen
import com.fantasykingdom.app.ui.splash.SplashScreen
import com.fantasykingdom.app.ui.ticket.QrTicketScreen

private val routesWithBottomBar = setOf(
    Destinations.HOME,
    Destinations.RIDE_CATEGORIES,
    Destinations.PARK_MAP,
    Destinations.TICKET_BOOKING,
    Destinations.PROFILE
)

/**
 * The whole app's navigation graph (14 screens) in one place. A bottom
 * navigation bar is shown only for the five "main tab" destinations; detail
 * / auth / utility screens push on top without it, matching common
 * consumer-app navigation patterns.
 */
@Composable
fun FantasyKingdomNavGraph(onSplashFinished: () -> Unit) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in routesWithBottomBar) {
                FantasyKingdomBottomBar(navController, currentRoute)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destinations.SPLASH,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(tween(250)) + slideInHorizontally(tween(250)) { it / 6 } },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(250)) },
            popExitTransition = { fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { it / 6 } }
        ) {
            composable(Destinations.SPLASH) {
                SplashScreen(
                    onFinished = { destination ->
                        onSplashFinished()
                        navController.navigate(destination) {
                            popUpTo(Destinations.SPLASH) { inclusive = true }
                        }
                    }
                )
            }
            composable(Destinations.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Destinations.HOME) { popUpTo(0) }
                    },
                    onNavigateToSignUp = { navController.navigate(Destinations.SIGN_UP) }
                )
            }
            composable(Destinations.SIGN_UP) {
                SignUpScreen(
                    onSignUpSuccess = {
                        navController.navigate(Destinations.HOME) { popUpTo(0) }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
            composable(Destinations.HOME) {
                HomeScreen(
                    onNavigate = { route -> navController.navigate(route) }
                )
            }
            composable(Destinations.PARK_INFO) {
                ParkInfoScreen(onBack = { navController.popBackStack() })
            }
            composable(Destinations.RIDE_CATEGORIES) {
                RideCategoriesScreen(
                    onRideClick = { rideId -> navController.navigate(Destinations.rideDetail(rideId)) }
                )
            }
            composable(
                route = Destinations.RIDE_DETAIL,
                arguments = listOf(navArgument("rideId") { type = NavType.StringType })
            ) { backStack ->
                val rideId = backStack.arguments?.getString("rideId").orEmpty()
                RideDetailScreen(rideId = rideId, onBack = { navController.popBackStack() })
            }
            composable(Destinations.TICKET_BOOKING) {
                TicketBookingScreen(
                    onBookingComplete = { ticketId ->
                        navController.navigate(Destinations.qrTicket(ticketId))
                    }
                )
            }
            composable(
                route = Destinations.QR_TICKET,
                arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
            ) { backStack ->
                val ticketId = backStack.arguments?.getString("ticketId").orEmpty()
                QrTicketScreen(ticketId = ticketId, onBack = { navController.popBackStack() })
            }
            composable(Destinations.FOOD_LIST) {
                FoodListScreen(onBack = { navController.popBackStack() })
            }
            composable(Destinations.PARK_MAP) {
                ParkMapScreen()
            }
            composable(Destinations.EVENTS_OFFERS) {
                EventsOffersScreen(onBack = { navController.popBackStack() })
            }
            composable(Destinations.GALLERY) {
                ImageGalleryScreen(onBack = { navController.popBackStack() })
            }
            composable(Destinations.PROFILE) {
                ProfileScreen(
                    onLogout = { navController.navigate(Destinations.LOGIN) { popUpTo(0) } },
                    onNavigateSettings = { navController.navigate(Destinations.SETTINGS) }
                )
            }
            composable(Destinations.SETTINGS) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
private fun FantasyKingdomBottomBar(
    navController: androidx.navigation.NavHostController,
    currentRoute: String?
) {
    NavigationBar {
        val items = listOf(
            Triple(Destinations.HOME, "Home", Icons.Filled.Home),
            Triple(Destinations.RIDE_CATEGORIES, "Rides", Icons.Filled.Landscape),
            Triple(Destinations.PARK_MAP, "Map", Icons.Filled.Map),
            Triple(Destinations.TICKET_BOOKING, "Tickets", Icons.Filled.ConfirmationNumber),
            Triple(Destinations.PROFILE, "Profile", Icons.Filled.Person),
        )
        items.forEach { (route, label, icon) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { androidx.compose.material3.Text(label) }
            )
        }
    }
}
