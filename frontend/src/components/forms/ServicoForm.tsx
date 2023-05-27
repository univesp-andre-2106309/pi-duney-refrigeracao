import {
  Button,
  Col,
  Container,
  FloatingLabel,
  Row,
  Form,
  Tabs,
  Tab,
  Card,
} from "react-bootstrap";
import styles from "./ServicoForm.module.css";
import { Controller, useForm } from "react-hook-form";
import StatusServico from "../../enum/StatusServico";
import { useContext, useEffect, useState } from "react";
import Cliente from "../../dto/cliente/Cliente";
import { ServicoProdutoForm } from "../../components/forms/ServicoProdutoForm";
import { ServicoFornecedorForm } from "../../components/forms/ServicoFornecedorForm";
import { ServicoTecnicoForm } from "../../components/forms/ServicoTecnicoForm";

import { ServicoContext } from "../../context/IContextServico";
import moment from "moment";
import { ToastManager } from "../../toast/ToastManager";
import { useNavigate } from "react-router-dom";
import { ToastManagerLevel } from "../../toast/ToastManagerLevel";
import CreateServicoRequest from "../../dto/servico/request/CreateServicoRequest";
import Servico from "../../dto/servico/Servico";
import ProdutoServico from "../../dto/servico/ProdutoServico";
import TecnicoServico from "../../dto/servico/TecnicoServico";
import FornecedorServico from "../../dto/servico/FornecedorServico";
import UpdateServicoRequest from "../../dto/servico/request/UpdateServicoRequest";
import { useLoginRefresh } from "../../hooks/useLoginRefresh";

type ServicoFormProps = {
  id?: string;
};

