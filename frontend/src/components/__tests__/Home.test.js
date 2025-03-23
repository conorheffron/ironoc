import { render, screen, act } from '@testing-library/react';
import Home from '../Home';

describe('Home', () => {
  test('renders Home component', async () => {
    await act(async () => {
      render(<Home />);
    });
    const element = screen.getByText(/Home/i);
    expect(element).toBeInTheDocument();
  });
});
