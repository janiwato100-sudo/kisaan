package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.CustomPump
import com.example.data.SolarProject
import com.example.viewmodel.SolarViewModel

// --- Agricultural Solar Theming Colors ---
val OrganicGreen = Color(0xFF1B5E20)
val SoftLeafGreen = Color(0xFF4CAF50)
val SolarGold = Color(0xFFF57C00)
val DarkSkyBlue = Color(0xFF0D47A1)
val WarmSandBackground = Color(0xFFFCFCF7)
val CardLeafTint = Color(0xFFF1F8E9)

object SolarTranslations {
    private val en = mapOf(
        "app_title" to "Kisaan Solar",
        "app_subtitle" to "Smart Tubewell Sizer & Crop Planner",
        "lang_btn" to "اردو میں دیکھیں",
        "home" to "Home",
        "calculator" to "Sizing",
        "pump_water" to "Pump & Crops",
        "bom_quote" to "BOM Quotation",
        "tech_guide" to "Guide & Admin",
        "guest_mode" to "Guest Mode Enabled",
        "login_prompt" to "Customer Sign-In",
        "register_desc" to "Enter farmer name & email to save personalized solar quotes.",
        "p_name_hint" to "Farmer Name",
        "p_email_hint" to "Email / Mobile",
        "login_now" to "Set Active Profile",
        "logout" to "Change Profile",
        "saved_projects" to "Saved Solar Projects",
        "hp_label" to "Motor Power:",
        "bore_label" to "Bore Depth:",
        "water_lbl" to "Discharge Demand:",
        "location_lbl" to "Location Area:",
        "panel_select_lbl" to "Solar Panel Type:",
        "wire_mat_lbl" to "Wiring Alloy:",
        "battery_lbl" to "Include Battery Bank",
        "efficiency_lbl" to "System Efficiency",
        "panels_count_lbl" to "Panels Count",
        "recommended_inv" to "Recommended VFD Inverter",
        "estimated_pkr" to "Estimated Cost (PKR)",
        "save_to_history" to "Save Sizing to History",
        "project_history" to "Project History",
        "crop_calculator" to "Crop Water Planner",
        "select_crop" to "Select Active Crop:",
        "target_acres" to "Target Acres Land:",
        "water_needed" to "Total Water Output Needed",
        "est_runtime" to "Est. Pump Runtime Needed",
        "days_needed" to "Irri-Days per Watering Cycle",
        "inquiry_whatsapp" to "Whatsapp Sizing Report",
        "call_support" to "Call Helpine (03279791100)",
        "inverter_spec_guide" to "3-Phase VFD Inverter Tuning",
        "admin_config" to "Solar Pricing Controller",
        "compare_mode" to "Comparative Sizing Suite",
        "current_setup" to "Current Design",
        "compare_setup" to "Comparison Draft",
        "add_custom_pump" to "Add Custom Pump Model",
        "price_per_watt" to "Solar pricing (PKR / item)",
        "restore_defaults" to "Restore Market Defaults",
        "installer_commission" to "Add Installer Margin %",
        "invoice_preview" to "Digital Invoice Receipt",
        "share_bom" to "Forward Bill of Materials Map",
        "empty_history" to "No saved solar projects found. Run a sizing calculation and save!",
        "wire_gauge" to "Recommended Wire Cable",
        "sun_hours" to "Peak Sun Hours",
        "pump_specs" to "Pump Technical Matrix",
        "amp_calc" to "Current draw (Amp)",
        "volts" to "Voltage"
    )

    private val ur = mapOf(
        "app_title" to "کیسان سولر",
        "app_subtitle" to "سمارٹ ٹیوب ویل اور فصلوں کے لیے کیلکولیٹر",
        "lang_btn" to "View in English",
        "home" to "ڈیش بورڈ",
        "calculator" to "سولر سائزنگ",
        "pump_water" to "پمپ اور فصلیں",
        "bom_quote" to "بل آف میٹریل",
        "tech_guide" to "رہنمائی و ایڈمن",
        "guest_mode" to "بغیر لاگ ان (مہمان موڈ)",
        "login_prompt" to "کسٹمر پروفائل بنائیں",
        "register_desc" to "سولر کوٹیشن محفوظ کرنے کے لیے کسان کا نام اور موبائل درج کریں۔",
        "p_name_hint" to "کسان کا نام",
        "p_email_hint" to "موبائل یا ای میل",
        "login_now" to "پروفائل لاگو کریں",
        "logout" to "پروفائل تبدیل کریں",
        "saved_projects" to "محفوظ شدہ کوٹیشنز",
        "hp_label" to "موٹر ہارس پاور:",
        "bore_label" to "بور کی گہرائی:",
        "water_lbl" to "پانی کا بہاؤ:",
        "location_lbl" to "مقام (صوبہ):",
        "panel_select_lbl" to "سولر پینل کی قسم:",
        "wire_mat_lbl" to "تار کا مٹیریل:",
        "battery_lbl" to "بیٹری سپورٹ شامل کریں",
        "efficiency_lbl" to "سسٹم کی کارکردگی",
        "panels_count_lbl" to "ضروری سولر پینلز",
        "recommended_inv" to "مجوزہ VFD انورٹر",
        "estimated_pkr" to "کل تخمینی قیمت (PKR)",
        "save_to_history" to "کوٹیشن ہسٹری میں محفوظ کریں",
        "project_history" to "محفوظ شدہ منصوبے",
        "crop_calculator" to "فصل کے پانی کا پلانر",
        "select_crop" to "فصل کا انتخاب کریں:",
        "target_acres" to "کل رقبہ (ایکڑ):",
        "water_needed" to "مطلوبہ کل پانی (لیٹر)",
        "est_runtime" to "ضروری پمپ ٹائم (گھنٹے)",
        "days_needed" to "پانی لگانے کے دن",
        "inquiry_whatsapp" to "واٹس ایپ سولر رپورٹ",
        "call_support" to "ہیلپ لائن (03279791100)",
        "inverter_spec_guide" to "تھری فیز انورٹر سیٹنگز",
        "admin_config" to "سولر ریٹس اپڈیٹ کریں",
        "compare_mode" to "موازنہ سولر سائزنگ",
        "current_setup" to "موجودہ سسٹم",
        "compare_setup" to "متبادل سسٹم موازنہ",
        "add_custom_pump" to "نیا پمپ ماڈل شامل کریں",
        "price_per_watt" to "سولر ریٹ لسٹ (مٹیریل)",
        "restore_defaults" to "مارکیٹ ریٹ بحال کریں",
        "installer_commission" to "انسٹالر منافع فیصد ٪",
        "invoice_preview" to "ڈیجیٹل رسید بل",
        "share_bom" to "بل شیئر کریں واٹس ایپ",
        "empty_history" to "ابھی تک کوئی سولر پروجیکٹ محفوظ نہیں ملا۔ حساب کر کے محفوظ کریں!",
        "wire_gauge" to "تجویز کردہ کیبل سائز",
        "sun_hours" to "دھوپ کے گھنٹے (اوسط)",
        "pump_specs" to "پمپ کی تفصیلات",
        "amp_calc" to "کرنٹ ایمپیئر (Amp)",
        "volts" to "وولٹیج"
    )

