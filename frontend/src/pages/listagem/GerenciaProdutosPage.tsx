import Container from "react-bootstrap/Container";
import styles from "./GerenciaProdutoPage.module.css";
import InputGroup from "react-bootstrap/InputGroup";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Table from "react-bootstrap/Table";
import { useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import Produto from "../../dto/produto/Produto";
import { useNavigate } from "react-router-dom";
import { Col, FloatingLabel, Modal, Pagination, Row } from "react-bootstrap";
import { useLoginRefresh } from "../../hooks/useLoginRefresh";

type SarchParams = {
  nome: string;
  index: number;
  precoMin?: number;
  precoMax?: number;
  order?: string;
  checkboxPreco: boolean;
};

export const GerenciaProdutosPage = () => {
  const tabelaQtdPagina: number = 10;

  const handleClose = () => setShow(false);

  const [listaTabela, setListaTabela] = useState<Produto[]>([]);
  const [searchParams, setSearchParams] = useState<SarchParams | null>(null);

  const [authToken, callLoginStatus] = useLoginRefresh();

  const [qtdPagina, setQtdPagina] = useState(0);
  const [pageIndex, setPageIndex] = useState(0);
  const [selectedProduto, setSelectedProduto] = useState<Produto>();
  const [show, setShow] = useState(false);
  const [searchPrice, setSearchPrice] = useState(false);

  const handleShow = (produto: Produto) => {
    setSelectedProduto(produto);
    setShow(true);
  };

  const navigate = useNavigate();

  const { register, handleSubmit } = useForm();

  useEffect(() => {
    if (searchParams) {
      callLoginStatus();
      let token = localStorage.getItem("jwtToken")
        ? localStorage.getItem("jwtToken")
        : "";
      let refreshToken = localStorage.getItem("refreshToken")
        ? localStorage.getItem("refreshToken")
        : "";

      const apiUrl = searchParams.checkboxPreco
        ? `http://localhost:8080/api/produto/search?nome=${searchParams.nome}&precoMin=${searchParams.precoMin}&precoMax=${searchParams.precoMax}&order=&index=${searchParams.index}&numPages=${tabelaQtdPagina}`
        : `http://localhost:8080/api/produto/search?nome=${searchParams.nome}&precoMin=&precoMax=&order=&index=${searchParams.index}&numPages=${tabelaQtdPagina}`;

      fetch(apiUrl, {
        headers: {
          Authorization: token!,
          RefreshToken: refreshToken!,
        },
      }).then(async (response) => {
        setPageIndex(searchParams.index);
        let result = await response.json();
        setQtdPagina(Math.ceil(result.maxResultSize / tabelaQtdPagina));

        if (response.ok) {
          setPageIndex(searchParams.index);
          let listaProdutos: Produto[] = result.result;
          setListaTabela(listaProdutos);
        }
      });
    }
  }, [searchParams]);

  useEffect(() => {
    if (show) {
      setShow(true);
    }
  }, [selectedProduto, show]);

  const submitForm = handleSubmit(async (action) => {
    const nome: string = action["nome"] ? action["nome"] : "";
    const checkboxPreco: boolean = action["precoCheckbox"];

    const precoMin: number = action["precoMin"] ? action["precoMin"] : 0;
    const precoMax: number = action["precoMax"] ? action["precoMax"] : 0;

    setSearchParams({
      nome: nome,
      index: 0,
      checkboxPreco,
      precoMin,
      precoMax,
    });
  });

  const editItem = (id: number) => {
    navigate(`/atualiza-produto?id=${id}`);
  };

  const changeIndex = (value: number) => {
    setPageIndex(value);

    setSearchParams({
      nome: searchParams?.nome ? searchParams.nome : "",
      index: value,
      checkboxPreco: searchParams?.checkboxPreco
        ? searchParams.checkboxPreco
        : false,
      precoMin: searchParams?.precoMin ? searchParams.precoMin : 0,
      precoMax: searchParams?.precoMax ? searchParams.precoMax : 0,
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
    const item = selectedProduto;
    if (item) {
      fetch(`http://localhost:8080/api/produto/delete?id=${item?.id}`, {
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
            checkboxPreco: searchParams?.checkboxPreco
              ? searchParams.checkboxPreco
              : false,
            precoMin: searchParams?.precoMin ? searchParams.precoMin : 0,
            precoMax: searchParams?.precoMax ? searchParams.precoMax : 0,
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
          <Row>
            <InputGroup className="mb-3">
              <InputGroup.Text id="basic-addon1">Nome:</InputGroup.Text>
              <Form.Control
                placeholder="Buscar"
                aria-label="produto"
                aria-describedby="pesquisa-produto-nome"
                {...register("nome")}
              />
            </InputGroup>
          </Row>
          <Container className={styles["preco-slider"]}>
            <Row>
              <Col>
                <Form.Check
                  type="switch"
                  id="switch-preco"
                  label="Pesquisa por preço?"
                  className={styles["preco-checkbox"]}
                  {...register("precoCheckbox")}
                  onChange={(e) => setSearchPrice(Boolean(e.target.checked))}
                  
                />
              </Col>
            </Row>
            {searchPrice && (
              <Row>
                <Col xs={{ span: 6 }}>
                  <Form.Group className="mb-3" controlId="formPrecoMin">
                    <Form.Label>Min</Form.Label>
                    <Form.Control type="number" {...register("precoMin")} defaultValue={0} min={0}/>
                  </Form.Group>
                </Col>
                <Col xs={{ span: 6 }}>
                  <Form.Group className="mb-3" controlId="formPrecoMax"  >
                    <Form.Label>Max</Form.Label>
                    <Form.Control type="number" {...register("precoMax")} defaultValue={0}  min={0}/>
                  </Form.Group>
                </Col>
              </Row>
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
                  <th>Nome</th>
                  <th>Descrição</th>
                  <th>Preço</th>
                  <th>Estoque</th>
                  <th>#</th>
                </tr>
              </thead>
              <tbody>
                {listaTabela.map((item) => (
                  <tr key={item.nome}>
                    <td>{item.nome}</td>
                    <td>{item.descricao}</td>
                    <td>R$ {item.preco}</td>
                    <td>{item.estoque}</td>
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
            <Modal.Title>Remover produto</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            Tem certeza que deseja remover o produto {selectedProduto?.nome}
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
