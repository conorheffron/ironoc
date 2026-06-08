import navy from '../img/darkblue-bg.png';
import teal from '../img/teal-bg.png';
import red from '../img/red-bg.png';

const CAMERA_ROLL_IMAGE_MAP = {
    'navy-bg': navy,
    'teal-bg': teal,
    'red-bg': red
};

const DEFAULT_CAMERA_ROLL_IMAGES = {
    home: [teal, navy, red],
    about: [navy, red, teal]
};

const parseCameraRollConfig = (rawConfig) => {
    const parsedConfig = {};
    let currentSection = null;

    rawConfig
        .split('\n')
        .map((line) => line.trim())
        .forEach((line) => {
            if (!line || line.startsWith('#')) {
                return;
            }

            if (line.endsWith(':')) {
                currentSection = line.slice(0, -1).trim();
                parsedConfig[currentSection] = parsedConfig[currentSection] || [];
                return;
            }

            if (line.startsWith('-') && currentSection) {
                const imageKey = line.replace('-', '').trim();
                const image = CAMERA_ROLL_IMAGE_MAP[imageKey];

                if (image) {
                    parsedConfig[currentSection].push(image);
                }
            }
        });

    return parsedConfig;
};

export const loadCameraRollImages = async (section) => {
    const fallbackImages = DEFAULT_CAMERA_ROLL_IMAGES[section] || DEFAULT_CAMERA_ROLL_IMAGES.home;

    try {
        const response = await fetch('/camera-roll.yml', { cache: 'no-store' });
        if (!response.ok) {
            return fallbackImages;
        }

        const parsedConfig = parseCameraRollConfig(await response.text());
        const configuredImages = parsedConfig[section];

        if (!configuredImages || configuredImages.length === 0) {
            return fallbackImages;
        }

        return configuredImages;
    } catch {
        return fallbackImages;
    }
};
