import FornecedorServico from "../FornecedorServico"
import ProdutoServico from "../ProdutoServico"
import TecnicoServico from "../TecnicoServico"

type UpdateServicoRequest = {
    id: number,
    clienteId: number,
    descricao: string,
    statusServico: string,
    dtInicial: string,
    dtFinalizacao: string,
    listaProduto: ProdutoServico[],
    listaFornecedor: FornecedorServico[],
    listaTecnico: TecnicoServico[]
}

export default UpdateServicoRequest;