package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class SolarRepository(private val solarDao: SolarDao) {

    val allProjects: Flow<List<SolarProject>> = solarDao.getAllProjects()
    val customPumps: Flow<List<CustomPump>> = solarDao.getAllCustomPumps()

    // Default prices in PKR
    private val defaultPrices = mapOf(
        "panel_550w" to "20500", // PKR per panel
        "panel_580w" to "22000",
        "panel_600w" to "23200",
        "inverter_per_hp" to "14500", // PKR per HP of motor
        "structure_per_panel" to "4500", // Mounting frame per panel
        "installation_base" to "25000", // Flat labor & basic accessories
        "wire_copper_per_m" to "1400", // COPPER wire per meter (avg 25m needed)
        "wire_silver_per_m" to "700"  // SILVER wire per meter
    )

    private val defaultPumps = listOf(
        CustomPump(name = "KSB Submersible S-100", type = "Submersible", hp = 10.0, voltage = 380, dischargeLpm = 450.0, headFeet = 180.0),
        CustomPump(name = "Golden Submersible G-50", type = "Submersible", hp = 5.0, voltage = 220, dischargeLpm = 250.0, headFeet = 120.0),
        CustomPump(name = "Faisal Surface Monoblock", type = "Surface pump", hp = 7.5, voltage = 380, dischargeLpm = 380.0, headFeet = 80.0),
        CustomPump(name = "DC Solar High Head 3HP", type = "DC solar pump", hp = 3.0, voltage = 220, dischargeLpm = 180.0, headFeet = 150.0)
    )

    suspend fun insertProject(project: SolarProject) = solarDao.insertProject(project)
    suspend fun updateProject(project: SolarProject) = solarDao.updateProject(project)
    suspend fun deleteProject(project: SolarProject) = solarDao.deleteProject(project)

    // Seed initial pumps if empty
    suspend fun checkAndSeedPumps() {
        // Collect first list of custom pumps
        val currentPumps = customPumps.firstOrNull() ?: emptyList()
        if (currentPumps.isEmpty()) {
            for (pump in defaultPumps) {
                solarDao.insertPump(pump)
            }
        }
    }

    // Dynamic price settings getter
    suspend fun getPriceSetting(key: String): Double {
        val setting = solarDao.getSettingByKey(key)
        val valueStr = setting?.value ?: defaultPrices[key] ?: "0"
        return valueStr.toDoubleOrNull() ?: 0.0
    }

    suspend fun savePriceSetting(key: String, value: Double) {
        solarDao.saveSetting(AppSetting(key, value.toString()))
    }

    // Custom pump operations
    suspend fun addCustomPump(pump: CustomPump) = solarDao.insertPump(pump)
    suspend fun deleteCustomPump(pump: CustomPump) = solarDao.deletePump(pump)
}
