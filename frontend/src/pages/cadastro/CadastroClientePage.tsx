import Form from "react-bootstrap/Form";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import FloatingLabel from "react-bootstrap/FloatingLabel";
import styles from "./CadastroClientePage.module.css";
import { Controller, useForm } from "react-hook-form";
import CadastrarClienteReq from "../../dto/cliente/request/CadastrarClienteRequest";
import estadosBrasil from "../../formdata/Estados";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { ToastManager } from "../../toast/ToastManager";
import { ToastManagerLevel } from "../../toast/ToastManagerLevel";
import { Card } from "react-bootstrap";
import { useEffect } from "react";
import { useLoginRefresh } from "../../hooks/useLoginRefresh";

export const CadastroClientePage = () => {
  const listaUf: string[] = estadosBrasil.map((item) => item.uf);
  

  const { register, handleSubmit, setValue, control } = useForm();
  const navigate = useNavigate();

  const [authToken, callLoginStatus] = useLoginRefresh();

  useEffect(() => {
    //Padrão do Select
    setValue("estado",listaUf[0]);
  }, [])
  

  const submitForm = handleSubmit(async (action) => {
    let uf = action["estado"];
    let estado = estadosBrasil.find((item) => item.uf === uf);

    if (!estado) {
      estado = {
        nome: "Desconhecido",
        uf: "??",
      };
    }
    let toastManager = new ToastManager();

    let body: CadastrarClienteReq = {
      nome: action["nome"],
      bairro: action["bairro"],
      cep: action["cep"],
      cidade: action["cidade"],
      documento: action["documento"],
      email: action["email"],
      estado: estado.nome,
      uf: estado.uf,
      rua: action["rua"],
      numTel: action["telefone"],
      numCel: action["celular"],
      numResidencia: action["residencia"],
      info: action["info"],
      dtNascimento: "11/11/2011",
    };

    callLoginStatus();

    let token = localStorage.getItem("jwtToken")
      ? localStorage.getItem("jwtToken")
      : "";
    let refreshToken = localStorage.getItem("refreshToken")
      ? localStorage.getItem("refreshToken")
      : "";

    fetch("http://localhost:8080/api/cliente/create", {
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
            "Cadastro de Cliente",
            "Cliente cadastrado com sucesso!",
            ToastManagerLevel.SUCCESS
          );
          navigate("/gerencia-cliente");
          return;
        }
        toastManager.sendMessage(
          result.message,
          result.title,
          ToastManagerLevel.ERROR
        );
      })
      .catch((er) => {
        toast.error("Ocorreu um erro ao tentar salvar os dados", {
          position: toast.POSITION.BOTTOM_CENTER,
        });
      });
  });

  const cancelForm = () => {
    navigate("/gerencia-cliente");
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
                  <Form.Control type="text" {...register("nome")} required/>
                </FloatingLabel>
              </Form.Group>
            </Col>
            <Col>
              <Form.Group
                className="mb-3"
                controlId="exampleForm.ControlInput1"
              >
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
              <Form.Group
                className="mb-3"
                controlId="exampleForm.ControlInput1"
              >
                <FloatingLabel
                  controlId="cidadeFloatInput"
                  label="Cidade"
                  className="mb-3"
                >
                  <Form.Control type="text" {...register("cidade")} required />
                </FloatingLabel>
              </Form.Group>
            </Col>
            <Col>
              <Form.Group
                className="mb-3"
                controlId="exampleForm.ControlInput1"
              >
                <FloatingLabel
                  controlId="estadoFloatInput"
                  label="Estado"
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
              <Form.Group
                className="mb-3"
                controlId="exampleForm.ControlInput1"
              >
                <FloatingLabel
                  controlId="ruaFloatInput"
                  label="Rua"
                  className="mb-3"
                >
                  <Form.Control type="text" {...register("rua")}  />
                </FloatingLabel>
              </Form.Group>
            </Col>
            <Col>
              <Form.Group
                className="mb-3"
                controlId="exampleForm.ControlInput1"
              >
                <FloatingLabel
                  controlId="nResFloatInput"
                  label="N° Residencia"
                  className="mb-3"
                >
                  <Form.Control type="text" {...register("residencia")} />
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
                  controlId="bairroFloatInput"
                  label="Bairro"
                  className="mb-3"
                >
                  <Form.Control type="text" {...register("bairro")} />
                </FloatingLabel>
              </Form.Group>
            </Col>
            <Col>
              <Form.Group
                className="mb-3"
                controlId="exampleForm.ControlInput1"
              >
                <FloatingLabel
                  controlId="cepFloatInput"
                  label="CEP"
                  className="mb-3"
                >
                  <Form.Control type="text" {...register("cep")} />
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
                  controlId="telFloatInput"
                  label="Telefone"
                  className="mb-3"
                >
                  <Form.Control type="text" {...register("telefone")} />
                </FloatingLabel>
              </Form.Group>
            </Col>
            <Col>
              <Form.Group
                className="mb-3"
                controlId="exampleForm.ControlInput1"
              >
                <FloatingLabel
                  controlId="celFloatInput"
                  label="Celular"
                  className="mb-3"
                >
                  <Form.Control type="text" {...register("celular")} />
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
                  controlId="emailFloatInput"
                  label="Email"
                  className="mb-3"
                >
                  <Form.Control type="email" {...register("email")} required/>
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
        </Card.Body>
      </Card>
    </Container>
  );
};
