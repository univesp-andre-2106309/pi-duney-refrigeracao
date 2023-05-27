import { useContext, useEffect, useState } from "react";
import {
  Button,
  Card,
  Col,
  Container,
  FloatingLabel,
  Form,
  Row,
} from "react-bootstrap";
import { Controller, useForm } from "react-hook-form";
import Fornecedor from "../../dto/fornecedor/Fornecedor";
import FornecedorServico from "../../dto/servico/FornecedorServico";
import styles from "./ServicoFornecedorForm.module.css";
import { v4 as uuidv4 } from "uuid";
import { ServicoFornecedorListCard } from "../cards/ServicoFornecedorListCard";
import { ServicoContext } from "../../context/IContextServico";
import { ToastManager } from "../../toast/ToastManager";
import { ToastManagerLevel } from "../../toast/ToastManagerLevel";
import { useLoginRefresh } from "../../hooks/useLoginRefresh";

type ServicoFornecedorFormProps = {
  fornecedoresSalvos?: FornecedorServico[];
};

export const ServicoFornecedorForm = (props: ServicoFornecedorFormProps) => {
  const [carregados, setCarregados] = useState<Fornecedor[]>();
  const [fornecedorServicoLista, setFornecedorServicoLista] = useState<
    FornecedorServico[]
  >([]);
  const { register, handleSubmit, control, setValue } = useForm();
  const { setFornecedorServico } = useContext(ServicoContext);
  const toast = new ToastManager();

  const [authToken, callLoginStatus] = useLoginRefresh();

  useEffect(() => {
    callLoginStatus();

    let token = localStorage.getItem("jwtToken")
      ? localStorage.getItem("jwtToken")
      : "";
    let refreshToken = localStorage.getItem("refreshToken")
      ? localStorage.getItem("refreshToken")
      : "";

    fetch(
      `http://localhost:8080/api/fornecedor/search?documento=&nome=&numPages=`,
      {
        headers: {
          Authorization: token!,
          RefreshToken: refreshToken!,
        },
      }
    ).then(async (response) => {
      let result = await response.json();

      if (response.ok) {
        let listaFornecedor: Fornecedor[] = result.result;
        setCarregados(listaFornecedor);
        if (listaFornecedor.length) {
          setValue("listaFornecedor", listaFornecedor[0].id.toString());
        }
      }
    });

    if (props.fornecedoresSalvos) {
      setFornecedorServicoLista(props.fornecedoresSalvos);
    }
  }, []);

  useEffect(() => {
    setFornecedorServico(fornecedorServicoLista);
  }, [fornecedorServicoLista]);

  const submitFornecedorForm = handleSubmit((action) => {
    const tecnicoId: string = action["fornecedor"];
    const descricao: string = action["fornecedorInfo"];

    const fornecedor = carregados?.find(
      (item) => item.id.toString() === tecnicoId
    );

    if (!fornecedor) {
      toast.sendMessage(
        "Não existe fornecedor para adicionar para o serviço!",
        "Formulario de fornecedor",
        ToastManagerLevel.WARN
      );
      return;
    }

    const tecnicosAdicionados = fornecedorServicoLista.map(
      (item) => item.fornecedor
    );

    if (tecnicosAdicionados.includes(fornecedor)) {
      toast.sendMessage(
        "Fornecedor já existente no serviço!",
        "Formulario de fornecedor",
        ToastManagerLevel.WARN
      );
      return;
    }

    const fornecedorServicoObj: FornecedorServico = {
      id: 0,
      descricao,
      fornecedor: fornecedor,
    };

    setFornecedorServicoLista((fornecedores) => [
      ...fornecedores,
      fornecedorServicoObj,
    ]);
  });

  const removeFornecedorBind = (id: number) => {
    const produtosNovos = fornecedorServicoLista.filter(
      (item) => item.fornecedor?.id !== id
    );
    setFornecedorServicoLista(produtosNovos);
  };

  return (
    <Container>
      <Card>
        <Card.Body>
          <Card.Title>Adicionar Fornecedor</Card.Title>
          <Form onSubmit={submitFornecedorForm}>
            <Row>
              <Col xs={9}>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlInput1"
                >
                  <FloatingLabel
                    controlId="fornecedorFloatInput"
                    label="Tecnico"
                    className="mb-3"
                  >
                    <Controller
                      name="fornecedor"
                      control={control}
                      render={({ field }) => (
                        <Form.Select
                          aria-label="Default select example"
                          {...field}
                          required
                          onChange={(e) => {
                            setValue("fornecedor", e.target.value);
                          }}
                        >
                          {carregados?.map((item, index) => (
                            <option key={index} value={item.id}>
                              {item.nome}
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
                <Form.Group className="mb-3" controlId="extraInfoGroup">
                  <Form.Label>Descrição</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    {...register("fornecedorInfo")}
                  />
                </Form.Group>
              </Col>
            </Row>

            <Row>
              <Col>
                <Button variant="primary" type="submit">
                  Adicionar
                </Button>
              </Col>
            </Row>
          </Form>
        </Card.Body>
      </Card>

      {fornecedorServicoLista && fornecedorServicoLista.length > 0 && (
        <Card className="mt-3">
          <Card.Header as="h5" className={styles["card-header"]}>
            Listagem
          </Card.Header>
          <Card.Body>
            {fornecedorServicoLista?.map((item) => (
              <ServicoFornecedorListCard
                key={uuidv4()}
                model={item}
                removeProduto={removeFornecedorBind}
              />
            ))}
          </Card.Body>
        </Card>
      )}
    </Container>
  );
};
