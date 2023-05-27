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
import Produto from "../../dto/produto/Produto";
import styles from "./ServicoProdutoForm.module.css";
import { ServicoProdutoListCard } from "../cards/ServicoProdutoListCard";
import ProdutoServico from "../../dto/servico/ProdutoServico";
import { v4 as uuidv4 } from "uuid";
import { ServicoContext } from "../../context/IContextServico";
import { ToastManager } from "../../toast/ToastManager";
import { ToastManagerLevel } from "../../toast/ToastManagerLevel";
import { useLoginRefresh } from "../../hooks/useLoginRefresh";

type ServicoProdutoFormProps = {
  produtosSalvos?: ProdutoServico[];
};

export const ServicoProdutoForm = (props: ServicoProdutoFormProps) => {
  const [produtosCarregados, setProdutosCarregados] = useState<Produto[]>();
  const [produtos, setProdutos] = useState<ProdutoServico[]>([]);
  const { register, handleSubmit, control, setValue } = useForm();
  const { setProdutoServico } = useContext(ServicoContext);

  const [authToken, callLoginStatus] = useLoginRefresh();

  const toast = new ToastManager();

  useEffect(() => {

    callLoginStatus();

    let token = localStorage.getItem("jwtToken")
      ? localStorage.getItem("jwtToken")
      : "";
    let refreshToken = localStorage.getItem("refreshToken")
      ? localStorage.getItem("refreshToken")
      : "";

    fetch(
      `http://localhost:8080/api/produto/search?documento=&nome=&numPages=`,
      {
        headers: {
          Authorization: token!,
          RefreshToken: refreshToken!,
        },
      }
    ).then(async (response) => {
      let result = await response.json();

      if (response.ok) {
        let listaProdutos: Produto[] = result.result;
        setProdutosCarregados(listaProdutos);
        if (listaProdutos.length) {
          setValue("produto", listaProdutos[0].id.toString());
          setValue("precoOriginal", listaProdutos[0].preco);
        }
      }
    });

    if (props.produtosSalvos) {
      setProdutos(props.produtosSalvos);
    }
  }, []);

  useEffect(() => {
    setProdutoServico(produtos);
  }, [produtos]);

  const changeProductBind = (value: string) => {
    let produto = produtosCarregados?.find(
      (item) => item.id.toString() === value
    );

    setValue("precoOriginal", produto?.preco);
  };

  const submitProdutoForm = handleSubmit((action) => {
    const produtoId: string = action["produto"];
    const quantidade: number = action["qtd"];
    const novoPreco: number = action["novoPreco"];
    const descricao: string = action["produtoInfo"];
    
    const produto = produtosCarregados?.find(
      (item) => item.id.toString() === produtoId
    );

    if (!produto) {
      toast.sendMessage("Não existe produtos para adicionar para o serviço!","Formulario de produto",ToastManagerLevel.WARN)
      return;
    }
   

    const produtosAdicionados = produtos.map((item) => item.produto);

    if (produtosAdicionados.includes(produto)) {
      toast.sendMessage("Produto já existente no serviço!","Formulario de produto",ToastManagerLevel.WARN)
      return;
    }

    const produtoServico: ProdutoServico = {
      id: 0,
      produto: produto,
      precoProduto: novoPreco,
      descricao: descricao,
      quantidade: quantidade,
    };

    setProdutos((produtos) => [...produtos, produtoServico]);


    setValue("qtd", 0);
    setValue("novoPreco", 0);

  });

  const removeProdutoBind = (id: number) => {
    const produtosNovos = produtos.filter((item) => item.produto?.id !== id);
    setProdutos(produtosNovos);
  };

  return (
    <Container>
      <Card>
        <Card.Body>
          <Card.Title>Adicionar Produto</Card.Title>
          <Form onSubmit={submitProdutoForm}>
            <Row>
              <Col xs={9}>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlInput1"
                >
                  <FloatingLabel
                    controlId="produtoFloatInput"
                    label="Produto"
                    className="mb-3"
                  >
                    <Controller
                      name="produto"
                      control={control}
                      render={({ field }) => (
                        <Form.Select
                          aria-label="Default select example"
                          {...field}
                          required
                          onChange={(e) => {
                            setValue("produto", e.target.value);
                            changeProductBind(e.target.value);
                          }}
                        >
                          {produtosCarregados?.map((item, index) => (
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
              <Col>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlInput1"
                >
                  <FloatingLabel
                    controlId="produtoFloatInput"
                    label="Preço original"
                    className="mb-3"
                  >
                    <Form.Control
                      type="number"
                      {...register("precoOriginal")}
                      disabled
                      readOnly
                    />
                  </FloatingLabel>
                </Form.Group>
              </Col>
            </Row>

            <Row>
              <Col xs={{ offset: 7, span: 2 }}>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlInput1"
                >
                  <FloatingLabel
                    controlId="qtdFloatInput"
                    label="Quantidade"
                    className="mb-3"
                  >
                    <Form.Control type="number" {...register("qtd")} required min={0} />
                  </FloatingLabel>
                </Form.Group>
              </Col>
              <Col xs={{ span: 3 }}>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlInput1"
                >
                  <FloatingLabel
                    controlId="novoprecoFloatInput"
                    label="Preço unid."
                    className="mb-3"
                  >
                    <Form.Control
                      type="number"
                      {...register("novoPreco")}
                      step="0.01"
                      required
                      min={0}
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
                    {...register("produtoInfo")}
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

      {produtos && produtos.length > 0 && (
        <Card className="mt-3">
          <Card.Header as="h5" className={styles["card-header"]}>
            Listagem
          </Card.Header>
          <Card.Body>
            {produtos?.map((item) => (
              <ServicoProdutoListCard
                key={uuidv4()}
                model={item}
                removeProduto={removeProdutoBind}
              />
            ))}
          </Card.Body>
        </Card>
      )}
    </Container>
  );
};
