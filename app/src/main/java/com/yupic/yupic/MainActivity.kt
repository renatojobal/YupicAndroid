package com.yupic.yupic

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yupic.yupic.model.User
import com.yupic.yupic.ui.BottomNavigationItem
import com.yupic.yupic.ui.HomeScreen
import com.yupic.yupic.ui.form.FormScreen
import com.yupic.yupic.ui.login.LoginScreen
import com.yupic.yupic.ui.offset.OffsetScreen
import com.yupic.yupic.ui.theme.YupicTheme
import kotlinx.coroutines.launch
import timber.log.Timber


class MainActivity : ComponentActivity() {

    // View model
    private val sharedViewModel: SharedViewModel by viewModels()

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private lateinit var googleSignInClient: GoogleSignInClient

    private var currentUser: FirebaseUser? = null

    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END config_signin]


        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]
        setContent {
            YupicAppCompose { signIn() }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = auth.currentUser
        updateUI(currentUser)
    }

    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Timber.d("firebaseAuthWithGoogle:%s", account.id)
                firebaseAuthWithGoogle(account.idToken!!)
                sharedViewModel.loginUser(
                    User(
                        name = account.displayName ?: "Anonymus",
                        mail = account.email ?: "",
                        carbonFootprint = 4813.45
                    )
                )
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.w(e, "Google sign in failed")
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w(task.exception, "signInWithCredential:failure")
                    updateUI(null)
                }
            }
    }
    // [END auth_with_google]

    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    @OptIn(
        ExperimentalMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class,
        androidx.compose.animation.ExperimentalAnimationApi::class
    )
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            sharedViewModel.loginUser(
                User(
                    name = user.displayName ?: "Anonymus",
                    mail = user.email ?: "",
                    carbonFootprint = 4813.45
                )
            )
        }

    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

}


@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun YupicAppCompose(signIn: () -> Unit) {
    YupicTheme {

        val navController = rememberNavController()
        val backStackEntry = navController.currentBackStackEntryAsState()
        // State of bottomBar, set state to false, if current page route is "car_details"
        val bottomBarState = rememberSaveable { (mutableStateOf(true)) }


        val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
        val scope = rememberCoroutineScope()

        val sharedViewModel = viewModel<SharedViewModel>()

        val user by sharedViewModel.user.observeAsState()

        BottomDrawer(
            drawerState = drawerState,
            drawerContent = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(36.dp)
                ) {
                    ConstraintLayout(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val (_, close) = createRefs()
                        val (_, options) = createRefs()
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            modifier = Modifier
                                .constrainAs(close) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)

                                }
                        ) {
                            Icon(Icons.Filled.Close, "Close")
                        }
                        Column(
                            modifier = Modifier
                                .constrainAs(options) {
                                    top.linkTo(close.bottom)
                                    width = Dimension.fillToConstraints

                                }
                        ) {
                            Button(onClick = {
                                sharedViewModel.logoutUser()
                                scope.launch {
                                    drawerState.close()
                                }
                            }) {
                                Text(text = "Cerrar sesión")
                            }
                        }
                    }

                }
            }
        ) {

            Scaffold(
                topBar = {
                    user?.run {
                        AppTopBar {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    }
                },
                bottomBar = {

                    BottomBar(
                        navController = navController,
                        bottomBarState = bottomBarState
                    )

                }
            )
            {
                YupicNavHost(
                    navHostController = navController,
                    bottomBarState = bottomBarState
                ) { signIn() }
            }

        }


    }
}

/**
 *  Todo: Check her if the user is logged
 */
fun isUserLogged(): Boolean {
    return false
}

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun YupicNavHost(
    navHostController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    signIn: () -> Unit
) {

    // User
    val sharedViewModel = viewModel<SharedViewModel>()

    val user by sharedViewModel.user.observeAsState()

    val startDestination = if (user != null) {
        "home"
    } else {
        "login"
    }

    NavHost(
        navController = navHostController,
        modifier = Modifier.fillMaxSize(),
        startDestination = startDestination
    ) {
        composable(route = "login") {
            LoginScreen {
                signIn()
            }
            // show BottomBar
            bottomBarState.value = false
        }
        composable(route = BottomNavigationItem.Home.route) {
            HomeScreen { navHostController.navigate(BottomNavigationItem.Offset.route) }
            // show BottomBar
            bottomBarState.value = true
        }
        composable(route = BottomNavigationItem.Form.route) {
            FormScreen()
            // show BottomBar
            bottomBarState.value = true
        }
        composable(route = BottomNavigationItem.Offset.route) {
            OffsetScreen(sharedViewModel = sharedViewModel)
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
                items.forEach { item ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = item.title
                            )
                        },
                        label = { Text(text = item.title) },
                        selectedContentColor = MaterialTheme.colors.onPrimary,
                        unselectedContentColor = MaterialTheme.colors.onPrimary.copy(alpha = ContentAlpha.disabled),
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

@Composable
fun AppTopBar(onClick: () -> Unit) {
    TopAppBar(
        elevation = 10.dp,
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (title, menu) = createRefs()
            IconButton(
                onClick = {
                    onClick()
                },
                modifier = Modifier
                    .constrainAs(menu) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                Icon(Icons.Filled.Menu, "Menu")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    AppTopBar {}
}


@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    // BottomNavigationBar()
}

