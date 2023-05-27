import TecnicoServico from "../../dto/servico/TecnicoServico";
import styles from "./ServicoTecnicoListCard.module.css"
import {
    Accordion,
    Button,
    Card,
    Row,
  } from "react-bootstrap";

  type TecnicoServicoListCardProps = {
    model?: TecnicoServico;
    removeProduto: (id: number) => void;
  };
  

export const ServicoTecnicoListCard = (props: TecnicoServicoListCardProps) => {
    return (
        <Card className={styles["card-container"]}>
          <Card.Header as="h5">Técnico: {props.model?.tecnico?.nome}</Card.Header>
          <Card.Body>
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
                if (props.model?.tecnico?.id) {
                  props.removeProduto(props.model.tecnico?.id);
                }
              }}
            >
              Remover
            </Button>
          </Card.Body>
        </Card>
      );
}
