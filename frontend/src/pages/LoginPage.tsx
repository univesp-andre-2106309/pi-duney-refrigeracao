import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Form from "react-bootstrap/Form";
import styles from "./LoginPage.module.css";
import { FormEvent, useRef } from "react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

export const LoginPage = () => {
  let emailRef = useRef<HTMLInputElement>(null);
  let passwordRef = useRef<HTMLInputElement>(null);

  const navigate = useNavigate();

  const executeLogin = async (event: FormEvent) => {
    event.preventDefault();

    const emailValue = emailRef.current?.value;
    const passwordValue = passwordRef.current?.value;

    if (!emailValue) {
      toast.warn("Erro ao realizar login:Campo email vazio", {
        position: toast.POSITION.BOTTOM_CENTER,
      });
      return;
    }

    if (!passwordValue) {
      toast.warn("Erro ao realizar login: Campo senha vazio", {
        position: toast.POSITION.BOTTOM_CENTER,
      });
      return;
    }

    await fetch("http://localhost:8080/api/account/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email: emailValue,
        password: passwordValue,
      }),
    })
      .then(async (response) => {
        if (response.status === 200) {
          const jwtToken = response.headers.get("Authorization");
          const refreshToken = response.headers.get("RefreshToken");

          if (jwtToken && refreshToken) {
            // Save the tokens
            localStorage.setItem("jwtToken", jwtToken!);
            localStorage.setItem("refreshToken", refreshToken!);

            toast.success("Login Realizado com sucesso", {
              position: toast.POSITION.BOTTOM_CENTER,
            });
            navigate("/menu");
          }
        } else {
          let resultado = await response.json();

          toast.error(resultado.message, {
            position: toast.POSITION.BOTTOM_CENTER,
          });
        }
      })
      .catch((er) => {
      });
  };

  return (
    <div className={styles["page-container"]}>
      <Container className={styles["card-container"]}>
        <Form onSubmit={executeLogin}>
          <Col>
            <Row className={styles["login-title"]}>
              <h1>Duney</h1>
              <h2>Refrigeração</h2>
            </Row>
            <Row>
              <Form.Group className={styles["input-group"]}>
                <Form.Control
                  type="email"
                  placeholder="Email"
                  ref={emailRef}
                  className={styles["input-group-value"]}
                />
              </Form.Group>
            </Row>

            <Row>
              <Form.Group className={styles["input-group"]}>
                <Form.Control
                  type="password"
                  placeholder="Senha"
                  ref={passwordRef}
                />
              </Form.Group>
            </Row>
            <Row className="mt-5">
              <Button variant="primary" type="submit">
                Login
              </Button>
            </Row>
            <Row className="mt-5">
              <hr />
              <p>Esqueceu sua senha?</p>
            </Row>
          </Col>
        </Form>
      </Container>
    </div>
  );
};
