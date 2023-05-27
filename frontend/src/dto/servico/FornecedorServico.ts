import Fornecedor from "../fornecedor/Fornecedor"

type FornecedorServico = {
    id: number,
    fornecedor: Fornecedor,
    dtCriacao?: string,
    descricao: string
}

export default FornecedorServico;