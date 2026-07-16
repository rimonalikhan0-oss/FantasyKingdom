package com.fantasykingdom.app.navigation

/** Central route registry — single source of truth for every screen's nav route. */
object Destinations {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"

    const val HOME = "home"
    const val PARK_INFO = "park_info"
    const val RIDE_CATEGORIES = "rides"
    const val RIDE_DETAIL = "ride_detail/{rideId}"
    const val TICKET_BOOKING = "ticket_booking"
    const val QR_TICKET = "qr_ticket/{ticketId}"
    const val FOOD_LIST = "food_list"
    const val PARK_MAP = "park_map"
    const val EVENTS_OFFERS = "events_offers"
    const val GALLERY = "gallery"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"

    fun rideDetail(rideId: String) = "ride_detail/$rideId"
    fun qrTicket(ticketId: String) = "qr_ticket/$ticketId"
}

/** Bottom navigation bar destinations (feature-rich but keeps the chrome simple). */
sealed class BottomNavItem(val route: String, val label: String) {
    data object Home : BottomNavItem(Destinations.HOME, "Home")
    data object Rides : BottomNavItem(Destinations.RIDE_CATEGORIES, "Rides")
    data object Map : BottomNavItem(Destinations.PARK_MAP, "Map")
    data object Tickets : BottomNavItem(Destinations.TICKET_BOOKING, "Tickets")
    data object Profile : BottomNavItem(Destinations.PROFILE, "Profile")
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Rides,
    BottomNavItem.Map,
    BottomNavItem.Tickets,
    BottomNavItem.Profile
)
