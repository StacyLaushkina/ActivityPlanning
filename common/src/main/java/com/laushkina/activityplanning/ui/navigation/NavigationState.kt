package com.laushkina.activityplanning.ui.navigation

class NavigationState {
    var navigation: Navigation? = null

    companion object {
        val instance by lazy { NavigationState() }
    }
}