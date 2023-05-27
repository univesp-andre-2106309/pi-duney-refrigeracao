import Container from "react-bootstrap/Container";
import styles from "./GerenciaClientePage.module.css";
import InputGroup from "react-bootstrap/InputGroup";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Table from "react-bootstrap/Table";
import { useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Cliente from "../../dto/cliente/Cliente";
import { Modal, Pagination } from "react-bootstrap";
import { v4 as uuidv4 } from "uuid";
import { useLoginRefresh } from "../../hooks/useLoginRefresh";

type SearchParams = {
  nome: string;
  index: number;
  documento: string;
};

export const GerenciaClientePage = () => {
  const tabelaQtdPagina: number = 10;
  const [listaTabela, setListaTabela] = useState<Cliente[]>([]);
  const [selectedCliente, setSelectedCliente] = useState<Cliente>();

  const [searchParams, setSearchParams] = useState<SearchParams | null>(null);
  const [qtdPagina, setQtdPagina] = useState(0);
  const [pageIndex, setPageIndex] = useState(0);
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = (cliente: Cliente) => {
    setSelectedCliente(cliente);
    setShow(true);
  };
  const [authToken, callLoginStatus] = useLoginRefresh();

  const navigate = useNavigate();

  const { register, handleSubmit } = useForm();

  useEffect(() => {
    if (show) {
      setShow(true);
    }
  }, [selectedCliente, show]);

  useEffect(() => {
    callLoginStatus();

    if (searchParams) {
      let token = localStorage.getItem("jwtToken")
        ? localStorage.getItem("jwtToken")
        : "";
      let refreshToken = localStorage.getItem("refreshToken")
        ? localStorage.getItem("refreshToken")
        : "";
        console.log(searchParams)

      fetch(
        `http://localhost:8080/api/cliente/search?documento=${searchParams.documento}&nome=${searchParams.nome}&index=${searchParams.index}&numPages=${tabelaQtdPagina}`,
        {
          headers: {
            Authorization: token!,
            RefreshToken: refreshToken!,
          },
        }
      ).then(async (response) => {
        let result = await response.json();

        setQtdPagina(Math.ceil(result.maxResultSize / tabelaQtdPagina));

        if (response.ok) {
          setPageIndex(searchParams.index);
          let listaProdutos: Cliente[] = result.result;
          setListaTabela(listaProdutos);
        }
      });
    }
  }, [searchParams]);

  const submitForm = handleSubmit(async (action) => {
    const nome: string = action["nome"] ? action["nome"] : "";
    const documento: string = action["documento"] ? action["documento"] : "";

    setSearchParams({
      nome: nome,
      index: 0,
      documento,
    });
  });

  const editItem = (id: number) => {
    navigate(`/atualiza-cliente?id=${id}`);
  };

  const changeIndex = (value: number) => {
    setPageIndex(value);

    setSearchParams({
      nome: searchParams?.nome ? searchParams.nome : "",
      index: value,
      documento: searchParams?.documento ? searchParams.documento : "",
    });
  };

  const removeItemBind = () => {
    callLoginStatus();
    let token = localStorage.getItem("jwtToken")
      ? localStorage.getItem("jwtToken")
      : "";
    let refreshToken = localStorage.getItem("refreshToken")
      ? localStorage.getItem("refreshToken")
      : "";
    const item = selectedCliente;
    if (item) {
      fetch(`http://localhost:8080/api/cliente/delete?id=${item?.id}`, {
        headers: {
          Authorization: token!,
          RefreshToken: refreshToken!,
        },
        method: "PATCH",
      }).then(async (response) => {
        let result = await response.json();
        if (response.ok) {
          setSearchParams({
            nome: searchParams?.nome ? searchParams.nome : "",
            index: 0,
            documento: searchParams?.documento ? searchParams?.documento : "",
          });
        }
      });
      setShow(false);
    }
  };

  return (
    <div>
      <Container>
        <Form className={styles["form-container"]} onSubmit={submitForm}>
          <div>
            <InputGroup className="mb-3">
              <InputGroup.Text id="basic-addon1">Nome:</InputGroup.Text>
              <Form.Control
                placeholder="Buscar"
                aria-label="cliente"
                aria-describedby="pesquisa-cliente-nome"
                {...register("nome")}
              />
            </InputGroup>
            <InputGroup className="mb-3">
              <InputGroup.Text id="basic-addon1">Documento:</InputGroup.Text>
              <Form.Control
                placeholder="Buscar"
                aria-label="documento"
                aria-describedby="pesquisa-cliente-documento"
                {...register("documento")}
              />
            </InputGroup>
            <Button variant="success" type="submit">
              Buscar
            </Button>
          </div>
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
                  <th>Nome</th>
                  <th>Documento</th>
                  <th>#</th>
                </tr>
              </thead>
              <tbody>
                {listaTabela.map((item) => (
                  <tr key={uuidv4()}>
                    <td>{item.nome}</td>
                    <td>{item.documento}</td>
                    <td>
                      <Button
                        variant="outline-secondary"
                        className="m-2"
                        onClick={() => editItem(item.id)}
                      >
                        Editar
                      </Button>
                      <Button
                        variant="outline-danger"
                        className="m-2"
                        onClick={() => handleShow(item)}
                      >
                        Deletar
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Container>
        )}
        <Modal show={show} onHide={handleClose} centered>
          <Modal.Header closeButton>
            <Modal.Title>Remover cliente</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            Tem certeza que deseja remover o cliente {selectedCliente?.nome}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="warning" onClick={handleClose}>
              Cancelar
            </Button>
            <Button variant="danger" onClick={removeItemBind}>
              Remover
            </Button>
          </Modal.Footer>
        </Modal>
      </Container>
    </div>
  );
};
