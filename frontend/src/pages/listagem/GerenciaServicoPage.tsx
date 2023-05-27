import Container from "react-bootstrap/Container";
import styles from "./GerenciaServicoPage.module.css";
import InputGroup from "react-bootstrap/InputGroup";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Table from "react-bootstrap/Table";
import { Controller, useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import { v4 as uuidv4 } from "uuid";
import { useNavigate } from "react-router-dom";
import { Col, FloatingLabel, Pagination, Row } from "react-bootstrap";
import Servico from "../../dto/servico/Servico";
import moment from "moment";
import { ToastManager } from "../../toast/ToastManager";
import { ToastManagerLevel } from "../../toast/ToastManagerLevel";
import { useLoginRefresh } from "../../hooks/useLoginRefresh";

type SearchParams = {
  id: number;
  index: number;
  dtInicio: string;
  dtFim: string;
  statusServico: string | null;
};

const arrayStatus = ["CONCLUIDO", "FILA_DE_ESPERA", "MANUTENCAO", "CANCELADO"];

export const GerenciaServicoPage = () => {
  const tabelaQtdPagina: number = 10;

  const [listaTabela, setListaTabela] = useState<Servico[]>([]);
  const [searchParams, setSearchParams] = useState<SearchParams | null>(null);

  const [authToken, callLoginStatus] = useLoginRefresh();

  const [qtdPagina, setQtdPagina] = useState(0);
  const [pageIndex, setPageIndex] = useState(0);
  const [showParams, setShowParams] = useState(false);

  const navigate = useNavigate();
  const toast = new ToastManager();

  const { register, handleSubmit, control, setValue } = useForm();

  useEffect(() => {
    setValue("statusServico", arrayStatus[0]);
  }, []);

  useEffect(() => {
    if (searchParams) {
      callLoginStatus();
      let token = localStorage.getItem("jwtToken")
        ? localStorage.getItem("jwtToken")
        : "";
      let refreshToken = localStorage.getItem("refreshToken")
        ? localStorage.getItem("refreshToken")
        : "";

      let buscaTipo: boolean = searchParams.id > 0;
      let apiUrl: string = "";
      let dtInicialFormat: string;
      let dtFinalFormat: string;

      if (showParams) {
        if(!searchParams?.dtInicio || !searchParams?.dtFim) {
          toast.sendMessage(
            "Campos de Data possui valores invalidos!",
            "Busca de serviço",
            ToastManagerLevel.WARN
          );
          return;
        }


        dtInicialFormat = moment(searchParams?.dtInicio).format("DD/MM/YYYY");
        dtFinalFormat = moment(searchParams?.dtFim).format("DD/MM/YYYY");

        apiUrl = `http://localhost:8080/api/servico/search?index=${searchParams.index}&dtInicio=${dtInicialFormat}&dtFim=${dtFinalFormat}&order=&statusServico=${searchParams.statusServico}`;
      } else {
        apiUrl = `http://localhost:8080/api/servico/search?index=${searchParams.index}`;
      }

      fetch(
        buscaTipo
          ? `http://localhost:8080/api/servico/find?id=${searchParams.id}`
          : apiUrl,
        {
          headers: {
            Authorization: token!,
            RefreshToken: refreshToken!,
          },
        }
      )
        .then(async (response) => {
          let result = await response.json();

          switch (response.status) {
            case 200:
              let listaServico: Servico[];
              if (buscaTipo) {
                setQtdPagina(1);
                setPageIndex(0);
                listaServico = [result.result];
              } else {
                setQtdPagina(Math.ceil(result.maxResultSize / tabelaQtdPagina));
                setPageIndex(searchParams.index);
                listaServico = result.result;
              }
              setListaTabela(listaServico);
              return;
            case 400:
              setQtdPagina(0);
              setPageIndex(0);
              setListaTabela([]);
              toast.sendMessage(
                result.message,
                result.title,
                ToastManagerLevel.WARN
              );
              return;
          }
        })
        .catch((er) => {
          console.log(er.message);
        });
    }
  }, [searchParams]);

  const submitForm = handleSubmit(async (action) => {
    const idBusca: number = action["idBusca"] ? action["idBusca"] : 0;
    const dtInicio: string = action["dtMin"] ? action["dtMin"] : null;
    const dtFim: string = action["dtMax"] ? action["dtMax"] : null;
    const statusServico: string = action["statusServico"]
      ? action["statusServico"]
      : null;

    setSearchParams({
      id: idBusca,
      index: 0,
      dtInicio,
      dtFim,
      statusServico,
    });
  });

  const editItem = (id: number) => {
    navigate(`/cadastro-servico?id=${id}`);
  };

  const changeIndex = (value: number) => {
    setPageIndex(value);

    setSearchParams({
      index: value,
      id: 0,
      dtInicio: searchParams?.dtInicio ? searchParams.dtInicio : "",
      dtFim: searchParams?.dtFim ? searchParams.dtFim : "",
      statusServico: searchParams?.statusServico
        ? searchParams.statusServico
        : "",
    });
  };

  return (
    <div>
      <Container>
        <Form className={styles["form-container"]} onSubmit={submitForm}>
          <Row>
            <InputGroup className="mb-3">
              <InputGroup.Text id="basic-addon1">ID</InputGroup.Text>
              <Form.Control
                placeholder="Buscar"
                aria-label="servico"
                aria-describedby="pesquisa-servico-nome"
                {...register("idBusca")}
              />
            </InputGroup>
          </Row>

          <Container className={styles["params-extra-container"]}>
            <Row>
              <Col>
                <Form.Check
                  type="switch"
                  id="switch-params"
                  label="Pesquisa por parametros adicionais?"
                  className={styles["params-extra-checkbox"]}
                  {...register("precoCheckbox")}
                  onChange={(e) => {
                    setShowParams(e.target.checked);
                  }}
                />
              </Col>
            </Row>

            {showParams && (
              <div>
                <Row>
                  <Col xs={{ offset: 6 }}>
                    <Form.Group
                      className="mb-3"
                      controlId="exampleForm.ControlInput1"
                    >
                      <FloatingLabel
                        controlId="estadoFloatInput"
                        label="statusServico"
                        className="mb-3"
                      >
                        <Controller
                          name="statusServico"
                          control={control}
                          render={({ field }) => (
                            <Form.Select
                              aria-label="Default select example"
                              {...field}
                              required
                              onChange={(e) => {
                                setValue("statusServico", e.target.value);
                              }}
                            >
                              {arrayStatus.map((item, index) => (
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
                </Row>
                <Row>
                  <Col xs={{ span: 6 }}>
                    <Form.Group className="mb-3" controlId="formDtMin">
                      <Form.Label>Inicio</Form.Label>
                      <Form.Control type="date" {...register("dtMin")} />
                    </Form.Group>
                  </Col>
                  <Col xs={{ span: 6 }}>
                    <Form.Group className="mb-3" controlId="formDtMax">
                      <Form.Label>Fim</Form.Label>
                      <Form.Control type="date" {...register("dtMax")} />
                    </Form.Group>
                  </Col>
                </Row>
              </div>
            )}
          </Container>

          <Button variant="success" type="submit">
            Buscar
          </Button>
        </Form>

        {listaTabela.length > 0 && (
          <Container>
            <Pagination>
              {Array.from({ length: qtdPagina }).map((_, index) => (
                <Pagination.Item
                  key={index}
                  active={index === pageIndex}
                  onClick={() => changeIndex(index)}
                >
                  {index + 1}
                </Pagination.Item>
              ))}
            </Pagination>
            <Table striped bordered hover size="sm">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Status</th>
                  <th>Cliente</th>
                  <th>Data</th>
                  <th>#</th>
                </tr>
              </thead>
              <tbody>
                {listaTabela.map((item) => (
                  <tr key={uuidv4()}>
                    <td>{item.id}</td>
                    <td>{item.statusServico}</td>
                    <td>{item.cliente?.nome}</td>
                    <td>{moment(item.dtCriacao).format("DD/MM/YYYY")}</td>
                    <td>
                      <Button
                        variant="outline-secondary"
                        className="m-2"
                        onClick={() => editItem(item.id)}
                      >
                        Editar
                      </Button>
                      {/* <Button variant="outline-danger" className="m-2">
                        Deletar
                      </Button> */}
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Container>
        )}
      </Container>
    </div>
  );
};
