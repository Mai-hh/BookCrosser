package com.huaihao.bookcrosser.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterChips(
    items: List<String>,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    onSelected: (String) -> Unit = {}
) {
    var selected by remember { mutableIntStateOf(selectedIndex) }
    Row(modifier = modifier) {
        items.forEachIndexed { index, item ->
            FilterChip(
                leadingIcon = {
                    AnimatedVisibility(visible = selected == index) {
                        Icon(
                            Icons.Rounded.Done,
                            contentDescription = item,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                selected = (selected == index),
                onClick = {
                    selected = index
                    onSelected(item)
                },
                label = { Text(text = item) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}