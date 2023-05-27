import React, { useState } from "react";
import ProdutoServico from "../dto/servico/ProdutoServico";
import TecnicoServico from "../dto/servico/TecnicoServico";
import FornecedorServico from "../dto/servico/FornecedorServico";
import { ServicoContext } from "./IContextServico";

type ContextServicoProps = {
  children?: React.ReactNode;
};

export const ContextServico = (props: ContextServicoProps) => {
  const [ProdutoServico, setProdutoServico] = useState<ProdutoServico[]>([]);
  const [TecnicoServico, setTecnicoServico] = useState<TecnicoServico[]>([]);
  const [fornecedorServico, setFornecedorServico] = useState<
    FornecedorServico[]
  >([]);

  return (
    <ServicoContext.Provider
      value={{
        ProdutoServico,
        setProdutoServico,
        TecnicoServico,
        setTecnicoServico,
        fornecedorServico,
        setFornecedorServico,
      }}
    >
      {props.children}
    </ServicoContext.Provider>
  );
};
