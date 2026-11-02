package com.adnlv.lynd.ui.holdings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adnlv.lynd.ui.theme.LyndTheme

@Composable
fun HoldingsSyncBanner(
    syncError: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = syncError != null,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier,
    ) {
        syncError?.let { error ->
            Surface(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = LyndTheme.colors.feedbackContainer,
                contentColor = LyndTheme.colors.onFeedbackContainer,
                border =
                    BorderStroke(
                        width = 1.dp,
                        color = LyndTheme.colors.borderSubtle,
                    ),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Sync Error",
                        tint = LyndTheme.colors.onFeedbackContainer,
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Sync error",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = LyndTheme.colors.onFeedbackContainer,
                        )
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = LyndTheme.colors.onFeedbackContainer,
                            maxLines = 2,
                        )
                    }
                    TextButton(
                        onClick = onRetry,
                        contentPadding = PaddingValues(horizontal = 8.dp),
                    ) {
                        Text(
                            text = "Retry",
                            fontWeight = FontWeight.Bold,
                            color = LyndTheme.colors.onFeedbackContainer,
                        )
                    }
                }
            }
        }
    }
}
