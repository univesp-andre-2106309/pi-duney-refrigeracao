import Produto from "../../dto/produto/Produto";
import ProdutoServico from "../../dto/servico/ProdutoServico";
import styles from "./ServicoProdutoListCard.module.css";
import {
  Accordion,
  Button,
  Card,
  CloseButton,
  Col,
  ListGroup,
  Row,
} from "react-bootstrap";

type ProdutoServicoListCardProp = {
  model?: ProdutoServico;
  removeProduto: (id: number) => void;
};

export const ServicoProdutoListCard = (props: ProdutoServicoListCardProp) => {
  return (
    <Card className={styles["card-container"]}>
      <Card.Header as="h5">Produto: {props.model?.produto?.nome}</Card.Header>
      <Card.Body>
        <Row className={styles["list-group-container"]}>
          <ListGroup variant="flush">
            <Col>
              <ListGroup.Item>
                <Card.Title>Preço</Card.Title>
                R$ {props.model?.precoProduto}
              </ListGroup.Item>
            </Col>
            <Col>
              <ListGroup.Item>
                <Card.Title>Quantidade</Card.Title>
                {props.model?.quantidade} unidades
              </ListGroup.Item>
            </Col>
          </ListGroup>
        </Row>

        <Row className={styles["accordion-container"]}>
          <Accordion>
            <Accordion.Item eventKey="0">
              <Accordion.Header>Observações</Accordion.Header>
              <Accordion.Body>{props.model?.descricao}</Accordion.Body>
            </Accordion.Item>
          </Accordion>
        </Row>

        <Button
          variant="danger"
          className="float-right"
          onClick={() => {
            if (props.model?.produto?.id) {
              props.removeProduto(props.model.produto?.id);
            }
          }}
        >
          Remover
        </Button>
      </Card.Body>
    </Card>
  );
};