    fun t(key: String, isUrdu: Boolean): String {
        return if (isUrdu) ur[key] ?: en[key] ?: key else en[key] ?: key
    }
}

@Composable
fun SolarAppUI(viewModel: SolarViewModel) {
    var activeTab by remember { mutableStateOf("home") }
    val isUrdu = viewModel.isUrdu
    val context = LocalContext.current

    // Observe saved lists
    val projectsList by viewModel.savedProjects.collectAsStateWithLifecycle()
    val pumpsList by viewModel.customPumps.collectAsStateWithLifecycle()

    fun t(key: String): String = SolarTranslations.t(key, isUrdu)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_scaffold"),
        bottomBar = {
            NavigationBar(
                containerColor = OrganicGreen,
                contentColor = Color.White,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                listOf(
                    Triple("home", Icons.Default.Home, t("home")),
                    Triple("calc", Icons.Default.Bolt, t("calculator")),
                    Triple("water", Icons.Default.WaterDrop, t("pump_water")),
                    Triple("quote", Icons.Default.ReceiptLong, t("bom_quote")),
                    Triple("admin", Icons.Default.AdminPanelSettings, t("tech_guide"))
                ).forEach { (id, icon, label) ->
                    NavigationBarItem(
                        selected = activeTab == id,
                        onClick = { activeTab = id },
                        icon = { Icon(icon, contentDescription = label, tint = if (activeTab == id) SolarGold else Color.White) },
                        label = { Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = if (activeTab == id) SolarGold else Color.White) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = OrganicGreen.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WarmSandBackground)
        ) {
            // --- Custom Agricultural Header App Bar ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(OrganicGreen, SoftLeafGreen)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WbSunny, contentDescription = "Sun icon", tint = SolarGold, modifier = Modifier.size(26.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = t("app_title"),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 24.sp
                                )
                            )
                        }
                        Text(
                            text = t("app_subtitle"),
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                    }

                    Button(
                        onClick = { viewModel.isUrdu = !viewModel.isUrdu },
                        colors = ButtonDefaults.buttonColors(containerColor = SolarGold, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("lang_toggle_btn")
                    ) {
                        Text(t("lang_btn"), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // --- Contents Area based on tab ---
            AnimatedContent(
                targetState = activeTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                modifier = Modifier.weight(1f)
            ) { tab ->
                when (tab) {
                    "home" -> HomeScreen(viewModel, projectsList, { activeTab = "calc" })
                    "calc" -> SizingCalculatorScreen(viewModel, { activeTab = "quote" })
                    "water" -> PumpAndWaterScreen(viewModel, pumpsList)
                    "quote" -> BOMQuoteScreen(viewModel)
                    "admin" -> GuideAndAdminScreen(viewModel, pumpsList)
                }
            }
        }
    }
}

// ==========================================
// 1. HOME SCREEN & USER PORTAL PROFILE
// ==========================================
@Composable
fun HomeScreen(
    viewModel: SolarViewModel,
    savedProjects: List<SolarProject>,
    onStartSizing: () -> Unit
) {
    val isUrdu = viewModel.isUrdu
    fun t(key: String): String = SolarTranslations.t(key, isUrdu)

    var inputName by remember { mutableStateOf("") }
    var inputEmail by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Welcome / User Profile Area
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (viewModel.isLoggedIn) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.AccountCircle,
                                    contentDescription = "Avatar",
                                    tint = OrganicGreen,
                                    modifier = Modifier.size(44.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = viewModel.currentUserName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = OrganicGreen
                                    )
                                    Text(
                                        text = viewModel.currentUserEmail,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                            TextButton(onClick = { viewModel.logout() }) {
                                Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.Red, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(t("logout"), color = Color.Red, fontSize = 12.sp)
                            }
                        }
                    } else {
                        // User login registration
                        Text(t("login_prompt"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = OrganicGreen)
                        Text(t("register_desc"), fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = inputName,
                                onValueChange = { inputName = it },
                                label = { Text(t("p_name_hint"), fontSize = 11.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("farmer_name_input"),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = inputEmail,
                                onValueChange = { inputEmail = it },
                                label = { Text(t("p_email_hint"), fontSize = 11.sp) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("farmer_contact_input"),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Button(
                            onClick = {
                                if (inputName.isNotBlank() && inputEmail.isNotBlank()) {
                                    viewModel.login(inputName, inputEmail)
                                }
                            },
                            enabled = inputName.isNotBlank() && inputEmail.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = OrganicGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                        ) {
                            Text(t("login_now"), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = "guest", tint = Color.Gray, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(t("guest_mode"), fontSize = 11.sp, color = Color.Gray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                        }
                    }
                }
            }
        }

        // Quick Sizing Launcher Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(OrganicGreen, DarkSkyBlue)
                        )
                    )
                    .clickable { onStartSizing() }
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isUrdu) "نئی سولر ٹیوب ویل کیلکولیشن کریں" else "Create New Solar Sizing Project",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isUrdu) "کلک کر کے ہارس پاور، بور گہرائی اور سولر پینل سائز نکالیں۔" else "Specify HP motor size, depth & budget to plan real system.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Go",
                        tint = SolarGold,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, RoundedCornerShape(50))
                            .padding(6.dp)
                    )
                }
            }
        }

        // Saved calculation history list
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = t("saved_projects") + " (${savedProjects.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = OrganicGreen
                )
            }
        }

        if (savedProjects.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "empty", tint = Color.LightGray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = t("empty_history"),
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(savedProjects) { project ->
                SavedProjectCard(project = project, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SavedProjectCard(project: SolarProject, viewModel: SolarViewModel) {
    val context = LocalContext.current
    val isUrdu = viewModel.isUrdu

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(project.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OrganicGreen)
                    Text(
                        text = if (isUrdu) "مقام: ${project.location} | بور: ${project.boreDepth} فٹ" else "Location: ${project.location} | Bore: ${project.boreDepth} ft",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                IconButton(
                    onClick = { viewModel.deleteProjectRecord(project) },
                    modifier = Modifier.testTag("delete_project_${project.id}")
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.8f))
                }
            }

            Divider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(if (isUrdu) "ضروری سولر سائز" else "Solar Size", fontSize = 11.sp, color = Color.Gray)
                    Text("${"%.2f".format(project.totalSystemKw)} kW", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SolarGold)
                }
                Column {
                    Text(if (isUrdu) "پینلز تعداد" else "Panels Count", fontSize = 11.sp, color = Color.Gray)
                    Text("${project.panelCount} x ${project.panelWatt}W", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(if (isUrdu) "کل قیمت" else "Final Quote", fontSize = 11.sp, color = Color.Gray)
                    Text("PKR ${"%,d".format(project.finalQuotation.toInt())}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = OrganicGreen)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Fast actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        // Restore project values to current calculator inputs
                        viewModel.motorHp = project.motorHp
                        viewModel.boreDepth = project.boreDepth
                        viewModel.waterRequirement = project.waterRequirement
                        viewModel.location = project.location
                        viewModel.panelWattSelected = project.panelWatt
                        viewModel.wireType = project.wireType
                        viewModel.isBatterySupported = project.isBatterySupported
                        viewModel.profitMargin = project.profitMargin
                        Toast.makeText(context, if (isUrdu) "پیرامیٹرز بحال کر دیے گئے۔ سائزنگ ٹیب دیکھیں!" else "Parameters loaded into active Sizing tab!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SoftLeafGreen.copy(alpha = 0.1f), contentColor = OrganicGreen),
                    elevation = null,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Refresh, contentDescription = "Load", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isUrdu) "ترمیم / بحال کریں" else "Edit Draft", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val smsText = """
*Kisaan Solar Sizing Saved Project*
-------------------------
📂 *Project Name:* ${project.name}
🐎 *Motor:* ${project.motorHp} HP
🌊 *Bore Depth:* ${project.boreDepth} feet
🇵🇰 *Location:* ${project.location}
⚡ *Solar Required:* ${"%.2f".format(project.totalSystemKw)} kW (${project.panelCount} panels)
📟 *VFD Driver:* ${project.inverterBrand}
🔌 *Wiring:* ${project.wireType} Material
💰 *Budget Quote:* PKR ${"%,d".format(project.finalQuotation.toInt())}
-------------------------
Generated via kisaan solar app.
Helpline support: 03279791100
                        """.trimIndent()
                        val encoded = Uri.encode(smsText)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=923279791100&text=$encoded"))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OrganicGreen),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(14.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isUrdu) "شیر کریں" else "WhatsApp BOM", fontSize = 11.sp, color = Color.White)
                    }
                }
            }
        }
    }
}


