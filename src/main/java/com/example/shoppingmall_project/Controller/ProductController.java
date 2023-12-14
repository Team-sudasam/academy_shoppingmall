package com.example.shoppingmall_project.Controller;

import com.example.shoppingmall_project.model.vo.InquiriesVO;
import com.example.shoppingmall_project.service.InquiriesService;
import com.example.shoppingmall_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService ps;
    @Autowired
    private InquiriesService is;


    @GetMapping("/homeProduct")

    public void productHome(Model model) {
        model.addAttribute("list", ps.getProduct()); //product 정보
        model.addAttribute("review", is.getInquiries()); //리뷰
        model.addAttribute("num", ps.getCount());	// 사이즈
        model.addAttribute("color", ps.color());	// 색상
        model.addAttribute("img", ps.getimg());	//이미지
    }

    @PostMapping("/homeProduct")
    public String write(InquiriesVO input) {
        is.writeinquiries(input);

        return "redirect:/product/homeProduct";
    }

}