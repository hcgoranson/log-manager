package one.goranson.common.paging;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Page<T> {

  public Page(List<T> data) {
    this.data = data;
  }

  private List<T> data;
  private int recordsFiltered;
  private int recordsTotal;
  private int draw;
  private String message;

  public static <T> Page<T> empty(String message) {
    var page = new Page<T>(Collections.emptyList());
    page.setMessage(message);
    return page;
  }

}
