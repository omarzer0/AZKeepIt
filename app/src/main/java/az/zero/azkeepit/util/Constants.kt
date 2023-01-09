package az.zero.azkeepit.util

import az.zero.azkeepit.domain.model.UiNote

const val singleLineValue = 1

val Empty_Ui_Note = UiNote(
    title = "",
    content = "",
    isLocked = false,
    isUpdated = false,
    createdAt = JDateTimeUtil.now(),
    updatedAt = null,
    createdShortDateTime = "",
    createdLongDateTime = "",
    updatedShortDateTime = null,
    updatedLongDateTime = null,
    folderName = "",
    ownerFolderId = -1,
    noteId = -1
)
