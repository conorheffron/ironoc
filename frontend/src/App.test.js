import { render, screen } from '@testing-library/react';
import Home from './components/Home';

test('renders learn react link', () => {
  render(<Home />);
  const element = screen.getByText(/Home/i);
  expect(element).toBeInTheDocument();
});
