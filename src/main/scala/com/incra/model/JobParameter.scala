package com.incra.model

/**
 * @author Jeff Risberg
 * @since 12/16/15
 */
class JobParameter(nameC: String, valueC: Any) {
  var name: String = nameC
  var value: Any = valueC

  override def toString(): String = "(" + name + "=" + value + ")"
}
