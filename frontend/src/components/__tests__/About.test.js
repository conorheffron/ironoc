import { screen, render } from '@testing-library/react';
import About from '../About';

describe('About Component', () => {
  test('renders with default props', () => {
    render(<About />);

    expect(screen.getByAltText("View Conor Heffron's profile on LinkedIn")).toBeInTheDocument();
    expect(screen.getByAltText('Strava')).toBeInTheDocument();
  });

  test('renders with provided props', () => {
    const props = {
      link: 'https://example.com',
      imgSrc: 'https://example.com/image.png',
      imgAlt: 'Example Image',
      stravaLink: 'https://example.com/strava',
      stravaImgSrc: 'https://example.com/strava-image.png',
      stravaImgAlt: 'Example Strava',
    };
    render(<About {...props} />);
    expect(screen.getByAltText('Example Image')).toBeInTheDocument();
    expect(screen.getByAltText('Example Strava')).toBeInTheDocument();
  });
});
