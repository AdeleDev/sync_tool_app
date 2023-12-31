package scorewarrior.syncService.controller

import kotlinx.html.*
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class IndexController {
    @GetMapping("/")
    fun getIndex(): String {
        return "index.html"
    }
}