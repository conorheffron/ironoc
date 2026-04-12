describe('reportWebVitals', () => {
  test('does nothing when callback is not provided', async () => {
    const getCLSMock = jest.fn();
    const getFIDMock = jest.fn();
    const getFCPMock = jest.fn();
    const getLCPMock = jest.fn();
    const getTTFBMock = jest.fn();

    jest.resetModules();
    jest.isolateModules(() => {
      jest.doMock('web-vitals', () => ({
        getCLS: getCLSMock,
        getFID: getFIDMock,
        getFCP: getFCPMock,
        getLCP: getLCPMock,
        getTTFB: getTTFBMock,
      }));

      const reportWebVitals = require('./reportWebVitals').default;
      reportWebVitals();
    });
    await new Promise(resolve => setTimeout(resolve, 0));

    expect(getCLSMock).not.toHaveBeenCalled();
    expect(getFIDMock).not.toHaveBeenCalled();
    expect(getFCPMock).not.toHaveBeenCalled();
    expect(getLCPMock).not.toHaveBeenCalled();
    expect(getTTFBMock).not.toHaveBeenCalled();
  });

  test('forwards callback to web-vitals metrics', async () => {
    const getCLSMock = jest.fn();
    const getFIDMock = jest.fn();
    const getFCPMock = jest.fn();
    const getLCPMock = jest.fn();
    const getTTFBMock = jest.fn();
    const onPerfEntry = function onPerfEntry() {};

    jest.resetModules();
    jest.isolateModules(() => {
      jest.doMock('web-vitals', () => ({
        getCLS: getCLSMock,
        getFID: getFIDMock,
        getFCP: getFCPMock,
        getLCP: getLCPMock,
        getTTFB: getTTFBMock,
      }));

      const reportWebVitals = require('./reportWebVitals').default;
      reportWebVitals(onPerfEntry);
    });
    await new Promise(resolve => setTimeout(resolve, 0));

    expect(getCLSMock).toHaveBeenCalledWith(onPerfEntry);
    expect(getFIDMock).toHaveBeenCalledWith(onPerfEntry);
    expect(getFCPMock).toHaveBeenCalledWith(onPerfEntry);
    expect(getLCPMock).toHaveBeenCalledWith(onPerfEntry);
    expect(getTTFBMock).toHaveBeenCalledWith(onPerfEntry);
  });
});
