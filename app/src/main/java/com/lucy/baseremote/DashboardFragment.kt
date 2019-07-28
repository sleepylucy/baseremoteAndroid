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
import java.text.NumberFormat
import java.util.*

class DashboardFragment: Fragment() {
    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    lateinit var pssProgress: ProgressBar
    lateinit var pssPercentage: TextView
    lateinit var pssChange: TextView
    lateinit var pssMaintenance: TextView
    lateinit var pssEUdisplay: TextView

    lateinit var meInfo: TextView
    lateinit var meToggleButton: Button

    var meStatus = true
    var storage: Long = 0
    var storageI = 0
    var storageHist = arrayOf(0L, 0L, 0L, 0L, 0L)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // init views
        pssProgress = view!!.findViewById(R.id.pssProgressBar)
        pssPercentage = view!!.findViewById(R.id.pssPercentage)
        pssChange = view!!.findViewById(R.id.pssChangeText)
        pssMaintenance = view!!.findViewById(R.id.pssMaintenanceIndicator)
        pssEUdisplay = view!!.findViewById(R.id.pssEUdisplay)

        meInfo = view!!.findViewById(R.id.meInfo)
        meToggleButton = view!!.findViewById(R.id.meToggleButton)
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
        val components = serverResponse.split("/")

        val oldStorage = storage
        storage = components[0].toLong()
        val capacity = components[1].toLong()
        val maintenance = components[2] == "true"

        storageHist[storageI] = storage - oldStorage
        storageI++
        if (storageI >= 5) { storageI = 0 }

        var change: Long = 0
        for (i in storageHist) { change += i }
        change /= storageHist.size
        change /= 20
        if (change >= 0) {
            pssChange.setTextColor(resources.getColor(R.color.green))
            pssChange.text = getString(R.string.pss_change, "+", change)
        } else {
            pssChange.setTextColor(resources.getColor(R.color.red))
            pssChange.text = getString(R.string.pss_change, "-", change)
        }

        if (maintenance) {
            pssProgress.progressDrawable.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_IN)
            pssMaintenance.setTextColor(resources.getColor(R.color.red))
            pssMaintenance.setText(R.string.pss_issues)
        } else {
            pssProgress.progressDrawable.setColorFilter(resources.getColor(R.color.green), PorterDuff.Mode.SRC_IN)
            pssMaintenance.setTextColor(resources.getColor(R.color.green))
            pssMaintenance.setText(R.string.pss_no_issues)
        }

        val percentage = storage.toFloat() / capacity.toFloat()
        Log.wtf("percentage", percentage.toString())
        pssProgress.progress = (percentage * 100).toInt()
        pssPercentage.text = getString(R.string.pss_percent, (percentage * 100).toInt())
        val storageString = NumberFormat.getNumberInstance(Locale.GERMAN).format(storage)
        val capacityString = NumberFormat.getNumberInstance(Locale.GERMAN).format(capacity)
        pssEUdisplay.text = getString(R.string.pss_eu_display, storageString, capacityString)
    }

    fun updateMeUi(serverResponse: String) {
        val components = serverResponse.split("/")

        meStatus = components[0] == "true"
        val euUsage = components[1]

        if (meStatus) {
            meInfo.setTextColor(resources.getColor(R.color.white))
            meToggleButton.setTextColor(resources.getColor(R.color.red))

            meInfo.text = getString(R.string.me_online, euUsage)
            meToggleButton.text = getString(R.string.me_turn_off)
        } else {
            meInfo.setTextColor(resources.getColor(R.color.red))
            meToggleButton.setTextColor(resources.getColor(R.color.green))

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