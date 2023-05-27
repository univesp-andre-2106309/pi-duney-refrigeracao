import Form from "react-bootstrap/Form";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import FloatingLabel from "react-bootstrap/FloatingLabel";
import styles from "./CadastroProdutoPage.module.css";
import { ToastManager } from "../../toast/ToastManager";
import { ToastManagerLevel } from "../../toast/ToastManagerLevel";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import CadastrarProdutoRequest from "../../dto/produto/request/CadastrarProdutoRequest";
import { Card } from "react-bootstrap";
import { useLoginRefresh } from "../../hooks/useLoginRefresh";

export const CadastroProdutoPage = () => {
  const navigate = useNavigate();
  const { register, handleSubmit } = useForm();
  const [authToken, callLoginStatus] = useLoginRefresh();

  const toastManager = new ToastManager();

  const submitForm = handleSubmit(async (action) => {
    
    const body: CadastrarProdutoRequest = {
      nome: action["nome"],
      descricao: action["info"],
      preco: action["preco"],
    };

    callLoginStatus();
    let token = localStorage.getItem("jwtToken")
      ? localStorage.getItem("jwtToken")
      : "";
    let refreshToken = localStorage.getItem("refreshToken")
      ? localStorage.getItem("refreshToken")
      : "";

    fetch("http://localhost:8080/api/produto/create", {
      headers: {
        Authorization: token!,
        RefreshToken: refreshToken!,
        "Content-Type": "application/json",
      },
      method: "POST",
      body: JSON.stringify(body),
    })
      .then(async (response) => {
        let result = await response.json();
        if (response.ok) {
          toastManager.sendMessage(
            "Produto cadastrado com sucesso!",
            "Cadastro de produto",
            ToastManagerLevel.SUCCESS
          );
          navigate("/gerencia-produto");
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
          er.message,
          "Erro ao cadastrar produto",
          ToastManagerLevel.ERROR
        );
      });
  });

  const cancelForm = () => {
    navigate("/gerencia-produto");
  };

  return (
    <Container className={styles["container"]}>
      <Card className="mt-3 mb-5">
        <Card.Header as="h5" className={styles["card-header"]}>
          Cadastro
        </Card.Header>
        <Card.Body>
          <Form onSubmit={submitForm}>
            <Row>
              <Col>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlInput1"
                >
                  <FloatingLabel
                    controlId="nomeFloatInput"
                    label="Nome"
                    className="mb-3"
                  >
                    <Form.Control type="text" {...register("nome")} required />
                  </FloatingLabel>
                </Form.Group>
              </Col>

              <Col xs={3}>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlInput1"
                >
                  <FloatingLabel
                    controlId="precoFloatInput"
                    label="Preço (R$)"
                    className="mb-3"
                  >
                    <Form.Control
                      type="number"
                      {...register("preco")}
                      required
                      step="0.01"
                    />
                  </FloatingLabel>
                </Form.Group>
              </Col>
            </Row>
            <Row>
              <Col>
                <Form.Group className="mb-3" controlId="extraInfoGroup">
                  <Form.Label>Informações extras</Form.Label>
                  <Form.Control as="textarea" rows={3} {...register("info")} required/>
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
    </Container>
  );
};
