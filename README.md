# Fantasy Kingdom 🏰

A complete Android theme-park companion app: Kotlin, Jetpack Compose, Material 3,
MVVM, and Firebase (Auth, Firestore, Storage, Cloud Messaging).

## Features

1. Splash Screen — animated brand mark, routes to Login or Home based on auth state
2. Login & Sign Up — Firebase Authentication (email/password)
3. Home Dashboard — greeting, quick actions, featured rides, live offers
4. Park Information — hours, address, contact, parking, rules
5. Ride Categories — filterable grid (Thrill / Family / Kids / Water / Dark) + detail screen
6. Ticket Booking — pass types, guest count, live price calculation
7. QR Code Ticket — scannable QR generated locally from the booked ticket (ZXing)
8. Food & Restaurant List — dining options with cuisine, rating, price level
9. Park Map — interactive proportional zone pins with info cards
10. Events & Offers — seasonal events and limited-time discounts
11. Image Gallery — grid + full-screen lightbox viewer
12. User Profile — account info, photo upload to Storage, ticket history, logout
13. Push Notifications — Firebase Cloud Messaging with a custom notification channel
14. Settings — Dark/Light mode toggle, persisted with DataStore

## Tech stack

- **Kotlin** + **Jetpack Compose** (Material 3, single-Activity architecture)
- **MVVM**: `ui/<feature>/XxxViewModel.kt` + `ui/<feature>/XxxScreen.kt`
- **Hilt** for dependency injection
- **Firebase**: Authentication, Firestore, Storage, Cloud Messaging
- **Navigation Compose** with animated transitions and a bottom nav bar
- **Coil** for async image loading, **ZXing** for QR generation
- **DataStore** for local theme preference persistence

## Project structure

```
app/src/main/java/com/fantasykingdom/app/
├── MainActivity.kt              # Single-Activity host
├── FantasyKingdomApp.kt          # Application class (Hilt entry point, notif. channel)
├── navigation/                   # NavGraph + route registry + bottom nav
├── data/
│   ├── model/                    # Firestore document data classes
│   ├── remote/                   # Firebase DI module, DataStore instance
│   └── repository/               # Auth / Park content / Tickets / Storage repositories
├── notification/                  # FCM messaging service
└── ui/
    ├── theme/                    # Color.kt, Type.kt, Theme.kt, ThemeViewModel.kt
    ├── components/               # Shared buttons, text fields, cards, chips
    └── <feature>/                # One folder per feature, each with Screen + ViewModel
```

## Gradle Wrapper

This project includes `gradlew` / `gradlew.bat` and `gradle/wrapper/gradle-wrapper.properties`
(pinned to Gradle 8.7), but **not** the binary `gradle-wrapper.jar` — it isn't committed here.
You have three easy ways to get it:

1. **Android Studio (simplest):** just open the project. Studio detects the wrapper
   config and offers to regenerate the jar using its bundled Gradle — accept the prompt
   (or go to *File → Sync Project with Gradle Files* if it doesn't prompt automatically).
2. **Local one-liner:** if you have any Gradle installed (`brew install gradle`,
   `sdk install gradle`, `apt install gradle`, etc.), run `./generate-wrapper.sh` from the
   project root — it regenerates `gradlew`, `gradlew.bat`, and the jar to match Gradle 8.7.
3. **CI (GitHub Actions):** `.github/workflows/android-ci.yml` is already set up to
   provision Gradle and regenerate the wrapper automatically before every build, so
   pushing to GitHub builds correctly with no extra setup. Add a `GOOGLE_SERVICES_JSON`
   repo secret (the full file contents) for it to build against real Firebase config.

## Getting started

### 1. Open the project
Open this folder directly in the latest stable **Android Studio** ("Open an existing project").
Let Gradle sync — it will download all dependencies automatically (internet required).

### 2. Set up Firebase (required before the app can run)
1. Go to the [Firebase console](https://console.firebase.google.com) and create a project.
2. Add an Android app with package name **`com.fantasykingdom.app`**.
3. Download the generated `google-services.json` and place it at:
   `app/google-services.json` (a `google-services.json.template` is included in `app/`
   showing the expected shape — replace it with your real file, same location).
4. In the console, enable:
   - **Authentication** → Sign-in method → Email/Password
   - **Firestore Database** → Create database (start in production mode)
   - **Storage** → Get started
   - **Cloud Messaging** (enabled by default once the app is registered)
5. Apply the security rules in `firebase/firestore.rules` and `firebase/storage.rules`
   (console → Firestore/Storage → Rules tab → paste and publish).
6. Seed some content so the Home/Rides/Food/Gallery/Events/Map screens have data to show:
   import the documents in `firebase/seed_data.json` into their matching top-level
   Firestore collections (`rides`, `restaurants`, `events_offers`, `gallery`, `map_zones`).
   The Firebase CLI's `firestore:delete`/small Admin SDK script, or manual entry via the
   console, both work — the app itself only reads these collections.

### 3. Build & run
Once `google-services.json` is in place and Gradle has synced, click **Run** ▶ on a device
or emulator with API 24+.

## Notes for going further

- Replace the placeholder vector app icon (`res/mipmap*/ic_launcher*`) with real launcher
  artwork via Android Studio's Image Asset Studio (`res` → New → Image Asset).
- The Park Map screen positions pins proportionally over a plain background; swap in a real
  map illustration by loading it with `AsyncImage` behind the pins once you have one.
- `ProGuard`/R8 rules for release builds are pre-configured in `app/proguard-rules.pro`.
- To send a push notification for testing, use Firebase console → Cloud Messaging →
  "Send test message" with the FCM token logged/stored on the user's `/users/{uid}` doc.
