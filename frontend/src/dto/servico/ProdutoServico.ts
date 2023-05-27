import Produto from "../produto/Produto"

type ProdutoServico = {
    id: number,
    produto: Produto | null,
    descricao: string,
    precoProduto: number,
    quantidade: number,
}

export default ProdutoServico;