import { screen, render, waitFor, act } from '@testing-library/react';
import About from '../About';

describe('About Component', () => {
  beforeEach(() => {
    jest.spyOn(global, 'fetch').mockResolvedValue({
      ok: true,
      text: async () => `about:\n  - navy-bg\n  - red-bg`,
    });
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  test('renders with default props', async () => {
    await act(async () => {
      render(<About />);
    });

    expect(screen.getByAltText("View Conor Heffron's profile on LinkedIn")).toBeInTheDocument();
    expect(screen.getByAltText('Strava')).toBeInTheDocument();
  });

  test('renders with provided props', async () => {
    const props = {
      link: 'https://example.com',
      imgSrc: 'https://example.com/image.png',
      imgAlt: 'Example Image',
      stravaLink: 'https://example.com/strava',
      stravaImgSrc: 'https://example.com/strava-image.png',
      stravaImgAlt: 'Example Strava',
    };
    await act(async () => {
      render(<About {...props} />);
    });
    expect(screen.getByAltText('Example Image')).toBeInTheDocument();
    expect(screen.getByAltText('Example Strava')).toBeInTheDocument();
  });

  test('requests camera roll config on mount', async () => {
    await act(async () => {
      render(<About />);
    });

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith('/camera-roll.yml', { cache: 'no-store' });
    });
  });
});
