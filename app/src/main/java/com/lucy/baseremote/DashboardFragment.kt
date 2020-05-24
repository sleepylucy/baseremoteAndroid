package com.lucy.baseremote

import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lucy.baseremote.enums.MELocation
import com.lucy.baseremote.enums.PowerPackLocation
import com.lucy.baseremote.objects.MeSystem
import com.lucy.baseremote.objects.PowerPack
import java.text.NumberFormat
import java.util.*
import kotlin.Int as Int1
import kotlin.Long as Long

class DashboardFragment: Fragment() {
    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    private lateinit var pssProgress: ProgressBar
    private lateinit var pssPercentage: TextView
    private lateinit var pssChange: TextView
    private lateinit var pssMaintenance: TextView
    private lateinit var pssEUdisplay: TextView

    private lateinit var meInfo: TextView
    private lateinit var meToggleButton: Button

    private var meStatus = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // init views
        pssProgress = view.findViewById(R.id.pssProgressBar)
        pssPercentage = view.findViewById(R.id.pssPercentage)
        pssChange = view.findViewById(R.id.pssChangeText)
        pssMaintenance = view.findViewById(R.id.pssMaintenanceIndicator)
        pssEUdisplay = view.findViewById(R.id.pssEUdisplay)

        meInfo = view.findViewById(R.id.meInfo)
        meToggleButton = view.findViewById(R.id.meToggleButton)
        meToggleButton.setOnClickListener {
            toggleME()
        }

        // start timer for ui update
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object: Runnable {
            override fun run() {
                updateUI()
                mainHandler.postDelayed(this, 1000)
            }
        })

        return view
    }

    fun updateUI() {
        val pssURL = getPssURL()
        val meURL = getMeURL()
        val queue = Volley.newRequestQueue(this.context)

        val pssRequest = StringRequest(Request.Method.GET, pssURL, Response.Listener<String> { response ->
            updatePssUi(response)
        },
            Response.ErrorListener { error -> Log.e("updatePSS", error.message) })

        val meRequest = StringRequest(Request.Method.GET, meURL, Response.Listener<String> { response ->
            updateMeUi(response)
        },
            Response.ErrorListener { error -> Log.e("updateME", error.message) })

        queue.add(pssRequest)
        queue.add(meRequest)
    }

    fun updatePssUi(serverResponse: String) {
        println(serverResponse)
        val powerPackListType = object: TypeToken<MutableMap<PowerPackLocation, PowerPack>> () {}.type
        val powerPacks: MutableMap<PowerPackLocation, PowerPack> = Gson().fromJson(serverResponse, powerPackListType)
        val mountainPack = powerPacks[PowerPackLocation.OldBase] ?: return

        if (mountainPack.change >= 0) {
            pssChange.setTextColor(resources.getColor(R.color.green, null))
            pssChange.text = getString(R.string.pss_change, "+", mountainPack.change)
        } else {
            pssChange.setTextColor(resources.getColor(R.color.red, null))
            pssChange.text = getString(R.string.pss_change, "-", mountainPack.change)
        }

        if (mountainPack.maintenance) {
            pssProgress.progressDrawable.setColorFilter(resources.getColor(R.color.red, null), PorterDuff.Mode.SRC_IN)
            pssMaintenance.setTextColor(resources.getColor(R.color.red, null))
            pssMaintenance.setText(R.string.pss_issues)
        } else {
            pssProgress.progressDrawable.setColorFilter(resources.getColor(R.color.green, null), PorterDuff.Mode.SRC_IN)
            pssMaintenance.setTextColor(resources.getColor(R.color.green, null))
            pssMaintenance.setText(R.string.pss_no_issues)
        }

        val percentage = mountainPack.stored.toFloat() / mountainPack.capacity.toFloat()
        Log.wtf("percentage", percentage.toString())
        pssProgress.progress = (percentage * 100).toInt()
        pssPercentage.text = getString(R.string.pss_percent, (percentage * 100).toInt())
        val storageString = NumberFormat.getNumberInstance(Locale.GERMAN).format(mountainPack.stored)
        val capacityString = NumberFormat.getNumberInstance(Locale.GERMAN).format(mountainPack.capacity)
        pssEUdisplay.text = getString(R.string.pss_eu_display, storageString, capacityString)
    }

    fun updateMeUi(serverResponse: String) {
        val meListType = object: TypeToken<MutableMap<MELocation, MeSystem>> () {}.type
        val meSystems: MutableMap<MELocation, MeSystem> = Gson().fromJson(serverResponse, meListType)
        val meSystem = meSystems[MELocation.OldBase] ?: return

        if (meSystem.active) {
            meInfo.setTextColor(resources.getColor(R.color.white, null))
            meToggleButton.setTextColor(resources.getColor(R.color.red, null))

            meInfo.text = getString(R.string.me_online, meSystem.powerConsumption)
            meToggleButton.text = getString(R.string.me_turn_off)
        } else {
            meInfo.setTextColor(resources.getColor(R.color.red, null))
            meToggleButton.setTextColor(resources.getColor(R.color.green, null))

            meInfo.text = getString(R.string.me_offline)
            meToggleButton.text = getString(R.string.me_turn_on)
        }
    }

    fun getMeURL(): String {
        return getString(R.string.url) + getString(R.string.url_me)
    }

    fun getPssURL(): String {
        return getString(R.string.url) + getString(R.string.url_pss)
    }

    fun toggleME() {
        meStatus = !meStatus

        val url = getString(R.string.url) + getString(R.string.url_me) + "?status=" + meStatus
        val queue = Volley.newRequestQueue(this.context)
        val request = StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            Log.v("toggleMe", response)
        },
            Response.ErrorListener { error -> Log.e("toggleMe", error.message) })
        queue.add(request)
    }
}