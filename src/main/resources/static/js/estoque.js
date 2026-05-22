const apiUrl = '/api/produtos';
const form = document.getElementById('produto-form');
const messageElement = document.getElementById('message');
const tableBody = document.querySelector('#produtos-table tbody');
const resetButton = document.getElementById('reset-button');

function mostrarMensagem(text, type = 'success') {
    messageElement.textContent = text;
    messageElement.className = `message active ${type}`;
    setTimeout(() => {
        messageElement.className = 'message';
    }, 4000);
}

function carregarProdutos() {
    fetch(apiUrl)
        .then(response => response.json())
        .then(produtos => {
            tableBody.innerHTML = '';
            produtos.forEach(produto => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${produto.id}</td>
                    <td>${produto.nome}</td>
                    <td>${produto.descricao || '-'}</td>
                    <td>R$ ${Number(produto.preco).toFixed(2).replace('.', ',')}</td>
                    <td>${produto.quantidade}</td>
                    <td>
                        <button class="button button-secondary" type="button" onclick="editarProduto(${produto.id})">Editar</button>
                        <button class="button" type="button" onclick="deletarProduto(${produto.id})">Excluir</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(() => mostrarMensagem('Não foi possível carregar os produtos.', 'error'));
}

function editarProduto(id) {
    fetch(`${apiUrl}/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Produto não encontrado');
            }
            return response.json();
        })
        .then(produto => {
            document.getElementById('produtoId').value = produto.id;
            document.getElementById('nome').value = produto.nome;
            document.getElementById('descricao').value = produto.descricao;
            document.getElementById('preco').value = produto.preco;
            document.getElementById('quantidade').value = produto.quantidade;
        })
        .catch(() => mostrarMensagem('Falha ao buscar produto para edição.', 'error'));
}

function deletarProduto(id) {
    if (!confirm('Deseja realmente excluir este produto?')) {
        return;
    }

    fetch(`${apiUrl}/${id}`, { method: 'DELETE' })
        .then(response => {
            if (response.ok) {
                mostrarMensagem('Produto excluído com sucesso.', 'success');
                carregarProdutos();
            } else {
                throw new Error('Falha ao excluir');
            }
        })
        .catch(() => mostrarMensagem('Não foi possível excluir o produto.', 'error'));
}

function resetForm() {
    form.reset();
    document.getElementById('produtoId').value = '';
}

form.addEventListener('submit', (event) => {
    event.preventDefault();
    const id = document.getElementById('produtoId').value;
    const produto = {
        nome: document.getElementById('nome').value.trim(),
        descricao: document.getElementById('descricao').value.trim(),
        preco: parseFloat(document.getElementById('preco').value),
        quantidade: parseInt(document.getElementById('quantidade').value, 10)
    };

    if (!produto.nome || produto.preco < 0 || produto.quantidade < 0) {
        mostrarMensagem('Preencha os campos obrigatórios corretamente.', 'error');
        return;
    }

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${apiUrl}/${id}` : apiUrl;

    fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(produto)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => Promise.reject(data));
            }
            return response.json();
        })
        .then(() => {
            mostrarMensagem(`Produto ${id ? 'atualizado' : 'cadastrado'} com sucesso.`, 'success');
            carregarProdutos();
            resetForm();
        })
        .catch(error => {
            const message = error?.error || Object.values(error || {}).join(', ') || 'Erro ao salvar o produto.';
            mostrarMensagem(message, 'error');
        });
});

resetButton.addEventListener('click', resetForm);
window.addEventListener('load', carregarProdutos);
