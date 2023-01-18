package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role

internal interface MultipleEventsCutter {
    fun processEvent(event: () -> Unit)

    companion object
}

internal fun MultipleEventsCutter.Companion.get(): MultipleEventsCutter =
    MultipleEventsCutterImpl()

private class MultipleEventsCutterImpl : MultipleEventsCutter {
    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTimeMs: Long = 0

    override fun processEvent(event: () -> Unit) {
        if (now - lastEventTimeMs >= 300L) {
            event.invoke()
        }
        lastEventTimeMs = now
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.clickableSafeClick(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    onLongClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
        properties["onLongClick"] = onLongClick
        properties["onDoubleClick"] = onDoubleClick
    }
) {
    val onClickEventsCutter = remember { MultipleEventsCutter.get() }
//    val onLongEventsCutter = remember { MultipleEventsCutter.get() }
//    val onDoubleEventsCutter = remember { MultipleEventsCutter.get() }

    Modifier.combinedClickable(
        enabled = enabled,
        onClick = { onClickEventsCutter.processEvent { onClick() } },
//        onLongClick = { onLongEventsCutter.processEvent { onLongClick?.invoke() } },
//        onDoubleClick = { onDoubleEventsCutter.processEvent { onDoubleClick?.invoke() } },
        onLongClick = onLongClick,
        onDoubleClick = onDoubleClick,
        onLongClickLabel = onLongClickLabel,
        onClickLabel = onClickLabel,
        role = role,
        indication = LocalIndication.current,
        interactionSource = remember { MutableInteractionSource() }

    )
}

