import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import {Navbar, Container, Collapse, NavbarBrand, Nav, NavItem, NavLink, UncontrolledDropdown, DropdownToggle, DropdownItem, DropdownMenu } from "reactstrap";
import logo from './img/robot-logo.png';

export default function AppNavBar() {
  return (
        <div>
            <Navbar color="light" fixed="top" light expand={true} className="nav-bar">
                <Container>
                    <NavbarBrand href="/">
                        <img alt="" src={logo} width="30" height="30" className="d-inline-block align-top"/>{' '}Home
                    </NavbarBrand>
                    <Collapse navbar>
                    <Nav className="me-auto" navbar>
                        <UncontrolledDropdown inNavbar nav>
                            <DropdownToggle caret nav>iRonoc</DropdownToggle>
                            <DropdownMenu end>
                                <DropdownItem href="/about">About</DropdownItem>
                                <DropdownItem href="/highs">Portfolio</DropdownItem>
                                <DropdownItem divider />
                                <DropdownItem href="https://linktr.ee/conorheffron" target="_blank" rel="noreferrer">Link Tree</DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>
                        <UncontrolledDropdown inNavbar nav>
                            <DropdownToggle caret nav>GitHub Views</DropdownToggle>
                            <DropdownMenu end>
                                <DropdownItem href="/projects/conorheffron">Projects</DropdownItem>
                                <DropdownItem href="/issues/conorheffron/ironoc">Issues</DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>
                        <UncontrolledDropdown inNavbar nav>
                            <DropdownToggle caret nav>GitHub API</DropdownToggle>
                            <DropdownMenu end>
                                <DropdownItem target="_blank" href="/swagger-ui-ironoc.html">Swagger Doc</DropdownItem>
                                <DropdownItem divider />
                                <DropdownItem target="_blank" href="/get-repo-detail?username=conorheffron">Projects JSON (conorheffron)</DropdownItem>
                                <DropdownItem target="_blank" href="/get-repo-issue/conorheffron/ironoc/">Issues JSON (iRonoc)</DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>
                    </Nav>
                    </Collapse>
                </Container>
            </Navbar>
        </div>
  );
}