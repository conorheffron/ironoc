const mockRender = jest.fn();
const mockCreateRoot = jest.fn(() => ({
  render: mockRender,
}));

const elementContainsClassName = (node, className) => {
  if (!node) {
    return false;
  }

  if (Array.isArray(node)) {
    return node.some((child) => elementContainsClassName(child, className));
  }

  if (typeof node !== 'object') {
    return false;
  }

  const nodeClassName = node.props && typeof node.props.className === 'string'
    ? node.props.className.split(/\s+/)
    : [];

  if (nodeClassName.includes(className)) {
    return true;
  }

  return elementContainsClassName(node.props && node.props.children, className);
};

describe('index entrypoint', () => {
  beforeEach(() => {
    jest.resetModules();
    mockCreateRoot.mockReset();
    mockCreateRoot.mockImplementation(() => ({ render: mockRender }));
    mockRender.mockReset();
    document.body.innerHTML = '<div id="root"></div>';
  });

  test('renders application wrapper into root element', () => {
    jest.isolateModules(() => {
      jest.doMock('react-dom/client', () => ({
        createRoot: mockCreateRoot,
      }));
      jest.doMock('./App', () => () => null);
      jest.doMock('./AppNavbar', () => () => null);
      jest.doMock('./Footer', () => () => null);
      require('./index');
    });

    expect(mockCreateRoot).toHaveBeenCalledWith(document.getElementById('root'));
    expect(mockRender).toHaveBeenCalledTimes(1);
    expect(elementContainsClassName(mockRender.mock.calls[0][0], 'app-wrapper')).toBe(true);
  });
});
