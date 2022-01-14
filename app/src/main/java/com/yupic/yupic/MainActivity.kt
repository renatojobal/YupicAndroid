package com.yupic.yupic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.yupic.yupic.ui.BottomNavigationItem
import com.yupic.yupic.ui.login.LoginScreen


class MainActivity : ComponentActivity() {


    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YupicApp()
        }
    }


}

@ExperimentalAnimationApi
@Composable
fun YupicApp(){
    YupicTheme{

        val navController = rememberNavController()
        val backStackEntry = navController.currentBackStackEntryAsState()
        // State of bottomBar, set state to false, if current page route is "car_details"
        val bottomBarState = rememberSaveable {(mutableStateOf(true))}


        Scaffold(
            bottomBar = {

                    BottomBar(
                        navController = navController,
                        bottomBarState = bottomBarState
                    )

            }
        )
        {
            YupicNavHost(navHostController = navController, bottomBarState = bottomBarState)
        }


    }
}

/**
 *  Todo: Check her if the user is logged
 */
fun isUserLogged(): Boolean {
    return false
}

@Composable
fun YupicNavHost(navHostController: NavHostController, bottomBarState: MutableState<Boolean>) {
    NavHost(navController = navHostController,
        modifier = Modifier.fillMaxSize(),
        startDestination = "login"){
        composable(route="login"){
            LoginScreen{
                navHostController.navigate("home")
            }
            // show BottomBar
            bottomBarState.value = false
        }
        composable(route=BottomNavigationItem.Home.route){
            HomeScreen()
            // show BottomBar
            bottomBarState.value = true
        }
        composable(route=BottomNavigationItem.Form.route){
            FormScreen()
            // show BottomBar
            bottomBarState.value = true
        }
        composable(route=BottomNavigationItem.Offset.route){
            OffsetScreen()
            // show BottomBar
            bottomBarState.value = true
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun BottomBar(navController: NavController, bottomBarState: MutableState<Boolean>) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavigationItem.Form,
        BottomNavigationItem.Home,
        BottomNavigationItem.Offset
    )

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {

            BottomNavigation {
                items.forEach {item ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = item.title
                            )
                        },
                        label = { Text(text = item.title) },
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
    )


}


@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    // BottomNavigationBar()
}

