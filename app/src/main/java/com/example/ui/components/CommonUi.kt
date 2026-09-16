package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BookingStatus
import com.example.model.DriverVerificationStatus
import com.example.ui.theme.*

@Composable
fun LoadGoBrandLogo(
    modifier: Modifier = Modifier,
    isLight: Boolean = false,
    showTagline: Boolean = true
) {
    val primaryText = if (isLight) Color.White else LoadGoBlueDark
    val accentText = if (isLight) LoadGoAmber else LoadGoAmber
    val tagColor = if (isLight) Color.White.copy(alpha = 0.85f) else TextSecondary

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isLight) Color.White.copy(alpha = 0.2f) else LoadGoBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalShipping,
                    contentDescription = "LoadGo Logo",
                    tint = if (isLight) Color.White else Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Row {
                Text(
                    text = "Load",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = primaryText,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Go",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = accentText,
                    letterSpacing = (-0.5).sp
                )
            }
        }
        if (showTagline) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fast & Reliable Goods Delivery • सामान की आसान ढुलाई",
                fontSize = 12.sp,
                color = tagColor,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadGoTopBar(
    title: String,
    subtitle: String? = null,
    currentRoleName: String? = null,
    onSwitchRole: () -> Unit,
    onLogout: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (currentRoleName != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = LoadGoBlueLight,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = currentRoleName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LoadGoBlueDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        },
        actions = {
            actions()
            OutlinedButton(
                onClick = onSwitchRole,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(end = 6.dp)
                    .testTag("switch_role_topbar_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = "Switch Role",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Role",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SurfaceCard
        )
    )
}

@Composable
fun BookingStatusChip(status: BookingStatus) {
    val (bg, fg, icon) = when (status) {
        BookingStatus.SEARCHING_DRIVER -> Triple(
            LoadGoAmberLight,
            Color(0xFFE65100),
            Icons.Default.HourglassTop
        )
        BookingStatus.DRIVER_ASSIGNED -> Triple(
            LoadGoBlueLight,
            LoadGoBlueDark,
            Icons.Default.PersonPin
        )
        BookingStatus.DRIVER_ARRIVING -> Triple(
            Color(0xFFE0F7FA),
            Color(0xFF006064),
            Icons.Default.DirectionsCar
        )
        BookingStatus.TRIP_STARTED -> Triple(
            Color(0xFFEDE7F6),
            Color(0xFF4A148C),
            Icons.Default.PlayArrow
        )
        BookingStatus.TRIP_COMPLETED -> Triple(
            StatusGreenLight,
            StatusGreen,
            Icons.Default.CheckCircle
        )
        BookingStatus.CANCELLED -> Triple(
            StatusRedLight,
            StatusRed,
            Icons.Default.Cancel
        )
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(16.dp),
        border = null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = fg,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${status.label} • ${status.hindiLabel}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = fg
            )
        }
    }
}

@Composable
fun DriverVerificationChip(status: DriverVerificationStatus) {
    val (bg, fg, icon) = when (status) {
        DriverVerificationStatus.APPROVED -> Triple(
            StatusGreenLight,
            StatusGreen,
            Icons.Default.Verified
        )
        DriverVerificationStatus.PENDING -> Triple(
            LoadGoAmberLight,
            Color(0xFFE65100),
            Icons.Default.Schedule
        )
        DriverVerificationStatus.REJECTED -> Triple(
            StatusRedLight,
            StatusRed,
            Icons.Default.ErrorOutline
        )
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = fg,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${status.label} (${status.hindiLabel})",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = fg
            )
        }
    }
}

@Composable
fun StatMetricCard(
    title: String,
    hindiTitle: String,
    value: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                    Text(
                        text = hindiTitle,
                        fontSize = 10.sp,
                        color = TextSecondary.copy(alpha = 0.8f)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun LocationRouteDisplay(
    pickup: String,
    drop: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(StatusGreen)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "PICKUP / पिकअप",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = StatusGreen
                )
                Text(
                    text = pickup,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }
        }
        Row(
            modifier = Modifier.padding(start = 4.dp, top = 2.dp, bottom = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(18.dp)
                    .background(DividerColor)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(StatusRed)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "DROP / ड्राप",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = StatusRed
                )
                Text(
                    text = drop,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }
        }
    }
}
