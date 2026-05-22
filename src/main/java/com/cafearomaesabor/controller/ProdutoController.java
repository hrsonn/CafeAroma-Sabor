package com.cafearomaesabor.controller;

import com.cafearomaesabor.model.Produto;
import com.cafearomaesabor.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public String listarProdutos(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
        return "produto/listagem";
    }

    @GetMapping("/cadastrar")
    public String exibirFormulario(Model model) {
        model.addAttribute("produto", new Produto());
        return "produto/form-inserir";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Produto produto, BindingResult result) {
        if (result.hasErrors()) {
            return "produto/form-inserir";
        }
        produtoService.salvar(produto);
        return "redirect:/produto";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return "redirect:/produto";
    }

}
