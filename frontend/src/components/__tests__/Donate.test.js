import { render, screen } from '@testing-library/react';
import Donate from '../Donate';

const donateItems = [
    {
        donate: "https://www.jackandjill.ie/professionals/ways-to-donate/",
        link: "https://www.jackandjill.ie",
        img: "red1.png",
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
        img: "red2.png",
        alt: "red2",
        name: "Vision Ireland, the new name for NCBI",
        overview: `Vision Ireland, the name for NCBI is Ireland’s national charity working for the rising number of people affected by sight loss.
                   Our practical and emotional advice and support help 8,000 people and their families confidently face their futures every year.`,
        founded: 1931,
        phone: "+353 (0)1 830 7033"
    }
    // Add more items as needed
];

describe('Donate Component', () => {
    test('renders without crashing', () => {
        render(<Donate items={donateItems} />);
        expect(screen.getByText('The Jack and Jill Children’s Foundation')).toBeInTheDocument();
        expect(screen.getByText('Vision Ireland, the new name for NCBI')).toBeInTheDocument();
    });

    test('renders all carousel items', () => {
        render(<Donate items={donateItems} />);
        donateItems.forEach(item => {
            expect(screen.getByText(item.name)).toBeInTheDocument();
            expect(screen.getByText(new RegExp(`Founded in ${item.founded}`))).toBeInTheDocument();
            expect(screen.getByText(item.phone)).toBeInTheDocument();
            expect(screen.getByText(new RegExp(item.link))).toBeInTheDocument();
        });
    });

    test('renders images with correct alt text', () => {
        render(<Donate items={donateItems} />);
        donateItems.forEach(item => {
            expect(screen.getByAltText(item.alt)).toBeInTheDocument();
        });
    });
});
