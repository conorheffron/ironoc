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
                            <NavLink href="/about">About</NavLink>
                        </NavItem>
                        <NavItem>
                            <NavLink href="/highs">Portfolio</NavLink>
                        </NavItem>
                        <NavItem>
                            <NavLink href="/projects/conorheffron">Projects</NavLink>
                        </NavItem>
                        <NavItem>
                            <NavLink href="/issues/conorheffron/ironoc">Issues</NavLink>
                        </NavItem>
                        <NavItem>
                            <NavLink target="_blank" href="/swagger-ui-ironoc.html">API Doc</NavLink>
                        </NavItem>
                        <UncontrolledDropdown inNavbar nav>
                            <DropdownToggle caret nav>API Options</DropdownToggle>
                            <DropdownMenu end>
                                <DropdownItem target="_blank" href="/get-repo-detail?username=conorheffron">Projects JSON</DropdownItem>
                                <DropdownItem target="_blank" href="/get-repo-issue/conorheffron/ironoc/">Issues JSON (iRonoc)</DropdownItem>
                                <DropdownItem divider />
                                <DropdownItem href="/">Reset</DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>
                         <NavItem>
                             <NavLink href="https://linktr.ee/conorheffron" target="_blank" rel="noreferrer">Link Tree</NavLink>
                         </NavItem>
                    </Nav>
                    </Collapse>
                </Container>
            </Navbar>
        </div>
  );
}