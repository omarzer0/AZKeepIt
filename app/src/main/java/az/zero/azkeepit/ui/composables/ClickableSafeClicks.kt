package az.zero.azkeepit.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

/**
 * Debounce implementation to prevent multiple rapid clicks
 */
private class DebounceClickHandler(private val debounceDelayMs: Long = 300L) {
    private var lastClickTime = 0L

    fun canClick(): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastClickTime >= debounceDelayMs).also { canClick ->
            if (canClick) lastClickTime = currentTime
        }
    }
}

/**
 * Clickable modifier with debouncing to prevent rapid multiple clicks.
 *
 * Supports single click, long press, and double click with automatic debouncing
 * on the onClick action to prevent accidental duplicate triggers.
 *
 * @param enabled Whether the clickable is enabled
 * @param debounceMs Debounce delay in milliseconds (default: 300ms)
 * @param onClickLabel Semantic label for onClick action
 * @param onLongClickLabel Semantic label for onLongClick action
 * @param role Semantic role for accessibility
 * @param onClick Lambda invoked on click (debounced)
 * @param onLongClick Optional lambda invoked on long press (not debounced)
 * @param onDoubleClick Optional lambda invoked on double click (not debounced)
 */
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.clickableSafeClick(
    enabled: Boolean = true,
    debounceMs: Long = 300L,
    onClickLabel: String? = null,
    onLongClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
): Modifier = composed {
    val debouncer = remember { DebounceClickHandler(debounceMs) }
    val interactionSource = remember { MutableInteractionSource() }

    this.then(
        Modifier.combinedClickable(
            enabled = enabled,
            onClick = {
                if (debouncer.canClick()) onClick()
            },
            onLongClick = onLongClick,
            onDoubleClick = onDoubleClick,
            onClickLabel = onClickLabel,
            onLongClickLabel = onLongClickLabel,
            role = role,
            indication = LocalIndication.current,
            interactionSource = interactionSource
        )
    )
}