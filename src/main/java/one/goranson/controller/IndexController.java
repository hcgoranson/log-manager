package one.goranson.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;
import one.goranson.log.Configurator;
import one.goranson.log.dto.Config;
import one.goranson.log.dto.LogItem;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class IndexController {

    private final Configurator configurator;

    @GetMapping({ "/", "/index" })
    public String index(Model model) {
        model.addAttribute("config", configurator.getConfig());
        return "index";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("logger") String logger, Model model) {
        model.addAttribute("config", configurator.getConfig());
        model.addAttribute("logItem", LogItem.builder()
            .logger(logger)
            .build());
        return "edit";
    }

    @GetMapping("/config")
    public String config(Model model) {
        model.addAttribute("hostname", configurator.getHostname());
        model.addAttribute("config", Config.builder()
            .scheme(configurator.getConfig().getScheme())
            .host(configurator.getConfig().getHost())
            .port(configurator.getConfig().getPort())
            .build());
        return "config";
    }
}
