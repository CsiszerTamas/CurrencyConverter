# Currency Converter

This app fetches and displays conversion rates for a list of some currencies from the given API in every 1 second.

When the user taps on a currency, it is moved to the top of the list, the rate field is focused and the input is selected. If the user changes the input, all the other currencies will update with the actual rates.

When there is no internet connection, the app will provide offline conversions based on the latest base-rate data from the API.
These rates are saved in locally and updated whenever internet connectivity is restored.

When there is no internet connection, at app start a Snackbar informs the user about the offline usage.

The project uses the Model-View-ViewModel (MVVM) architecture with a Single Activity and Navigation Component.

## Issues fixed after feedback:
* Instead of keeping navControllerNotInitialized as variable, I use ::navController.isInitialized
* Moved rates calculations from main thread to background thread using coroutines (Dispatchers.IO), to make UI much smooth when scrolling
* Used ListAdapter instead of RecyclerView.Adapter to make easier diffs
* Remove EditText extension function decimal limitations, so full rates could be shown
* Saved chosen currency with SharedPreferences and show it after app restart too
* Added some unit tests to test Room

## Libraries used:
* **Koin** for dependency injection
* **Timber** for logging
* **Retrofit** for networking
* **Moshi** for JSON parsing
* **Coroutines** for asynchronous operations
* **JUnit 4, Truth, Espresso** for adding some tests
* **ktlint, detekt** for Kotlin code verification

You can download the APK from the following link:
https://github.com/CsiszerTamas/CurrencyConverter/blob/master/apk/CurrencyConverter_debug.apk

The flag icons are from
https://www.flaticon.com/

Architecture diagram:
![Alt text](https://user-images.githubusercontent.com/31385348/102409162-43afd180-3ff7-11eb-98e1-84d335bc42d7.png "Architecture diagram")

Screenshots:
![Alt text](https://user-images.githubusercontent.com/31385348/102409180-490d1c00-3ff7-11eb-8fdd-033a517198cf.png "Light mode")

![Alt text](https://user-images.githubusercontent.com/31385348/102409184-4ad6df80-3ff7-11eb-8a65-9c88183c3459.png "Dark mode")

