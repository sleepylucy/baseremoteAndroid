package com.lucy.baseremote

import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatImageView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lucy.baseremote.enums.PowerGenLocation
import com.lucy.baseremote.enums.PowerPackLocation
import com.lucy.baseremote.objects.PowerGen
import com.lucy.baseremote.objects.PowerPack
import kotlinx.android.synthetic.main.fragment_power.*
import java.text.NumberFormat
import java.util.*

class PowerFragment: Fragment() {
    companion object {
        fun newInstance(): PowerFragment {
            return PowerFragment()
        }
    }

    // Storage
    private lateinit var storageMountainIssues: TextView
    private lateinit var storageMountainProgress: ProgressBar
    private lateinit var storageMountainPercent: TextView
    private lateinit var storageMountainChange: TextView
    private lateinit var storageMountainDetail: TextView

    private lateinit var storageTowerIssues: TextView
    private lateinit var storageTowerProgress: ProgressBar
    private lateinit var storageTowerPercent: TextView
    private lateinit var storageTowerChange: TextView
    private lateinit var storageTowerDetail: TextView

    private lateinit var storageChemIssues: TextView
    private lateinit var storageChemProgress: ProgressBar
    private lateinit var storageChemPercent: TextView
    private lateinit var storageChemChange: TextView
    private lateinit var storageChemDetail: TextView

    // Production
    private lateinit var damTotal: TextView
    private lateinit var damTurbine11: TextView
    private lateinit var damTurbine12: TextView
    private lateinit var damTurbine13: TextView
    private lateinit var damTurbine14: TextView
    private lateinit var damTurbine15: TextView
    private lateinit var damTurbine16: TextView
    private lateinit var damTurbine17: TextView
    private lateinit var damTurbine21: TextView
    private lateinit var damTurbine22: TextView
    private lateinit var damTurbine23: TextView
    private lateinit var damTurbine24: TextView
    private lateinit var damTurbine25: TextView
    private lateinit var damTurbine26: TextView
    private lateinit var damTurbine27: TextView
    private lateinit var damTurbine11Image: AppCompatImageView
    private lateinit var damTurbine12Image: AppCompatImageView
    private lateinit var damTurbine13Image: AppCompatImageView
    private lateinit var damTurbine14Image: AppCompatImageView
    private lateinit var damTurbine15Image: AppCompatImageView
    private lateinit var damTurbine16Image: AppCompatImageView
    private lateinit var damTurbine17Image: AppCompatImageView
    private lateinit var damTurbine21Image: AppCompatImageView
    private lateinit var damTurbine22Image: AppCompatImageView
    private lateinit var damTurbine23Image: AppCompatImageView
    private lateinit var damTurbine24Image: AppCompatImageView
    private lateinit var damTurbine25Image: AppCompatImageView
    private lateinit var damTurbine26Image: AppCompatImageView
    private lateinit var damTurbine27Image: AppCompatImageView

    private lateinit var chemSolarTotal: TextView
    private lateinit var chemSolarN1: TextView
    private lateinit var chemSolarN2: TextView
    private lateinit var chemSolarE1: TextView
    private lateinit var chemSolarE2: TextView
    private lateinit var chemSolarS1: TextView
    private lateinit var chemSolarS2: TextView
    private lateinit var chemSolarW1: TextView
    private lateinit var chemSolarW2: TextView

    private lateinit var mountainWindTotal: TextView
    private lateinit var mountainWindTurbine1: TextView

