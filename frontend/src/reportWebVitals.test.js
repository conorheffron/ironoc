import { waitFor } from '@testing-library/react';
import reportWebVitals from './reportWebVitals';
import { getCLS, getFID, getFCP, getLCP, getTTFB } from 'web-vitals';

jest.mock('web-vitals', () => ({
  getCLS: jest.fn(),
  getFID: jest.fn(),
  getFCP: jest.fn(),
  getLCP: jest.fn(),
  getTTFB: jest.fn(),
}));

describe('reportWebVitals', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('does nothing when callback is not provided', async () => {
    reportWebVitals();
    await Promise.resolve();

    expect(getCLS).not.toHaveBeenCalled();
    expect(getFID).not.toHaveBeenCalled();
    expect(getFCP).not.toHaveBeenCalled();
    expect(getLCP).not.toHaveBeenCalled();
    expect(getTTFB).not.toHaveBeenCalled();
  });

  test('forwards callback to web-vitals metrics', async () => {
    const onPerfEntry = jest.fn();

    reportWebVitals(onPerfEntry);

    await waitFor(() => {
      expect(getCLS).toHaveBeenCalledWith(onPerfEntry);
      expect(getFID).toHaveBeenCalledWith(onPerfEntry);
      expect(getFCP).toHaveBeenCalledWith(onPerfEntry);
      expect(getLCP).toHaveBeenCalledWith(onPerfEntry);
      expect(getTTFB).toHaveBeenCalledWith(onPerfEntry);
    });
  });
});
