import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
inline fun <T> SegControl(
    options: MutableList<T>,
    cornerRadius: Dp = 20.dp,
    segColor: Color = Color.Black,
    modifier: Modifier = Modifier,
    crossinline content: @Composable (Int, T) -> Unit
) {
    val parentSize = remember { mutableStateOf(IntSize.Zero) }
    val offsetX = remember { mutableStateOf(0.dp) }
    val translationX by animateDpAsState(
        targetValue = offsetX.value,
        animationSpec = tween(durationMillis = 500)
    )
    var selectedIndex by remember { mutableStateOf(0) }

    Box(
        modifier = modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            parentSize.value = IntSize(constraints.maxWidth, constraints.maxHeight)
            layout(placeable.width, placeable.height) {
                placeable.place(0, 0)
            }
        },
    ) {
        val itemWidth = with(LocalDensity.current) {
            (parentSize.value.width / options.size).toDp()
        }
        val itemHeight = with(LocalDensity.current) {
            parentSize.value.height.toDp()
        }
        Row(modifier = Modifier
            .offset(translationX, 0.dp)
            .background(segColor, RoundedCornerShape(cornerRadius))
            .width(itemWidth)
            .height(itemHeight)) {}
        Row {
            options.forEachIndexed { index, option ->
                Column (modifier = Modifier.width(itemWidth).height(itemHeight).align(Alignment.CenterVertically), verticalArrangement = Arrangement.Center) {
                    val color = if (index == selectedIndex) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
                    ClickableText(
                        AnnotatedString(option.toString()),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = TextStyle(color = color),
                        onClick = {
                            selectedIndex = index
                            offsetX.value = itemWidth.times(index)
                        }
                    )
                }
            }
        }
    }
}
