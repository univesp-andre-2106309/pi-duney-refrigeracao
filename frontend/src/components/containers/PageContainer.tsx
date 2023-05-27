import React, { useState } from "react";
import styles from "./PageContainer.module.css";
import Container from "react-bootstrap/Container";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import NavDropdown from "react-bootstrap/NavDropdown";
import { NavLink, useNavigate } from "react-router-dom";
import { ToastManager } from "../../toast/ToastManager";
import { ToastManagerLevel } from "../../toast/ToastManagerLevel";
import { Button, Modal, Row } from "react-bootstrap";

interface mycomponentinterface {
  title?: string;
  enableHeader: boolean;
  children?: React.ReactNode;
}

export const PageContainer = (props: mycomponentinterface) => {
  const navigate = useNavigate();
  const toastManager = new ToastManager();
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const logoffBind = () => {
    localStorage.removeItem("jwtToken");
    localStorage.removeItem("refreshToken");

    handleClose();
    toastManager.sendMessage(
      "Logoff realizado com sucesso!",
      "Finalizar sessão",
      ToastManagerLevel.SUCCESS
    );

    navigate("/login");
  };

  const mainPageBind = () => {
    navigate("/menu");
  };

  if (props.enableHeader) {
    return (
      <React.Fragment>
        <div className={styles["page-layout"]}>
          <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
            <Container>
              <Navbar.Brand className={styles["brand-title"]} onClick={mainPageBind}>Duney Refrigeração</Navbar.Brand>
              <Navbar.Toggle aria-controls="responsive-navbar-nav" />
              <Navbar.Collapse id="responsive-navbar-nav">
                <Nav className="me-auto">
                  <NavDropdown title="Cliente" id="collasible-nav-dropdown">
                    <NavDropdown.Item>
                      <NavLink
                        className={styles["nav-link-header"]}
                        to="cadastro-cliente"
                      >
                        Cadastro de cliente
                      </NavLink>
                    </NavDropdown.Item>
                    <NavDropdown.Item>
                      <NavLink
                        className={styles["nav-link-header"]}
                        to="gerencia-cliente"
                      >
                        Gerenciamento de cliente
                      </NavLink>
                    </NavDropdown.Item>
                  </NavDropdown>
                  <NavDropdown title="Produto" id="collasible-nav-dropdown">
                    <NavDropdown.Item>
                      <NavLink
                        className={styles["nav-link-header"]}
                        to="cadastro-produto"
                      >
                        Cadastar de produto
                      </NavLink>
                    </NavDropdown.Item>
                    <NavDropdown.Item>
                      <NavLink
                        className={styles["nav-link-header"]}
                        to="gerencia-produto"
                      >
                        Gerenciamento de produto
                      </NavLink>
                    </NavDropdown.Item>
                  </NavDropdown>
                  <NavDropdown title="Tecnico" id="collasible-nav-dropdown">
                    <NavDropdown.Item>
                      <NavLink
                        className={styles["nav-link-header"]}
                        to="cadastro-tecnico"
                      >
                        Cadastar de tecnico
                      </NavLink>
                    </NavDropdown.Item>
                    <NavDropdown.Item>
                      <NavLink
                        className={styles["nav-link-header"]}
                        to="gerencia-tecnico"
                      >
                        Gerenciamento de técnico
                      </NavLink>
                    </NavDropdown.Item>
                  </NavDropdown>
                  <NavDropdown title="Fornecedor" id="collasible-nav-dropdown">
                    <NavDropdown.Item>
                      <NavLink
                        className={styles["nav-link-header"]}
                        to="cadastro-fornecedor"
                      >
                        Cadastar de fornecedor
                      </NavLink>
                    </NavDropdown.Item>
                    <NavDropdown.Item>
                      <NavLink
                        className={styles["nav-link-header"]}
                        to="gerencia-fornecedor"
                      >
                        Gerenciamento de fornecedor
                      </NavLink>
                    </NavDropdown.Item>
                  </NavDropdown>

                  <NavDropdown title="Serviço" id="collasible-nav-dropdown">
                    <NavDropdown.Item>
                      <NavLink
                        className={styles["nav-link-header"]}
                        to="cadastro-servico"
                      >
                        Cadastar de serviço
                      </NavLink>
                    </NavDropdown.Item>
                    <NavDropdown.Item>
                      <NavLink
                        className={styles["nav-link-header"]}
                        to="gerencia-servico"
                      >
                        Gerenciamento de serviço
                      </NavLink>
                    </NavDropdown.Item>
                  </NavDropdown>
                </Nav>
                <Nav>
                  <Nav.Link
                    className={styles["nav-link-header-logoff"]}
                    onClick={handleShow}
                  >
                    Sair da sessão
                  </Nav.Link>
                </Nav>
              </Navbar.Collapse>
            </Container>
          </Navbar>
          <div className={styles["container-card"]}>
            {props.title && (
              <header className={styles["container-title"]}>
                <h1>{props.title}</h1>
              </header>
            )}

            <main className={styles["container-content"]}>
              {props.children}
            </main>
          </div>
          <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
              <Modal.Title>Finalizar sessão</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              Tem certeza que deseja finalizar sua sessão?
            </Modal.Body>
            <Modal.Footer>
              <Button variant="warning" onClick={handleClose}>
                Cancelar
              </Button>
              <Button variant="danger" onClick={logoffBind}>
                Finalizar sessão
              </Button>
            </Modal.Footer>
          </Modal>
        </div>
        {/* <Row className={styles["footer"]}>
          <h4>Duney Refrigeração 1.0</h4>
          <p>Projeto integrador univesp</p>
        </Row> */}
      </React.Fragment>
    );
  } else {
    return (
      <main className={styles["container-content"]}>{props.children}</main>
    );
  }
};
