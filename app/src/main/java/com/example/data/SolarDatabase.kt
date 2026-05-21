package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "solar_projects")
data class SolarProject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val motorHp: Double,
    val boreDepth: Double,
    val waterRequirement: String, // "Rice", "Wheat", "Sugarcane", "Vegetables"
    val dailyUsageHours: Double,
    val location: String, // "Punjab", "Sindh", "KPK", "Balochistan"
    val panelWatt: Int, // 550, 580, 600
    val panelCount: Int,
    val totalSystemKw: Double,
    val inverterHp: Double,
    val inverterBrand: String, // "INVT", "INVENT", "Sky Power"
    val isBatterySupported: Boolean,
    val wireType: String, // "Copper" or "Silver"
    val panelCost: Double,
    val inverterCost: Double,
    val structureCost: Double,
    val installationCost: Double,
    val wireCost: Double,
    val profitMargin: Int, // percentage (e.g. 5, 10, 15)
    val totalCost: Double,
    val finalQuotation: Double, // totalCost + profitMargin
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "app_settings")
data class AppSetting(
    @PrimaryKey val key: String,
    val value: String
)

@Entity(tableName = "custom_pumps")
data class CustomPump(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String, // e.g. "Faisal Submersible"
    val type: String, // "Submersible", "Surface", "DC solar pump"
    val hp: Double,
    val voltage: Int, // 220, 380
    val dischargeLpm: Double, // Liters Per Minute
    val headFeet: Double
)

@Dao
interface SolarDao {
    @Query("SELECT * FROM solar_projects ORDER BY timestamp DESC")
    fun getAllProjects(): Flow<List<SolarProject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: SolarProject)

    @Update
    suspend fun updateProject(project: SolarProject)

    @Delete
    suspend fun deleteProject(project: SolarProject)

    @Query("SELECT * FROM app_settings")
    suspend fun getAllSettings(): List<AppSetting>

    @Query("SELECT * FROM app_settings WHERE `key` = :key LIMIT 1")
    suspend fun getSettingByKey(key: String): AppSetting?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSetting(setting: AppSetting)

    @Query("SELECT * FROM custom_pumps ORDER BY id DESC")
    fun getAllCustomPumps(): Flow<List<CustomPump>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPump(pump: CustomPump)

    @Delete
    suspend fun deletePump(pump: CustomPump)
}

@Database(entities = [SolarProject::class, AppSetting::class, CustomPump::class], version = 1, exportSchema = false)
abstract class SolarDatabase : RoomDatabase() {
    abstract fun solarDao(): SolarDao
}
