package org.softuni.residentevil.web.controllers.user;

import org.softuni.residentevil.config.WebConfig;
import org.softuni.residentevil.web.annotations.Layout;
import org.softuni.residentevil.web.controllers.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Layout
@Controller
@RequestMapping(WebConfig.URL_USER_HOME)
public class HomeUserController extends BaseController {

    private static final String VIEW_HOME = "user/home";

    @GetMapping
    public String get() {
        return VIEW_HOME;
    }
}
