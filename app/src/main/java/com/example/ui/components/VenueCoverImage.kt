package com.example.ui.components

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Deck
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Copies a photo picked via Android Photo Picker into app internal storage
 * so it remains accessible across app restarts without storage permissions.
 */
suspend fun copyPickedImageToInternalStorage(context: Context, uri: Uri): String? =
    withContext(Dispatchers.IO) {
        try {
            val coversDir = File(context.filesDir, "venue_covers").apply { mkdirs() }
            val destFile = File(coversDir, "cover_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            Uri.fromFile(destFile).toString()
        } catch (_: Exception) {
            uri.toString()
        }
    }

/**
 * Displays the single cover picture for an event space:
 * - Real picked image URI via Coil
 * - Custom architectural sample artwork for pre-seeded spaces
 * - Clean venue-type placeholder if no image is selected yet
 */
@Composable
fun VenueCoverImage(
    coverImageUri: String?,
    spaceType: String,
    spaceName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        when {
            coverImageUri == "sample://ballroom" -> {
                BallroomSampleCover(modifier = Modifier.fillMaxSize())
            }
            coverImageUri == "sample://loft" -> {
                LoftSampleCover(modifier = Modifier.fillMaxSize())
            }
            !coverImageUri.isNullOrBlank() -> {
                AsyncImage(
                    model = coverImageUri,
                    contentDescription = "Cover photo for $spaceName",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                DefaultVenuePlaceholder(
                    spaceType = spaceType,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun BallroomSampleCover(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(
            Brush.linearGradient(
                colors = listOf(Color(0xFF1E293B), Color(0xFF312E81), Color(0xFF78350F))
            )
        )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Warm stage floor glow
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color(0x66F59E0B)),
                    startY = h * 0.5f,
                    endY = h
                ),
                topLeft = Offset(0f, h * 0.5f),
                size = Size(w, h * 0.5f)
            )

            // Architectural arches
            val archCount = 3
            val archWidth = w / (archCount + 0.8f)
            val gap = (w - archCount * archWidth) / (archCount + 1)
            for (i in 0 until archCount) {
                val left = gap + i * (archWidth + gap)
                drawRoundRect(
                    color = Color(0x33FDE68A),
                    topLeft = Offset(left, h * 0.2f),
                    size = Size(archWidth, h * 0.65f),
                    cornerRadius = CornerRadius(archWidth / 2f, archWidth / 2f)
                )
            }

            // Chandelier circles
            val centers = listOf(w * 0.25f, w * 0.5f, w * 0.75f)
            centers.forEach { cx ->
                drawLine(
                    color = Color(0x99FCD34D),
                    start = Offset(cx, 0f),
                    end = Offset(cx, h * 0.24f),
                    strokeWidth = 3f
                )
                drawCircle(
                    color = Color(0xCCFDE68A),
                    radius = 16f,
                    center = Offset(cx, h * 0.28f)
                )
                drawCircle(
                    color = Color(0x44FDE68A),
                    radius = 34f,
                    center = Offset(cx, h * 0.28f)
                )
            }
        }

        Surface(
            color = Color.Black.copy(alpha = 0.45f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {
            Text(
                text = "Grand Ballroom Preview",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFFEF3C7),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun LoftSampleCover(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(
            Brush.linearGradient(
                colors = listOf(Color(0xFF0F766E), Color(0xFF1E3A8A), Color(0xFF334155))
            )
        )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Sunbeam diagonal highlight
            val beam = Path().apply {
                moveTo(w * 0.15f, 0f)
                lineTo(w * 0.55f, 0f)
                lineTo(w * 0.85f, h)
                lineTo(w * 0.35f, h)
                close()
            }
            drawPath(beam, color = Color(0x28E0F2FE))

            // Industrial loft window grid on right side
            val gridLeft = w * 0.52f
            val gridTop = h * 0.14f
            val gridW = w * 0.38f
            val gridH = h * 0.68f
            drawRoundRect(
                color = Color(0x33FFFFFF),
                topLeft = Offset(gridLeft, gridTop),
                size = Size(gridW, gridH),
                cornerRadius = CornerRadius(12f, 12f)
            )
            // Mullions
            drawLine(
                color = Color(0x55FFFFFF),
                start = Offset(gridLeft + gridW / 2f, gridTop),
                end = Offset(gridLeft + gridW / 2f, gridTop + gridH),
                strokeWidth = 3f
            )
            drawLine(
                color = Color(0x55FFFFFF),
                start = Offset(gridLeft, gridTop + gridH / 2f),
                end = Offset(gridLeft + gridW, gridTop + gridH / 2f),
                strokeWidth = 3f
            )
        }

        Surface(
            color = Color.Black.copy(alpha = 0.45f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {
            Text(
                text = "Creative Loft Preview",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFE0F2FE),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun DefaultVenuePlaceholder(
    spaceType: String,
    modifier: Modifier = Modifier
) {
    val icon = when (spaceType) {
        "Outdoor" -> Icons.Default.Deck
        "Hybrid" -> Icons.Default.Apartment
        else -> Icons.Default.MeetingRoom
    }
    Box(
        modifier = modifier.background(
            Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                )
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "$spaceType Event Space",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
