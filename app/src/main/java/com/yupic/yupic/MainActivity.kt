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
import androidx.compose.ui.res.painterResource


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

        YupicNavHost(navHostController = navController)

    }
}

@Composable
fun YupicNavHost(navHostController : NavHostController) {
    NavHost(navController = navHostController,
        modifier = Modifier.fillMaxSize(),
        startDestination = "home"){
        composable(route="home"){
            HomeScreen()
        }
        composable(route="form"){
            FormScreen()
        }
        composable(route="offset"){
            OffsetScreen()
        }
    }
}

@Composable
fun BottomNavigationBar() {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        BottomNavigationItem(
            icon= {Icon(painterResource(id = R.drawable.ic_calculate), contentDescription = "Icon")},
            label = { Text(text = "test")},
            selectedContentColor = MaterialTheme.colors.onSurface,
            unselectedContentColor = MaterialTheme.colors.error,
            alwaysShowLabel = true,
            selected = false,
            onClick = {/*TODO*/ }
        )
        BottomNavigationItem(
            icon= {Icon(painterResource(id = R.drawable.ic_check), contentDescription = "Icon")},
            label = { Text(text = "test")},
            selectedContentColor = MaterialTheme.colors.onSurface,
            unselectedContentColor = MaterialTheme.colors.error,
            alwaysShowLabel = true,
            selected = false,
            onClick = {/*TODO*/ }
        )
        BottomNavigationItem(
            icon= {Icon(painterResource(id = R.drawable.ic_money), contentDescription = "Icon")},
            label = { Text(text = "test")},
            selectedContentColor = MaterialTheme.colors.onSurface,
            unselectedContentColor = MaterialTheme.colors.error,
            alwaysShowLabel = true,
            selected = false,
            onClick = {/*TODO*/ }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    BottomNavigationBar()
}

