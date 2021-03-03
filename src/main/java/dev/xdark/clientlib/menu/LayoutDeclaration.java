package dev.xdark.clientlib.menu;

import dev.xdark.clientapi.jvm.Intrinsics;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.chars.Char2ShortArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ShortMap;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import java.util.Arrays;
import java.util.Map;

public final class LayoutDeclaration {

  private static final Map<Key, LayoutDeclaration> CACHE = new Object2ObjectOpenHashMap<>();
  private final Char2ObjectMap<ShortList> layout;
  private final Char2ShortMap offsets;

  private LayoutDeclaration(Char2ObjectMap<ShortList> layout, Char2ShortMap offsets) {
    this.layout = layout;
    this.offsets = offsets;
  }

  private LayoutDeclaration(int width, String... declaration) {
    Char2ObjectMap<ShortList> layout = this.layout = layoutSlots(width, declaration);
    Char2ShortMap offsets = this.offsets = new Char2ShortArrayMap(layout.size());
    CharIterator iterator = layout.keySet().iterator();
    while (iterator.hasNext()) {
      offsets.put(iterator.nextChar(), (short) 0);
    }
  }

  public int size(char c) {
    return this.layout.get(c).size();
  }

  public int offset(char c) {
    return this.offsets.get(c);
  }

  public short nextSlot(char c) {
    Char2ShortMap offsets = this.offsets;
    int offset = offsets.get(c);
    short slot = this.layout.get(c).getShort(offset);
    offsets.put(c, (short) (offset + 1));
    return slot;
  }

  public ShortList left(char c) {
    ShortList locations = this.layout.get(c);
    return locations.subList(this.offsets.get(c), locations.size());
  }

  private LayoutDeclaration cloneEmpty() {
    Char2ObjectMap<ShortList> layout = this.layout;
    Char2ShortMap offsets = new Char2ShortArrayMap(layout.size());
    CharIterator iterator = layout.keySet().iterator();
    while (iterator.hasNext()) {
      offsets.put(iterator.nextChar(), (short) 0);
    }
    return new LayoutDeclaration(layout, offsets);
  }

  public static LayoutDeclaration of(int width, String... declaration) {
    Map<Key, LayoutDeclaration> cache = CACHE;
    Key key = new Key(width, declaration);
    LayoutDeclaration existing = cache.get(key);
    if (existing != null) {
      return existing.cloneEmpty();
    }
    LayoutDeclaration dec = new LayoutDeclaration(width, declaration);
    cache.put(key, dec);
    return dec;
  }

  private static Char2ObjectMap<ShortList> layoutSlots(int width, String... strings) {
    int length = strings.length;
    Char2ObjectMap<ShortList> output = new Char2ObjectOpenHashMap<>(length);
    for (int row = 0; row < length; row++) {
      String string = strings[row];
      if (string.length() != width) {
        throw new IllegalArgumentException("String '" + string + "' does not a length of " + width
            + " but instead has a length of " + string.length());
      }
      char[] chars = Intrinsics.getChars(string);
      for (int slot = 0; slot < width; slot++) {
        char letter = chars[slot];
        if (letter != '_') {
          letter = Character.toLowerCase(letter);
          ShortList list = output.get(letter);
          if (list == null) {
            output.put(letter, list = new ShortArrayList());
          }
          list.add((short) ((row * width) + slot));
        }
      }
    }
    return output;
  }

  private static final class Key {

    private final int width;
    private final String[] declaration;

    private Key(int width, String[] declaration) {
      this.width = width;
      this.declaration = declaration;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Key)) {
        return false;
      }

      Key key = (Key) o;
      return width == key.width && Arrays.equals(declaration, key.declaration);
    }

    @Override
    public int hashCode() {
      int result = width;
      result = 31 * result + Arrays.hashCode(declaration);
      return result;
    }
  }
}
