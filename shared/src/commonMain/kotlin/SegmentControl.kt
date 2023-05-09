import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
inline fun <T> SegControl(
    options: Array<T>,
    cornerRadius: Dp = 20.dp,
    segColor: Color = Color.Black,
    padding: Dp = 2.dp,
    modifier: Modifier = Modifier,
    crossinline onValueChange: (T) -> Unit,
    crossinline content: @Composable (Int, T) -> Unit
) {
    val parentSize = remember { mutableStateOf(IntSize.Zero) }
    val offsetX = remember { mutableStateOf(padding) }
    val translationX by animateDpAsState(
        targetValue = offsetX.value,
        animationSpec = tween(durationMillis = 500)
    )
    val interactionSource = remember { MutableInteractionSource() }
    val paddingSize = with(LocalDensity.current) {
        padding.toPx().toInt()
    }
    Box(
        modifier = modifier.padding(padding).layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            parentSize.value = IntSize(constraints.maxWidth - paddingSize * 2, constraints.maxHeight - paddingSize * 2)
            layout(placeable.width, placeable.height) {
                placeable.place(0, 0)
            }
        },
    ) {
        val parentWidthDp = with(LocalDensity.current) {
            parentSize.value.width.toDp()
        }
        val itemWidth = parentWidthDp / options.size - padding
        val itemHeight = with(LocalDensity.current) {
            parentSize.value.height.toDp()
        }
        Row(
            modifier = Modifier
                .offset(translationX, padding)
                .background(segColor, RoundedCornerShape(cornerRadius))
                .width(itemWidth)
                .height(itemHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {}
        Row {
            options.forEachIndexed { index, option ->
                Column (
                    modifier = Modifier
                        .padding(padding)
                        .width(itemWidth)
                        .height(itemHeight)
                        .align(Alignment.CenterVertically)
                        .clickable(onClick = {
                            when (index) {
                                0 -> {
                                    offsetX.value = padding
                                    println("this is option: $option & offsetX: $offsetX")
                                }
//                                options.size - 1 -> {
//                                    offsetX.value = parentWidthDp.minus(itemWidth) +  padding * (index + 1)
//                                    println("this is last option: $option & offsetX: $offsetX")
//                                }
                                else -> {
                                    offsetX.value = itemWidth.times(index) + padding * (index + 1)
                                    println("this is else option: $option & offsetX: $offsetX")
                                }
                            }
                            onValueChange(option)
                        }, indication = null, interactionSource = interactionSource),
                    verticalArrangement = Arrangement.Center
                ) {
                    content(index, option)
                }
            }
        }
    }
}
