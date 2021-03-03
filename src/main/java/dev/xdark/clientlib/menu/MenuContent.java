package dev.xdark.clientlib.menu;

import dev.xdark.clientapi.entity.EntityPlayerSP;
import dev.xdark.clientapi.event.inventory.WindowClick;
import dev.xdark.clientapi.inventory.ContainerLocalMenu;
import dev.xdark.clientapi.inventory.Inventory;
import dev.xdark.clientapi.item.ItemStack;
import dev.xdark.clientapi.text.Text;
import it.unimi.dsi.fastutil.shorts.ShortList;

public final class MenuContent {

  private final int rows;
  private final int columns;
  private final String type;
  private final MenuItem[][] matrix;
  private LayoutDeclaration layout;
  private ContainerLocalMenu handle;
  private int windowId = Integer.MAX_VALUE;

  MenuContent(int rows, int columns, String type) {
    this.matrix = new MenuItem[rows][columns];
    this.rows = rows;
    this.columns = columns;
    this.type = type;
  }

  public MenuContent setLayout(String... layout) {
    this.layout = LayoutDeclaration.of(this.columns, layout);
    return this;
  }

  public MenuContent set(int row, int column, MenuItem item) {
    this.matrix[row][column] = item;
    this.update(row, column, item.item);
    return this;
  }

  public MenuContent set(int pos, MenuItem item) {
    int row = 0;
    int columns = this.columns;
    while (pos >= columns) {
      row++;
      pos -= columns;
    }
    return this.set(row, pos, item);
  }

  public int add(char key, MenuItem item) {
    int next = this.layout.nextSlot(key);
    this.set(next, item);
    return next;
  }

  public MenuContent fillMask(char key, MenuItem item) {
    ShortList left = this.layout.left(key);
    for (int i = 0, j = left.size(); i < j; i++) {
      this.set(left.getShort(i), item);
    }
    return this;
  }

  public int size(char key) {
    return this.layout.size(key);
  }

  public MenuContent title(Text title) {
    this.toMenu().setCustomName(title.getFormattedText());
    return this;
  }

  public MenuContent title(String title) {
    this.toMenu().setCustomName(title);
    return this;
  }

  public void copyInto(Inventory inventory) {
    MenuItem[][] matrix = this.matrix;
    int columns = this.columns;
    for (int i = 0, j = matrix.length; i < j; i++) {
      MenuItem[] row = matrix[i];
      for (int k = 0, l = row.length; k < l; k++) {
        MenuItem item = row[k];
        if (item == null) {
          continue;
        }
        ItemStack stack = item.item;
        if (!stack.isEmpty()) {
          inventory.setInventorySlotContents(columns * i + k, stack);
        }
      }
    }
  }

  public int getWindowId() {
    return this.windowId;
  }

  public int open(EntityPlayerSP player) {
    return this.windowId = player.displayContainerMenu(this.toMenu());
  }

  public void fireWindowClick(WindowClick click) {
    int pos = click.getSlot();
    if (pos == -999) {
      return;
    }
    int row = 0;
    int columns = this.columns;
    while (pos >= columns) {
      row++;
      pos -= columns;
    }
    MenuItem item = this.matrix[row][pos];
    if (item != null) {
      item.callback.accept(click);
    }
  }

  private ContainerLocalMenu toMenu() {
    ContainerLocalMenu handle = this.handle;
    if (handle == null) {
      handle = this.handle = ContainerLocalMenu
          .of(this.type, Text.of(""), this.rows * this.columns);
      this.copyInto(handle);
    }
    return handle;
  }

  private void update(int row, int column, ItemStack item) {
    int slot = this.columns * row + column;
    ContainerLocalMenu handle = this.handle;
    if (handle != null) {
      handle.setInventorySlotContents(slot, item);
    }
  }
}
