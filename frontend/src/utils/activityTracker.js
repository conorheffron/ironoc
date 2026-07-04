export const trackClickOut = (category, target) => {
  if (!category || !target) {
    return;
  }

  const payload = JSON.stringify({ category, target });

  try {
    if (typeof navigator !== 'undefined' && navigator.sendBeacon) {
      const blob = new Blob([payload], { type: 'application/json' });
      navigator.sendBeacon('/api/activity/click-out', blob);
      return;
    }

    fetch('/api/activity/click-out', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: payload,
      keepalive: true
    }).catch(() => null);
  } catch {
    // no-op to avoid blocking navigation
  }
};
