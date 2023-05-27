import FornecedorServico from '../../dto/servico/FornecedorServico';
import styles from './ServicoFornecedorListCard.module.css'
import {
    Accordion,
    Button,
    Card,
    Row,
  } from "react-bootstrap";

  type TecnicoServicoListCardProps = {
    model?: FornecedorServico;
    removeProduto: (id: number) => void;
  };


export const ServicoFornecedorListCard = (props: TecnicoServicoListCardProps) => {
    return (
        <Card className={styles["card-container"]}>
          <Card.Header as="h5">Fornecedor: {props.model?.fornecedor?.nome}</Card.Header>
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
                if (props.model?.fornecedor?.id) {
                  props.removeProduto(props.model.fornecedor?.id);
                }
              }}
            >
              Remover
            </Button>
          </Card.Body>
        </Card>
      );
}
