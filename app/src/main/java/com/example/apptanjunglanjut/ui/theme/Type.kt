package com.example.apptanjunglanjut.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.apptanjunglanjut.R

// 1. Definisi FontFamily Poppins (Ini sudah benar dari langkah sebelumnya)
val poppinsFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

// 2. Terapkan 'fontFamily = poppinsFamily' ke SETIAP style
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = poppinsFamily, // <-- TAMBAHKAN INI
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = poppinsFamily, // <-- TAMBAHKAN INI
        fontWeight = FontWeight.SemiBold, // Anda bisa sesuaikan weight
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = poppinsFamily, // <-- TAMBAHKAN INI
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    // Lakukan hal yang sama untuk SEMUA style lain di bawah ini:
    headlineSmall = TextStyle(
        fontFamily = poppinsFamily, // <-- TAMBAHKAN INI
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = poppinsFamily, // <-- TAMBAHKAN INI
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodySmall = TextStyle(
        fontFamily = poppinsFamily, // <-- TAMBAHKAN INI
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelSmall = TextStyle(
        fontFamily = poppinsFamily, // <-- TAMBAHKAN INI
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    /* ...dan seterusnya untuk semua style yang didefinisikan di file Type.kt Anda... */
)