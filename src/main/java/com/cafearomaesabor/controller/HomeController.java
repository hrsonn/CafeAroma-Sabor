package com.cafearomaesabor.controller;

import com.cafearomaesabor.service.EstoqueService;
import com.cafearomaesabor.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    public HomeController(ProdutoService produtoService, EstoqueService estoqueService) {
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("totalProdutos", produtoService.contarProdutos());
        model.addAttribute("totalMovimentacoes", estoqueService.contarMovimentacoes());
        return "home";
    }

}
