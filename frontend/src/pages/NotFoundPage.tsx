import React from 'react'
import NotFoundSvg from '../assets/svg/notFound.svg';
import Container from "react-bootstrap/Container";
import { Image } from 'react-bootstrap';
import styles from './NotFoundPage.module.css'

//TODO: Implementar pagina 404 ao inves de retornar ao login
export const NotFoundPage = () => {
  return (
    <Container className={styles["container-center"]}>
      <h1>A pagina não existe!</h1>
      <Image src={NotFoundSvg} fluid/>
    </Container>
  )
}
