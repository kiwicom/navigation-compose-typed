Navigation Compose Typed
========================

Compile-time type-safe arguments for the Jetpack Navigation Compose library. Based on KotlinX.Serialization.

[![Kiwi.com library](https://img.shields.io/badge/Kiwi.com-library-00A991)](https://code.kiwi.com)
[![CI Build](https://img.shields.io/github/actions/workflow/status/kiwicom/navigation-compose-typed/build.yml?branch=main)](https://github.com/kiwicom/navigation-compose-typed/actions/workflows/build.yml)
[![GitHub release](https://img.shields.io/github/v/release/kiwicom/navigation-compose-typed)](https://github.com/kiwicom/navigation-compose-typed/releases)
[![Maven release](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fcom%2Fkiwi%2Fnavigation-compose%2Ftyped%2Fcore%2Fmaven-metadata.xml)](https://central.sonatype.com/namespace/com.kiwi.navigation-compose.typed)

Major features:

- Complex types' support, including nullability for primitive types - the only condition is that the type has to be serializable with KotlinX.Serializable library.
- Based on the official Kotlin Serialization compiler plugin - no slowdown with KSP or KAPT.
- All Jetpack Navigation Compose features: e.g. `navigateUp()` after a deeplink preserves the top-level shared arguments.
- Few simple functions, no new complex `NavHost` or `NavController` types; this allows covering other Jetpack Navigation Compose extensions.
- Gradual integration, feel free to onboard just a part of your app.

Watch the talk about this library and its implementation details:

[![Watch the video](https://img.youtube.com/vi/bXuiYvGFbvs/default.jpg)](https://youtu.be/bXuiYvGFbvs)

### QuickStart

Add this library dependency and KotlinX.Serialization support

```kotlin
plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
}

dependencies {
    implementation("com.kiwi.navigation-compose.typed:core:<version>")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
}
```

> **Warning**
> This library uses Semantic Versioning. Be aware that BC breaks are allowed in minor versions before the major 1.0 version.

Create app's destinations

```kotlin

import com.kiwi.navigationcompose.typed.Destination

sealed interface Destinations : Destination {
    
    @Serializable
    data object Home : Destinations

    @Serializable
    data class Article(
        val id: String,
    ) : Destinations
}
```

and use them in the navigation graph definition

```kotlin
import com.kiwi.navigationcompose.typed.composable
import com.kiwi.navigationcompose.typed.createRoutePattern

NavGraph(
    startDestination = createRoutePattern<Destinations.Home>(),
) {
    composable<Destinations.Home> {
        Home()
    }
    composable<Destinations.Article> {
        // this is Destinations.Article
        Article(id)
    }
}
```

Now, it is time to navigate! Create a `Destination` instance and pass it to the navigate extension method on the standard `NavController`.

```kotlin
import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.navigate

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavGraph(
        navController = navController,
    ) {
        composable<Destinations.Home> {
            Home(navController::navigate)
        }
    }
}

@Composable
private fun Home(
    onNavigate: (Destination) -> Unit,
) {
    Home(
        onArticleClick = { id -> onNavigate(Destinations.Article(id)) },
    )
}

@Composable
private fun Home(
    onArticleClick: (id: Int) -> Unit,
) {
    Column {
        Button(onClick = { onArticleClick(1) }) { Text("...") }
        Button(onClick = { onArticleClick(2) }) { Text("...") }
    }
}
```

### ViewModel

You can pass your destination arguments directly from the UI using parameters/the assisted inject feature.

For example, in Koin:

```kotlin
val KoinModule = module {
    viewModelOf(::DemoViewModel)
}

fun DemoScreen(arguments: HomeDestinations.Demo) {
    val viewModel = getViewModel<DemoViewModel> { parametersOf(arguments) }
}

class DemoViewModel(
    arguments: HomeDestinations.Demo,
)
```

Alternatively, you can read your destination from a `SavedStateHandle` instance:

```kotlin
class DemoViewModel(
    state: SavedStateHandle,
) : ViewModel() {
    val arguments = state.decodeArguments<HomeDestinations.Demo>()
}
```

### Extensibility

What about cooperation with Accompanist's Material `bottomSheet {}` integration? Do not worry. Basically, all the functionality is just a few simple functions. Create your own abstraction and use `createRoutePattern()`, `createNavArguments()`, `decodeArguments()` and `registerDestinationType()` functions.

```kotlin
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.createNavArguments
import com.kiwi.navigationcompose.typed.decodeArguments
import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.registerDestinationType

private inline fun <reified T : Destination> NavGraphBuilder.bottomSheet(
    noinline content: @Composable T.(NavBackStackEntry) -> Unit,
) {
    val serializer = serializer<T>()
    registerDestinationType(T::class, serializer)
    bottomSheet(
        route = createRoutePattern(serializer),
        arguments = createNavArguments(serializer),
    ) {
        val arguments = decodeArguments(serializer, it)
        arguments.content(it)
    }
}

NavGraph {
    bottomSheet<Destinations.Article> {
        Article(id)
    }
}
```

### Result sharing

Another set of functionality is provided to support the result sharing. First, define the destination as `ResultDestination` type and specify the result type class. Then open the screen as usual and utilize `ComposableResultEffect` or `DialogResultEffect` to observe the destination's result. To send the result, use
`NavController`'s extension `setResult`.

> [!WARNING]  
> Do not make the Result class sealed as it may cause issue (see kiwicom/navigation-compose-typed#133). You may put a sealed type to a Result's property.

```kotlin
import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.DialogResultEffect
import com.kiwi.navigationcompose.typed.ResultDestination
import com.kiwi.navigationcompose.typed.setResult

sealed interface Destinations : Destination {

    @Serializable
    data object Dialog : Destinations, ResultDestination<Dialog.Result> {
        @Serializable
        data class Result(
            val something: Int,
        )
    }
}

@Composable
fun Host(navController: NavController) {
    DialogResultEffect(navController) { result: Destinations.Dialog.Result ->
        println(result)
        // process the result
    }

    Button(
        onClick = { navController.navigate(Destinations.Dialog) },
    ) {
        Text("Open")
    }
}

@Composable
fun Dialog(navController: NavController) {
    Button(
        onClick = {
            navController.setResult(Destinations.Dialog.Result(something = 42))
            navController.popBackStack()
        }
    ) {
        Text("Set and close")
    }
}
```
