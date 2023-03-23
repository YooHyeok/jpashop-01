package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderConroller {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    /**
     * 상품 주문
     */
    @GetMapping("order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMember();
        List<Item> items = itemService.findItems();
        model.addAttribute("members", members);
        model.addAttribute("items", items);
        return "order/orderForm";
    }

    @PostMapping("order")
    public String order(Long memberId, Long itemId, int count) {
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    /**
     * 주문 목록
     */
    @GetMapping("orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) { //@ModelAttribute는 기본적으로 Model의 기능을 함께 한다. 생략도 가능하다.
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "/order/orderList";
    }

    @PostMapping("orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
