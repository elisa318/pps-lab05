package ex

import util.Optionals.Optional
import util.Sequences.*

import scala.collection.immutable.Stream.Empty
trait Item:
  def code: Int
  def name: String
  def tags: Sequence[String]

object Item:
  def apply(code: Int, name: String, tags: Sequence[String] = Sequence.empty): Item = ItemImpl(code, name, tags)

/**
 * A warehouse is a place where items are stored.
 */
trait Warehouse:
  /**
   * Stores an item in the warehouse.
   * @param item the item to store
   */
  def store(item: Item): Unit
  /**
   * Searches for items with the given tag.
   * @param tag the tag to search for
   * @return the list of items with the given tag
   */
  def searchItems(tag: String): Sequence[Item]
  /**
   * Retrieves an item from the warehouse.
   * @param code the code of the item to retrieve
   * @return the item with the given code, if present
   */
  def retrieve(code: Int): Optional[Item]
  /**
   * Removes an item from the warehouse.
   * @param item the item to remove
   */
  def remove(item: Item): Unit
  /**
   * Checks if the warehouse contains an item with the given code.
   * @param itemCode the code of the item to check
   * @return true if the warehouse contains an item with the given code, false otherwise
   */
  def contains(itemCode: Int): Boolean
end Warehouse

object Warehouse:
  def apply(): Warehouse = WarehouseImpl()

@main def mainWarehouse(): Unit =
  val warehouse = Warehouse()

  val dellXps = Item(33, "Dell XPS 15", Sequence("notebook"))
  val dellInspiron = Item(34, "Dell Inspiron 13", Sequence("notebook"))
  val xiaomiMoped = Item(35, "Xiaomi S1", Sequence("moped", "mobility"))

  warehouse.store(dellXps) // side effect, add dell xps to the warehouse
  warehouse.contains(dellXps.code) // true
  warehouse.store(dellInspiron) // side effect, add dell Inspiron to the warehouse
  warehouse.store(xiaomiMoped) // side effect, add xiaomi moped to the warehouse
  println(warehouse.searchItems("mobility") )// Sequence(xiaomiMoped)
  warehouse.searchItems("notebook") // Sequence(dellXps, dell Inspiron)
  warehouse.retrieve(11) // None
  println(warehouse.retrieve(dellXps.code))// Just(dellXps)
  warehouse.remove(dellXps) // side effect, remove dell xps from the warehouse
  warehouse.retrieve(dellXps.code) // None
  println(warehouse.retrieve(dellXps.code))

/** Hints:
 * - Implement the Item with a simple case class
 * - Implement the Warehouse keeping a private List of items
 * - Start implementing contains and store
 * - Implement searchItems using filter and contains
 * - Implement retrieve using find
 * - Implement remove using filter
 * - Refactor the code of Item accepting a variable number of tags (hint: use _*)
*/
case class ItemImpl(override val code: Int, override val name: String, override val tags: Sequence[String] = Sequence.empty) extends Item

case class WarehouseImpl() extends  Warehouse :
  opaque type items = Sequence[Item]
  private var stored_items: items = Sequence.empty[Item]

  override def store(item: Item): Unit =
    stored_items = stored_items.concat(Sequence(item))

  override def searchItems(tag: String): Sequence[Item] =
    stored_items.filter(_.tags.contains(tag))

  override def retrieve(code: Int): Optional[Item] =
    stored_items.find(_.code == code)

  override def remove(item: Item): Unit =
    stored_items = stored_items.filter(_.code != item.code)

  override def contains(itemCode: Int): Boolean =
    stored_items.find(_.code == itemCode) match {
      case Optional.Empty() => false
      case _ => true
    }