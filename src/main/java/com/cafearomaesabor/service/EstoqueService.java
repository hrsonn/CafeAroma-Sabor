package com.cafearomaesabor.service;

import com.cafearomaesabor.model.MovimentacaoEstoque;
import com.cafearomaesabor.model.Produto;
import com.cafearomaesabor.repository.MovimentacaoEstoqueRepository;
import com.cafearomaesabor.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueService(MovimentacaoEstoqueRepository movimentacaoRepository,
                          ProdutoRepository produtoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<MovimentacaoEstoque> listarMovimentacoes() {
        return movimentacaoRepository.findAllByOrderByDataMovimentacaoDesc();
    }

    @Transactional
    public MovimentacaoEstoque registrarMovimentacao(MovimentacaoEstoque movimentacao) {
        Produto produto = movimentacao.getProduto();
        String tipo = movimentacao.getTipoMovimentacao();
        int quantidade = movimentacao.getQuantidade();

        atualizarEstoque(produto, quantidade, tipo);

        movimentacao.setDataMovimentacao(LocalDate.now());
        return movimentacaoRepository.save(movimentacao);
    }

    public void atualizarEstoque(Produto produto, int quantidade, String tipo) {
        Produto produtoGerenciado = produtoRepository.findById(produto.getId())
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado"));

        if ("ENTRADA".equalsIgnoreCase(tipo)) {
            produtoGerenciado.setQuantidadeEstoque(
                    produtoGerenciado.getQuantidadeEstoque() + quantidade);
        } else if ("SAIDA".equalsIgnoreCase(tipo)) {
            int estoqueAtual = produtoGerenciado.getQuantidadeEstoque();
            if (estoqueAtual < quantidade) {
                throw new RuntimeException("Estoque insuficiente para saida");
            }
            produtoGerenciado.setQuantidadeEstoque(estoqueAtual - quantidade);
        }

        produtoRepository.save(produtoGerenciado);
    }

    public long contarMovimentacoes() {
        return movimentacaoRepository.count();
    }

}
