package dev.xdark.clientlib.menu;

import dev.xdark.clientapi.event.inventory.WindowClick;
import dev.xdark.clientapi.item.ItemStack;
import dev.xdark.clientapi.item.Items;
import java.util.function.Consumer;

public final class MenuItem {

  public static final MenuItem EMPTY = new MenuItem(ItemStack.of(Items.AIR, 0, 0), __ -> { });
  final ItemStack item;
  final Consumer<WindowClick> callback;

  private MenuItem(ItemStack item, Consumer<WindowClick> callback) {
    this.item = item;
    this.callback = callback;
  }

  public static MenuItem of(ItemStack stack, Consumer<WindowClick> callback) {
    return new MenuItem(stack, callback);
  }
}
