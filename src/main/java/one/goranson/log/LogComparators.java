package one.goranson.log;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import one.goranson.common.paging.Direction;
import one.goranson.log.dto.LogItem;

public final class LogComparators {

  @EqualsAndHashCode
  @AllArgsConstructor
  @Getter
  static class Key {
    String name;
    Direction dir;
  }

  static Map<Key, Comparator<LogItem>> map = new HashMap<>();

  static {
    map.put(new Key("logger", Direction.asc), Comparator.comparing(LogItem::getLogger));
    map.put(new Key("logger", Direction.desc), Comparator.comparing(LogItem::getLogger)
        .reversed());
    map.put(new Key("level", Direction.asc), Comparator.comparing(LogItem::getLevel));
    map.put(new Key("level", Direction.desc), Comparator.comparing(LogItem::getLevel)
        .reversed());
  }

  public static Comparator<LogItem> getComparator(String name, Direction dir) {
    return map.get(new Key(name, dir));
  }

  private LogComparators() {
  }
}