    private var isStopped = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_power, container, false)

        // init views
        storageMountainIssues = view.findViewById(R.id.storageMountainIssues)
        storageMountainProgress = view.findViewById(R.id.storageMountainProgress)
        storageMountainPercent = view.findViewById(R.id.storageMountainPercent)
        storageMountainChange = view.findViewById(R.id.storageMountainChange)
        storageMountainDetail = view.findViewById(R.id.storageMountainDetail)

        storageTowerIssues = view.findViewById(R.id.storageTowerIssues)
        storageTowerProgress = view.findViewById(R.id.storageTowerProgress)
        storageTowerPercent = view.findViewById(R.id.storageTowerPercent)
        storageTowerChange = view.findViewById(R.id.storageTowerChange)
        storageTowerDetail = view.findViewById(R.id.storageTowerDetail)

        storageChemIssues = view.findViewById(R.id.storageChemIssues)
        storageChemProgress = view.findViewById(R.id.storageChemProgress)
        storageChemPercent = view.findViewById(R.id.storageChemPercent)
        storageChemChange = view.findViewById(R.id.storageChemChange)
        storageChemDetail = view.findViewById(R.id.storageChemDetail)

        damTotal = view.findViewById(R.id.damProduction)
        damTurbine11 = view.findViewById(R.id.turbine11prod)
        damTurbine12 = view.findViewById(R.id.turbine12prod)
        damTurbine13 = view.findViewById(R.id.turbine13prod)
        damTurbine14 = view.findViewById(R.id.turbine14prod)
        damTurbine15 = view.findViewById(R.id.turbine15prod)
        damTurbine16 = view.findViewById(R.id.turbine16prod)
        damTurbine17 = view.findViewById(R.id.turbine17prod)
        damTurbine21 = view.findViewById(R.id.turbine21prod)
        damTurbine22 = view.findViewById(R.id.turbine22prod)
        damTurbine23 = view.findViewById(R.id.turbine23prod)
        damTurbine24 = view.findViewById(R.id.turbine24prod)
        damTurbine25 = view.findViewById(R.id.turbine25prod)
        damTurbine26 = view.findViewById(R.id.turbine26prod)
        damTurbine27 = view.findViewById(R.id.turbine27prod)
        damTurbine11Image = view.findViewById(R.id.turbine11image)
        damTurbine12Image = view.findViewById(R.id.turbine12image)
        damTurbine13Image = view.findViewById(R.id.turbine13image)
        damTurbine14Image = view.findViewById(R.id.turbine14image)
        damTurbine15Image = view.findViewById(R.id.turbine15image)
        damTurbine16Image = view.findViewById(R.id.turbine16image)
        damTurbine17Image = view.findViewById(R.id.turbine17image)
        damTurbine21Image = view.findViewById(R.id.turbine21image)
        damTurbine22Image = view.findViewById(R.id.turbine22image)
        damTurbine23Image = view.findViewById(R.id.turbine23image)
        damTurbine24Image = view.findViewById(R.id.turbine24image)
        damTurbine25Image = view.findViewById(R.id.turbine25image)
        damTurbine26Image = view.findViewById(R.id.turbine26image)
        damTurbine27Image = view.findViewById(R.id.turbine27image)

        chemSolarTotal = view.findViewById(R.id.chemProduction)
        chemSolarN1 = view.findViewById(R.id.arrayN1prod)
        chemSolarN2 = view.findViewById(R.id.arrayN2prod)
        chemSolarE1 = view.findViewById(R.id.arrayE1prod)
        chemSolarE2 = view.findViewById(R.id.arrayE2prod)
        chemSolarS1 = view.findViewById(R.id.arrayS1prod)
        chemSolarS2 = view.findViewById(R.id.arrayS2prod)
        chemSolarW1 = view.findViewById(R.id.arrayW1prod)
        chemSolarW2 = view.findViewById(R.id.arrayW2prod)

        mountainWindTotal = view.findViewById(R.id.windProduction)
        mountainWindTurbine1 = view.findViewById(R.id.windTurbineProd)

        updateUI()

        return view
    }

    override fun onStart() {
        super.onStart()

        // start timer for ui update
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object: Runnable {
            override fun run() {
                if (isStopped) return
                updateUI()
                mainHandler.postDelayed(this, 1000)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        isStopped = true
    }

    private fun updateUI() {
        val pssURL = getPssURL()
        val powerURL = getPowerURL()

        val queue = Volley.newRequestQueue(this.context)

        val pssRequest = StringRequest(
            Request.Method.GET, pssURL, Response.Listener<String> { response ->
            updatePssUi(response)
        },
            Response.ErrorListener { error -> Log.e("updatePSS", error.message) })

        val prodRequest = StringRequest(
            Request.Method.GET, powerURL, Response.Listener<String> { response ->
                updatePowerProdUI(response)
            },
            Response.ErrorListener { error -> Log.e("updatePowerProd", error.message) })

        queue.add(pssRequest)
        queue.add(prodRequest)
    }

    private fun updatePssUi(serverResponse: String) {
        if (activity == null) return

        val powerPackListType = object: TypeToken<MutableMap<PowerPackLocation, PowerPack>>() {}.type
        val powerPacks: MutableMap<PowerPackLocation, PowerPack> = Gson().fromJson(serverResponse, powerPackListType)

        val mountainPack = powerPacks[PowerPackLocation.OldBase] ?: return
        updatePowerPack(mountainPack, storageMountainChange, storageMountainProgress, storageMountainIssues, storageMountainPercent, storageMountainDetail)
        val towerPack = powerPacks[PowerPackLocation.NewBase] ?: return
        updatePowerPack(towerPack, storageTowerChange, storageTowerProgress, storageTowerIssues, storageTowerPercent, storageTowerDetail)
        val chemPack = powerPacks[PowerPackLocation.Chem] ?: return
        updatePowerPack(chemPack, storageChemChange, storageChemProgress, storageChemIssues, storageChemPercent, storageChemDetail)
    }

    private fun updatePowerPack(powerPack: PowerPack, changeLabel: TextView, progressBar: ProgressBar, issuesLabel: TextView, percentLabel: TextView, detailLabel: TextView) {
        if (powerPack.change >= 0) {
            changeLabel.setTextColor(resources.getColor(R.color.green, null))
            changeLabel.text = getString(R.string.pss_change, "+", powerPack.change)
        } else {
            changeLabel.setTextColor(resources.getColor(R.color.red, null))
            changeLabel.text = getString(R.string.pss_change, "", powerPack.change)
        }

        if (powerPack.maintenance) {
            progressBar.progressDrawable.setColorFilter(resources.getColor(R.color.red, null), PorterDuff.Mode.SRC_IN)
            issuesLabel.setTextColor(resources.getColor(R.color.red, null))
            issuesLabel.setText(R.string.pss_issues)
        } else {
            progressBar.progressDrawable.setColorFilter(resources.getColor(R.color.green, null), PorterDuff.Mode.SRC_IN)
            issuesLabel.setTextColor(resources.getColor(R.color.green, null))
            issuesLabel.setText(R.string.pss_no_issues)
        }

        val percentage = powerPack.stored.toFloat() / powerPack.capacity.toFloat()
        progressBar.progress =  (percentage * 100).toInt()
        percentLabel.text = getString(R.string.pss_percent, percentage * 100)
        val storageString = NumberFormat.getNumberInstance(Locale.GERMAN).format(powerPack.stored)
        val capacityString = NumberFormat.getNumberInstance(Locale.GERMAN).format(powerPack.capacity)
        detailLabel.text = getString(R.string.pss_eu_display, storageString, capacityString)
    }

    private fun updatePowerProdUI(serverResponse: String) {
        if (activity == null) return

        val powerProdListType = object: TypeToken<MutableMap<PowerGenLocation, PowerGen>>() {}.type
        val powerPacks: MutableMap<PowerGenLocation, PowerGen> = Gson().fromJson(serverResponse, powerProdListType)

        damTurbine11.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam11]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam11]?.production, turbine11image)
        damTurbine12.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam12]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam12]?.production, turbine12image)
        damTurbine13.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam13]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam13]?.production, turbine13image)
        damTurbine14.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam14]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam14]?.production, turbine14image)
        damTurbine15.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam15]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam15]?.production, turbine15image)
        damTurbine16.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam16]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam16]?.production, turbine16image)
        damTurbine17.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam17]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam17]?.production, turbine17image)

        damTurbine21.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam21]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam21]?.production, turbine21image)
        damTurbine22.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam22]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam22]?.production, turbine22image)
        damTurbine23.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam23]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam23]?.production, turbine23image)
        damTurbine24.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam24]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam24]?.production, turbine24image)
        damTurbine25.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam25]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam25]?.production, turbine25image)
        damTurbine26.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam26]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam26]?.production, turbine26image)
        damTurbine27.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.Dam27]?.production)
        updateDamTurbineImage(powerPacks[PowerGenLocation.Dam27]?.production, turbine27image)

        var damTotal = (powerPacks[PowerGenLocation.Dam11]?.production ?: 0) + (powerPacks[PowerGenLocation.Dam12]?.production ?: 0) + (powerPacks[PowerGenLocation.Dam13]?.production ?: 0) + (powerPacks[PowerGenLocation.Dam14]?.production ?: 0) + (powerPacks[PowerGenLocation.Dam15]?.production ?: 0) + (powerPacks[PowerGenLocation.Dam16]?.production ?: 0) + (powerPacks[PowerGenLocation.Dam17]?.production ?: 0)
        damTotal +=  (powerPacks[PowerGenLocation.Dam21]?.production ?: 0) + (powerPacks[PowerGenLocation.Dam22]?.production ?: 0) + (powerPacks[PowerGenLocation.Dam23]?.production ?: 0) + (powerPacks[PowerGenLocation.Dam24]?.production ?: 0) + (powerPacks[PowerGenLocation.Dam25]?.production ?: 0) + (powerPacks[PowerGenLocation.Dam26]?.production ?: 0) + (powerPacks[PowerGenLocation.Dam27]?.production ?: 0)
        this.damTotal.text = getString(R.string.eu_production, damTotal)

        chemSolarN1.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.ChemN1]?.production)
        chemSolarN2.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.ChemN2]?.production)
        chemSolarE1.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.ChemE1]?.production)
        chemSolarE2.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.ChemE2]?.production)
        chemSolarS1.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.ChemS1]?.production)
        chemSolarS2.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.ChemS2]?.production)
        chemSolarW1.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.ChemW1]?.production)
        chemSolarW2.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.ChemW2]?.production)

        val chemSolarTotal = (powerPacks[PowerGenLocation.ChemN1]?.production ?: 0) + (powerPacks[PowerGenLocation.ChemN2]?.production ?: 0) + (powerPacks[PowerGenLocation.ChemE1]?.production ?: 0) + (powerPacks[PowerGenLocation.ChemE2]?.production ?: 0) + (powerPacks[PowerGenLocation.ChemS1]?.production ?: 0) + (powerPacks[PowerGenLocation.ChemS2]?.production ?: 0) + (powerPacks[PowerGenLocation.ChemW1]?.production ?: 0) + (powerPacks[PowerGenLocation.ChemW2]?.production ?: 0)
        this.chemSolarTotal.text = getString(R.string.eu_production, chemSolarTotal)

        mountainWindTurbine1.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.MountainWind]?.production)
        mountainWindTotal.text = getString(R.string.eu_production, powerPacks[PowerGenLocation.MountainWind]?.production)
    }

    private fun updateDamTurbineImage(production: Int?, imageView: AppCompatImageView) {
        val prod = production ?: return
        if (prod > 0) {
            Glide.with(this).load("file:///android_asset/white.gif").into(imageView)
        } else {
            imageView.setImageResource(R.drawable.ic_turbine)
        }
    }

    fun getPssURL(): String {
        return activity?.getString(R.string.url) + activity?.getString(R.string.url_pss)
    }

    fun getPowerURL(): String {
        return activity?.getString(R.string.url) + activity?.getString(R.string.url_power)
    }
}