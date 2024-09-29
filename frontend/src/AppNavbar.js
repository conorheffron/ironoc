import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import {Navbar, Container, Collapse, NavbarBrand, Nav, NavItem, NavLink, UncontrolledDropdown, DropdownToggle, DropdownItem, DropdownMenu } from "reactstrap";

export default function AppNavBar() {
  return (
    <div>
      <Navbar color="light" fixed="top" light expand={true} className="nav-bar">
      <Container>
        <NavbarBrand href="/">Home</NavbarBrand>
        <Collapse navbar>
          <Nav className="me-auto" navbar>
            <NavItem>
              <NavLink href="/about/">About</NavLink>
            </NavItem>
            <NavItem>
              <NavLink href="/projects">GitHub Projects</NavLink>
            </NavItem>
            <NavItem>
              <NavLink target="_blank" href="/swagger-ui-ironoc.html">iRonoc API</NavLink>
            </NavItem>
            <UncontrolledDropdown inNavbar nav>
              <DropdownToggle caret nav>
                API Options
              </DropdownToggle>
              <DropdownMenu end>
                <DropdownItem href="/get-repo-detail?username=conorheffron">Projects JSON</DropdownItem>
                <DropdownItem>Issues JSON</DropdownItem>
                <DropdownItem divider />
                <DropdownItem href="/">Reset</DropdownItem>
              </DropdownMenu>
            </UncontrolledDropdown>
          </Nav>
        </Collapse>
        </Container>
      </Navbar>
    </div>
  );
}