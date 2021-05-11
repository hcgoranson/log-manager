package one.goranson.log;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.goranson.common.paging.Column;
import one.goranson.common.paging.Order;
import one.goranson.common.paging.Page;
import one.goranson.common.paging.PagingRequest;
import one.goranson.log.requests.LogRequest;
import one.goranson.log.dto.LogItem;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {

  private static final Comparator<LogItem> EMPTY_COMPARATOR_LOG = (e1, e2) -> 0;

  private final LogClient logClient;
  private final CacheManager cacheManager;

  public Page<LogItem> getLogger(PagingRequest pagingRequest) {
    try {
      var logeResponse = logClient.getLogs();

      var logItems = logeResponse.getLoggers().entrySet().stream()
          .map(entry -> new LogItem(entry.getKey(), entry.getValue().getLevel()))
          .collect(Collectors.toList());

      return getPage(logItems, pagingRequest);
    } catch (Exception exception) {
      return Page.empty("Failed to fetch logs due to '" + exception.getMessage() +"'");
    }
  }

  private Page<LogItem> getPage(List<LogItem> logItems, PagingRequest pagingRequest) {
    var filtered = logItems.stream()
        .sorted(sortLogItems(pagingRequest))
        .filter(filterLogItems(pagingRequest))
        .skip(pagingRequest.getStart())
        .limit(pagingRequest.getLength())
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    long count = logItems.stream()
        .filter(filterLogItems(pagingRequest))
        .count();

    Page<LogItem> page = new Page<>(filtered);
    page.setRecordsFiltered((int) count);
    page.setRecordsTotal((int) count);
    page.setDraw(pagingRequest.getDraw());

    return page;
  }

  private Predicate<LogItem> filterLogItems(PagingRequest pagingRequest) {
    if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
        .getValue())) {
      return employee -> true;
    }

    String value = pagingRequest.getSearch().getValue().toLowerCase();

    return logItem -> logItem.getLogger()
                          .toLowerCase()
                          .contains(value)
                      || logItem.getLevel()
                          .toLowerCase()
                          .contains(value);
  }

  private Comparator<LogItem> sortLogItems(PagingRequest pagingRequest) {
    if (pagingRequest.getOrder() == null) {
      return EMPTY_COMPARATOR_LOG;
    }

    try {
      Order order = pagingRequest.getOrder()
          .get(0);

      int columnIndex = order.getColumn();
      Column column = pagingRequest.getColumns()
          .get(columnIndex);

      Comparator<LogItem> comparator = LogComparators
          .getComparator(column.getData(), order.getDir());
      if (comparator == null) {
        return EMPTY_COMPARATOR_LOG;
      }

      return comparator;

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return EMPTY_COMPARATOR_LOG;
  }

  public void updateLogItem(LogItem logItem) {
    logClient.updateLog(LogRequest.toLogRequest(logItem));
    evictCache();
  }

  public void evictCache() {
    cacheManager.getCacheNames().stream()
        .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
  }

}
