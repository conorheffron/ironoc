import React, { Component } from 'react';
import Carousel from 'react-bootstrap/Carousel';
import 'bootstrap/dist/css/bootstrap.css';
import '../App.css';
import { Container } from 'reactstrap';
import AppNavbar from '../AppNavbar';
import Footer from '../Footer';
import red from '../img/red-bg.png';

const donateItems = [
    {
        donate: "https://www.jackandjill.ie/professionals/ways-to-donate/",
        link: "https://www.jackandjill.ie",
        img: red,
        alt: "red1",
        name: "The Jack and Jill Children’s Foundation",
        overview: `The Jack and Jill Children’s Foundation is a nationwide charity that funds and provides in-home nursing care
                   and respite support for children with severe to profound neurodevelopmental delay, up to the age of 6.
                   This may include children with brain injury, genetic diagnosis, cerebral palsy and undiagnosed conditions.
                   Another key part of our service is end-of-life care for all children up to the age of 6, irrespective of diagnosis.`,
        founded: 1997,
        phone: "+353 (045) 894 538"
    },
    {
        donate: "https://vi.ie/supporting-us/donate-now/",
        link: "https://linktr.ee/vision_ireland",
        img: red,
        alt: "red2",
        name: "Vision Ireland, the new name for NCBI",
        overview: `Vision Ireland, the name for NCBI is Ireland’s national charity working for the rising number of people affected by sight loss.
                   Our practical and emotional advice and support help 8,000 people and their families confidently face their futures every year.`,
        founded: 1931,
        phone: "+353 (0)1 830 7033"
    },
    {
        donate: "https://www.childrenshealth.ie/donate/",
        link: "http://www.cuh.ie/",
        img: red,
        alt: "red3",
        name: "Temple Street Children’s University Hospital",
        overview: `Temple Street, is an acute national paediatric hospital. Seven major specialties at Temple Street today include
                   neonatal & paediatric surgery, neurology, neurosurgery, nephrology, orthopaedics, ENT & plastic surgery.
                   Temple Street cares for 145,000 children per year including 45,000 who attend the Emergency Department (ED) every year.
                   A staff of 85 Consultants& over 950 other full-time & part-time HSCPs (Health & Social
                   Care Professionals) % other staff deliver care & services.`,
        founded: 1872,
        phone: `<br/><b>Temple Street:</b> +353 01 878 4200,
                        <b>Tallaght:</b> +353 01 693 7500,<br />
                        <b>Crumlin:</b> +353 01 409 6100,
                        <b>Connolly:</b> +353 01 640 7500`
    },
    {
        donate: "https://donors.cancer.ie/page/FUNMTNWKDWV",
        link: "https://www.cancer.ie/",
        img: red,
        alt: "red4",
        name: "Irish Cancer Society",
        overview: `We are a community of patients, survivors, volunteers, supporters, health and social care professionals and researchers.
                   Together we are transforming the experiences and outcomes of people affected by cancer through our advocacy, support services and research.`,
        founded: 1963,
        phone: "+353 (0) 1800200700"
    },
    {
        donate: "https://www.svp.ie/donate/",
        link: "http://www.svp.ie",
        img: red,
        alt: "red5",
        name: "The Society of Saint Vincent de Paul",
        overview: `The Society of Saint Vincent de Paul exists to fight poverty. The Society of St. Vincent de Paul is the largest, voluntary, charitable organisation in Ireland.
                   Its membership of over 11,000 volunteer members throughout the country are supported by 600 staff, working for social justice and the creation of a more just,
                   caring nation. This unique network of social concern also gives practical support to those experiencing poverty and social exclusion, by providing a wide range
                   of services to people in need.`,
        founded: 1844,
        phone: "+353 1 884 8200"
    },
    {
        donate: "http://www.dubsimon.ie",
        link: "http://www.dubsimon.ie",
        img: red,
        alt: "red6",
        name: "Dublin Simon Community",
        overview: `Covering counties Meath, Cavan, Monaghan & Louth. We provide services at all stages of homelessness and enable people to move to a place they can call home.
                   At Simon, we listen to people who turn to us for help and do everything we can to support them to move out of homelessness into independent living.
                   We strive to empower people to access, secure and retain a home of their own by reducing the reliance on short-term emergency accommodation and providing
                   permanent supported housing for people to sustain a home in their local community. Moving people into supported housing produces life-enhancing and
                   life-saving results and is more cost-effective in the long run.`,
        founded: 1969,
        phone: "+353 (01) 671 5551"
    }
];

class Donate extends Component {
    render() {
        const { items = donateItems } = this.props;

        return (
            <div>
                <AppNavbar />
                <Container>
                    <Carousel className="App-header">
                        {items.map((item, index) => (
                            <Carousel.Item key={index} interval={500}>
                                <a href={item.donate} target="_blank" rel="noreferrer">
                                    <img className="d-block w-100" src={item.img} alt={item.alt} />
                                    <Carousel.Caption>
                                        <h1>
                                            <u>{item.name}</u>
                                        </h1><br />
                                        <h8>
                                            <b>Contact & Help by Phone: </b><span dangerouslySetInnerHTML={{ __html: item.phone }} />
                                        </h8><br />
                                        <h7>
                                            <b>Home page: </b><a href={item.link}>{item.link}</a>
                                        </h7><br /><br />
                                        <h11 className="overview-text">
                                            <b>Overview:</b> Founded in {item.founded}, {item.overview}
                                        </h11>
                                    </Carousel.Caption>
                                </a>
                            </Carousel.Item>
                        ))}
                    </Carousel>
                </Container>
                <Footer />
            </div>
        );
    }
}

export default Donate;
