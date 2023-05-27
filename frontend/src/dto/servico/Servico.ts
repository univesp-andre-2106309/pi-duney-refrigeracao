import Cliente from "../cliente/Cliente"
import FornecedorServico from "./FornecedorServico"
import ProdutoServico from "./ProdutoServico"
import TecnicoServico from "./TecnicoServico"

type Servico = {
    id: number,
    descricao: string,
    statusServico: string,
    dtInicial: string,
    dtFinalizacao: string,
    dtCriacao: string,
    cliente: Cliente,
    listaProduto: ProdutoServico[],
    listaTecnico: TecnicoServico[],
    listaFornecedor: FornecedorServico[]

}

export default Servico;