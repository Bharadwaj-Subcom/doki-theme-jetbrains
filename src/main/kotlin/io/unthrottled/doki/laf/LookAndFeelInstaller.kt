package io.unthrottled.doki.laf

import com.intellij.ide.ui.laf.darcula.ui.DarculaCheckBoxMenuItemUI
import com.intellij.ui.JBColor.DARK_GRAY
import com.intellij.ui.JBColor.namedColor
import io.unthrottled.doki.icon.DokiIcons
import io.unthrottled.doki.service.GlassNotificationService
import io.unthrottled.doki.service.PluginService
import io.unthrottled.doki.ui.DokiCheckboxUI
import io.unthrottled.doki.ui.DokiTableSelectedCellHighlightBorder
import io.unthrottled.doki.ui.TitlePaneUI
import io.unthrottled.doki.ui.ToggleButtonUI
import javax.swing.BorderFactory
import javax.swing.UIManager
import kotlin.collections.set

object LookAndFeelInstaller {
  init {
    installAllUIComponents()
  }

  fun installAllUIComponents() {
    installIcons()
    installTitlePane()
    installButtons()
    installCheckboxes()
    installDefaults()
    GlassNotificationService.makeNotificationSeeThrough()
  }

  private fun installDefaults() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults["TextPaneUI"] = "javax.swing.plaf.basic.BasicTextPaneUI"
    val tableSelectedBorder = DokiTableSelectedCellHighlightBorder()
    defaults["Table.focusSelectedCellHighlightBorder"] = tableSelectedBorder
    defaults["Table.focusCellHighlightBorder"] = tableSelectedBorder
    defaults["List.focusCellHighlightBorder"] = BorderFactory.createEmptyBorder()
    defaults["TitledBorder.border"] = BorderFactory.createLineBorder(
      namedColor("Doki.Accent.color", DARK_GRAY)
    )
  }

  private fun installIcons() {
    if (PluginService.areIconsInstalled()) {
      return
    }

    val defaults = UIManager.getLookAndFeelDefaults()
    defaults[DokiIcons.Tree.COLLAPSED_KEY] = DokiIcons.Tree.COLLAPSED
    defaults[DokiIcons.Tree.SELECTED_COLLAPSED_KEY] = DokiIcons.Tree.COLLAPSED
    defaults[DokiIcons.Tree.EXPANDED_KEY] = DokiIcons.Tree.EXPANDED
    defaults[DokiIcons.Tree.SELECTED_EXPANDED_KEY] = DokiIcons.Tree.EXPANDED
  }

  private fun installButtons() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults["OnOffButtonUI"] = ToggleButtonUI::class.java.name
    defaults[ToggleButtonUI::class.java.name] = ToggleButtonUI::class.java
  }

  private fun installCheckboxes() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults[DokiIcons.CheckBox.CHECK_MARK_KEY] = DokiIcons.CheckBox.CHECK_MARK
    defaults["CheckBoxMenuItem.borderPainted"] = false
    defaults["CheckBoxUI"] = DokiCheckboxUI::class.java.name
    defaults[DokiCheckboxUI::class.java.name] = DokiCheckboxUI::class.java
    defaults["CheckBoxMenuItemUI"] = DarculaCheckBoxMenuItemUI::class.java.name
    defaults[DarculaCheckBoxMenuItemUI::class.java.name] = DarculaCheckBoxMenuItemUI::class.java
  }

  private fun installTitlePane() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults["RootPaneUI"] = TitlePaneUI::class.java.name
    defaults[TitlePaneUI::class.java.name] = TitlePaneUI::class.java
  }
}
