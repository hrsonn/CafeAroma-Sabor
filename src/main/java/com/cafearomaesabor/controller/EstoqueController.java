package com.cafearomaesabor.controller;

import com.cafearomaesabor.model.MovimentacaoEstoque;
import com.cafearomaesabor.service.EstoqueService;
import com.cafearomaesabor.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;
    private final ProdutoService produtoService;

    public EstoqueController(EstoqueService estoqueService, ProdutoService produtoService) {
        this.estoqueService = estoqueService;
        this.produtoService = produtoService;
    }

    @GetMapping
    public String listarMovimentacoes(Model model) {
        model.addAttribute("movimentacoes", estoqueService.listarMovimentacoes());
        model.addAttribute("produtos", produtoService.listarTodos());
        model.addAttribute("movimentacao", new MovimentacaoEstoque());
        return "estoque/movimentacao";
    }

    @PostMapping("/movimentar")
    public String registrarMovimentacao(@Valid MovimentacaoEstoque movimentacao,
                                        BindingResult result,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("movimentacoes", estoqueService.listarMovimentacoes());
            model.addAttribute("produtos", produtoService.listarTodos());
            return "estoque/movimentacao";
        }
        try {
            estoqueService.registrarMovimentacao(movimentacao);
            redirectAttributes.addFlashAttribute("mensagem", "Movimentacao registrada com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/estoque";
    }

}
