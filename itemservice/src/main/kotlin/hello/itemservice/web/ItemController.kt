package hello.itemservice.web

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemSearchCond
import hello.itemservice.repository.ItemUpdateDto
import hello.itemservice.service.ItemService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/items")
class ItemController(private val itemService: ItemService) {
    @GetMapping
    fun items(@ModelAttribute("itemSearch") itemSearch: ItemSearchCond, model: Model): String {
        val items: List<Item> = itemService.findItems(itemSearch)
        model.addAttribute("items", items)
        return "items"
    }

    @GetMapping("/{itemId}")
    fun item(@PathVariable itemId: Long, model: Model): String {
        val item: Item? = itemService.findById(itemId)
        model.addAttribute("item", item)
        return "item"
    }

    @GetMapping("/add")
    fun addForm(): String {
        return "addForm"
    }

    @PostMapping("/add")
    fun addItem(@ModelAttribute item: Item, redirectAttributes: RedirectAttributes): String {
        val savedItem: Item = itemService.save(item)
        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)
        return "redirect:/items/{itemId}"
    }

    @GetMapping("/{itemId}/edit")
    fun editForm(@PathVariable itemId: Long, model: Model): String {
        val item: Item? = itemService.findById(itemId)
        model.addAttribute("item", item)
        return "editForm"
    }

    @PostMapping("/{itemId}/edit")
    fun edit(@PathVariable itemId: Long, @ModelAttribute updateParam: ItemUpdateDto): String {
        itemService.update(itemId, updateParam)
        return "redirect:/items/{itemId}"
    }
}
