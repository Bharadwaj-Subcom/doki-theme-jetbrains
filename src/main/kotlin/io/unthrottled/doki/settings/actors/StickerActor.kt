package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.stickers.CustomStickerService
import io.unthrottled.doki.stickers.StickerComponent
import io.unthrottled.doki.stickers.StickerLevel
import io.unthrottled.doki.stickers.StickerPaneService
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.performWithAnimation

object StickerActor {

  fun swapStickers(newStickerType: CurrentSticker, withAnimation: Boolean = true) {
    if (ThemeConfig.instance.currentSticker != newStickerType) {
      performWithAnimation(withAnimation) {
        ThemeConfig.instance.currentSticker = newStickerType
        ThemeManager.instance.currentTheme.ifPresent {
          StickerComponent.activateForTheme(it)
        }
      }
    }
  }

  fun enableStickers(enabled: Boolean, withAnimation: Boolean = true) {
    if (enabled != (ThemeConfig.instance.currentStickerLevel == StickerLevel.ON)) {
      setStickers(withAnimation, enabled)
    }
  }

  fun setStickers(withAnimation: Boolean, enabled: Boolean) {
    performWithAnimation(withAnimation) {
      if (enabled) {
        ThemeConfig.instance.stickerLevel = StickerLevel.ON.name
        ThemeManager.instance.currentTheme.ifPresent {
          StickerPaneService.instance.activateForTheme(it)
        }
      } else {
        ThemeConfig.instance.stickerLevel = StickerLevel.OFF.name
        StickerPaneService.instance.remove()
      }
    }
  }

  fun setCustomSticker(
    customStickerPath: String,
    isCustomStickers: Boolean,
    withAnimation: Boolean
  ) {
    val isCustomStickersChanged = CustomStickerService.isCustomStickers != isCustomStickers
    CustomStickerService.isCustomStickers = isCustomStickers

    val isNewStickerPath = CustomStickerService.getCustomStickerPath()
      .map { it != customStickerPath }
      .orElse(true) && customStickerPath.isNotBlank()
    if (isNewStickerPath) {
      CustomStickerService.setCustomStickerPath(customStickerPath)
    }

    val shouldReDraw = isNewStickerPath || isCustomStickersChanged
    if (shouldReDraw) {
      activateNewSticker(withAnimation)
    }
  }

  fun setDimensionCapping(
    capStickerDimensions: Boolean,
    maxStickerWidth: Int,
    maxStickerHeight: Int
  ) {
    ThemeConfig.instance.maxStickerWidth = maxStickerWidth
    ThemeConfig.instance.maxStickerHeight = maxStickerHeight
    if(ThemeConfig.instance.capStickerDimensions != capStickerDimensions) {
      ThemeConfig.instance.capStickerDimensions = capStickerDimensions
      activateNewSticker(false)
    }
  }

  private fun activateNewSticker(withAnimation: Boolean) {
    performWithAnimation(withAnimation) {
      ThemeManager.instance.currentTheme.ifPresent {
        StickerPaneService.instance.activateForTheme(it)
      }
    }
  }
}
