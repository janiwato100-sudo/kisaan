package com.example.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.CustomPump
import com.example.data.SolarDatabase
import com.example.data.SolarProject
import com.example.data.SolarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SolarViewModel(
    application: Application,
    private val repository: SolarRepository
) : AndroidViewModel(application) {

    // --- Localization ---
    var isUrdu by mutableStateOf(false)

    // --- Simple Authentication Settings ---
    var currentUserEmail by mutableStateOf("")
    var currentUserName by mutableStateOf("")
    var isGuestMode by mutableStateOf(true)
    var isLoggedIn by mutableStateOf(false)

    // Logged in check
    fun login(name: String, email: String) {
        currentUserName = name
        currentUserEmail = email
        isLoggedIn = true
        isGuestMode = false
    }

    fun logout() {
        currentUserName = ""
        currentUserEmail = ""
        isLoggedIn = false
        isGuestMode = true
    }

    // --- Saved Projects ---
    val savedProjects: StateFlow<List<SolarProject>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Custom Pump Database ---
    val customPumps: StateFlow<List<CustomPump>> = repository.customPumps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Inputs for Core Solar Calculator ---
    var motorHp by mutableDoubleStateOf(10.0) // 1HP to 100HP
    var boreDepth by mutableDoubleStateOf(150.0) // feet
    var waterRequirement by mutableStateOf("Medium") // Low, Medium, High
    var dailyUsageHours by mutableDoubleStateOf(6.0)
    var location by mutableStateOf("Punjab") // Punjab, Sindh, KPK, Balochistan
    var panelWattSelected by mutableIntStateOf(580) // 550, 580, 600
    var wireType by mutableStateOf("Copper") // Copper, Silver
    var isBatterySupported by mutableStateOf(false)
    var profitMargin by mutableIntStateOf(10) // 0% to 30% commission/profit for installer

    // --- Inputs for Water Calculator ---
    var cropAcres by mutableDoubleStateOf(5.0)
    var cropType by mutableStateOf("Wheat") // Wheat, Rice, Sugarcane, Vegetables, Orchards

    // --- Admin/Pricing Inputs (Live Editable) ---
    var pricePanel550w by mutableDoubleStateOf(20500.0)
    var pricePanel580w by mutableDoubleStateOf(22000.0)
    var pricePanel600w by mutableDoubleStateOf(23200.0)
    var priceInverterPerHp by mutableDoubleStateOf(14500.0)
    var priceStructurePerPanel by mutableDoubleStateOf(4500.0)
    var priceInstallationBase by mutableDoubleStateOf(25000.0)
    var priceWireCopperPerM by mutableDoubleStateOf(1400.0)
    var priceWireSilverPerM by mutableDoubleStateOf(700.0)

    // --- Fresh Pump Addition State ---
    var addPumpName by mutableStateOf("")
    var addPumpType by mutableStateOf("Submersible")
    var addPumpHp by mutableDoubleStateOf(10.0)
    var addPumpVoltage by mutableIntStateOf(380)
    var addPumpDischargeLpm by mutableDoubleStateOf(400.0)
    var addPumpHeadFeet by mutableDoubleStateOf(150.0)

    // Initializer
    init {
        viewModelScope.launch {
            repository.checkAndSeedPumps()
            loadPrices()
        }
    }

    // Load price configurations from Room database
    suspend fun loadPrices() {
        pricePanel550w = repository.getPriceSetting("panel_550w")
        pricePanel580w = repository.getPriceSetting("panel_580w")
        pricePanel600w = repository.getPriceSetting("panel_600w")
        priceInverterPerHp = repository.getPriceSetting("inverter_per_hp")
        priceStructurePerPanel = repository.getPriceSetting("structure_per_panel")
        priceInstallationBase = repository.getPriceSetting("installation_base")
        priceWireCopperPerM = repository.getPriceSetting("wire_copper_per_m")
        priceWireSilverPerM = repository.getPriceSetting("wire_silver_per_m")
    }

    fun updatePriceSettings(
        p550: Double, p580: Double, p600: Double,
        pinv: Double, pstruct: Double, pinst: Double,
        wcopp: Double, wsilv: Double
    ) {
        viewModelScope.launch {
            repository.savePriceSetting("panel_550w", p550)
            repository.savePriceSetting("panel_580w", p580)
            repository.savePriceSetting("panel_600w", p600)
            repository.savePriceSetting("inverter_per_hp", pinv)
            repository.savePriceSetting("structure_per_panel", pstruct)
            repository.savePriceSetting("installation_base", pinst)
            repository.savePriceSetting("wire_copper_per_m", wcopp)
            repository.savePriceSetting("wire_silver_per_m", wsilv)
            loadPrices()
        }
    }

    fun resetPricesToDefault() {
        viewModelScope.launch {
            repository.savePriceSetting("panel_550w", 20500.0)
            repository.savePriceSetting("panel_580w", 22000.0)
            repository.savePriceSetting("panel_600w", 23200.0)
            repository.savePriceSetting("inverter_per_hp", 14500.0)
            repository.savePriceSetting("structure_per_panel", 4500.0)
            repository.savePriceSetting("installation_base", 25000.0)
            repository.savePriceSetting("wire_copper_per_m", 1400.0)
            repository.savePriceSetting("wire_silver_per_m", 700.0)
            loadPrices()
        }
    }

    // Add Custom Pump Model
    fun addNewPump() {
        if (addPumpName.isNotBlank()) {
            val newPump = CustomPump(
                name = addPumpName,
                type = addPumpType,
                hp = addPumpHp,
                voltage = addPumpVoltage,
                dischargeLpm = addPumpDischargeLpm,
                headFeet = addPumpHeadFeet
            )
            viewModelScope.launch {
                repository.addCustomPump(newPump)
                // Clear state
                addPumpName = ""
            }
        }
    }

    fun removePump(pump: CustomPump) {
        viewModelScope.launch {
            repository.deleteCustomPump(pump)
        }
    }

    // --- Dynamic Calculation Logic ---
    fun calculateSizing(): SizingResult {
        // Core peak sizing: motor requires 1.4x solar multiplier for steady running in Pakistani dynamic regions
        val multiplier = 1.38
        val baseKw = (motorHp * 0.746 * multiplier)
        val finalKw = if (isBatterySupported) baseKw * 1.15 else baseKw // 15% loss adjustment for charging circuit

        val panelPricePerItem = when (panelWattSelected) {
            550 -> pricePanel550w
            580 -> pricePanel580w
            else -> pricePanel600w
        }

        val panelsNeededCount = kotlin.math.ceil((finalKw * 1000) / panelWattSelected).toInt()
        val calculatedActualKw = (panelsNeededCount * panelWattSelected) / 1000.0

        // Inverter size: 3-phase submersible startup load requires 1.5x motor horsepower inverter
        val inverterHp = if (motorHp <= 5.0) motorHp * 2.0 else motorHp * 1.5

        // Sun factor check based on geography selection
        val sunHours = when (location) {
            "Punjab" -> 5.5
            "Sindh" -> 5.8
            "KPK" -> 5.2
            "Balochistan" -> 6.0
            else -> 5.5
        }

        // Efficiency %: Deep bore depth decreases performance. Temperature decreases panel efficiency.
        val boreLoss = (boreDepth / 350.0) * 12.0 // small loss percentage for deeper bore lifts, max ~12%
        val seasonalFactor = 0.92 // typical peak running efficiency
        val baseEff = (95.0 - boreLoss) * seasonalFactor
        val finalEfficiency = baseEff.coerceIn(55.0, 95.0)

        // Costs calculation
        val totalPanelsCost = panelsNeededCount * panelPricePerItem
        val totalInverterCost = inverterHp * priceInverterPerHp
        val totalStructureCost = panelsNeededCount * priceStructurePerPanel
        val wirePricePerMeter = if (wireType == "Copper") priceWireCopperPerM else priceWireSilverPerM
        val wireQuantityMeter = 25.0 // average cable length down the bore & to panels
        val totalWireCost = wireQuantityMeter * wirePricePerMeter
        val totalInstallationCost = priceInstallationBase

        val rawTotal = totalPanelsCost + totalInverterCost + totalStructureCost + totalWireCost + totalInstallationCost
        val systemCommissionVal = rawTotal * (profitMargin / 100.0)
        val finalQuotationPrice = rawTotal + systemCommissionVal

        // Matching Inverters Recommendation Description
        val recommendedModel = when {
            motorHp <= 3.0 -> "INVT GD100 (Single-Phase to 3-Phase Core Converter)"
            motorHp <= 10.0 -> "INVT GD200A 3-Phase Industrial Standard"
            motorHp <= 30.0 -> "INVENT Series v3.2 Heavy Duty 3-Phase"
            else -> "Sky Power Master Solar Tri-phase VFD"
        }

        // Wiring recommendation description
        val wireGaugeRecommend = if (wireType == "Copper") {
            when {
                motorHp <= 5.0 -> "4 Core 4.0mm Copper Flat Cable"
                motorHp <= 15.0 -> "4 Core 6.0mm Copper Flat Cable"
                else -> "4 Core 10.0mm Heavy Copper Flat Cable"
            }
        } else {
            when {
                motorHp <= 5.0 -> "4 Core 6.0mm Aluminium Flat Cable"
                motorHp <= 15.0 -> "4 Core 10.0mm Aluminium Flat Cable"
                else -> "4 Core 16.0mm Premium Aluminium Flat Cable"
            }
        }

        return SizingResult(
            solarCapacityKw = calculatedActualKw,
            panelsCount = panelsNeededCount,
            inverterSizeHp = inverterHp,
            recommendedInverterModel = recommendedModel,
            panelsCost = totalPanelsCost,
            inverterCost = totalInverterCost,
            structureCost = totalStructureCost,
            installationCost = totalInstallationCost,
            wireCost = totalWireCost,
            commissionAmount = systemCommissionVal,
            rawTotalCost = rawTotal,
            finalQuotationPrice = finalQuotationPrice,
            systemEfficiencyPercent = finalEfficiency,
            peakSunHours = sunHours,
            wireRecommendationDetail = wireGaugeRecommend,
            wireUsedType = wireType
        )
    }

    // Save Project to local database
    fun saveProject(customName: String, onFinished: () -> Unit) {
        val sizingResult = calculateSizing()
        val formattedName = if (customName.isNotBlank()) customName else "Calculated Project (${motorHp}HP)"

        val project = SolarProject(
            name = formattedName,
            motorHp = motorHp,
            boreDepth = boreDepth,
            waterRequirement = waterRequirement,
            dailyUsageHours = dailyUsageHours,
            location = location,
            panelWatt = panelWattSelected,
            panelCount = sizingResult.panelsCount,
            totalSystemKw = sizingResult.solarCapacityKw,
            inverterHp = sizingResult.inverterSizeHp,
            inverterBrand = if (motorHp <= 10) "INVT" else if (motorHp <= 30) "INVENT" else "Sky Power",
            isBatterySupported = isBatterySupported,
            wireType = wireType,
            panelCost = sizingResult.panelsCost,
            inverterCost = sizingResult.inverterCost,
            structureCost = sizingResult.structureCost,
            installationCost = sizingResult.installationCost,
            wireCost = sizingResult.wireCost,
            profitMargin = profitMargin,
            totalCost = sizingResult.rawTotalCost,
            finalQuotation = sizingResult.finalQuotationPrice
        )

        viewModelScope.launch {
            repository.insertProject(project)
            onFinished()
        }
    }

    fun deleteProjectRecord(project: SolarProject) {
        viewModelScope.launch {
            repository.deleteProject(project)
        }
    }

    // --- Water Runtime Calculations ---
    fun calculateWaterRuntime(): WaterRuntimeResult {
        // Water requirement based on crops in Liters per acre per watering cycle
        val waterLitersPerAcre = when (cropType) {
            "Wheat" -> 250000.0   // Relatively low frequency
            "Rice" -> 850000.0    // Heavy immersion
            "Sugarcane" -> 600000.0 // Moderate water logging
            "Vegetables" -> 180000.0 // Shallow roots
            "Orchards" -> 350000.0 // Medium soaking
            else -> 300000.0
        }

        val totalWaterNeededLiters = cropAcres * waterLitersPerAcre

        // Dynamic water discharge estimation based on Pump HP and bore suction heads
        // Formula: Liters per minute ≈ HP * 45 - (BoreDepth/10 * 3)
        val estimatedDischargeLpm = ((motorHp * 45.0) - (boreDepth / 10.0 * 3.0)).coerceAtLeast(60.0)

        // Total pump runtime required to satisfy crop limits
        val runtimeMinutes = totalWaterNeededLiters / estimatedDischargeLpm
        val runtimeHours = runtimeMinutes / 60.0

        // Number of sunny days to complete single irrigation cycle (if max solar running daily hours)
        val daysRequired = kotlin.math.ceil(runtimeHours / dailyUsageHours).toInt()

        return WaterRuntimeResult(
            totalWaterLiters = totalWaterNeededLiters,
            flowRateLpm = estimatedDischargeLpm,
            runtimeHoursTotal = runtimeHours,
            daysNeeded = daysRequired
        )
    }

    // --- WhatsApp Message Generator ---
    fun generateWhatsAppText(customName: String = "App Inquiry"): String {
        val sizing = calculateSizing()
        val waterRes = calculateWaterRuntime()
        val langFlag = if (isUrdu) "🇵🇰 LOGS" else "☀️ ESTIMATE"

        return """
*Kisaan Solar Service Report* ($langFlag)
--------------------------
👤 *Customer:* ${if (currentUserName.isNotBlank()) currentUserName else "Guest Farmer"}
🏠 *Location:* $location (Pakistan)
💧 *Bore Depth:* ${boreDepth}ft
🐎 *Motor HP:* ${motorHp}HP
🌿 *Crop:* $cropType (${cropAcres} Acres)

⚡ *System Sizing:*
- Solar Capacity: ${"%.2f".format(sizing.solarCapacityKw)} kW
- Total Panels: ${sizing.panelsCount} x ${panelWattSelected}W
- Controller Inverter: ${"%.1f".format(sizing.inverterSizeHp)}HP (${sizing.recommendedInverterModel})
- Wiring: ${sizing.wireRecommendationDetail} (${sizing.wireUsedType})

💰 *BOM Cost Estimate:*
- Panel Cost: PKR ${"%,d".format(sizing.panelsCost.toInt())}
- Inverter Cost: PKR ${"%,d".format(sizing.inverterCost.toInt())}
- Structure: PKR ${"%,d".format(sizing.structureCost.toInt())}
- Wiring: PKR ${"%,d".format(sizing.wireCost.toInt())}
- Installation: PKR ${"%,d".format(sizing.installationCost.toInt())}
========================
🔥 *Total Budget:* PKR *${"%,d".format(sizing.finalQuotationPrice.toInt())}*
--------------------------
_Generated via kisaan solar Application_
🇵🇰 Helpline WhatsApp: 03279791100
        """.trimIndent()
    }
}

// Data class for calculation output
data class SizingResult(
    val solarCapacityKw: Double,
    val panelsCount: Int,
    val inverterSizeHp: Double,
    val recommendedInverterModel: String,
    val panelsCost: Double,
    val inverterCost: Double,
    val structureCost: Double,
    val installationCost: Double,
    val wireCost: Double,
    val commissionAmount: Double,
    val rawTotalCost: Double,
    val finalQuotationPrice: Double,
    val systemEfficiencyPercent: Double,
    val peakSunHours: Double,
    val wireRecommendationDetail: String,
    val wireUsedType: String
)

data class WaterRuntimeResult(
    val totalWaterLiters: Double,
    val flowRateLpm: Double,
    val runtimeHoursTotal: Double,
    val daysNeeded: Int
)

class SolarViewModelFactory(
    private val application: Application,
    private val repository: SolarRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SolarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SolarViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
