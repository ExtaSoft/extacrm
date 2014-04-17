package ru.extas.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * @author Valery Orlov
 *         Date: 14.04.2014
 *         Time: 13:26
 */
@Controller
public class RootController {

    @RequestMapping("/")
    public String uiRedirect() throws IOException {
        return "redirect:/ui";
    }
}
