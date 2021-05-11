package one.goranson.log;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.goranson.common.paging.Page;
import one.goranson.common.paging.PagingRequest;
import one.goranson.log.dto.Config;
import one.goranson.log.dto.LogItem;

@Slf4j
@RestController
@RequestMapping("logs")
@RequiredArgsConstructor
public class LogController {

  private final LogService logService;
  private final Configurator configurator;

  @PostMapping
  public Page<LogItem> getLogs(@RequestBody PagingRequest pagingRequest) {
    return logService.getLogger(pagingRequest);
  }

  @PostMapping(value = "/update")
  public ModelAndView updateLog(@ModelAttribute LogItem logItem, Model model) {
    logService.updateLogItem(logItem);
    log.info("Save log item " + logItem);
    return new ModelAndView("redirect:/");
  }

  @PostMapping(value = "/configure")
  public ModelAndView configure(@ModelAttribute Config config, Model model) {
    configurator.updateConfig(config);
    return new ModelAndView("redirect:/");
  }

  @PostMapping(value = "/evictCache")
  @ResponseStatus(HttpStatus.OK)
  public void evictCache() {
    logService.evictCache();
  }

}
