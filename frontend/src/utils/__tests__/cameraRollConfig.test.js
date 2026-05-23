import { loadCameraRollImages } from '../cameraRollConfig';

describe('cameraRollConfig', () => {
  afterEach(() => {
    jest.restoreAllMocks();
  });

  test('loads configured images for a section from camera-roll.yml', async () => {
    jest.spyOn(global, 'fetch').mockResolvedValue({
      ok: true,
      text: async () => `home:\n  - red-bg\n  - teal-bg`,
    });

    const images = await loadCameraRollImages('home');

    expect(images).toHaveLength(2);
    expect(images[0]).toContain('red-bg');
    expect(images[1]).toContain('teal-bg');
  });

  test('falls back to defaults when config fetch fails', async () => {
    jest.spyOn(global, 'fetch').mockRejectedValue(new Error('network error'));

    const images = await loadCameraRollImages('about');

    expect(images).toHaveLength(3);
    expect(images[0]).toContain('darkblue-bg');
  });
});
