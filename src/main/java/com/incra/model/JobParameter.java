package com.incra.model

/**
 * @author Jeff Risberg
 * @since 12/16/15
 */
@Data
class JobParameter {
    (
}nameC: String, valueC: Any) {
  val name = nameC
  val value = valueC

  override def toString(): String = "(" + name + "=" + value + ")"
}
