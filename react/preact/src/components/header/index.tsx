import { Signal } from "@preact/signals";
import { h } from "preact";
import { Container, Nav, Navbar } from "react-bootstrap";

interface Props {
  activeUrl: Signal<string>;
}

const Header = ({ activeUrl }: Props) => (
  <Navbar className="shadow-sm mb-3">
    <Container>
      <Navbar.Brand href="/">
        <img
          src="../../assets/preact-logo-inverse.svg"
          alt="Preact Logo"
          height="32"
          width="32"
        />{" "}
        Preact
      </Navbar.Brand>

      <Nav className="me-auto">
        <Nav.Link href="/" active={activeUrl.value == "/"}>
          Home
        </Nav.Link>
        <Nav.Link href="/profile" active={activeUrl.value == "/profile"}>
          Profiles
        </Nav.Link>
        <Nav.Link
          href="/profile/john"
          active={activeUrl.value == "/profile/john"}
        >
          John
        </Nav.Link>
      </Nav>
    </Container>
  </Navbar>
);

export default Header;
