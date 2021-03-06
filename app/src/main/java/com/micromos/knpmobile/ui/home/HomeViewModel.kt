package com.micromos.knpmobile.ui.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.micromos.knpmobile.BuildConfig
import com.micromos.knpmobile.Event
import com.micromos.knpmobile.ui.login.LoginViewModel.Companion.program_id

class HomeViewModel : ViewModel() {
    private var _menuItemList = MutableLiveData<List<MenuItem>>()
    val menuItemList: LiveData<List<MenuItem>> = _menuItemList

    private val _goToAnotherView = MutableLiveData<Event<MenuItem>>()
    val goToAnotherView: LiveData<Event<MenuItem>> = _goToAnotherView

    init {



        val programList = listOf(
            MenuItem.ProductCoilIn,
            MenuItem.ProductCoilOut,
            MenuItem.ProductStockCheck,
            MenuItem.ProductChangePos,
            MenuItem.ProductMaterialCheck
        )

        val allocationProgramList = mutableListOf<MenuItem>()
        allocationProgramList.clear()


        programList.forEach {
            if(BuildConfig.DEBUG){
                allocationProgramList.add(it)
            }
            else {
                for (i in 0..program_id.lastIndex) {
                    if (it.id == program_id[i])
                        allocationProgramList.add(it)
                }
            }
        }
        _menuItemList.value = allocationProgramList
    }

    fun goToAnotherView(menuItemList: MenuItem) {
        _goToAnotherView.value = Event(menuItemList)
    }

}