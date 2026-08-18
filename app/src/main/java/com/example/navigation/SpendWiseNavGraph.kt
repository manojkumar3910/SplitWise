package com.example.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.data.SpendWiseRepository
import com.example.ui.screens.AddExpenseScreen
import com.example.ui.screens.AddInvestmentSelectScreen
import com.example.ui.screens.AddStockInvestmentScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ExpensesScreen
import com.example.ui.screens.FinancialGoalsScreen
import com.example.ui.screens.InvestmentOverviewScreen
import com.example.ui.screens.InvestmentTransactionsScreen
import com.example.ui.screens.InvestmentsScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.ProfileScreen

@Composable
fun SpendWiseNavGraph(
    navController: NavHostController,
    repository: SpendWiseRepository = SpendWiseRepository.instance,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        // 1. Login Screen (Initial Screen)
        composable(
            route = Screen.Login.route,
            enterTransition = { fadeIn(tween(250)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 9. Dashboard Screen
        composable(
            route = Screen.Dashboard.route,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            DashboardScreen(
                repository = repository,
                onNavigate = { targetRoute ->
                    if (targetRoute != Screen.Dashboard.route) {
                        navController.navigate(targetRoute) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        // 6. Expenses Screen
        composable(
            route = Screen.Expenses.route,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            ExpensesScreen(
                repository = repository,
                onNavigate = { targetRoute ->
                    navController.navigate(targetRoute) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // 3. Add Expense Screen (Slide Up)
        composable(
            route = Screen.AddExpense.route,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300)
                ) + fadeIn(tween(300))
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(250)
                ) + fadeOut(tween(250))
            }
        ) {
            AddExpenseScreen(
                repository = repository,
                onBack = { navController.popBackStack() }
            )
        }

        // 4. Investments Screen
        composable(
            route = Screen.Investments.route,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            InvestmentsScreen(
                repository = repository,
                onNavigate = { targetRoute ->
                    navController.navigate(targetRoute) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // 10. Investment Overview Screen
        composable(
            route = Screen.InvestmentOverview.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(250)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(250)
                )
            }
        ) {
            InvestmentOverviewScreen(
                repository = repository,
                onNavigate = { targetRoute ->
                    navController.navigate(targetRoute) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // 8. Add Investment Select Screen (Modal slide up)
        composable(
            route = Screen.AddInvestmentSelect.route,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300)
                ) + fadeIn(tween(300))
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(250)
                ) + fadeOut(tween(250))
            }
        ) {
            AddInvestmentSelectScreen(
                onClose = { navController.popBackStack() },
                onSelectStocks = {
                    navController.navigate(Screen.AddStockInvestment.route)
                }
            )
        }

        // 7. Add Stock Investment Screen
        composable(
            route = Screen.AddStockInvestment.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(250)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(250)
                )
            }
        ) {
            AddStockInvestmentScreen(
                repository = repository,
                onBack = { navController.popBackStack() },
                onSubmitSuccess = {
                    navController.navigate(Screen.InvestmentTransactions.route) {
                        popUpTo(Screen.InvestmentOverview.route)
                    }
                }
            )
        }

        // 5. Investment Transactions Screen
        composable(
            route = Screen.InvestmentTransactions.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(250)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(250)
                )
            }
        ) {
            InvestmentTransactionsScreen(
                repository = repository,
                onNavigate = { targetRoute ->
                    navController.navigate(targetRoute) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // 2. Financial Goals Screen
        composable(
            route = Screen.FinancialGoals.route,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            FinancialGoalsScreen(
                repository = repository,
                onNavigate = { targetRoute ->
                    navController.navigate(targetRoute) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // Profile Screen
        composable(
            route = Screen.Profile.route,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            ProfileScreen(
                repository = repository,
                onNavigate = { targetRoute ->
                    navController.navigate(targetRoute) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
