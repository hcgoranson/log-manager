package one.goranson.log;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.goranson.log.dto.LogResponse;
import one.goranson.log.requests.LogRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogClient {

  private final RestTemplate restTemplate;
  private final Configurator configurator;

  @Cacheable("loggers")
  public LogResponse getLogs() {
    var url = String.format("%s/actuator/loggers", configurator.getUrl());

    return restTemplate.getForEntity(url, LogResponse.class).getBody();
  }

  public void updateLog(LogRequest logRequest) {
    var url =
        String.format("%s/actuator/loggers/%s", configurator.getUrl(), logRequest.getLogger());
    var request = new HttpEntity(logRequest.getBody());
    restTemplate.postForObject(url, new HttpEntity(logRequest.getBody()), String.class);
  }

}
