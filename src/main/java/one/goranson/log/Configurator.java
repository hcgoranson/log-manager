package one.goranson.log;

import org.springframework.stereotype.Service;
import one.goranson.log.dto.Config;

@Service
public final class Configurator {

  private static Config config = Config.builder()
      .scheme("http://")
      .host("localhost")
      .port("8080")
      .build();

  public String getHostname() {
    return String.format("%s:%s", config.getHost(), config.getPort());
  }

  public String getUrl() {
    return String.format("%s%s:%s", config.getScheme(), config.getHost(), config.getPort());
  }

  public Config getConfig() {
    return config;
  }

  public void updateConfig(Config config1) {
    config = config1;
  }
}
