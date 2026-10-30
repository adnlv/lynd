package com.adnlv.lynd.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adnlv.lynd.ui.theme.LyndLayoutDefaults

@Composable
fun LyndScreenHeader(
    title: String,
    modifier: Modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(LyndLayoutDefaults.ScreenHeaderHeight)
            .then(modifier),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
