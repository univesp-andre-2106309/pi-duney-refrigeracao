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
import Tecnico from "../../dto/tecnico/Tecnico";
import TecnicoServico from "../../dto/servico/TecnicoServico";
import styles from "./ServicoTecnicoForm.module.css";
import { ServicoTecnicoListCard } from "../cards/ServicoTecnicoListCard";
import { v4 as uuidv4 } from "uuid";
import { ServicoContext } from "../../context/IContextServico";
import { ToastManager } from "../../toast/ToastManager";
import { ToastManagerLevel } from "../../toast/ToastManagerLevel";
import { useLoginRefresh } from "../../hooks/useLoginRefresh";


type ServicoTecnicoFormProps = {
  tecnicosSalvos?: TecnicoServico[]
}

export const ServicoTecnicoForm = (props: ServicoTecnicoFormProps) => {
  const [tecnicoCarregados, setTecnicoCarregados] = useState<Tecnico[]>();
  const [tecnicoServicoLista, setTecnicoServicoLista] = useState<
    TecnicoServico[]
  >([]);

  const toast = new ToastManager();
  const { register, handleSubmit, control, setValue } = useForm();
  const { setTecnicoServico } = useContext(ServicoContext);
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
      `http://localhost:8080/api/tecnico/search?documento=&nome=&numPages=`,
      {
        headers: {
          Authorization: token!,
          RefreshToken: refreshToken!,
        },
      }
    ).then(async (response) => {
      let result = await response.json();

      if (response.ok) {
        let listaTecnico: Tecnico[] = result.result;
        setTecnicoCarregados(listaTecnico);

        if(listaTecnico.length) {
          setValue("tecnico",listaTecnico[0].id.toString())
        }
      }
    });

    if(props.tecnicosSalvos) {
      setTecnicoServicoLista(props.tecnicosSalvos);
    }
  }, []);

  useEffect(() => {
    setTecnicoServico(tecnicoServicoLista);
  }, [tecnicoServicoLista]);

  const submitTecnicoForm = handleSubmit((action) => {
    const tecnicoId: string = action["tecnico"];
    const descricao: string = action["tecnicoInfo"];

    const tecnico = tecnicoCarregados?.find(
      (item) => item.id.toString() === tecnicoId
    );

    if (!tecnico) {
      toast.sendMessage("Não existe técnico para adicionar para o serviço!","Formulario de tecnico",ToastManagerLevel.WARN)
      return;
    }

    const tecnicosAdicionados = tecnicoServicoLista.map((item) => item.tecnico);

    if (tecnicosAdicionados.includes(tecnico)) {
      toast.sendMessage("Técnico já existente no serviço!","Formulario de tecnico",ToastManagerLevel.WARN)
      return;
    }

    const tecnicoServicoObj: TecnicoServico = {
      id: 0,
      descricao,
      tecnico,
    };

    setTecnicoServicoLista((tecnicos) => [...tecnicos, tecnicoServicoObj]);
  });

  const removeTecnicoBind = (id: number) => {

    const produtosNovos = tecnicoServicoLista.filter(
      (item) => item.tecnico?.id !== id
    );
    setTecnicoServicoLista(produtosNovos);
  };

  return (
    <Container>
      <Card>
        <Card.Body>
          <Card.Title>Adicionar Técnico</Card.Title>
          <Form onSubmit={submitTecnicoForm}>
            <Row>
              <Col xs={9}>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlInput1"
                >
                  <FloatingLabel
                    controlId="tecnicoFloatInput"
                    label="Tecnico"
                    className="mb-3"
                  >
                    <Controller
                      name="tecnico"
                      control={control}
                      render={({ field }) => (
                        <Form.Select
                          aria-label="Default select example"
                          {...field}
                          required
                          onChange={(e) => {
                            setValue("tecnico", e.target.value);
                          }}
                        >
                          {tecnicoCarregados?.map((item, index) => (
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
                    {...register("tecnicoInfo")}
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
      {(tecnicoServicoLista && tecnicoServicoLista.length > 0) && (
        <Card className="mt-3">
          <Card.Header as="h5" className={styles["card-header"]}>
            Listagem
          </Card.Header>
          <Card.Body>
            {tecnicoServicoLista?.map((item) => (
              <ServicoTecnicoListCard
                key={uuidv4()}
                model={item}
                removeProduto={removeTecnicoBind}
              />
            ))}
          </Card.Body>
        </Card>
      )}
    </Container>
  );
};
