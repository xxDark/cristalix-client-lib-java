package dev.xdark.clientlib.menu;

public final class MenuContentBuilder {

  private int rows;
  private int columns;
  private String type;

  private MenuContentBuilder() {
  }

  public static MenuContentBuilder builder() {
    return new MenuContentBuilder();
  }

  public MenuContentBuilder rows(int rows) {
    this.rows = rows;
    return this;
  }

  public MenuContentBuilder columns(int columns) {
    this.columns = columns;
    return this;
  }

  public MenuContentBuilder type(String type) {
    this.type = type;
    return this;
  }

  public MenuContent build() {
    return new MenuContent(this.rows, this.columns, this.type);
  }
}