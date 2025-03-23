import { render, screen } from '@testing-library/react';
import ControlledCarousel from '../ControlledCarousel';

describe('ControlledCarousel Component', () => {
  const items = [
    {
      link: 'https://example.com',
      img: 'https://example.com/image.png',
      alt: 'Example Image',
      title: 'Example Title',
      description: 'Example Description',
      techStack: 'Example TechStack',
    },
  ];

  test('renders with default props', () => {
    render(<ControlledCarousel items={items} />);
    expect(screen.getByAltText('Example Image')).toBeInTheDocument();
    expect(screen.getByText('Example Title')).toBeInTheDocument();
    expect(screen.getByText('Example Description')).toBeInTheDocument();
    expect(screen.getByText('Example TechStack')).toBeInTheDocument();
  });

  test('renders carousel items', () => {
    render(<ControlledCarousel items={items} />);
    items.forEach((item) => {
      expect(screen.getByAltText(item.alt)).toBeInTheDocument();
      expect(screen.getByText(item.title)).toBeInTheDocument();
      expect(screen.getByText(item.description)).toBeInTheDocument();
      expect(screen.getByText(item.techStack)).toBeInTheDocument();
    });
  });
});
