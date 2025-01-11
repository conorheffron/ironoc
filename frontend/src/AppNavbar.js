import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import {Navbar, NavbarText, Container, Collapse, NavbarBrand, Nav, UncontrolledDropdown, DropdownToggle, DropdownItem, DropdownMenu } from "reactstrap";
import logo from './img/robot-logo.png';

export default function AppNavBar() {
  return (
        <div>
            <Navbar color="primary" light={false} dark={true} fixed="top" container={true} expand={true} className="nav-bar">
                <Container>
                    <NavbarBrand href="/">
                        <img alt="" src={logo} width="30" height="30" className="d-inline-block align-top"/>
                    </NavbarBrand>
                    <Collapse navbar>
                    <Nav className="me-auto" navbar>
                        <UncontrolledDropdown inNavbar nav>
                            <DropdownToggle caret nav>iRonoc</DropdownToggle>
                            <DropdownMenu end>
                                <DropdownItem href="/about">About</DropdownItem>
                                <DropdownItem href="/portfolio">Portfolio</DropdownItem>
                                <DropdownItem href="/brews">Brews</DropdownItem>
                                <DropdownItem divider />
                                <DropdownItem href="https://linktr.ee/conorheffron" target="_blank" rel="noreferrer">Link Tree</DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>
                        <UncontrolledDropdown inNavbar nav>
                            <DropdownToggle caret nav>GitHub PM</DropdownToggle>
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
                                <DropdownItem target="_blank" href="/get-repo-detail?username=conorheffron">GET Projects</DropdownItem>
                                <DropdownItem target="_blank" href="/get-repo-issue/conorheffron/ironoc/">GET Issues</DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>
                        <UncontrolledDropdown inNavbar nav>
                            <DropdownToggle caret nav>GitHub Projects</DropdownToggle>
                            <DropdownMenu end>
                                <DropdownItem target="_blank" href="https://github.com/conorheffron/ironoc">iRonoc</DropdownItem>
                                <DropdownItem divider />
                                <DropdownItem target="_blank" href="https://github.com/conorheffron/ironoc-db">iRonoc-DB</DropdownItem>
                                <DropdownItem target="_blank" href="https://github.com/conorheffron/booking-sys">Booking System Sample</DropdownItem>
                                <DropdownItem target="_blank" href="https://github.com/conorheffron/nba-stats">NBA Stats Analysis</DropdownItem>
                                <DropdownItem target="_blank" href="https://github.com/conorheffron/ironoc-pytest">PyTest / GitHub Client Package</DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>
                    </Nav>
                    <a href="/">
                        <NavbarText href="/">Home</NavbarText>
                    </a>
                    </Collapse>
                </Container>
            </Navbar>
        </div>
  );
}
