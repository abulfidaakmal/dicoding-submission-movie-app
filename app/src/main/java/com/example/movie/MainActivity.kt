package com.example.movie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movie.ui.theme.MovieTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{ true }

        CoroutineScope(Dispatchers.Main).launch{
            delay(2000L)
            splashScreen.setKeepOnScreenCondition{ false }
        }

        setContent {
            MovieTheme(darkTheme = true) {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.background(color = Color(0xff181C14)),
                    topBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route

                        TopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color(0xff16423C),
                            ),
                            title = {
                                if (currentRoute == "movie_list") {
                                    Text("Movies", fontSize = 28.sp)
                                }
                            },
                            navigationIcon = {
                                if (currentRoute != "movie_list") {
                                    IconButton(
                                        onClick = { navController.navigateUp() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.ArrowBack,
                                            contentDescription = "Back",
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }
                                }
                            },
                            actions = {
                                if (currentRoute != "about_page") {
                                    IconButton(
                                        onClick = { navController.navigateUp() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = "About",
                                            modifier = Modifier
                                                .clickable {
                                                    navController.navigate("about_page")
                                                }
                                                .size(36.dp)
                                        )
                                    }
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    MovieAppNavHost(
                        navController = navController,
                        Modifier
                            .padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
fun MovieAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "movie_list",
        modifier = modifier
    ) {
        composable("movie_list") {
            MovieList(
                navController = navController,
            )
        }
        composable(
            route = "movie_detail/{title}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            MovieDetailScreen(
                title = title,
                navController= navController
            )
        }
        composable("about_page") {
            AboutScreen()
        }
    }
}


@Composable
fun MovieList(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val resources = LocalContext.current.resources
    val titles = resources.getStringArray(R.array.data_title)
    val overviews = resources.getStringArray(R.array.data_overview)
    val images = resources.getStringArray(R.array.data_image)

    val movieItems = titles.mapIndexed { index, title ->
        Movies(
            title = title,
            overview = overviews[index],
            imageUrl = images[index]
        )
    }

    LazyColumn(modifier = modifier) {
        items(movieItems) { item ->
            MovieItemView(item = item, onClick = {
                navController.navigate("movie_detail/${item.title}")
            })
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieItemView(
    item: Movies,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xff3C3D37))
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
        ) {
            GlideImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp)
                    .padding(vertical = 4.dp)
            )
            Column(
                Modifier.padding(end = 12.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(
                    modifier = Modifier.height(4.dp)
                )
                Text(
                    text = item.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieTheme {
        val navController = rememberNavController()
        MovieAppNavHost(navController)
    }
}
