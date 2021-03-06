package org.scalajs.testsuite.utils

import scala.language.implicitConversions

import scala.scalajs.js
import js.annotation.JSExport

object JSUtils {
  /** The detected global object. */
  val globalObject: js.Dynamic = {
    import js.Dynamic.{global => g}
    // We've got to use selectDynamic explicitly not to crash Scala 2.10
    if (js.typeOf(g.selectDynamic("global")) != "undefined" &&
        (g.selectDynamic("global").selectDynamic("Object") eq g.selectDynamic("Object"))) {
      // Node.js environment detected
      g.selectDynamic("global")
    } else {
      // In all other well-known environment, we can use the global `this`
      js.special.globalThis.asInstanceOf[js.Dynamic]
    }
  }

  /* We use java.lang.Character explicitly, because this class is used by
   * tests that check that Chars are actually boxed by the compiler.
   * If we rely on the compiler doing the job in here, we might have false
   * positives just because the value was never boxed, and never unboxed.
   */

  @JSExport
  def isChar(c: Any): Boolean = c.isInstanceOf[java.lang.Character]

  @JSExport
  def stringToChar(s: String): java.lang.Character = {
    assert(s.length == 1, "makeChar() requires a string of length 1")
    new java.lang.Character(s.charAt(0))
  }

  @JSExport
  def charToString(c: Any): String =
    c.asInstanceOf[java.lang.Character].toString()

  implicit def asJSAny(jsUtils: JSUtils.type): js.Any =
    jsUtils.asInstanceOf[js.Any]
}
