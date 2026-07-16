import { render, screen, act, waitFor } from '@testing-library/react';
import Home from '../Home';

describe('Home', () => {
  beforeEach(() => {
    jest.spyOn(global, 'fetch').mockResolvedValue({
      ok: true,
      text: async () => `home:\n  - navy-bg`,
    });
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  test('renders Home component', async () => {
    await act(async () => {
      render(<Home />);
    });
    const element = screen.getByText(/Home/i);
    expect(element).toBeInTheDocument();
  });

  test('requests camera roll config on mount', async () => {
    render(<Home />);

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith('/camera-roll.yml', { cache: 'no-store' });
    });
  });
});
