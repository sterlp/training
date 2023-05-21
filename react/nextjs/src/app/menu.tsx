"use client";
import Image from "next/image";
import * as React from "react";
import { Container, Nav, Navbar } from "react-bootstrap";

export interface IMenuProps {}

export default class Menu extends React.Component<IMenuProps> {
  public render() {
    return (
      <Navbar>
        <Container>
          <Navbar.Brand href="/">
            <Image src="/next.svg" alt="NextJs Logo" height="48" width="48" />
          </Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link href="/">Home</Nav.Link>
            <Nav.Link href="/persons">Persons</Nav.Link>
          </Nav>
        </Container>
      </Navbar>
    );
  }
}