// ==========================================
// 2. CORE SOLAR SIZING CALCULATOR SCREEN
// ==========================================
@Composable
fun SizingCalculatorScreen(
    viewModel: SolarViewModel,
    onNavigateToBom: () -> Unit
) {
    val isUrdu = viewModel.isUrdu
    fun t(key: String): String = SolarTranslations.t(key, isUrdu)
    val context = LocalContext.current

    val sizingResult = viewModel.calculateSizing()
    var projectSaveDialogVisible by remember { mutableStateOf(false) }
    var inputProjectName by remember { mutableStateOf("") }

    var isCompareSuiteEnabled by remember { mutableStateOf(false) }
    var compareMotorHp by remember { mutableDoubleStateOf(15.0) }
    var compareBoreDepth by remember { mutableDoubleStateOf(180.0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Compare suite toggling banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SolarGold.copy(alpha = 0.08f)),
                border = borderStrike(SolarGold.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Compare, contentDescription = "compare", tint = SolarGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(t("compare_mode"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                            Text(if (isUrdu) "دو مختلف ہارس پاور سسٹمز کا ایک ساتھ موازنہ کریں" else "Compare two motor system capacities simultaneously", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                    Switch(
                        checked = isCompareSuiteEnabled,
                        onCheckedChange = { isCompareSuiteEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = SolarGold, checkedTrackColor = SolarGold.copy(alpha = 0.3f))
                    )
                }
            }
        }

        // INPUTS BLOCK
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isCompareSuiteEnabled) t("current_setup") else (if (isUrdu) "ٹیوب ویل ان پٹ تبدیل کریں" else "Tubewell Technical Inputs"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = OrganicGreen
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Motor HP Slider Row
                    Text(
                        text = "${t("hp_label")} ${viewModel.motorHp.toInt()} HP",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                    Slider(
                        value = viewModel.motorHp.toFloat(),
                        onValueChange = { viewModel.motorHp = it.toInt().toDouble() },
                        valueRange = 1f..100f,
                        steps = 99,
                        colors = SliderDefaults.colors(thumbColor = OrganicGreen, activeTrackColor = OrganicGreen),
                        modifier = Modifier.testTag("motor_hp_slider")
                    )

                    // Bore Depth Slider Row
                    Text(
                        text = "${t("bore_label")} ${viewModel.boreDepth.toInt()} Ft",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                    Slider(
                        value = viewModel.boreDepth.toFloat(),
                        onValueChange = { viewModel.boreDepth = it.toInt().toDouble() },
                        valueRange = 30f..1000f,
                        steps = 97,
                        colors = SliderDefaults.colors(thumbColor = SoftLeafGreen, activeTrackColor = SoftLeafGreen),
                        modifier = Modifier.testTag("bore_depth_slider")
                    )

                    // Other drop selection elements
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(t("location_lbl"), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Punjab", "Sindh", "KPK", "Balochistan").forEach { prov ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (viewModel.location == prov) OrganicGreen else Color.LightGray.copy(alpha = 0.2f))
                                    .clickable { viewModel.location = prov }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    prov,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (viewModel.location == prov) Color.White else Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(t("panel_select_lbl"), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(550, 580, 600).forEach { watt ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (viewModel.panelWattSelected == watt) SolarGold else Color.LightGray.copy(alpha = 0.2f))
                                    .clickable { viewModel.panelWattSelected = watt }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${watt}W",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (viewModel.panelWattSelected == watt) Color.White else Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(t("wire_mat_lbl"), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Copper", "Silver").forEach { alloy ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (viewModel.wireType == alloy) DarkSkyBlue else Color.LightGray.copy(alpha = 0.2f))
                                    .clickable { viewModel.wireType = alloy }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    alloy,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (viewModel.wireType == alloy) Color.White else Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Battery support checkbox row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.isBatterySupported = !viewModel.isBatterySupported }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = viewModel.isBatterySupported,
                            onCheckedChange = { viewModel.isBatterySupported = it },
                            colors = CheckboxDefaults.colors(checkedColor = OrganicGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(t("battery_lbl"), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text(if (isUrdu) "ہائبرڈ بیٹری بیک اپ کے لیے 15٪ اضافی سولر صلاحیت چارجر سرکٹ" else "Hybrid backup expands total kW calculations for chargers", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        // COMPARE SUITE ACTIVE PORT BLOCK
        if (isCompareSuiteEnabled) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = WarmSandBackground),
                    shape = RoundedCornerShape(16.dp),
                    border = borderStrike(SolarGold.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = t("compare_setup"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = SolarGold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "${t("hp_label")} ${compareMotorHp.toInt()} HP",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Slider(
                            value = compareMotorHp.toFloat(),
                            onValueChange = { compareMotorHp = it.toInt().toDouble() },
                            valueRange = 1f..100f,
                            colors = SliderDefaults.colors(thumbColor = SolarGold, activeTrackColor = SolarGold)
                        )

                        Text(
                            text = "${t("bore_label")} ${compareBoreDepth.toInt()} Ft",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Slider(
                            value = compareBoreDepth.toFloat(),
                            onValueChange = { compareBoreDepth = it.toInt().toDouble() },
                            valueRange = 30f..1000f,
                            colors = SliderDefaults.colors(thumbColor = Color.DarkGray, activeTrackColor = Color.DarkGray)
                        )
                    }
                }
            }
        }

        // CALCULATED OUTPUT SYSTEM PREVIEW CARD
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = OrganicGreen),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Output, contentDescription = "Sizing Result", tint = SolarGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUrdu) "حساب شدہ سولر ریکوائرمنٹ" else "System Sizing Design Parameters",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(if (isUrdu) "ضروری سولر طاقت" else "Solar Pack kW", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("${"%.2f".format(sizingResult.solarCapacityKw)} kW", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = SolarGold)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(t("panels_count_lbl"), fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("${sizingResult.panelsCount} Panels", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(t("recommended_inv"), fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("${sizingResult.inverterSizeHp.toInt()} HP / VFD Driver", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(sizingResult.recommendedInverterModel, fontSize = 11.sp, color = SolarGold)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(t("efficiency_lbl"), fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("${"%.1f".format(sizingResult.systemEfficiencyPercent)}%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Divider(color = Color.White.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(t("estimated_pkr"), fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("PKR ${"%,d".format(sizingResult.finalQuotationPrice.toInt())}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(t("wire_gauge"), fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(sizingResult.wireRecommendationDetail, fontSize = 11.sp, color = SolarGold)
                        }
                    }
                }
            }
        }

        // COMPARTIVE DATA BREAKDOWN REPORT (IF COMPARE TOGLE ACTIVE)
        if (isCompareSuiteEnabled) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = borderStrike(SolarGold.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (isUrdu) "موازنہ شیٹ" else "Sizing Comparison Assessment",
                            fontWeight = FontWeight.Bold,
                            color = SolarGold,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Left (Main) setup values vs Right (Compared) setup values
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(t("current_setup"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OrganicGreen)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Motor: ${viewModel.motorHp.toInt()} HP", fontSize = 12.sp)
                                Text("Bore: ${viewModel.boreDepth.toInt()} Ft", fontSize = 12.sp)
                                Text("Solar: ${"%.2f".format(sizingResult.solarCapacityKw)} kW", fontSize = 12.sp)
                                Text("Amount: PKR ${"%,d".format(sizingResult.finalQuotationPrice.toInt())}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Divider(modifier = Modifier
                                .width(1.dp)
                                .height(80.dp)
                                .padding(horizontal = 8.dp), color = Color.LightGray)

                            // Formulate secondary comparisons directly matching pricing parameters
                            val compBaseKw = (compareMotorHp * 0.746 * 1.38)
                            val compPanels = kotlin.math.ceil((compBaseKw * 1000) / viewModel.panelWattSelected).toInt()
                            val compSolarKw = (compPanels * viewModel.panelWattSelected) / 1000.0
                            val compPanelPrice = when (viewModel.panelWattSelected) {
                                550 -> viewModel.pricePanel550w
                                580 -> viewModel.pricePanel580w
                                else -> viewModel.pricePanel600w
                            }
                            val compPanelCost = compPanels * compPanelPrice
                            val compInverterCost = (compareMotorHp * 1.5) * viewModel.priceInverterPerHp
                            val compStructCost = compPanels * viewModel.priceStructurePerPanel
                            val compWirePricePerMeter = if (viewModel.wireType == "Copper") viewModel.priceWireCopperPerM else viewModel.priceWireSilverPerM
                            val compTotalCost = compPanelCost + compInverterCost + compStructCost + (25.0 * compWirePricePerMeter) + viewModel.priceInstallationBase
                            val compFinalQuotation = compTotalCost + (compTotalCost * (viewModel.profitMargin / 100.0))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(t("compare_setup"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SolarGold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Motor: ${compareMotorHp.toInt()} HP", fontSize = 12.sp)
                                Text("Bore: ${compareBoreDepth.toInt()} Ft", fontSize = 12.sp)
                                Text("Solar: ${"%.2f".format(compSolarKw)} kW", fontSize = 12.sp)
                                Text("Amount: PKR ${"%,d".format(compFinalQuotation.toInt())}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // BOTTOM ACTION TRIGGERS
        item {
            Button(
                onClick = { projectSaveDialogVisible = true },
                colors = ButtonDefaults.buttonColors(containerColor = SoftLeafGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("save_estimate_btn")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Save, contentDescription = "Save")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(t("save_to_history"), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            Button(
                onClick = {
                    val message = viewModel.generateWhatsAppText()
                    val encoded = Uri.encode(message)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=923279791100&text=$encoded"))
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = OrganicGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("whatsapp_share_calculator_btn")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Share, contentDescription = "whatsapp", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(t("inquiry_whatsapp"), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        item {
            Button(
                onClick = { onNavigateToBom() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = borderStrike(OrganicGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ListAlt, contentDescription = "bom", tint = OrganicGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isUrdu) "ڈیٹیل بل آف میٹریل کوٹیشن دیکھیں" else "View Granular Cost Breakdown List", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OrganicGreen)
                }
            }
        }
    }

    // Modal dialogue to save calculation parameters
    if (projectSaveDialogVisible) {
        AlertDialog(
            onDismissRequest = { projectSaveDialogVisible = false },
            title = { Text(if (isUrdu) "پروجیکٹ محفوظ کریں" else "Save Design Estimation", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(if (isUrdu) "سولر ٹیوب ویل کا نام درج کریں جس سے ہسٹری میں پہچان ہو سکے:" else "Give a name to reference in your dashboard history list:", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = inputProjectName,
                        onValueChange = { inputProjectName = it },
                        placeholder = { Text("e.g. Chaudhary Solar 10HP") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("project_name_save_field")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveProject(inputProjectName) {
                            projectSaveDialogVisible = false
                            inputProjectName = ""
                            Toast.makeText(context, if (isUrdu) "کامیابی سے محفوظ ہو گیا!" else "Solar design estimation backed up!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OrganicGreen)
                ) {
                    Text(if (isUrdu) "محفوظ کریں" else "Backup Project")
                }
            },
            dismissButton = {
                TextButton(onClick = { projectSaveDialogVisible = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


// ==========================================
// 3. PUMP SELECTOR & IRRIGATION SCIENCE SCREEN
// ==========================================
@Composable
fun PumpAndWaterScreen(
    viewModel: SolarViewModel,
    pumpsList: List<CustomPump>
) {
    val isUrdu = viewModel.isUrdu
    fun t(key: String): String = SolarTranslations.t(key, isUrdu)
    val waterResult = viewModel.calculateWaterRuntime()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // WATER REQUIREMENTS SECTION
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = t("crop_calculator"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OrganicGreen
                    )
                    Text(
                        text = if (isUrdu) "فصلوں کے رقبے اور ضرورت کے مطابق پانی کا حساب کریں" else "Calculate hourly runtimes matching crop water volume limits.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Crop Select Segment
                    Text(t("select_crop"), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("Wheat", "Rice", "Sugarcane", "Vegetables", "Orchards").forEach { cr ->
                            val isSel = viewModel.cropType == cr
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) SolarGold else Color.LightGray.copy(alpha = 0.2f))
                                    .clickable { viewModel.cropType = cr }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isUrdu && cr == "Wheat") "گندم"
                                    else if (isUrdu && cr == "Rice") "چاول"
                                    else if (isUrdu && cr == "Sugarcane") "کماد"
                                    else if (isUrdu && cr == "Vegetables") "سبزی"
                                    else if (isUrdu && cr == "Orchards") "باغ"
                                    else cr,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.White else Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Acres Input Slider
                    Text(
                        text = "${t("target_acres")} ${viewModel.cropAcres.toInt()} Acres",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Slider(
                        value = viewModel.cropAcres.toFloat(),
                        onValueChange = { viewModel.cropAcres = it.toInt().toDouble() },
                        valueRange = 1f..100f,
                        colors = SliderDefaults.colors(thumbColor = SoftLeafGreen, activeTrackColor = SoftLeafGreen)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Detailed math projections
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardLeafTint),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(t("water_needed"), fontSize = 11.sp, color = Color.Gray)
                                    Text("${"%,d".format(waterResult.totalWaterLiters.toInt())} Liters", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OrganicGreen)
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(if (isUrdu) "تخمینی اخراج رفتار" else "Est. Flow Rate", fontSize = 11.sp, color = Color.Gray)
                                    Text("${waterResult.flowRateLpm.toInt()} LPM / GPM", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                                }
                            }

                            Divider(color = Color.LightGray, modifier = Modifier.padding(vertical = 10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(t("est_runtime"), fontSize = 11.sp, color = Color.Gray)
                                    Text("${"%.1f".format(waterResult.runtimeHoursTotal)} Hours", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OrganicGreen)
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(t("days_needed"), fontSize = 11.sp, color = Color.Gray)
                                    Text("${waterResult.daysNeeded} Sunny Days", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SolarGold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // PUMP SELECTION MATRIX SECTION
        item {
            Text(
                text = t("pump_specs"),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = OrganicGreen,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(pumpsList) { pump ->
            PumpSpecificationCard(pump = pump, currentVmHp = viewModel.motorHp, isUrdu = isUrdu)
        }
    }
}

@Composable
fun PumpSpecificationCard(pump: CustomPump, currentVmHp: Double, isUrdu: Boolean) {
    // Math helpers: HP to Watt, Amps calculations based on standard local parameters
    val powerWatts = pump.hp * 746.0
    val powerKw = powerWatts / 1000.0

    // Apparent Ampere draw (3-phase 380V vs 1-phase 220V)
    // Formula: I = Watts / (V * sqrt(3) * CosPhi) for 3-phase, I = Watts / (V * CosPhi) for 1-phase
    val voltFactor = if (pump.voltage == 380) 380.0 * 1.73 * 0.82 else 220.0 * 0.85
    val currentAmpere = powerWatts / voltFactor

    val isRecommended = currentVmHp.toInt() == pump.hp.toInt()

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = borderStrike(if (isRecommended) SolarGold else Color.LightGray.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Water,
                        contentDescription = "pump",
                        tint = if (isRecommended) SolarGold else SoftLeafGreen
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(pump.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(pump.type, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Light)
                    }
                }

                if (isRecommended) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SolarGold.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(if (isUrdu) "بہترین میچ" else "Best Match", color = SolarGold, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                }
            }

            Divider(color = Color.LightGray.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(if (isUrdu) "ہارس پاور پاور" else "Capacity Power", fontSize = 10.sp, color = Color.Gray)
                    Text("${pump.hp} HP / ${"%.2f".format(powerKw)} kW", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Black)
                }

                Column {
                    Text(if (isUrdu) "وولٹیج کنکشن" else "Voltage Loop", fontSize = 10.sp, color = Color.Gray)
                    Text("${pump.voltage}V (3-Phase)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = DarkSkyBlue)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(if (isUrdu) "کرنٹ ایمپیئر" else "Amperage Draw", fontSize = 10.sp, color = Color.Gray)
                    Text("${"%.1f".format(currentAmpere)} Amp", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = OrganicGreen)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(if (isUrdu) "میکسمم لفٹ ہیڈ" else "Max Head Lift", fontSize = 10.sp, color = Color.Gray)
                    Text("${pump.headFeet.toInt()} ft depth", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(if (isUrdu) "پانی کا ڈسچارج" else "Water Discharge", fontSize = 10.sp, color = Color.Gray)
                    Text("${pump.dischargeLpm.toInt()} LPM Flow rate", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                }
            }
        }
    }
}


// ==========================================
// 4. GRANULAR COST ESTIMATOR & PDF/BOM TRANS
// ==========================================
@Composable
fun BOMQuoteScreen(viewModel: SolarViewModel) {
    val isUrdu = viewModel.isUrdu
    fun t(key: String): String = SolarTranslations.t(key, isUrdu)
    val context = LocalContext.current
    val sizing = viewModel.calculateSizing()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Budget Margin Tuning Tool
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrendingUp, contentDescription = "Profit tuning", tint = SolarGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(t("installer_commission"), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OrganicGreen)
                    }

                    Text(
                        text = if (isUrdu) "انسٹالر کا سروس مارجن یا کمیشن ایڈجسٹ کریں" else "Adjust system integrator markup commission.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${viewModel.profitMargin}% Markup Margin",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Slider(
                        value = viewModel.profitMargin.toFloat(),
                        onValueChange = { viewModel.profitMargin = it.toInt() },
                        valueRange = 0f..30f,
                        steps = 30,
                        colors = SliderDefaults.colors(thumbColor = SolarGold, activeTrackColor = SolarGold),
                        modifier = Modifier.testTag("profit_margin_slider")
                    )
                }
            }
        }

        // DIGITAL RECEIPT INVOICE BODY
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FBF9)),
                shape = RoundedCornerShape(16.dp),
                border = borderStrike(OrganicGreen.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Receipt Header
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("KISAAN SOLAR SERVICES", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = OrganicGreen)
                        Text(t("invoice_preview").uppercase(), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SolarGold)
                        Text("Helpline: 03279791100 | Pakistan", fontSize = 10.sp, color = Color.Gray)
                        Text("Date: May 2026", fontSize = 10.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("SYSTEM DESIGN: ${viewModel.motorHp.toInt()} HP Motor / Bore: ${viewModel.boreDepth.toInt()} ft", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = Color.LightGray)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Raw Bill Materials rows
                    val rowItems = listOf(
                        Pair(t("panels_count_lbl") + " (${sizing.panelsCount} x ${viewModel.panelWattSelected}W)", sizing.panelsCost),
                        Pair("3-Phase Variable Frequency Drive (VFD)", sizing.inverterCost),
                        Pair("Mounting Structure, Rails & Base Stands", sizing.structureCost),
                        Pair("Heavy Cable wiring (${viewModel.wireType})", sizing.wireCost),
                        Pair("Grounding, Site Works & Installation Fee", sizing.installationCost)
                    )

                    rowItems.forEach { (item, cost) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item, fontSize = 11.sp, color = Color.DarkGray, modifier = Modifier.weight(0.7f))
                            Text("PKR ${"%,d".format(cost.toInt())}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.Black, modifier = Modifier.weight(0.3f), textAlign = TextAlign.End)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("System Subtotal Raw Cost:", fontSize = 11.sp, color = Color.Gray)
                        Text("PKR ${"%,d".format(sizing.rawTotalCost.toInt())}", fontSize = 11.sp, color = Color.Gray)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Integrator Markup Margin (${viewModel.profitMargin}%):", fontSize = 11.sp, color = Color.Gray)
                        Text("PKR ${"%,d".format(sizing.commissionAmount.toInt())}", fontSize = 11.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = OrganicGreen, thickness = 2.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("FINAL QUOTED BUDGET:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = OrganicGreen)
                        Text("PKR ${"%,d".format(sizing.finalQuotationPrice.toInt())}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = OrganicGreen)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Notice text
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Yellow.copy(alpha = 0.15f))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = if (isUrdu)
                                "* نوٹ: مٹیریل اور پینل ریٹس روزانہ کی بنیاد پر پاکستان مارکیٹ کے مطابق تبدیل ہو سکتے ہیں۔"
                            else
                                "* Note: Dynamic component market and solar rates fluctuate. Print quotation to lock parameters.",
                            fontSize = 9.sp,
                            color = Color(0xFF5D4037),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // WhatsApp share action
        item {
            Button(
                onClick = {
                    val message = viewModel.generateWhatsAppText()
                    val encoded = Uri.encode(message)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=923279791100&text=$encoded"))
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = OrganicGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("submit_inquiry_bill_btn")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Share, contentDescription = "whatsapp")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(t("share_bom"), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Live Printing simulated button triggering quick feedback
        item {
            Button(
                onClick = {
                    Toast.makeText(context, if (isUrdu) "کیسان سولر رپورٹ پی ڈی ایف ڈاؤن لوڈ ہو رہی ہے..." else "Downloading fully qualified PDF Quotation...", Toast.LENGTH_LONG).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = "pdf", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isUrdu) "پی ڈی ایف کوٹیشن ڈاؤن لوڈ کریں" else "Download Formal PDF Report", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}


// ==========================================
// 5. SETTINGS, TECHNICAL GUIDE & ADMIN PANEL
// ==========================================
@Composable
fun GuideAndAdminScreen(viewModel: SolarViewModel, customPumps: List<CustomPump>) {
    val isUrdu = viewModel.isUrdu
    fun t(key: String): String = SolarTranslations.t(key, isUrdu)
    val context = LocalContext.current

    var selectedGuideCompany by remember { mutableStateOf("INVT") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // CONTACT SYSTEM HELPLINES
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isUrdu) "کیسان سولر رابطہ ہیلپ لائن" else "Direct Hotlines & Customer Service",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OrganicGreen
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=923279791100&text=Salam%20Kisaan%20Solar%20Service%20Inquiry"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SoftLeafGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("hotline_whatsapp_btn")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = "whatsapp", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(t("inquiry_whatsapp") + " (03279791100)", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:03279791100"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OrganicGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("hotline_call_btn")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = "call", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(t("call_support"), fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // 3-PHASE VFD INVERTER MANUAL SPEC TUNING DETAILS
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoFixHigh, contentDescription = "vfd tuning", tint = SolarGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(t("inverter_spec_guide"), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OrganicGreen)
                    }

                    Text(
                        text = if (isUrdu) "تھری فیز انورٹر سیٹنگ کوڈز اور رہنمائی گائیڈ" else "Select driver company to view terminal keypad parameters:",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Company toggle selector Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("INVT", "INVENT", "Sky Power").forEach { comp ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedGuideCompany == comp) OrganicGreen else Color.LightGray.copy(alpha = 0.2f))
                                    .clickable { selectedGuideCompany = comp }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    comp,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedGuideCompany == comp) Color.White else Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Detail text blocks based on selection
                    val tuningText = when (selectedGuideCompany) {
                        "INVT" -> listOf(
                            "⚙️ Parameter P00.03: Set to 1 (Enables Terminal Control for pressure float switches).",
                            "⚙️ Parameter P00.18: Set to 30.00s (Inductive ramp-up deceleration time protection).",
                            "⚙️ Parameter P02.01: Motor Power setting (Represented in kW = HP * 0.746). Must match nameplate exactly.",
                            "⚙️ Parameter P11.02: Underload Dry-Run protection. Stops VFD when bore water drops to guard pump.",
                            "⚠️ Wiring: Use 4-Core copper double insulated 6mm flat subcable run to bore."
                        )
                        "INVENT" -> listOf(
                            "⚙️ Parameter F0.02: Set to 1 (External VFD Run command toggled).",
                            "⚙️ Parameter F2.01: Set to rated flow frequency limit, recommended 50.00 Hz.",
                            "⚙️ Parameter F9.07: Overcurrent thermal trip delay. Shift limit factor to 1.15 to avoid false starts.",
                            "⚙️ Parameter FC.12: Automatic sunrise launch frequency threshold. Recommended value: 25Hz."
                        )
                        else -> listOf(
                            "⚙️ Parameter D-01: Frequency upper rating floor, limit tightly at 50Hz to secure motor windings.",
                            "⚙️ Parameter D-08: Voltage boost curve adjustment. Increase by 2% during weak early morning sun.",
                            "⚙️ Parameter E-22: Deep dry runoff stop delay. Recommended 15s delay to let bore pool recharge."
                        )
                    }

                    tuningText.forEach { rule ->
                        Text(
                            text = rule,
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // DYNAMIC ADMIN PRICING EDITOR
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Settings, contentDescription = "admin", tint = SolarGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(t("admin_config"), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OrganicGreen)
                    }

                    Text(
                        text = if (isUrdu) "مارکیٹ مارجن قیمتیں اپڈیٹ کریں" else "Fine-tune base procurement values in PKR manually:",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Granular editor fields mapped directly to VM vars
                    EditablePriceRow(t("panels_count_lbl") + " (550W)", viewModel.pricePanel550w) { viewModel.pricePanel550w = it }
                    EditablePriceRow(t("panels_count_lbl") + " (580W)", viewModel.pricePanel580w) { viewModel.pricePanel580w = it }
                    EditablePriceRow(t("panels_count_lbl") + " (600W)", viewModel.pricePanel600w) { viewModel.pricePanel600w = it }
                    EditablePriceRow("Inverter Base PKG (per HP)", viewModel.priceInverterPerHp) { viewModel.priceInverterPerHp = it }
                    EditablePriceRow("Structure Stand Frame (per panel)", viewModel.priceStructurePerPanel) { viewModel.priceStructurePerPanel = it }
                    EditablePriceRow("Flat base labor installation", viewModel.priceInstallationBase) { viewModel.priceInstallationBase = it }
                    EditablePriceRow("Copper Wire base (per Meter)", viewModel.priceWireCopperPerM) { viewModel.priceWireCopperPerM = it }
                    EditablePriceRow("Silver Wire base (per Meter)", viewModel.priceWireSilverPerM) { viewModel.priceWireSilverPerM = it }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            viewModel.updatePriceSettings(
                                viewModel.pricePanel550w, viewModel.pricePanel580w, viewModel.pricePanel600w,
                                viewModel.priceInverterPerHp, viewModel.priceStructurePerPanel, viewModel.priceInstallationBase,
                                viewModel.priceWireCopperPerM, viewModel.priceWireSilverPerM
                            )
                            Toast.makeText(context, if (isUrdu) "قیمتیں اپڈیٹ ہو گئیں!" else "Procurement values locked!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OrganicGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_save_prices_btn")
                    ) {
                        Text(if (isUrdu) "ریٹ لسٹ لاگو کریں" else "Apply Dynamic Rate Updates")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(
                        onClick = {
                            viewModel.resetPricesToDefault()
                            Toast.makeText(context, "System rates reset!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.RestartAlt, contentDescription = "reset", tint = Color.Red)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(t("restore_defaults"), color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // ADD CUSTOM PUMP FORM
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(t("add_custom_pump"), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OrganicGreen)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = viewModel.addPumpName,
                        onValueChange = { viewModel.addPumpName = it },
                        label = { Text("Model Name (e.g., KSB Sub-15)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_pump_name_inp"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Horsepower input field
                    OutlinedTextField(
                        value = viewModel.addPumpHp.toString(),
                        onValueChange = { viewModel.addPumpHp = it.toDoubleOrNull() ?: 1.0 },
                        label = { Text("Motor Capacity (HP)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_pump_hp_inp"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = viewModel.addPumpDischargeLpm.toString(),
                            onValueChange = { viewModel.addPumpDischargeLpm = it.toDoubleOrNull() ?: 200.0 },
                            label = { Text("Flow (Lpm)", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = viewModel.addPumpHeadFeet.toString(),
                            onValueChange = { viewModel.addPumpHeadFeet = it.toDoubleOrNull() ?: 100.0 },
                            label = { Text("Max Head Lift (ft)", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.addNewPump()
                            Toast.makeText(context, "New pump registered!", Toast.LENGTH_SHORT).show()
                        },
                        enabled = viewModel.addPumpName.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = SoftLeafGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_add_pump_confirm_btn")
                    ) {
                        Text("Register Pump to Inventory")
                    }
                }
            }
        }
    }
}

@Composable
fun EditablePriceRow(label: String, value: Double, onValueUpdate: (Double) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 12.sp, modifier = Modifier.weight(0.6f))
        var textVar by remember(value) { mutableStateOf(value.toInt().toString()) }

        OutlinedTextField(
            value = textVar,
            onValueChange = {
                textVar = it
                it.toDoubleOrNull()?.let { dVal -> onValueUpdate(dVal) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .width(110.dp)
                .height(44.dp)
                .testTag("admin_price_field_${label.replace(" ", "_")}"),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 11.sp),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

// Border Helper
fun borderStrike(color: Color) = androidx.compose.foundation.BorderStroke(1.dp, color)
