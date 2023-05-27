import { createContext } from "react";
import FornecedorServico from "../dto/servico/FornecedorServico";
import ProdutoServico from "../dto/servico/ProdutoServico";
import TecnicoServico from "../dto/servico/TecnicoServico";

interface IContextServico {
    ProdutoServico: ProdutoServico[];
    setProdutoServico: React.Dispatch<React.SetStateAction<ProdutoServico[]>>;
    TecnicoServico: TecnicoServico[];
    setTecnicoServico: React.Dispatch<React.SetStateAction<TecnicoServico[]>>;
    fornecedorServico: FornecedorServico[];
    setFornecedorServico: React.Dispatch<React.SetStateAction<FornecedorServico[]>>;
  }
  export const ServicoContext = createContext<IContextServico>({} as IContextServico);