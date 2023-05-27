import Container from "react-bootstrap/Container";
import styles from "./GerenciaTecnicoPage.module.css";
import InputGroup from "react-bootstrap/InputGroup";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Table from "react-bootstrap/Table";
import { useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Tecnico from "../../dto/tecnico/Tecnico";
import { useLoginRefresh } from "../../hooks/useLoginRefresh";
import { Modal, Pagination } from "react-bootstrap";

type SearchParams = {
  nome: string;
  index: number;
  documento: string;
};

export const GerenciaTecnicoPage = () => {
  const tabelaQtdPagina: number = 10;
  const [listaTabela, setListaTabela] = useState<Tecnico[]>([]);
  const [searchParams, setSearchParams] = useState<SearchParams | null>(null);

  const [qtdPagina, setQtdPagina] = useState(0);
  const [pageIndex, setPageIndex] = useState(0);
  const [selectedTecnico, setSelectedTecnico] = useState<Tecnico>();

  const [authToken, callLoginStatus] = useLoginRefresh();

  const navigate = useNavigate();

  const { register, handleSubmit } = useForm();

  const [show, setShow] = useState(false);

  const handleShow = (produto: Tecnico) => {
    setSelectedTecnico(produto);
    setShow(true);
  };

  const handleClose = () => setShow(false);

  useEffect(() => {
    if (searchParams) {
      callLoginStatus();

      let token = localStorage.getItem("jwtToken")
        ? localStorage.getItem("jwtToken")
        : "";
      let refreshToken = localStorage.getItem("refreshToken")
        ? localStorage.getItem("refreshToken")
        : "";

      fetch(
        `http://localhost:8080/api/tecnico/search?documento=${searchParams.documento}&nome=${searchParams.nome}&index=${searchParams.index}&numPages=${tabelaQtdPagina}`,
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
          let listaProdutos: Tecnico[] = result.result;
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
      documento
    });
  });

  const editItem = (id: number) => {
    navigate(`/atualiza-tecnico?id=${id}`);
  };

  const removeItemBind = () => {
    callLoginStatus();
    let token = localStorage.getItem("jwtToken")
      ? localStorage.getItem("jwtToken")
      : "";
    let refreshToken = localStorage.getItem("refreshToken")
      ? localStorage.getItem("refreshToken")
      : "";
    const item = selectedTecnico;
    if (item) {
      fetch(`http://localhost:8080/api/tecnico/delete?id=${item?.id}`, {
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
            documento: searchParams?.documento ? searchParams.documento : "",
          });
        }
      });
      setShow(false);
    }
  };

  const changeIndex = (value: number) => {
    setPageIndex(value);

    setSearchParams({
      nome: searchParams?.nome ? searchParams.nome : "",
      index: value,
      documento: searchParams?.documento ? searchParams.documento : "",
    });
  };

  return (
    <div>
      <Container>
        <Form className={styles["form-container"]} onSubmit={submitForm}>
          <InputGroup className="mb-3">
            <InputGroup.Text id="basic-addon1">Nome:</InputGroup.Text>
            <Form.Control
              placeholder="Buscar"
              aria-label="tecnico"
              aria-describedby="pesquisa-tecnico-nome"
              {...register("nome")}
            />
          </InputGroup>
          <InputGroup className="mb-3">
            <InputGroup.Text id="basic-addon1">Documento:</InputGroup.Text>
            <Form.Control
              placeholder="Buscar"
              aria-label="documento"
              aria-describedby="pesquisa-tecnico-documento"
              {...register("documento")}
            />
          </InputGroup>
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
                  <th>Nome</th>
                  <th>Documento</th>
                  <th>#</th>
                </tr>
              </thead>
              <tbody>
                {listaTabela.map((item) => (
                  <tr key={item.nome}>
                    <td>{item.nome}</td>
                    <td>{item.cpf}</td>
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
            <Modal.Title>Remover técnico</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            Tem certeza que deseja remover o técnico {selectedTecnico?.nome}?
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
