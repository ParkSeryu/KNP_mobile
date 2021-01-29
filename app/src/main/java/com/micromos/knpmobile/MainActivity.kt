package com.micromos.knpmobile

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.*
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.micromos.knpmobile.dto.GetCodeCdFeed
import com.micromos.knpmobile.network.KNPApi
import com.micromos.knpmobile.ui.home.HomeFragment
import com.micromos.knpmobile.ui.login.LoginViewModel.Companion.name
import kotlinx.android.synthetic.main.app_bar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val api = KNPApi.create()
    private val code_kind = "SYS05"
    private val use_cls = "1"

    companion object {
        val codeCdList = mutableListOf<String>()
        val codeNmList = mutableListOf<String>()
        val codeList = mutableMapOf<String, String>()

        fun autoCompleteTextViewCustom(AT: AutoCompleteTextView, context: Context) {
            var text = AT.text.toString()
            val rstList = java.util.ArrayList<String>()

            if (text.isNotEmpty()) {
                if (text.length >= 3 && text.toCharArray()[2] != '-') {
                    val char = text.substring(0, 2)
                    text = char + "-" + text.substring(2)
                }
                Log.d("testMatchText", text)
                for (i in 0 until codeNmList.size) {
                    if (codeNmList[i].contains(text)) {
                        Log.d("testMatchList", codeNmList[i])
                        rstList.add(codeNmList[i])
                    }

                }

                val adapter =
                    ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_list_item_1,
                        rstList
                    )
                Log.d("testMatchShow", "$rstList")
                if (rstList.size == 0) {
                    //AT.dismissDropDown()
                } else {
                    AT.setAdapter(adapter)
                    AT.showDropDown()
                }
            } else {
                val adapter = ArrayAdapter<String>(
                    context,
                    R.layout.custom_auto_complete_layout,
                    codeNmList
                )
                AT.setAdapter(adapter)
                AT.showDropDown()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar_name.text = name
        getCodeCd()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        HomeButton.setOnClickListener {
            goHome()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getCodeCd() {
        api.getCodeCd(code_kind, use_cls).enqueue(object : Callback<GetCodeCdFeed> {
            override fun onResponse(call: Call<GetCodeCdFeed>, response: Response<GetCodeCdFeed>) {
                codeCdList.clear()
                codeNmList.clear()
                Log.d("testGetCode", response.body().toString())
                response.body()?.items?.forEach {
                    codeCdList.add(it.codeCd)
                    codeNmList.add(it.codeNm)
                    codeList[it.codeCd] = it.codeNm
                }
            }

            override fun onFailure(call: Call<GetCodeCdFeed>, t: Throwable) {
                Log.d("testFailedGetCode", t.message.toString())
            }
        })
    }

    fun transToUpperCase(s: Editable?, editText: EditText) {
        var text = s.toString()
        if (text != text.toUpperCase(Locale.ROOT)) {
            text = text.toUpperCase(Locale.ROOT)
            editText.setText(text)
            editText.setSelection(editText.length())
        }
    }

    fun setTextChangedListener(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                transToUpperCase(s, editText)
            }
        })
    }

    private fun goHome() {
        replaceFragment(HomeFragment.newInstance())
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment).commit()
    }

    fun logout() {
        //back(취소)키가 눌렸을때 종료여부를 묻는 다이얼로그 띄움
        CustomDialog(this, R.layout.dialog_app_finish)
            .setMessage(R.string.prompt_exit)
            .setPositiveButton("예") {
                moveTaskToBack(true)
                finishAndRemoveTask()
                android.os.Process.killProcess(android.os.Process.myPid())
            }.setNegativeButton("아니오") {
            }.show()
    }
}