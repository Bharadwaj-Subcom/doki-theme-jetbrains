package io.unthrottled.doki.stickers

import com.intellij.openapi.Disposable
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBLayeredPane
import com.intellij.ui.jcef.HwFacadeJPanel
import org.intellij.lang.annotations.Language
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import javax.swing.JComponent
import javax.swing.JLayeredPane
import javax.swing.JPanel

internal class StickerPane(
  private val drawablePane: JLayeredPane,
) : HwFacadeJPanel(), Disposable {

  private lateinit var memeDisplay: JComponent

  companion object {
    private const val NOTIFICATION_Y_OFFSET = 10
  }

  @Volatile
  private var screenX = 0

  @Volatile
  private var screenY = 0

  @Volatile
  private var myX = 0

  @Volatile
  private var myY = 0

  @Volatile
  private var parentX = drawablePane.width

  @Volatile
  private var parentY = drawablePane.height

  init {
    isOpaque = false
    layout = null

    addMouseListener(object : MouseListener {
      override fun mouseClicked(e: MouseEvent?) {}

      override fun mousePressed(e: MouseEvent) {
        screenX = e.xOnScreen
        screenY = e.yOnScreen
        myX = x
        myY = y
      }

      override fun mouseReleased(e: MouseEvent?) {}

      override fun mouseEntered(e: MouseEvent?) {}

      override fun mouseExited(e: MouseEvent?) {}

    })

    addMouseMotionListener(object : MouseMotionListener {
      override fun mouseDragged(e: MouseEvent) {
        val deltaX = e.xOnScreen - screenX
        val deltaY = e.yOnScreen - screenY

        setLocation(myX + deltaX, myY + deltaY)
      }

      override fun mouseMoved(e: MouseEvent?) {}
    })
  }

  fun displaySticker(stickerUrl: String) {
    val (memeContent, memeDisplay) = createMemeContentPanel(stickerUrl)
    this.memeDisplay = memeDisplay
    add(memeContent)
    this.size = memeContent.size

    positionMemePanel(
      memeContent.size.width,
      memeContent.size.height,
    )

    drawablePane.add(this)
    drawablePane.setLayer(this, JBLayeredPane.DEFAULT_LAYER)
    doDumbStuff()

    // todo: clean up previous
    drawablePane.addComponentListener(object : ComponentAdapter() {
      override fun componentResized(e: ComponentEvent) {
        val layer = e.component
        if (layer !is JBLayeredPane) return

        val deltaX = layer.width - parentX
        val deltaY = layer.height - parentY
        setLocation(x + deltaX, y + deltaY)
        parentX = layer.width
        parentY = layer.height
      }
    })
  }

  /**
   * I'm not going to pretend like I know what I am doing.
   * I do know that the render issue goes away, when another
   * component is added to the root pane. Finna treat the symptom
   * and not fix the cause.
   */
  private fun doDumbStuff() {
    val ghostHax = JPanel()
    drawablePane.add(ghostHax)
    drawablePane.setLayer(ghostHax, JBLayeredPane.DRAG_LAYER)
    drawablePane.revalidate()
    drawablePane.repaint()
    drawablePane.remove(ghostHax)
    drawablePane.revalidate()
    drawablePane.repaint()
  }


  private fun createMemeContentPanel(stickerUrl: String): Pair<JComponent, JComponent> {
    val memeContent = JPanel()
    memeContent.layout = null
    @Language("html")
    val memeDisplay = JBLabel(
      """<html>
           <img src='${stickerUrl}' />
         </html>
      """
    )
    val memeSize = memeDisplay.preferredSize
    memeContent.size = Dimension(
      memeSize.width,
      memeSize.height,
    )
    memeContent.isOpaque = false
    memeContent.add(memeDisplay)
    val parentInsets = memeDisplay.insets
    memeDisplay.setBounds(
      parentInsets.left,
      parentInsets.top,
      memeSize.width,
      memeSize.height
    )

    return memeContent to memeDisplay
  }

  private fun positionMemePanel(width: Int, height: Int) {
    val (x, y) = getPosition(
      drawablePane.x + drawablePane.width,
      drawablePane.y + drawablePane.height,
      Rectangle(width, height)
    )
    myX = x
    myY = y
    setLocation(x, y)
  }

  private fun getPosition(
    parentWidth: Int,
    parentHeight: Int,
    memePanelBoundingBox: Rectangle,
  ): Pair<Int, Int> =
    parentWidth - memePanelBoundingBox.width - NOTIFICATION_Y_OFFSET to
      parentHeight - memePanelBoundingBox.height - NOTIFICATION_Y_OFFSET

  fun detach() {
    // todo: dis
  }

  override fun dispose() {
    // todo dis
  }
}