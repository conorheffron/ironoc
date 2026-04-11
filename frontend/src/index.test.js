const renderMock = jest.fn();
const reportWebVitalsMock = jest.fn();
const createRootMock = jest.fn(() => ({ render: renderMock }));

jest.mock('react-dom/client', () => ({
  createRoot: (...args) => createRootMock(...args),
}));

jest.mock('./reportWebVitals', () => (...args) => reportWebVitalsMock(...args));
jest.mock('./App', () => () => <div data-testid="app" />);
jest.mock('./AppNavbar', () => () => <div data-testid="navbar" />);
jest.mock('./Footer', () => () => <div data-testid="footer" />);

describe('index entrypoint', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    document.body.innerHTML = '<div id="root"></div>';
  });

  test('mounts app into root and starts web vitals reporting', () => {
    jest.isolateModules(() => {
      require('./index');
    });

    expect(createRootMock).toHaveBeenCalledWith(document.getElementById('root'));
    expect(renderMock).toHaveBeenCalledTimes(1);
    expect(reportWebVitalsMock).toHaveBeenCalledTimes(1);
  });
});
