Navigation Compose Typed
========================

Compile-time type-safe arguments for JetPack Navigation Compose library. Based on KotlinX.Serialization.

[![Kiwi.com library](https://img.shields.io/badge/Kiwi.com-library-00A991)](https://code.kiwi.com)
[![CI Build](https://img.shields.io/github/workflow/status/kiwicom/navigation-compose-typed/Build/main)](https://github.com/kiwicom/navigation-compose-typed/actions/workflows/build.yml)
[![GitHub release](https://img.shields.io/github/v/release/kiwicom/navigation-compose-typed)](https://github.com/kiwicom/navigation-compose-typed/releases)
[![Maven release](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fcom%2Fkiwi%2Fnavigation-compose%2Ftyped%2Fcore%2Fmaven-metadata.xml)](https://search.maven.org/search?q=g:com.kiwi.navigation-compose.typed)

Major features:

- Complex types support, including nullability for primitive types - the only condition is that the type has to be serializable with KotlinX.Serializable library.
- Based on official Kotlin Serialization compiler plugin - no slowdown with KSP nor KAPT.
- All JetPack Navigation Compose features: e.g. `navigateUp()` after a deeplink preserves the top-level shared arguments.
- Few simple functions, no new complex `NavHost` or `NavController` types; this allows covering other JetPack Navigation Compose extensions.
- Gradual integration, feel free to onboard just a part of your app.

### QuickStart

Add the dependency

```kotlin
implementation("com.kiwi.navigation-compose.typed:core:0.1.0")
```

> **Warning**
> This library uses Semantic Versioning. Be aware that BC breaks are allowed in minor versions before the major 1.0 version.

Create app's destinations

```kotlin

import com.kiwi.navigationcompose.typed.Destination

sealed interface Destinations : Destination {
	@Serializable
	object Home : Destinations

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

Now, it is time to navigate! Convert the destination to `Route` instance and pass it to the navigate extension method on the standard `NavController`.

```kotlin
import com.kiwi.navigationcompose.typed.toRoute
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
	onNavigate: (Route) -> Unit,
) {
	Home(
		onArticleClick = { id -> onNavigate(Destinations.Article(id).toRoute()) },
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

### Extensibility

What about cooperation with Accompanist's `AnimatedNavHost` or `bottomSheet {}`? Do not worry. Basically, all this are just three simple functions. Create your own abstraction and use `createRoutePattern()`, `createNavArguments()` and `decodeArguments()` functions.

```kotlin
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.createNavArguments
import com.kiwi.navigationcompose.typed.decodeArguments
import com.kiwi.navigationcompose.typed.Destination

private inline fun <reified T : Destination> NavGraphBuilder.bottomSheet(
	noinline content: @Composable T.(NavBackStackEntry) -> Unit,
) {
	val serializer = serializer<T>()
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
