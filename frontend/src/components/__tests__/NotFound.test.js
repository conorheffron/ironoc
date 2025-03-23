import { render, screen } from '@testing-library/react';
import NotFound from '../NotFound';

describe('NotFound', () => {
  test('renders AppNavbar component', () => {
    render(<NotFound />);
    expect(screen.getByRole('banner')).toBeInTheDocument();
  });

  test('displays 404 error message', () => {
    render(<NotFound />);
    expect(screen.getByText('404 - Page Not Found')).toBeInTheDocument();
  });

  test('displays the apology message', () => {
    render(<NotFound />);
    expect(screen.getByText('Sorry, the page you are looking for could not be found.')).toBeInTheDocument();
  });
});
