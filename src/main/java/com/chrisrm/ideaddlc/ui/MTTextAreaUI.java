/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Chris Magnussen and Elior Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */
package com.chrisrm.ideaddlc.ui;

import com.intellij.ide.ui.laf.darcula.ui.DarculaTextAreaUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.JTextComponent;

public final class MTTextAreaUI extends DarculaTextAreaUI {
  @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass",
      "unused"})
  public static ComponentUI createUI(final JComponent component) {
    return new MTTextAreaUI();
  }

  @Override
  public void installUI(final JComponent c) {
    super.installUI(c);
    if (((JTextComponent) c).isEditable()) {
      c.setBorder(new MTTextBorder());
    }
  }

}
