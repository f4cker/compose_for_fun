import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }
        var selection by remember { mutableStateOf(StyleReference.IMAGE) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                greetingText = "Hello, ${getPlatformName()}"
                showImage = !showImage
            }) {
                Text(selection.value)
            }
            AnimatedVisibility(showImage) {
                Image(
                    painterResource("compose-multiplatform.xml"),
                    null
                )
            }
            SegControl(
                options = StyleReference.values(),
                segColor = MaterialTheme.colors.surface,
                modifier = Modifier.width(250.dp)
                    .height(30.dp)
                    .background(Color.Gray, RoundedCornerShape(20.dp)),
                onValueChange = { selection = it }
            ) { _, style ->
                val color = if (style == selection) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
                Text(style.value,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize= 12.sp,
                    style = TextStyle(color = color),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

enum class StyleReference(val value: String) {
    IMAGE("Upload Image"),
    TEMPLATE("AI Template"),
    DEFAULT("Default")
}

expect fun getPlatformName(): String