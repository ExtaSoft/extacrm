package ru.extas.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * <p>RootController class.</p>
 *
 * @author Valery Orlov
 *         Date: 14.04.2014
 *         Time: 13:26
 * @version $Id: $Id
 * @since 0.4.2
 */
@Controller
public class RootController {

    /**
     * <p>uiRedirect.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    @RequestMapping("/")
    public String uiRedirect() throws IOException {
        return "redirect:/ui";
    }
}
