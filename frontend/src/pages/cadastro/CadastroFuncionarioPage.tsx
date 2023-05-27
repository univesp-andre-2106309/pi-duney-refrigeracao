import Form from "react-bootstrap/Form";
import Container from "react-bootstrap/Container"
import Row from "react-bootstrap/Row"
import Col from "react-bootstrap/Col"
import Button from "react-bootstrap/Button"
import FloatingLabel from "react-bootstrap/FloatingLabel";
import styles from "./CadastroFuncionarioPage.module.css"

//TODO - Adicionar cadastro de funcionario
export const CadastroFuncionarioPage = () => {
  return (
    <Container className={styles["container"]}>
    <Form>

    <Row>
        <Col>
          <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
            <FloatingLabel
              controlId="cidadeFloatInput"
              label="Nome do usuario"
              className="mb-3"
            >
              <Form.Control type="text" />
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
              <Form.Control type="text" />
            </FloatingLabel>
          </Form.Group>
        </Col>
        <Col>
          <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
            <FloatingLabel
              controlId="documentoFloatInput"
              label="Sobrenome"
              className="mb-3"
            >
              <Form.Control type="text" />
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
              <Form.Control type="text" />
            </FloatingLabel>
          </Form.Group>
        </Col>
      </Row>

      <Row>
        <Col>
          <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
            <FloatingLabel
              controlId="senhaFloatInput"
              label="Senha"
              className="mb-3"
            >
              <Form.Control type="password" />
            </FloatingLabel>
          </Form.Group>
        </Col>
      </Row>

      <Row className="mt-5">
        <Col>
          <Button variant="outline-danger" size="lg">
            Cancelar
          </Button>
        </Col>
        <Col>
          <Button variant="outline-success" size="lg">
            Salvar
          </Button>
        </Col>
      </Row>
    </Form>
  </Container>
  );
};
