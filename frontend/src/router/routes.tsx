import { RouteObject } from "react-router-dom";
import App from '../App';
import { LoginPage } from "../pages/LoginPage";
import { MenuPage } from "../pages/MenuPage";
import { CadastroClientePage } from "../pages/cadastro/CadastroClientePage";
import { GerenciaProdutosPage } from "../pages/listagem/GerenciaProdutosPage";
import { CadastroProdutoPage } from "../pages/cadastro/CadastroProdutoPage";
import { CadastroFuncionarioPage } from "../pages/cadastro/CadastroFuncionarioPage";
import { PaginaTeste } from "../pages/PaginaTeste";
import { CadastroTecnicoPage } from "../pages/cadastro/CadastroTecnicoPage";
import { NotFoundPage } from "../pages/NotFoundPage";
import { AtualizarProdutoPage } from "../pages/atualizar/AtualizarProdutoPage";
import { GerenciaClientePage } from "../pages/listagem/GerenciaClientePage";
import { AtualizaClientePage } from "../pages/atualizar/AtualizaClientePage";
import { GerenciaTecnicoPage } from "../pages/listagem/GerenciaTecnicoPage";
import { AtualizarTecnicoPage } from "../pages/atualizar/AtualizarTecnicoPage";
import { CadastroFornecedorPage } from "../pages/cadastro/CadastroFornecedorPage";
import { GerenciaFornecedorPage } from "../pages/listagem/GerenciaFornecedorPage";
import { AtualizarFornecedorPage } from "../pages/atualizar/AtualizarFornecedorPage";
import { CadastroServicoPage } from "../pages/cadastro/CadastroServicoPage";
import { GerenciaServicoPage } from "../pages/listagem/GerenciaServicoPage";

const routesArray: RouteObject[] = [
    {
      path: "/",
      element: <App />,
      children: [
        {
          path: "login",
          element: <LoginPage />
        },
        {
          path: "menu",
          element: <MenuPage />
        },
        {
          path: "cadastro-cliente",
          element: <CadastroClientePage />
        },
        {
          path: "gerencia-produto",
          element: <GerenciaProdutosPage />
        },
        {
          path: "cadastro-produto",
          element: <CadastroProdutoPage />
        },
        {
          path: "cadastro-funcionario",
          element: <CadastroFuncionarioPage />
        },
        {
          path: "pagina-teste",
          element: <PaginaTeste  />
        },
        {
          path: "cadastro-tecnico",
          element: <CadastroTecnicoPage />
        },
        {
          path: "atualiza-produto",
          element: <AtualizarProdutoPage />
        },
        {
          path: "gerencia-cliente",
          element: <GerenciaClientePage />
        },
        {
          path: "atualiza-cliente",
          element: <AtualizaClientePage />
        },
        {
          path: "gerencia-tecnico",
          element: <GerenciaTecnicoPage />
        },
        {
          path: "atualiza-tecnico",
          element: <AtualizarTecnicoPage />
        },
        {
          path: "cadastro-fornecedor",
          element: <CadastroFornecedorPage />
        },
        {
          path: "gerencia-fornecedor",
          element: <GerenciaFornecedorPage />
        },
        {
          path: "atualiza-fornecedor",
          element: <AtualizarFornecedorPage />
        },
        {
          path: "cadastro-servico",
          element: <CadastroServicoPage />
        },
        {
          path: "gerencia-servico",
          element: <GerenciaServicoPage />
        },
        {
          path: "*",
          element: <NotFoundPage />
        }
      ]
    }
  ]


  export default routesArray;