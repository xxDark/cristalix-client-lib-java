package dev.xdark.clientlib.item;

import dev.xdark.clientapi.item.Item;
import dev.xdark.clientapi.item.ItemStack;
import dev.xdark.clientapi.nbt.NBTTagCompound;
import dev.xdark.clientapi.nbt.NBTTagList;
import dev.xdark.clientapi.nbt.NBTTagString;
import dev.xdark.clientapi.text.TextFormatting;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ItemBuilder {

  private static final Function<String, NBTTagString> TEXT_NORMALIZER = s -> NBTTagString
      .of(TextFormatting.RESET + s);
  private Item item;
  private int quantity;
  private int metadata;
  private NBTTagCompound tag;

  private ItemBuilder() {
  }

  public static ItemBuilder builder() {
    return new ItemBuilder();
  }

  public static ItemBuilder builder(ItemStack stack) {
    ItemBuilder builder = new ItemBuilder();
    builder.item = stack.getItem();
    builder.quantity = stack.getCount();
    builder.metadata = stack.getMetadata();
    NBTTagCompound tag = stack.getTagCompound();
    if (tag != null) {
      builder.tag = NBTTagCompound.copy(tag);
    }
    return builder;
  }

  public ItemBuilder item(Item item) {
    this.item = item;
    return this;
  }

  public ItemBuilder quantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  public ItemBuilder metadata(int metadata) {
    this.metadata = metadata;
    return this;
  }

  public ItemBuilder other(String other) {
    this.tag().put("other", NBTTagString.of(other));
    return this;
  }

  public ItemBuilder name(String name) {
    this.displayTag().put("Name", NBTTagString.of(TextFormatting.RESET + name));
    return this;
  }

  public ItemBuilder lore(List<String> lore) {
    NBTTagList list = (NBTTagList) this.displayTag().computeIfAbsent("Lore", __ -> NBTTagList.of(new ObjectArrayList<>(lore.size())));
    for (int i = 0, j = lore.size(); i < j; i++) {
      list.add(TEXT_NORMALIZER.apply(lore.get(i)));
    }
    return this;
  }

  public ItemBuilder tooltip(List<String> tooltip) {
    this.tag().put("Tooltip", NBTTagList.of(tooltip.stream().map(TEXT_NORMALIZER).collect(
        Collectors.toList())));
    return this;
  }

  public ItemBuilder hideTooltip() {
    this.tag().put("HideTooltip", true);
    return this;
  }

  public ItemBuilder tag(Consumer<NBTTagCompound> block) {
    block.accept(this.tag());
    return this;
  }

  public ItemBuilder normalTooltip() {
    this.tag().put("TooltipFlag", 0);
    return this;
  }

  public ItemBuilder advancedTooltip() {
    this.tag().put("TooltipFlag", 1);
    return this;
  }

  public ItemStack build() {
    ItemStack stack = ItemStack.of(this.item, this.quantity, this.metadata);
    NBTTagCompound tag = this.tag;
    if (tag != null) {
      stack.setTagCompound(tag);
    }
    return stack;
  }

  private NBTTagCompound tag() {
    NBTTagCompound tag = this.tag;
    if (tag == null) {
      return this.tag = NBTTagCompound.of();
    }
    return tag;
  }

  private NBTTagCompound displayTag() {
    return (NBTTagCompound) this.tag().computeIfAbsent("display", __ -> NBTTagCompound.of());
  }
}
