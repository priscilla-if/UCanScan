package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import nz.ac.uclive.dsi61.ucanscan.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun RaceScreen(context: Context,
               navController: NavController) {

    val items = listOf(
        Screens.Preferences,
        Screens.MainMenu
    )

    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
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
    ) { innerPadding ->
        NavHost(navController, startDestination = Screens.Profile.route, Modifier.padding(innerPadding)) {
            composable(Screens.Profile.route) { Profile(navController) }
            composable(Screens.FriendsList.route) { FriendsList(navController) }
        }
    }




}

//
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background
//    ) {
//
//        Column(
//            modifier = Modifier.fillMaxSize().padding(top = 100.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Text(
//                modifier = Modifier.padding(bottom = 70.dp),
//                text = stringResource(id = R.string.next_landmark),
//                style = TextStyle(
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            )
//
//
//
//            Box(
//                modifier = Modifier
//                    .size(300.dp)
//                    .background(Color.Gray, shape = CircleShape)
//            ) {
//                Text(
//                    text = stringResource(id = R.string.jack_erskine_landmark),
//                    color = Color.Black,
//                    fontSize = 28.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(vertical = 130.dp)
//                        .align(Alignment.Center),
//                    style = TextStyle(
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//            }
//
//
//
//            Row(
//                modifier = Modifier.fillMaxSize(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//
//                Button(
//                    modifier = Modifier
//                        .size(100.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    onClick = {
//                    },
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.camera),
//                        contentDescription = "Camera",
//                        modifier = Modifier
//                            .size(100.dp)
//                    )
//                }
//
//
//
//                Button(
//                    modifier = Modifier
//                        .size(100.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    onClick = {
//                        //TODO handle map btn onclick
//                    },
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.map),
//                        contentDescription = "Map",
//                        modifier = Modifier
//                            .size(100.dp)
//                    )
//                }
//
//
//            }
//
//
//        }

