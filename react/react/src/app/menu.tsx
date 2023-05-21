"use client";
import * as React from "react";
import { Container, Nav, Navbar } from "react-bootstrap";

export interface IMenuProps {}

export default class Menu extends React.Component<IMenuProps> {
  public render() {
    return (
      <Navbar bg="primary" variant="dark">
        <Container>
          <Navbar.Brand href="/">Navbar</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link href="/">Home</Nav.Link>
            <Nav.Link href="/persons">Persons</Nav.Link>
          </Nav>
        </Container>
      </Navbar>
    );
  }
}
