package com.adnlv.lynd.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adnlv.lynd.ui.icons.Add
import com.adnlv.lynd.ui.icons.Experiment
import com.adnlv.lynd.ui.icons.Lightbulb
import com.adnlv.lynd.ui.icons.MaterialSymbols

@Composable
fun LyndNoHoldingsPlate(
    title: String,
    description: String,
    onAddHolding: () -> Unit,
    onLoadTestPortfolio: () -> Unit,
    modifier: Modifier = Modifier,
    addHoldingLabel: String = "Add Holding",
    loadTestPortfolioLabel: String = "Load test portfolio"
) {
    LyndEmptyState(
        title = title,
        description = description,
        icon = MaterialSymbols.Lightbulb,
        primaryActionLabel = addHoldingLabel,
        primaryActionIcon = MaterialSymbols.Add,
        onPrimaryAction = onAddHolding,
        secondaryActionLabel = loadTestPortfolioLabel,
        secondaryActionIcon = MaterialSymbols.Experiment,
        onSecondaryAction = onLoadTestPortfolio,
        modifier = modifier
    )
}
