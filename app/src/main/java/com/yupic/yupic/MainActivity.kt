package com.yupic.yupic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yupic.yupic.ui.FormScreen
import com.yupic.yupic.ui.HomeScreen
import com.yupic.yupic.ui.OffsetScreen
import com.yupic.yupic.ui.theme.YupicTheme

import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.yupic.yupic.ui.NavigationItem


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YupicApp()
        }
    }
}

@Composable
fun YupicApp(){
    YupicTheme{

        val navController = rememberNavController()
        val backStackEntry = navController.currentBackStackEntryAsState()
        Scaffold(bottomBar = { BottomNavigationBar(navController)}) {
            YupicNavHost(navHostController = navController)
        }



    }
}

@Composable
fun YupicNavHost(navHostController : NavHostController) {
    NavHost(navController = navHostController,
        modifier = Modifier.fillMaxSize(),
        startDestination = NavigationItem.Home.route){
        composable(route=NavigationItem.Home.route){
            HomeScreen()
        }
        composable(route=NavigationItem.Form.route){
            FormScreen()
        }
        composable(route=NavigationItem.Offset.route){
            OffsetScreen()
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        NavigationItem.Form,
        NavigationItem.Home,
        NavigationItem.Offset
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onSurface
    ) {

        items.forEach{item ->
            BottomNavigationItem(
                icon= {Icon(painterResource(id = item.icon), contentDescription = item.title)},
                label = { Text(text = item.title)},
                selectedContentColor = MaterialTheme.colors.onSurface,
                unselectedContentColor = MaterialTheme.colors.error,
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )

        }


    }
}


@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    // BottomNavigationBar()
}

