import Form from "react-bootstrap/Form";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import FloatingLabel from "react-bootstrap/FloatingLabel";
import styles from "./AtualizarFornecedorPage.module.css";
import { ToastManager } from "../../toast/ToastManager";
import { ToastManagerLevel } from "../../toast/ToastManagerLevel";
import { useNavigate, useSearchParams } from "react-router-dom";
import { Controller, useForm } from "react-hook-form";
import CadastrarFornecedorRequest from "../../dto/fornecedor/request/CadastrarFornecedorRequest";
import estadosBrasil from "../../formdata/Estados";
import { useEffect, useState } from "react";
import Fornecedor from "../../dto/fornecedor/Fornecedor";
import { useLoginRefresh } from "../../hooks/useLoginRefresh";

export const AtualizarFornecedorPage = () => {
  const [searchParams] = useSearchParams();

  const [fornecedorObj, setFornecedorObj] = useState<Fornecedor>();

  const [authToken, callLoginStatus] = useLoginRefresh();

  const listaUf: string[] = estadosBrasil.map((item) => item.uf);
  const navigate = useNavigate();
  const { register, handleSubmit, setValue, control } = useForm();
  const toastManager = new ToastManager();

  useEffect(() => {
    const id = searchParams.get("id");

    if (!id) {
      navigate("/gerencia-fornecedor");
      return;
    }

    callLoginStatus();

    let token = localStorage.getItem("jwtToken")
      ? localStorage.getItem("jwtToken")
      : "";
    let refreshToken = localStorage.getItem("refreshToken")
      ? localStorage.getItem("refreshToken")
      : "";

    fetch(`http://localhost:8080/api/fornecedor/find?id=${id}`, {
      headers: {
        Authorization: token!,
        RefreshToken: refreshToken!,
      },
    }).then(async (response) => {
      let result = await response.json();

      if (response.ok) {
        const value: Fornecedor = result.fornecedor;
        setFornecedorObj(value);
        setValue("id", value.id);
        setValue("nome", value.nome);
        setValue("documento", value.cnpj);
        setValue("cep", value.cep);
        setValue("cidade", value.cidade);
        setValue("email", value.email);
        setValue("endereco", value.endereco);
        setValue("estado", value.estado);
        setValue("telefone", value.telefone);

        return;
      }

      navigate("/gerencia-fornecedor");
      return;
    });
  }, []);

  const submitForm = handleSubmit(async (action) => {
    const body: CadastrarFornecedorRequest = {
      nome: action["nome"],
      endereco: action["endereco"],
      cidade: action["cidade"],
      estado: action["estado"],
      cep: action["cep"],
      telefone: action["telefone"],
      email: action["email"],
      cnpj: action["documento"],
    };

    let token = localStorage.getItem("jwtToken")
      ? localStorage.getItem("jwtToken")
      : "";
    let refreshToken = localStorage.getItem("refreshToken")
      ? localStorage.getItem("refreshToken")
      : "";

    fetch(
      `http://localhost:8080/api/fornecedor/update?id=${fornecedorObj?.id}`,
      {
        headers: {
          Authorization: token!,
          RefreshToken: refreshToken!,
          "Content-Type": "application/json",
        },
        method: "PUT",
        body: JSON.stringify(body),
      }
    )
      .then(async (response) => {
        let result = await response.json();
        if (response.ok) {
          toastManager.sendMessage(
            "Fornecedor atualizado com sucesso!",
            "Cadastro de fornecedor",
            ToastManagerLevel.SUCCESS
          );
          navigate("/gerencia-fornecedor");
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
          "Erro ao cadastrar Fornecedor",
          ToastManagerLevel.ERROR
        );
      });
  });
  const cancelForm = () => {
    navigate("/gerencia-fornecedor")
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

          <Col xs={3}>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <FloatingLabel
                controlId="documentoFloatInput"
                label="Documento"
                className="mb-3"
              >
                <Form.Control type="text" {...register("documento")} required />
              </FloatingLabel>
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <FloatingLabel
                controlId="enderecoFloatInput"
                label="Endereco"
                className="mb-3"
              >
                <Form.Control type="text" {...register("endereco")} required />
              </FloatingLabel>
            </Form.Group>
          </Col>
          <Col>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <FloatingLabel
                controlId="cidadeFloatInput"
                label="Cidade"
                className="mb-3"
              >
                <Form.Control type="text" {...register("cidade")} required/>
              </FloatingLabel>
            </Form.Group>
          </Col>
          <Col xs={2}>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <FloatingLabel
                controlId="estadoFloatInput"
                label="UF"
                className="mb-3"
              >
                <Controller
                  name="estado"
                  control={control}
                  render={({ field }) => (
                    <Form.Select
                      aria-label="Default select example"
                      {...field}
                      required
                      onChange={(e) => setValue("estado", e.target.value)}
                    >
                      {listaUf.map((item, index) => (
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
          <Col>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <FloatingLabel
                controlId="cepFloatInput"
                label="Cep"
                className="mb-3"
              >
                <Form.Control type="text" {...register("cep")} required />
              </FloatingLabel>
            </Form.Group>
          </Col>
        </Row>

        <Row>
          <Col>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <FloatingLabel
                controlId="emailFloatInput"
                label="Email"
                className="mb-3"
              >
                <Form.Control type="email" {...register("email")} />
              </FloatingLabel>
            </Form.Group>
          </Col>

          <Col xs={3}>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <FloatingLabel
                controlId="documentoFloatInput"
                label="Telefone"
                className="mb-3"
              >
                <Form.Control type="text" {...register("telefone")} />
              </FloatingLabel>
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
