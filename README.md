# Currency Converter

This app fetches and displays conversion rates for a list of some currencies from the given API in every 1 second.

When the user taps on a currency, it is moved to the top of the list, the rate field is focused and the input is selected. If the user changes the input, all the other currencies will update with the actual rates.

When there is no internet connection, the app will provide offline conversions based on the latest base-rate data from the API.
These rates are saved in locally and updated whenever internet connectivity is restored.

When there is no internet connection, at app start a Snackbar informs the user about the offline usage.

The project uses the Model-View-ViewModel (MVVM) architecture with a Single Activity and Navigation Component.

## Libraries used:
* **Koin** for dependency injection
* **Timber** for logging
* **Retrofit** for networking
* **Moshi** for JSON parsing
* **Couroutines** for asynchronous io (api & database)
* **JUnit 4, Truth, Espresso** for adding some tests
* **ktlint, detekt ** for Kotlin code verification

The flag icons are from
https://www.flaticon.com/