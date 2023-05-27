import { Container, Row, Col, Button, Card, ListGroup } from "react-bootstrap";
import styles from "./MenuPage.module.css";
import { useNavigate } from "react-router-dom";

export const MenuPage = () => {

  const navigate = useNavigate();

  const navigatePageBind = (pageName: string) => {
    navigate(pageName,{ replace: true });
  }

  return (
    <Container className={styles["page-container"]}>
      <Row className={styles["main-title"]} >
        <h3>Seja bem vindo ao DUNEY REFRIGERAÇÃO</h3>
      </Row>

      <Row>
        <Card className={styles["card-container"]}>
          <Card.Body>
            <Row>
              <Col>
                <Card.Title className={styles["card-title"]}>
                  <h3>Cliente</h3>
                </Card.Title>
              </Col>
              <Col>
                <ListGroup variant="flush" className={styles["list-group"]}>
                  <ListGroup.Item>
                    <Button onClick={() => navigatePageBind("/gerencia-cliente")} variant="outline-dark">Gerenciamento</Button>
                  </ListGroup.Item>
                  <ListGroup.Item>
                    <Button onClick={() => navigatePageBind("/cadastro-cliente")} variant="outline-dark">Cadastro</Button>
                  </ListGroup.Item>
                </ListGroup>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      </Row>

      <Row>
        <Card className={styles["card-container"]}>
          <Card.Body>
            <Row>
              <Col>
                <Card.Title className={styles["card-title"]}>
                  <h3>Produto</h3>
                </Card.Title>
              </Col>
              <Col>
                <ListGroup variant="flush" className={styles["list-group"]}>
                  <ListGroup.Item>
                    <Button onClick={() => navigatePageBind("/gerencia-produto")} variant="outline-dark">Gerenciamento</Button>
                  </ListGroup.Item>
                  <ListGroup.Item>
                    <Button onClick={() => navigatePageBind("/cadastro-produto")} variant="outline-dark">Cadastro</Button>
                  </ListGroup.Item>
                </ListGroup>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      </Row>

      <Row>
        <Card className={styles["card-container"]}>
          <Card.Body>
            <Row>
              <Col>
                <Card.Title className={styles["card-title"]}>
                  <h3>Técnico</h3>
                </Card.Title>
              </Col>
              <Col>
                <ListGroup variant="flush" className={styles["list-group"]}>
                  <ListGroup.Item>
                    <Button onClick={() => navigatePageBind("/gerencia-tecnico")} variant="outline-dark">Gerenciamento</Button>
                  </ListGroup.Item>
                  <ListGroup.Item>
                    <Button onClick={() => navigatePageBind("/cadastro-tecnico")} variant="outline-dark">Cadastro</Button>
                  </ListGroup.Item>
                </ListGroup>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      </Row>

      <Row>
        <Card className={styles["card-container"]}>
          <Card.Body>
            <Row>
              <Col>
                <Card.Title className={styles["card-title"]}>
                  <h3>Fornecedor</h3>
                </Card.Title>
              </Col>
              <Col>
                <ListGroup variant="flush" className={styles["list-group"]}>
                  <ListGroup.Item>
                    <Button onClick={() => navigatePageBind("/gerencia-fornecedor")} variant="outline-dark">Gerenciamento</Button>
                  </ListGroup.Item>
                  <ListGroup.Item>
                    <Button onClick={() => navigatePageBind("/cadastro-fornecedor")} variant="outline-dark">Cadastro</Button>
                  </ListGroup.Item>
                </ListGroup>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      </Row>

      <Row>
        <Card className={styles["card-container"]}>
          <Card.Body>
            <Row>
              <Col>
                <Card.Title className={styles["card-title"]}>
                  <h3>Serviço</h3>
                </Card.Title>
              </Col>
              <Col>
                <ListGroup variant="flush" className={styles["list-group"]}>
                  <ListGroup.Item>
                    <Button onClick={() => navigatePageBind("/gerencia-servico")} variant="outline-dark">Gerenciamento</Button>
                  </ListGroup.Item>
                  <ListGroup.Item>
                    <Button onClick={() => navigatePageBind("/cadastro-servico")} variant="outline-dark">Cadastro</Button>
                  </ListGroup.Item>
                </ListGroup>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      </Row>

    </Container>
  );
};
