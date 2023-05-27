import Form from "react-bootstrap/Form";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import FloatingLabel from "react-bootstrap/FloatingLabel";
import styles from "./AtualizarProdutoPage.module.css";
import { ToastManager } from "../../toast/ToastManager";
import { ToastManagerLevel } from "../../toast/ToastManagerLevel";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import Produto from "../../dto/produto/Produto";
import AtualizarProdutoRequest from "../../dto/produto/request/AtualizarProdutoRequest";
import { useLoginRefresh } from "../../hooks/useLoginRefresh";

export const AtualizarProdutoPage = () => {
  const navigate = useNavigate();
  const { register, handleSubmit, setValue } = useForm();
  const toastManager = new ToastManager();
  const [searchParams] = useSearchParams();
  const [authToken, callLoginStatus] = useLoginRefresh();

  const [produtoObj, setProdutoObj] = useState<Produto>();

  useEffect(() => {
    const id = searchParams.get("id");

    if (!id) {
      navigate("/gerencia-produto");
      return;
    }
    callLoginStatus();
    let token = localStorage.getItem("jwtToken")
      ? localStorage.getItem("jwtToken")
      : "";
    let refreshToken = localStorage.getItem("refreshToken")
      ? localStorage.getItem("refreshToken")
      : "";

    fetch(`http://localhost:8080/api/produto/find?id=${id}`, {
      headers: {
        Authorization: token!,
        RefreshToken: refreshToken!,
      },
    }).then(async (response) => {
      let result = await response.json();

      if (response.ok) {
        const value: Produto = result.result;
        setProdutoObj(value);
        setValue("nome", value.nome);
        setValue("info", value.descricao);
        setValue("preco", value.preco);
        setValue("id", value.id);
        setValue("estoque", value.estoque);
        return;
      }

      navigate("/gerencia-produto");
      return;
    });
  }, []);

  const submitForm = handleSubmit(async (action) => {
    const body: AtualizarProdutoRequest = {
      nome: action["nome"],
      descricao: action["info"],
      preco: action["preco"],
      estoque: action["estoque"],
    };

    callLoginStatus();
    
    let token = localStorage.getItem("jwtToken")
      ? localStorage.getItem("jwtToken")
      : "";
    let refreshToken = localStorage.getItem("refreshToken")
      ? localStorage.getItem("refreshToken")
      : "";

    fetch(`http://localhost:8080/api/produto/update?id=${produtoObj?.id}`, {
      headers: {
        Authorization: token!,
        RefreshToken: refreshToken!,
        "Content-Type": "application/json",
      },
      method: "PUT",
      body: JSON.stringify(body),
    })
      .then(async (response) => {
        let result = await response.json();
        if (response.ok) {
          toastManager.sendMessage(
            "Produto atualizado com sucesso!",
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
    navigate("/gerencia-produto")
  }
  return (
    <Container className={styles["container"]}>
      <Form onSubmit={submitForm}>
        <Row>
          <Col xs={3}>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <FloatingLabel
                controlId="precoFloatInput"
                label="Id do Produto"
                className="mb-3"
              >
                <Form.Control
                  type="number"
                  {...register("id")}
                  disabled
                  readOnly
                />
              </FloatingLabel>
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <FloatingLabel
                controlId="nomeFloatInput"
                label="Nome"
                className="mb-3"
              >
                <Form.Control type="text" {...register("nome")} required />
              </FloatingLabel>
            </Form.Group>
          </Col>

          <Col xs={2}>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <FloatingLabel
                controlId="precoFloatInput"
                label="Preço (R$)"
                className="mb-3"
              >
                <Form.Control
                  type="number"
                  {...register("preco")}
                  step="0.01"
                />
              </FloatingLabel>
            </Form.Group>
          </Col>
          <Col xs={2}>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <FloatingLabel
                controlId="precoFloatInput"
                label="estoque"
                className="mb-3"
              >
                <Form.Control type="number" {...register("estoque")} required min="0" />
              </FloatingLabel>
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col>
            <Form.Group className="mb-3" controlId="extraInfoGroup">
              <Form.Label>Informações extras</Form.Label>
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
    </Container>
  );
};
