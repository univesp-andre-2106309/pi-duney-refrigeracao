import { ContextServico } from "../../context/ContextServico";
import { ServicoForm } from "../../components/forms/ServicoForm";
import { useLocation, useSearchParams } from "react-router-dom";

type ContextServico = {};

export const CadastroServicoPage = () => {
  let [searchParams] = useSearchParams();

  let id = searchParams.get("id");

  return (
    <ContextServico>
      {id && <ServicoForm id={id} />}
      {!id && <ServicoForm />}
    </ContextServico>
  );
};
