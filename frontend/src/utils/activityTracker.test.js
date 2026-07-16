import { trackClickOut } from './activityTracker';

describe('activityTracker', () => {
  let originalSendBeacon;
  let originalFetch;

  beforeEach(() => {
    originalSendBeacon = global.navigator.sendBeacon;
    originalFetch = global.fetch;
  });

  afterEach(() => {
    if (originalSendBeacon === undefined) {
      delete global.navigator.sendBeacon;
    } else {
      global.navigator.sendBeacon = originalSendBeacon;
    }
    global.fetch = originalFetch;
    jest.restoreAllMocks();
  });

  test('returns early when category or target is missing', () => {
    const sendBeaconMock = jest.fn();
    Object.defineProperty(global.navigator, 'sendBeacon', {
      value: sendBeaconMock,
      configurable: true,
      writable: true
    });

    trackClickOut(null, 'target');
    trackClickOut('category', null);
    trackClickOut('', '');

    expect(sendBeaconMock).not.toHaveBeenCalled();
  });

  test('calls navigator.sendBeacon when available', () => {
    const sendBeaconMock = jest.fn().mockReturnValue(true);
    Object.defineProperty(global.navigator, 'sendBeacon', {
      value: sendBeaconMock,
      configurable: true,
      writable: true
    });

    trackClickOut('test-category', 'test-target');

    expect(sendBeaconMock).toHaveBeenCalledTimes(1);
    expect(sendBeaconMock.mock.calls[0][0]).toBe('/api/activity/click-out');
    
    // Validate payload is correct
    const blobArg = sendBeaconMock.mock.calls[0][1];
    expect(blobArg).toBeInstanceOf(Blob);
    expect(blobArg.type).toBe('application/json');
  });

  test('falls back to fetch when navigator.sendBeacon is not available', async () => {
    if ('sendBeacon' in global.navigator) {
      delete global.navigator.sendBeacon;
    }
    const fetchMock = jest.fn().mockResolvedValue({ ok: true });
    global.fetch = fetchMock;

    trackClickOut('test-category', 'test-target');

    expect(fetchMock).toHaveBeenCalledTimes(1);
    expect(fetchMock).toHaveBeenCalledWith('/api/activity/click-out', expect.objectContaining({
      method: 'POST',
      body: JSON.stringify({ category: 'test-category', target: 'test-target' }),
      keepalive: true
    }));
  });

  test('handles errors gracefully', () => {
    if ('sendBeacon' in global.navigator) {
      delete global.navigator.sendBeacon;
    }
    const fetchMock = jest.fn().mockRejectedValue(new Error('Fetch failed'));
    global.fetch = fetchMock;

    // Should not throw
    expect(() => {
      trackClickOut('test-category', 'test-target');
    }).not.toThrow();
  });
});
