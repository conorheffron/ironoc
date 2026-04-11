import { waitFor } from '@testing-library/react';

describe('index entrypoint', () => {
  beforeEach(() => {
    jest.resetModules();
    document.body.innerHTML = '<div id="root"></div>';
  });

  test('renders application wrapper into root element', async () => {
    require('./index');
    await waitFor(() => {
      expect(document.querySelector('.app-wrapper')).toBeInTheDocument();
    });
  });
});
