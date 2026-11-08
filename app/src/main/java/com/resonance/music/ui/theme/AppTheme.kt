package com.resonance.music.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * All available themes in Resonance.
 * Each theme has a display name, a preview color, and dark/light color schemes.
 */
enum class AppTheme(
    val displayName: String,
    val description: String,
    val previewColor: Color
) {
    NEON_PULSE(
        displayName = "Neon Pulse",
        description = "青蓝点缀",
        previewColor = NeonCyan
    ),
    AURORA(
        displayName = "Aurora",
        description = "极光绿",
        previewColor = AuroraGreen
    ),
    MIDNIGHT_OCEAN(
        displayName = "Midnight Ocean",
        description = "深海蓝",
        previewColor = OceanBlue
    ),
    SOLAR_FLARE(
        displayName = "Solar Flare",
        description = "暖橙",
        previewColor = SolarAmber
    ),
    VOID(
        displayName = "Void",
        description = "极简灰",
        previewColor = VoidWhite
    ),
    SAKURA_BLOOM(
        displayName = "Sakura Bloom",
        description = "樱花粉",
        previewColor = SakuraPink
    ),
    EMERALD_CITY(
        displayName = "Emerald City",
        description = "翡翠绿",
        previewColor = EmeraldGreen
    ),
    CYBER_VIOLET(
        displayName = "Cyber Violet",
        description = "电子紫",
        previewColor = CyberViolet
    ),
    ARCTIC_ICE(
        displayName = "Arctic Ice",
        description = "冰蓝",
        previewColor = ArcticBlue
    ),
    MATERIAL_YOU(
        displayName = "Material You",
        description = "跟随壁纸",
        previewColor = Color(0xFF6750A4)
    );

    fun darkColorScheme(): ColorScheme = when (this) {
        NEON_PULSE -> darkColorScheme(
            primary = NeonCyan,
            onPrimary = Color.Black,
            primaryContainer = NeonCyanDim,
            onPrimaryContainer = Color.White,
            secondary = NeonMagenta,
            onSecondary = Color.Black,
            secondaryContainer = NeonMagentaDim,
            onSecondaryContainer = Color.White,
            tertiary = NeonMagenta,
            surface = NeonSurface,
            surfaceVariant = NeonSurfaceVariant,
            onSurface = NeonOnSurface,
            onSurfaceVariant = NeonOnSurfaceVariant,
            background = NeonBackground,
            onBackground = NeonOnSurface,
            error = NeonError,
            outline = NeonOnSurfaceVariant,
            outlineVariant = Color(0xFF2A3550)
        )

        AURORA -> darkColorScheme(
            primary = AuroraGreen,
            onPrimary = Color.Black,
            primaryContainer = AuroraGreenDim,
            onPrimaryContainer = Color.White,
            secondary = AuroraPurple,
            onSecondary = Color.Black,
            secondaryContainer = AuroraPurpleDim,
            onSecondaryContainer = Color.White,
            tertiary = AuroraPurple,
            surface = AuroraSurface,
            surfaceVariant = AuroraSurfaceVariant,
            onSurface = AuroraOnSurface,
            onSurfaceVariant = Color(0xFF7AA090),
            background = AuroraBackground,
            onBackground = AuroraOnSurface,
            error = Color(0xFFFF5252),
            outline = Color(0xFF4A6058),
            outlineVariant = Color(0xFF1E3028)
        )

        MIDNIGHT_OCEAN -> darkColorScheme(
            primary = OceanBlue,
            onPrimary = Color.White,
            primaryContainer = OceanBlueDim,
            onPrimaryContainer = Color.White,
            secondary = OceanTeal,
            onSecondary = Color.Black,
            secondaryContainer = OceanTealDim,
            onSecondaryContainer = Color.Black,
            tertiary = OceanTeal,
            surface = OceanSurface,
            surfaceVariant = OceanSurfaceVariant,
            onSurface = OceanOnSurface,
            onSurfaceVariant = Color(0xFF7090A8),
            background = OceanBackground,
            onBackground = OceanOnSurface,
            error = Color(0xFFFF5252),
            outline = Color(0xFF3A5068),
            outlineVariant = Color(0xFF162838)
        )

        SOLAR_FLARE -> darkColorScheme(
            primary = SolarAmber,
            onPrimary = Color.Black,
            primaryContainer = SolarAmberDim,
            onPrimaryContainer = Color.Black,
            secondary = SolarOrange,
            onSecondary = Color.Black,
            secondaryContainer = SolarOrangeDim,
            onSecondaryContainer = Color.White,
            tertiary = SolarOrange,
            surface = SolarSurface,
            surfaceVariant = SolarSurfaceVariant,
            onSurface = SolarOnSurface,
            onSurfaceVariant = Color(0xFFA89070),
            background = SolarBackground,
            onBackground = SolarOnSurface,
            error = Color(0xFFFF5252),
            outline = Color(0xFF685040),
            outlineVariant = Color(0xFF382818)
        )

        VOID -> darkColorScheme(
            primary = VoidWhite,
            onPrimary = Color.Black,
            primaryContainer = VoidGrey,
            onPrimaryContainer = Color.White,
            secondary = VoidAccent,
            onSecondary = Color.Black,
            secondaryContainer = Color(0xFF444444),
            onSecondaryContainer = Color.White,
            tertiary = VoidGrey,
            surface = VoidSurface,
            surfaceVariant = VoidSurfaceVariant,
            onSurface = VoidOnSurface,
            onSurfaceVariant = Color(0xFF808080),
            background = VoidBackground,
            onBackground = VoidOnSurface,
            error = Color(0xFFFF4444),
            outline = Color(0xFF404040),
            outlineVariant = Color(0xFF252525)
        )

        SAKURA_BLOOM -> darkColorScheme(
            primary = SakuraPink,
            onPrimary = Color.Black,
            primaryContainer = SakuraPinkDim,
            onPrimaryContainer = Color.White,
            secondary = SakuraRose,
            onSecondary = Color.Black,
            secondaryContainer = SakuraRoseDim,
            onSecondaryContainer = Color.White,
            tertiary = SakuraRose,
            surface = SakuraSurface,
            surfaceVariant = SakuraSurfaceVariant,
            onSurface = SakuraOnSurface,
            onSurfaceVariant = Color(0xFFA08090),
            background = SakuraBackground,
            onBackground = SakuraOnSurface,
            error = Color(0xFFFF5252),
            outline = Color(0xFF604050),
            outlineVariant = Color(0xFF302028)
        )

        EMERALD_CITY -> darkColorScheme(
            primary = EmeraldGreen,
            onPrimary = Color.Black,
            primaryContainer = EmeraldGreenDim,
            onPrimaryContainer = Color.White,
            secondary = EmeraldGold,
            onSecondary = Color.Black,
            secondaryContainer = EmeraldGoldDim,
            onSecondaryContainer = Color.Black,
            tertiary = EmeraldGold,
            surface = EmeraldSurface,
            surfaceVariant = EmeraldSurfaceVariant,
            onSurface = EmeraldOnSurface,
            onSurfaceVariant = Color(0xFF80A890),
            background = EmeraldBackground,
            onBackground = EmeraldOnSurface,
            error = Color(0xFFFF5252),
            outline = Color(0xFF406850),
            outlineVariant = Color(0xFF1A3020)
        )

        CYBER_VIOLET -> darkColorScheme(
            primary = CyberViolet,
            onPrimary = Color.Black,
            primaryContainer = CyberVioletDim,
            onPrimaryContainer = Color.White,
            secondary = CyberIndigo,
            onSecondary = Color.White,
            secondaryContainer = CyberIndigoDim,
            onSecondaryContainer = Color.White,
            tertiary = CyberIndigo,
            surface = CyberSurface,
            surfaceVariant = CyberSurfaceVariant,
            onSurface = CyberOnSurface,
            onSurfaceVariant = Color(0xFF9080B0),
            background = CyberBackground,
            onBackground = CyberOnSurface,
            error = Color(0xFFFF5252),
            outline = Color(0xFF504068),
            outlineVariant = Color(0xFF201838)
        )

        ARCTIC_ICE -> darkColorScheme(
            primary = ArcticBlue,
            onPrimary = Color.Black,
            primaryContainer = ArcticBlueDim,
            onPrimaryContainer = Color.Black,
            secondary = ArcticWhite,
            onSecondary = Color.Black,
            secondaryContainer = ArcticWhiteDim,
            onSecondaryContainer = Color.Black,
            tertiary = ArcticWhite,
            surface = ArcticSurface,
            surfaceVariant = ArcticSurfaceVariant,
            onSurface = ArcticOnSurface,
            onSurfaceVariant = Color(0xFF80A8B8),
            background = ArcticBackground,
            onBackground = ArcticOnSurface,
            error = Color(0xFFFF5252),
            outline = Color(0xFF385868),
            outlineVariant = Color(0xFF142830)
        )

        MATERIAL_YOU -> darkColorScheme() // Placeholder — replaced at runtime
    }
    /**
     * Light (white) schemes. The app is white-first: every theme is a white
     * surface with one accent colour, so the UI reads the same in sunlight and
     * the album art carries the visual weight instead of a dark chrome.
     */
    fun lightColorScheme(): ColorScheme {
        val accent = when (this) {
            NEON_PULSE -> Color(0xFF00838F)
            AURORA -> Color(0xFF00875A)
            MIDNIGHT_OCEAN -> Color(0xFF2962FF)
            SOLAR_FLARE -> Color(0xFFB26500)
            VOID -> Color(0xFF333333)
            SAKURA_BLOOM -> Color(0xFFD81B60)
            EMERALD_CITY -> Color(0xFF2E7D32)
            CYBER_VIOLET -> Color(0xFF5E35B1)
            ARCTIC_ICE -> Color(0xFF0277BD)
            MATERIAL_YOU -> Color(0xFF6750A4)
        }
        val secondary = when (this) {
            NEON_PULSE -> Color(0xFFC2185B)
            AURORA -> Color(0xFF7B4FBF)
            MIDNIGHT_OCEAN -> Color(0xFF00897B)
            SOLAR_FLARE -> Color(0xFFC43E00)
            VOID -> Color(0xFF666666)
            SAKURA_BLOOM -> Color(0xFFB03060)
            EMERALD_CITY -> Color(0xFFA67C00)
            CYBER_VIOLET -> Color(0xFF4527A0)
            ARCTIC_ICE -> Color(0xFF455A64)
            MATERIAL_YOU -> Color(0xFF7D5260)
        }
        return lightColorScheme(
            primary = accent,
            onPrimary = Color.White,
            primaryContainer = accent.copy(alpha = 0.14f),
            onPrimaryContainer = Color(0xFF14161A),
            secondary = secondary,
            onSecondary = Color.White,
            secondaryContainer = secondary.copy(alpha = 0.12f),
            onSecondaryContainer = Color(0xFF14161A),
            tertiary = secondary,
            background = Color(0xFFFFFFFF),
            onBackground = Color(0xFF14161A),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF14161A),
            surfaceVariant = Color(0xFFF2F3F5),
            onSurfaceVariant = Color(0xFF5F6368),
            outline = Color(0xFFDADCE0),
            outlineVariant = Color(0xFFEDEFF2),
            error = Color(0xFFD32F2F),
            onError = Color.White
        )
    }
}
