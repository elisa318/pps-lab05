package util

import ex.*
import util.Sequences.Sequence
import org.junit.Test

class WarehouseTest :

  @Test
  def testCreateItem(): Unit =
    val item1 = Item(5, "vi", Sequence())