export const ServicoForm = (props: ServicoFormProps) => {
  const { register, handleSubmit, control, setValue } = useForm();
  const [clienteList, setClienteList] = useState<Cliente[]>([]);
  const [updateForm, setUpdateForm] = useState<boolean>(false);
  const [authToken, callLoginStatus] = useLoginRefresh();

  const [savedServProduto, setSavedServProduto] = useState<ProdutoServico[]>(
    []
  );
  const [savedServTecnico, setSavedServTecnico] = useState<TecnicoServico[]>(
    []
  );
  const [savedServFornecedor, setSavedServFornecedor] = useState<
    FornecedorServico[]
  >([]);

  const navigate = useNavigate();

  const arStatusServico = [
    StatusServico[StatusServico.CONCLUIDO],
    StatusServico[StatusServico.FILA_DE_ESPERA],
    StatusServico[StatusServico.MANUTENCAO],
    StatusServico[StatusServico.CANCELADO],
  ];

  const { ProdutoServico, TecnicoServico, fornecedorServico } =
    useContext(ServicoContext);
  let toastManager = new ToastManager();

  useEffect(() => {

    callLoginStatus();
    let token = localStorage.getItem("jwtToken")
      ? localStorage.getItem("jwtToken")
      : "";
    let refreshToken = localStorage.getItem("refreshToken")
      ? localStorage.getItem("refreshToken")
      : "";

    fetch(`http://localhost:8080/api/cliente/search?documento=&nome=&index=0`, {
      headers: {
        Authorization: token!,
        RefreshToken: refreshToken!,
      },
    }).then(async (response) => {
      let result = await response.json();

      if (response.ok) {
        let listaCliente: Cliente[] = result.result;
        setClienteList(listaCliente);
      }
      if (props.id) {
        setUpdateForm(true);
      }
    });
  }, []);

  useEffect(() => {
    if (updateForm) {
      callLoginStatus();
      let token = localStorage.getItem("jwtToken")
        ? localStorage.getItem("jwtToken")
        : "";
      let refreshToken = localStorage.getItem("refreshToken")
        ? localStorage.getItem("refreshToken")
        : "";

      fetch(`http://localhost:8080/api/servico/find?id=${props.id}`, {
        headers: {
          Authorization: token!,
          RefreshToken: refreshToken!,
        },
      }).then(async (response) => {
        let result = await response.json();
        if (response.ok) {
          const value: Servico = result.result;
          const dtInicialFormat = moment(value.dtInicial, "DD/MM/YYYY").format(
            "yyyy-MM-DD"
          );
          const dtFinalFormat = moment(
            value.dtFinalizacao,
            "DD/MM/YYYY"
          ).format("yyyy-MM-DD");

          const dtCriacaoFormat = moment(value.dtCriacao, "DD/MM/YYYY").format(
            "yyyy-MM-DD"
          );

          setValue("id", value.id);
          setValue("info", value.descricao);
          setValue("status", value.statusServico);
          setValue("dtInicial", dtInicialFormat);
          setValue("dtFinalizacao", dtFinalFormat);
          setValue("dtCriacao", dtCriacaoFormat);
          setValue("cliente", value.cliente?.id);
          setValue("listaProduto", value.listaProduto);
          setSavedServProduto(value.listaProduto);
          setValue("listaTecnico", value.listaTecnico);
          setSavedServTecnico(value.listaTecnico);
          setValue("listaFornecedor", value.listaFornecedor);
          setSavedServFornecedor(value.listaFornecedor);

          return;
        }

        navigate("/gerencia-produto");
        return;
      });
    }
  }, [updateForm]);

  const submitForm = handleSubmit((action) => {
    const status: string = action["status"];
    const clienteId: string = action["cliente"];
    const descricao: string = action["info"];

    const dtInicial: string = action["dtInicial"];
    const dtFinalizacao: string = action["dtFinalizacao"];

    const cliente = clienteList?.find(
      (item) => item.id.toString() === clienteId
    );

    if (cliente) {
      callLoginStatus();
      let token = localStorage.getItem("jwtToken")
        ? localStorage.getItem("jwtToken")
        : "";
      let refreshToken = localStorage.getItem("refreshToken")
        ? localStorage.getItem("refreshToken")
        : "";

      let url: string;
      let method: string;
      let body: CreateServicoRequest | UpdateServicoRequest;

      if (updateForm) {
        url = "http://localhost:8080/api/servico/update";
        method = "PUT";
        body = {
          id: action["id"],
          clienteId: cliente.id,
          descricao,
          statusServico: status,
          dtInicial: moment(dtInicial).format("DD/MM/YYYY"),
          dtFinalizacao: moment(dtFinalizacao).format("DD/MM/YYYY"),
          listaProduto: ProdutoServico,
          listaFornecedor: fornecedorServico,
          listaTecnico: TecnicoServico,
        };
      } else {
        url = "http://localhost:8080/api/servico/create";
        method = "POST";
        body = {
          clienteId: cliente.id,
          descricao,
          statusServico: status,
          dtInicial: moment(dtInicial).format("DD/MM/YYYY"),
          dtFinalizacao: moment(dtFinalizacao).format("DD/MM/YYYY"),
          listaProduto: ProdutoServico,
          listaFornecedor: fornecedorServico,
          listaTecnico: TecnicoServico,
        };
      }

      fetch(url, {
        headers: {
          Authorization: token!,
          RefreshToken: refreshToken!,
          "Content-Type": "application/json",
        },
        method: method,
        body: JSON.stringify(body),
      })
        .then(async (response) => {
          let result = await response.json();
          if (response.ok) {
            toastManager.sendMessage(
              "Serviço cadastrado com sucesso!",
              "Cadastro de serviço",
              ToastManagerLevel.SUCCESS
            );
            navigate("/gerencia-servico");
            return;
          }
          toastManager.sendMessage(
            result.message,
            result.title,
            ToastManagerLevel.ERROR
          );
        })
        .catch((er) => {
          toastManager.sendMessage(
            "Ocorreu um erro ao salvar o serviço!",
            "Erro genérico",
            ToastManagerLevel.ERROR
          );
        });
    } else {
      toastManager.sendMessage(
        "É necessario selecionar um cliente válido para poder salvar os dados!",
        "Cadastro de serviço",
        ToastManagerLevel.WARN
      );
    }
  });

  const cancelForm = () => {
    navigate("/gerencia-servico");
  };

  return (
    <Container className={styles["container"]}>
      <Card className="mt-3 mb-5">
        <Card.Header as="h5" className={styles["card-header"]}>
          {!updateForm && <p>CADASTRO</p>}
          {updateForm && <p>ATUALIZAR CADASTRO</p>}
        </Card.Header>
        <Card.Body>
          <Form onSubmit={submitForm}>
            {updateForm && (
              <Row>
                <Col xs={3}>
                  <Form.Group
                    className="mb-3"
                    controlId="exampleForm.ControlInput1"
                  >
                    <FloatingLabel
                      controlId="idFloatInput"
                      label="ID"
                      className="mb-3"
                    >
                      <Form.Control
                        type="text"
                        {...register("id")}
                        disabled
                        readOnly
                      />
                    </FloatingLabel>
                  </Form.Group>
                </Col>
              </Row>
            )}

            <Row>
              <Col xs={5}>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlInput1"
                >
                  <FloatingLabel
                    controlId="statusFloatInput"
                    label="Status"
                    className="mb-3"
                  >
                    <Controller
                      name="status"
                      control={control}
                      render={({ field }) => (
                        <Form.Select
                          aria-label="Default select example"
                          {...field}
                          required
                          defaultValue={
                            arStatusServico.length > 0 ? arStatusServico[0] : ""
                          }
                          onChange={(e) => setValue("status", e.target.value)}
                        >
                          {arStatusServico.map((item, index) => (
                            <option key={index} value={item}>
                              {item}
                            </option>
                          ))}
                        </Form.Select>
                      )}
                    />
                  </FloatingLabel>
                </Form.Group>
              </Col>
              <Col xs={5}>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlInput1"
                >
                  <FloatingLabel
                    controlId="clienteFloatInput"
                    defaultValue={
                      clienteList && clienteList?.length > 0
                        ? clienteList[0].id
                        : ""
                    }
                    label="Cliente"
                    className="mb-3"
                  >
                    <Controller
                      name="cliente"
                      control={control}
                      render={({ field }) => (
                        <Form.Select
                          aria-label="Default select example"
                          {...field}
                          required
                          onChange={(e) => setValue("cliente", e.target.value)}
                        >
                          {clienteList?.map((item, index) => {
                            return (
                              <option key={index} value={item.id}>
                                {`${item.nome} - ${item.documento}`}
                              </option>
                            );
                          })}
                        </Form.Select>
                      )}
                    />
                  </FloatingLabel>
                </Form.Group>
              </Col>
            </Row>

            <Row>
              <Col>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlInput1"
                >
                  <FloatingLabel
                    controlId="dtInicioFloatInput"
                    label="data inicio"
                    className="mb-3"
                  >
                    <Form.Control type="date" {...register("dtInicial")} />
                  </FloatingLabel>
                </Form.Group>
              </Col>

              <Col>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlInput1"
                >
                  <FloatingLabel
                    controlId="dtFinalizacaoFloatInput"
                    label="data fim"
                    className="mb-3"
                  >
                    <Form.Control type="date" {...register("dtFinalizacao")} />
                  </FloatingLabel>
                </Form.Group>
              </Col>
            </Row>
            <Row>
              <Col>
                <Form.Group className="mb-3" controlId="extraInfoGroup">
                  <Form.Label>Descrição</Form.Label>
                  <Form.Control as="textarea" rows={3} {...register("info")} />
                </Form.Group>
              </Col>
            </Row>
            <Row className="mt-5">
              <Col>
                <Button variant="outline-danger" size="lg" onClick={cancelForm}>
                  Cancelar
                </Button>
              </Col>
              <Col>
                <Button variant="outline-success" size="lg" type="submit">
                  Salvar
                </Button>
              </Col>
            </Row>
          </Form>
        </Card.Body>
      </Card>

      <Row>
        <Tabs
          id="controlled-tab-example"
          defaultActiveKey="produto"
          className="mb-3"
        >
          <Tab eventKey="produto" title="Produto">
            {savedServProduto && savedServProduto.length > 0 && (
              <ServicoProdutoForm produtosSalvos={savedServProduto} />
            )}
            {(!savedServProduto || savedServProduto?.length === 0) && (
              <ServicoProdutoForm />
            )}
          </Tab>
          <Tab eventKey="tecnico" title="Tecnico">
            {savedServTecnico && savedServTecnico.length > 0 && (
              <ServicoTecnicoForm tecnicosSalvos={savedServTecnico} />
            )}
            {(!savedServTecnico || savedServTecnico?.length === 0) && (
              <ServicoTecnicoForm />
            )}
          </Tab>
          <Tab eventKey="fornecedor" title="Fornecedor">
            {savedServFornecedor && savedServFornecedor.length > 0 && (
              <ServicoFornecedorForm fornecedoresSalvos={savedServFornecedor} />
            )}
            {(!savedServFornecedor || savedServFornecedor?.length === 0) && (
              <ServicoFornecedorForm />
            )}
          </Tab>
        </Tabs>
      </Row>
    </Container>
  );
};
